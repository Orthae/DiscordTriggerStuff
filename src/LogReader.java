import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LogReader {
    //  Singleton
    private LogReader() {
    }

    public static LogReader getInstance() {
        return LogReaderHolder.INSTANCE;
    }

    public static class LogReaderHolder {
        public static final LogReader INSTANCE = new LogReader();
    }

    //  Fields
    private Path logFilePath;
    private DelayedTriggersThread delayedTriggersThread;
    private TriggerThread triggerThread;

    //  Getters
    public Path getLogFilePath() {
        return logFilePath;
    }

    private DelayedTriggersThread getDelayedTriggersThread() {
        return delayedTriggersThread;
    }

    private TriggerThread getTriggerThread(){
        return triggerThread;
    }

//  Setters

    @SuppressWarnings("WeakerAccess")
    public void setLogFilePath(Path logFilePath) {
        this.logFilePath = logFilePath;
    }


    //  Methods
    public void stopThread() {
        if(getDelayedTriggersThread() != null){
            getTriggerThread().isSearchActive = false;
        }
        if(getTriggerThread() != null && getDelayedTriggersThread() != null){
            getDelayedTriggersThread().isSearchActive = false;
        }
    }

    public void startThread() {
        if (logFilePath != null) {
            triggerThread = new TriggerThread();
            delayedTriggersThread = new DelayedTriggersThread();
            triggerThread.start();
            delayedTriggersThread.start();
        }
    }


    private void alertWindow() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LanguageData.getInstance().getMsg("mainACTLogFileErrorTitle"));
        alert.setHeaderText(null);
        alert.setContentText(LanguageData.getInstance().getMsg("mainACTLogFileError"));
        alert.showAndWait();
    }

    public String findLogFolder() {
        try {
            Path actTest = Paths.get(System.getenv("APPDATA"), "Advanced Combat Tracker/Config/Advanced Combat Tracker.config.xml");
            File actConfigFIle = new File(actTest.toUri());
            if (!actConfigFIle.exists()) {
                return null;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(actConfigFIle);
            document.normalize();
            NodeList nList = document.getElementsByTagName("DirectoryInfo");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    if (element.getAttribute("Name").equals("folderLogs")) {
                        return element.getAttribute("Value");
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            return null;
        }
        return null;
    }

    public void findLogFile() {
            stopThread();
        try (DirectoryStream<Path> logFiles = Files.newDirectoryStream(Paths.get(Settings.getInstance().getLogFolder()), "*.log")) {
            Path newestFilePath = null;
            int newestFile = 0;
            for (Path logFile : logFiles) {
                String fileName = logFile.getFileName().toString();
                if (fileName.contains("split")) {
                    continue;
                }
                fileName = fileName.substring(8, 16);
                if (fileName.matches("[0-9]+")) {
                    if (Integer.parseInt(fileName) > newestFile) {
                        newestFile = Integer.parseInt(fileName);
                        newestFilePath = logFile;
                    }
                }
            }
            setLogFilePath(newestFilePath);
            startThread();
        } catch (IOException e) {
            alertWindow();
            return;
        }
        if (getLogFilePath() == null) {
            alertWindow();
        }
    }

    public void manuallySelectLogFile(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ACT Log File", "*.log"));
        if (Settings.getInstance().getLogFolder() != null) {
            fileChooser.setInitialDirectory(new File(Settings.getInstance().getLogFolder()));
        }
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            setLogFilePath(file.toPath());
            startThread();
        }
    }

    //  Thread reading the log file
    class TriggerThread extends Thread {
        boolean isSearchActive = true;
        boolean isEnfOfFile = false;
        @Override
        public void run() {
            while (isSearchActive) {
                if (TriggerData.getInstance().getTriggersArray() != null) {
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getLogFilePath().toFile()))) {
                        while (true) {
                            while (!isEnfOfFile) {
                                if (bufferedReader.ready()) {
                                    bufferedReader.readLine();
                                } else {
                                    isEnfOfFile = true;
                                }
                            }
                            if (bufferedReader.ready()) {
                                String logFileLine = bufferedReader.readLine();
                                for (Trigger trigger : TriggerData.getInstance().getTriggersArray()) {
                                    if(trigger.getEnabled().getValue()){
                                        if (logFileLine.matches(".*" + trigger.getTriggerCommand().getValue() + ".*")) {
                                            if (trigger.getTriggerDelay().getValue() != 0) {
                                                getDelayedTriggersThread().addDelayedTrigger(trigger);
                                            } else {
                                                trigger.runTrigger();
                                            }
                                        }
                                    }
                                }
                            } else {
                                if(!isSearchActive){
                                    break;
                                }
                                Thread.sleep(250);
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        System.out.println("This should not happen");
                        e.getMessage();
                    }
                }
            }
        }
    }

    //  Thread holding triggers with delay
    class DelayedTriggersThread extends Thread {
        boolean isSearchActive;
        public ArrayList<FFTriggerDelayed> delayedTriggers = new ArrayList<>();

        public ArrayList<FFTriggerDelayed> getDelayedTriggers() {
            return delayedTriggers;
        }

        public void addDelayedTrigger(Trigger trigger) {
            FFTriggerDelayed delayedTrigger = new FFTriggerDelayed(trigger, (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + trigger.getTriggerDelay().getValue()));
            if (!getDelayedTriggers().contains(delayedTrigger)) {
                getDelayedTriggers().add(delayedTrigger);
            }
        }

        @Override
        public void run() {
            while (isSearchActive) {
                ArrayList<FFTriggerDelayed> executedTriggers = new ArrayList<>();
                for (FFTriggerDelayed triggerList : getDelayedTriggers()) {
                    if (triggerList.getTimeToDeploy() <= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())) {
                        triggerList.getTrigger().runTrigger();
                        executedTriggers.add(triggerList);
                    }
                }
                getDelayedTriggers().removeAll(executedTriggers);
                executedTriggers.clear();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    System.out.println("This should not happen");
                    e.getMessage();
                }
            }
        }

        //  Inner helper class
        class FFTriggerDelayed {
            private Trigger trigger;
            private long timeToDeploy;

            public Trigger getTrigger() {
                return trigger;
            }

            public long getTimeToDeploy() {
                return timeToDeploy;
            }

            public FFTriggerDelayed(Trigger trigger, long timeToDeploy) {
                this.trigger = trigger;
                this.timeToDeploy = timeToDeploy;
            }
        }
    }
}
