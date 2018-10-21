package exceptions;

import enums.AudioExceptionType;

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


}
