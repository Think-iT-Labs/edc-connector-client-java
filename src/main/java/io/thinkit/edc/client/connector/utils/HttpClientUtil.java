package io.thinkit.edc.client.connector.utils;

import static io.thinkit.edc.client.connector.utils.Constants.*;

public class HttpClientUtil {
    public static boolean isSuccessful(Integer value) {
        return value >= 200 && value <= 299;
    }
}
