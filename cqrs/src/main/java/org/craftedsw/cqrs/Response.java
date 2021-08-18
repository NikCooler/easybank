package org.craftedsw.cqrs;

/**
 * @author Nikolay Smirnov
 */
public final class Response {

    private final Integer status;
    private final String  message;
    private final Object  payload;

    private Response(Integer status, String message, Object payload) {
        this.status  = status;
        this.message = message;
        this.payload = payload;
    }

    public static Response of(HttpCode httpCode) {
        return new Response(httpCode.getCode(), httpCode.getMessage(), null);
    }

    public static Response of(HttpCode httpCode, Object payload) {
        return new Response(httpCode.getCode(), httpCode.getMessage(), payload);
    }

    public static Response of(Integer status, String message) {
        return new Response(status, message, null);
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getPayload() {
        return payload;
    }
}
