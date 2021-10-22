package integration.helper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;

import static org.easybank.util.SneakyThrow.doWithRuntimeException;

/**
 * @author Nikolay Smirnov
 */
public final class TestUtils {

    private TestUtils() {}

    /**
     * Read json-content from the file as {@link String}
     */
    public static String readFromJsonFile(String fileName, Class<?> clazz) {
        ObjectMapper om = new ObjectMapper();
        return doWithRuntimeException(() -> {
            InputStream resourceAsStream = clazz.getResourceAsStream("/" + fileName);
            return om.readTree(resourceAsStream).toString();
        });
    }

}
