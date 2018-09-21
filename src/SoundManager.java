import javafx.scene.control.Alert;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class SoundManager {
    public final URL DEFAULT_AUDIO_FILE = this.getClass().getResource("sound/default.mp3");
    private LocalSoundManager localSoundManager;
    private DiscordSoundManager discordSoundManager;

    //  Singleton
    private SoundManager() {
        this.localSoundManager = new LocalSoundManager();
        this.discordSoundManager = new DiscordSoundManager();
    }

    public static SoundManager getInstance() {
        return SoundManagerHolder.INSTANCE;
    }

    public static class SoundManagerHolder {
        public static final SoundManager INSTANCE = new SoundManager();
    }

    //  Getters
    private DiscordSoundManager getDiscordSoundManager() {
        return this.discordSoundManager;
    }

    private LocalSoundManager getLocalSoundManager() {
        return this.localSoundManager;
    }

    //  Setters
    public void setLocalVolume(double volume) {
        getLocalSoundManager().setVolume(volume);
    }

    public void setDiscordVolume(double volume) {
        getDiscordSoundManager().setVolume((float) volume);
    }

    //  Methods
    private void alertWindow(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(LanguageData.getInstance().getMsg("AudioErrorTitle"));
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void createDiscordSoundPlayer(IGuild guild) {
        getDiscordSoundManager().createPlayer(guild);
        getDiscordSoundManager().getAudioPlayer().clear();
    }

    public void clearDiscordPlayer() {
        getDiscordSoundManager().clearPlayer();
    }

    public void playAudio(boolean isPersonal, Path audioPath) {
        this.playAudio(false, isPersonal, audioPath);
    }

    @SuppressWarnings("WeakerAccess")
    public void playAudio(boolean isPersonal, URL audioURL) {
        this.playAudio(false, isPersonal, audioURL);
    }

    @SuppressWarnings("WeakerAccess")
    public void playAudio(boolean isTable, boolean isPersonal, URL audioURL) {
        try {
            if (isPersonal) {
                getLocalSoundManager().playAudio(audioURL);
            } else {
                if (getDiscordSoundManager().audioPlayer != null) {
                    getDiscordSoundManager().playAudio(audioURL);
                } else {
                    if (isTable) {
                        alertWindow(LanguageData.getInstance().getMsg("AudioErrorDiscordNotConnected"));
                    } else {
                        getLocalSoundManager().playAudio(audioURL);
                    }
                }
            }
        } catch (MediaException e) {
            switch (e.getType()) {
                case MEDIA_UNAVAILABLE:
                    if (isTable) {
                        alertWindow(LanguageData.getInstance().getMsg("AudioErrorMediaUnavailable") + "\n" + e.getMessage());
                    } else {
                        System.out.println("Exception: " + e.getType() + ". Unavailable audio file, this should not happen, doing nothing");
                    }
                    break;
                case MEDIA_UNSPECIFIED:
                    if (isTable) {
                        alertWindow(LanguageData.getInstance().getMsg("AudioErrorMediaUnspecified") + "\n" + e.getMessage());
                    } else {
                        System.out.println("Exception: " + e.getType() + ". Unspecified audio file, this should not happen, playing default audio file");
                        this.playAudio(false, isPersonal, DEFAULT_AUDIO_FILE);
                    }
                    break;
                case MEDIA_UNSUPPORTED:
                    if (isTable) {
                        alertWindow(LanguageData.getInstance().getMsg("AudioErrorMediaUnsupported") + "\n" + e.getMessage());
                    } else {
                        System.out.println("Exception: " + e.getType() + ". Unsupported audio file, playing default audio file");
                        this.playAudio(false, isPersonal, DEFAULT_AUDIO_FILE);
                    }
                    break;
                default:
                    if (isTable) {
                        alertWindow(LanguageData.getInstance().getMsg("AudioErrorUnknown") + "\n" + e.getMessage());
                    } else {
                        System.out.println("Exception: " + e.getType() + ". Doing nothing");
                    }
                    break;
            }
        } catch (IOException e) {
            if (isTable) {
                alertWindow(LanguageData.getInstance().getMsg("AudioErrorMediaUnavailable") + "\n" + e.getMessage());
            } else {
                System.out.println("IOException " + e.getMessage() + ". Doing nothing");
            }
        } catch (UnsupportedAudioFileException e) {
            if (isTable) {
                alertWindow(LanguageData.getInstance().getMsg("AudioErrorMediaUnsupported") + "\n" + e.getMessage());
            } else {
                System.out.println("Exception: " + e.getMessage() + ". Unsupported audio file, playing default audio file");
                this.playAudio(false, DEFAULT_AUDIO_FILE);
            }
        }
    }

    public void playAudio(boolean isTable, boolean isPersonal, Path audioPath) {
        URL audioURL;
        if (Files.exists(audioPath)) {
            try {
                audioURL = audioPath.toUri().toURL();
            } catch (MalformedURLException e) {
                System.out.println(e.getMessage());
                System.out.println("This should never happen");
                return;
            }
        } else {
            if (isTable) {
                alertWindow(LanguageData.getInstance().getMsg("AudioErrorFileDoesNotExist"));
                return;
            } else {
                audioURL = DEFAULT_AUDIO_FILE;
            }
        }
        this.playAudio(isTable, isPersonal, audioURL);
    }

    //  Inner classes
    private class DiscordSoundManager {
        private AudioPlayer audioPlayer;
        private float playerVolume;
        private URL lastTrack;

        //  Constructor
        private DiscordSoundManager() {
            this.lastTrack = null;
            this.audioPlayer = null;
            this.playerVolume = (float) Settings.getInstance().getDiscordPlayerVolume() / 100;
        }

        //  Getters
        private AudioPlayer getAudioPlayer() {
            return this.audioPlayer;
        }

        private URL getLastTrack() {
            return this.lastTrack;
        }

        private void setLastTrack(URL audioURL) {
            this.lastTrack = audioURL;
        }

        //  Setters
        private void setVolume(float playerVolume) {
            setPlayerVolume(playerVolume);
            if (getAudioPlayer() != null) {
                getAudioPlayer().setVolume(playerVolume);
            }
        }

        private void setPlayerVolume(float playerVolume) {
            this.playerVolume = playerVolume;
        }

        //  Methods
        private void createPlayer(IGuild guild) {
            this.audioPlayer = new AudioPlayer(guild);
            getAudioPlayer().setVolume(playerVolume);
        }

        private void clearPlayer() {
            getAudioPlayer().clear();
            this.lastTrack = null;
            this.audioPlayer = null;
        }

        private void playAudio(URL audioURL) throws IOException, UnsupportedAudioFileException {
            if (getAudioPlayer() != null) {
                if (getAudioPlayer().getCurrentTrack() != null && getLastTrack().equals(audioURL)) {
                    return;
                }
                getAudioPlayer().queue(audioURL);
                setLastTrack(audioURL);
            }
        }
    }

    private class LocalSoundManager {
        private MediaPlayer audioPlayer;
        private double playerVolume;
        private ArrayList<LocalAudioQueue> audioQueue;

        //  Constructor
        private LocalSoundManager() {
            this.audioQueue = new ArrayList<>();
            this.playerVolume = Settings.getInstance().getLocalPlayerVolume();
        }

        //  Getters
        private MediaPlayer getAudioPlayer() {
            return this.audioPlayer;
        }

        private ArrayList<LocalAudioQueue> getAudioQueue() {
            return this.audioQueue;
        }

        private double getPlayerVolume() {
            return this.playerVolume;
        }

        //  Setters
        private void setPlayerVolume(double playerVolume) {
            this.playerVolume = playerVolume;
        }

        private void setVolume(double volume) {
            setPlayerVolume(volume);
            if (getAudioPlayer() != null) {
                getAudioPlayer().setVolume(volume);
            }
        }

        //  Methods
        private void playAudio(URL audioURL) throws MediaException {
            if (getAudioQueue().isEmpty()) {
                getAudioQueue().add(new LocalAudioQueue(audioURL, new Media(audioURL.toExternalForm())));
                newPlayer(getAudioQueue().get(0).getAudioStream());
            } else {
                if (!getAudioQueue().get(getAudioQueue().size() - 1).getAudioURL().equals(audioURL)) {
                    getAudioQueue().add(new LocalAudioQueue(audioURL, new Media(audioURL.toExternalForm())));
                }
            }

        }

        private void newPlayer(Media media) {
            this.audioPlayer = new MediaPlayer(media);
            getAudioPlayer().setVolume(getPlayerVolume());
            getAudioPlayer().play();
            getAudioPlayer().setOnEndOfMedia(() -> {
                getAudioQueue().remove(0);
                if (!getAudioQueue().isEmpty()) {
                    newPlayer(getAudioQueue().get(0).getAudioStream());
                }
            });
        }

        //  Special helper class
        private class LocalAudioQueue {
            //  Fields
            private URL audioURL;
            private Media audioStream;

            //  Constructor
            private LocalAudioQueue(URL audioURL, Media audioStream) {
                this.audioURL = audioURL;
                this.audioStream = audioStream;
            }

            //  Getters
            private URL getAudioURL() {
                return audioURL;
            }

            private Media getAudioStream() {
                return audioStream;
            }
        }
    }

}
