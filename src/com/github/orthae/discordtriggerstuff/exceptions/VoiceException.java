package com.github.orthae.discordtriggerstuff.exceptions;

import com.github.orthae.discordtriggerstuff.LanguageData;
import com.github.orthae.discordtriggerstuff.enums.VoiceExceptionType;

public class VoiceException extends Exception {
    //  Fields
    private VoiceExceptionType exceptionType;

    //  Constructor
    public VoiceException(VoiceExceptionType exceptionType) {
        super(exceptionType.toString());
        this.exceptionType = exceptionType;
    }

    //  Getters
    public VoiceExceptionType getExceptionType() {
        return exceptionType;
    }

    public String getAlertMessage(){
        switch (exceptionType){
            case REQUEST_COUNT_EXCEEDED:
               return  LanguageData.getInstance().getMsg("AlertVoiceRequestCap");
            case REQUEST_TO_LARGE:
                return LanguageData.getInstance().getMsg("AlertVoiceRequestToLarge");
            case LANGUAGE_NOT_SUPPORTED:
                return LanguageData.getInstance().getMsg("AlertVoiceLanguageNotSupported");
            case LANGUAGE_NOT_SPECIFIED:
                return LanguageData.getInstance().getMsg("AlertVoiceLanguageNotSpecified");
            case TEXT_NOT_SPECIFIED:
                return LanguageData.getInstance().getMsg("AlertVoiceTextNotSpecified");
            case API_KEY_NOT_AVAILABLE:
                return LanguageData.getInstance().getMsg("AlertVoiceKeyNotAvailable");
            case API_KEY_NOT_DEFINED:
                return LanguageData.getInstance().getMsg("AlertVoiceKeyNotDefined");
            case SSML_NOT_SUPPORTED:
                return LanguageData.getInstance().getMsg("AlertVoiceSSMLNotSupported");
            case IO:
                return LanguageData.getInstance().getMsg("AlertVoiceIO");
            case ILLEGAL_CHAR:
                return LanguageData.getInstance().getMsg("AlertVoiceIllegalChar");
            case UNKNOWN:
                return LanguageData.getInstance().getMsg("AlertVoiceUnknown");
            default:
                return LanguageData.getInstance().getMsg("AlertVoiceDefault");
        }
    }
}
