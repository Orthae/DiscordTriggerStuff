package com.github.orthae.discordtriggerstuff.exceptions;

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
}
