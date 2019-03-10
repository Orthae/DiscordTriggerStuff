package com.github.orthae.discordtriggerstuff.exceptions;

import com.github.orthae.discordtriggerstuff.enums.DiscordExceptionType;

public class DiscordException extends Exception {

    //  Fields
    private DiscordExceptionType exceptionType;

    //  Constructor
    public DiscordException(DiscordExceptionType exceptionType) {
        super(exceptionType.toString());
        this.exceptionType = exceptionType;
    }

    //  Getters
    public DiscordExceptionType getExceptionType() {
        return exceptionType;
    }

}
