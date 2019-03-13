package com.github.orthae.discordtriggerstuff.exceptions;

import com.github.orthae.discordtriggerstuff.LanguageData;
import com.github.orthae.discordtriggerstuff.enums.AudioExceptionType;

public class AudioException extends Exception {
    //  Fields
    private AudioExceptionType exceptionType;

    //  Constructor
    public AudioException(AudioExceptionType exceptionType) {
        super(exceptionType.toString());
        this.exceptionType = exceptionType;
    }

    //  Getters
    public AudioExceptionType getExceptionType() {
        return exceptionType;
    }

    public String getAlertMessage(){
        switch (getExceptionType()) {
            case UNSUPPORTED_DISCORD_FILE:
                return LanguageData.getInstance().getMsg("AlertAudioUnsupported");
            case DISCORD_NOT_CONNECTED:
                return LanguageData.getInstance().getMsg("AlertAudioDiscordNotConnected");
            case UNSUPPORTED_AUDIO_FILE:
                return LanguageData.getInstance().getMsg("AlertAudioUnsupported");
            case IO_DISCORD:
                return LanguageData.getInstance().getMsg("AlertIOException");
            case UNSPECIFIED_AUDIO_FILE:
                return LanguageData.getInstance().getMsg("AlertAudioUnspecified");
            case UNAVAILABLE_AUDIO_FILE:
                return LanguageData.getInstance().getMsg("AlertAudioUnavailable");
            case CORRUPTED_AUDIO_FILE:
                return LanguageData.getInstance().getMsg("AlertAudioCorrupted");
            case UNKNOWN:
            default:
                return LanguageData.getInstance().getMsg("AlertAudioUnknown");
        }


    }
}
