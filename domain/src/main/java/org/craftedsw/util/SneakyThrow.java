package org.craftedsw.util;

import java.util.concurrent.Callable;

/**
 * @author Nikolay Smirnov
 */
public class SneakyThrow {

    /**
     * Throws checked exceptions as unchecked
     *
     * @param executable a functional interface
     */
    public static void doWithRuntimeException(Executable executable) {
        try {
            executable.execute();
        } catch (Exception ex) {
            sneakyThrow(ex);
        }
    }

    /**
     * Throws checked exceptions as unchecked
     *
     * @param callable a functional interface
     *
     * @return V
     */
    public static <V> V doWithRuntimeException(Callable<V> callable) {
        V result = null;
        try {
            result = callable.call();
        } catch (Exception ex) {
            sneakyThrow(ex);
        }
        return result;
    }

    /**
     * Throw checked exception as unchecked {@link RuntimeException}
     *
     * @param ex Checked / Unchecked exception
     * @throws {@link RuntimeException}
     */
    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void sneakyThrow(Throwable ex) throws E {
        throw (E) ex;
    }

}