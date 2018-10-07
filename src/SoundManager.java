import exceptions.AudioException;
import enums.AudioExceptionType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;

public class SoundManager {
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

    //  Fields
    public final URL DEFAULT_AUDIO_FILE = this.getClass().getResource("sound/default.mp3");
    private LocalSoundManager localSoundManager;
    private DiscordSoundManager discordSoundManager;

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
    public void createDiscordSoundPlayer(IGuild guild) {
        getDiscordSoundManager().createPlayer(guild);
        getDiscordSoundManager().getAudioPlayer().clear();
    }

    public void clearDiscordPlayer() {
        getDiscordSoundManager().clearPlayer();
    }

    public void playDebug(boolean isPersonal, URL audioUrl) throws AudioException {
        if(isPersonal){
            getLocalSoundManager().playAudio(audioUrl);
        } else {
            if(getDiscordSoundManager().getAudioPlayer() != null){
                getDiscordSoundManager().playAudio(audioUrl);
            } else {
                throw new AudioException(AudioExceptionType.DISCORD_NOT_CONNECTED);
            }
        }
    }

    public void playDebug(boolean isPersonal, Path audioPath) throws AudioException {
        if(!audioPath.toFile().exists()){
            throw new AudioException(AudioExceptionType.UNAVAILABLE_AUDIO_FILE);
        }
        try {
                playDebug(isPersonal, audioPath.toUri().toURL());
            } catch (MalformedURLException e) {
            Logger.getInstance().log("MalformedURLException thrown by playDebug(boolean, Path) this should not happen");
            }
    }

    public void play(boolean isPersonal, Path audioPath){
        if(!audioPath.toFile().exists()){
            Logger.getInstance().log("Couldn't find audio file: " + "\"" + audioPath.toAbsolutePath() + "\". Playing default sound file");
            play(isPersonal, DEFAULT_AUDIO_FILE);
            return;
        }
        try {
             play(isPersonal, audioPath.toUri().toURL());
        } catch (MalformedURLException e) {
            Logger.getInstance().log("MalformedURLException thrown by play(boolean, Path) this should not happen");
            play(isPersonal, DEFAULT_AUDIO_FILE);
        }
    }

    public void play(boolean isPersonal, URL audioUrl) {
        try {
            if (isPersonal) {
                getLocalSoundManager().playAudio(audioUrl);
            } else {
                if (getDiscordSoundManager().getAudioPlayer() != null) {
                    getDiscordSoundManager().playAudio(audioUrl);
                } else {
                    getLocalSoundManager().playAudio(audioUrl);
                }
            }
        } catch (AudioException e) {
            Logger.getInstance().log("AudioException thrown while playing audio \""+ audioUrl.getPath() +"\". Exception type \"" + e.getExceptionType() +
            "\". Playing default sound");
            try {
                switch (e.getExceptionType()) {
                    case IO_DISCORD:
                    case UNSUPPORTED_DISCORD_FILE:
                        getDiscordSoundManager().playAudio(DEFAULT_AUDIO_FILE);
                        break;
                    case UNSUPPORTED_AUDIO_FILE:
                    case UNSPECIFIED_AUDIO_FILE:
                    case UNKNOWN:
                    case UNAVAILABLE_AUDIO_FILE:
                    case CORRUPTED_AUDIO_FILE:
                        getLocalSoundManager().playAudio(DEFAULT_AUDIO_FILE);
                        break;
                }
            } catch (AudioException e1) {
                Logger.getInstance().log("AudioException thrown while playing default sound. Exception type: \"" + e1.getExceptionType() + "\".");
            }
        }
    }

    //  Inner classes
    private class DiscordSoundManager {
        private AudioPlayer audioPlayer;
        private URL lastTrack;

        //  Constructor
        private DiscordSoundManager() {
            this.lastTrack = null;
            this.audioPlayer = null;
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
            if (getAudioPlayer() != null) {
                getAudioPlayer().setVolume(playerVolume);
            }
        }

        //  Methods
        private void createPlayer(IGuild guild) {
            this.audioPlayer = new AudioPlayer(guild);
            getAudioPlayer().setVolume((float) Settings.getInstance().getDiscordPlayerVolume() / 100);
        }

        private void clearPlayer() {
            getAudioPlayer().clear();
            this.lastTrack = null;
            this.audioPlayer = null;
        }

        private void playAudio(URL audioURL) throws AudioException {
            System.out.println(getAudioPlayer());
            if (getAudioPlayer() != null) {
                    if (getAudioPlayer().getCurrentTrack() != null && getLastTrack().equals(audioURL)) {
                        return;
                    }
                    try {
                        getAudioPlayer().queue(audioURL);
                        setLastTrack(audioURL);
                    } catch (UnsupportedAudioFileException e) {
                        throw new AudioException(AudioExceptionType.UNSUPPORTED_DISCORD_FILE);
                    } catch (IOException e) {
                        throw new AudioException(AudioExceptionType.IO_DISCORD);
                    }
            }
        }
    }

    private class LocalSoundManager {
        private MediaPlayer audioPlayer;
        private ArrayList<LocalAudioQueue> audioQueue;

        //  Constructor
        private LocalSoundManager() {
            this.audioQueue = new ArrayList<>();
        }

        //  Getters
        private MediaPlayer getAudioPlayer() {
            return this.audioPlayer;
        }

        private ArrayList<LocalAudioQueue> getAudioQueue() {
            return this.audioQueue;
        }

        //  Setters

        private void setVolume(double volume) {
            if (getAudioPlayer() != null) {
                getAudioPlayer().setVolume(volume);
            }
        }

        //  Methods
        private void playAudio(URL audioURL) throws AudioException {
            if (getAudioQueue().isEmpty()) {
                try {
                    getAudioQueue().add(new LocalAudioQueue(audioURL, new Media(audioURL.toExternalForm())));
                    newPlayer(getAudioQueue().get(0).getAudioStream());
                } catch (MediaException e) {
                    switch (e.getType()) {
                        case MEDIA_CORRUPTED:
                            throw new AudioException(AudioExceptionType.CORRUPTED_AUDIO_FILE);
                        case MEDIA_INACCESSIBLE:
                        case MEDIA_UNAVAILABLE:
                            throw new AudioException(AudioExceptionType.UNAVAILABLE_AUDIO_FILE);
                        case MEDIA_UNSPECIFIED:
                            throw new AudioException(AudioExceptionType.UNSPECIFIED_AUDIO_FILE);
                        case MEDIA_UNSUPPORTED:
                            throw new AudioException(AudioExceptionType.UNSUPPORTED_AUDIO_FILE);
                        case OPERATION_UNSUPPORTED:
                        case PLAYBACK_ERROR:
                        case PLAYBACK_HALTED:
                        case UNKNOWN:
                            throw new AudioException(AudioExceptionType.UNKNOWN);
                    }
                }
            } else {
                if (!getAudioQueue().get(getAudioQueue().size() - 1).getAudioURL().equals(audioURL)) {
                    getAudioQueue().add(new LocalAudioQueue(audioURL, new Media(audioURL.toExternalForm())));
                }
            }

        }

        private void newPlayer(Media media) {
            this.audioPlayer = new MediaPlayer(media);
            getAudioPlayer().setVolume(Settings.getInstance().getLocalPlayerVolume());
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
