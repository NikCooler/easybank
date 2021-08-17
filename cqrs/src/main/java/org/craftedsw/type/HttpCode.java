package org.craftedsw.type;

/**
 * @author Nikolay Smirnov
 */
public enum HttpCode {

    OK(200,"OK"),
    BAD_REQUEST(400,"Bad Request");

    private final int    code;
    private final String message;

    HttpCode(int code, String message) {
        this.code    = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
