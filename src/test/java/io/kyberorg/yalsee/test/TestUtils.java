package io.kyberorg.yalsee.test;

import io.kyberorg.yalsee.constants.App;
import io.kyberorg.yalsee.constants.Header;
import io.kyberorg.yalsee.constants.MimeType;
import io.kyberorg.yalsee.json.YalsErrorJson;
import io.kyberorg.yalsee.test.utils.HostIdentifier;
import io.kyberorg.yalsee.utils.AppUtils;
import kong.unirest.Headers;
import kong.unirest.HttpResponse;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Useful stuff for testing
 *
 * @since 2.0
 */
public class TestUtils {

    /**
     * Result contains JSON.
     *
     * @param result string with http response
     */
    public static void assertResultIsJson(final HttpResponse<String> result) {
        assertNotNull(result);
        assertTrue(result.getHeaders().containsKey(Header.CONTENT_TYPE));
        assertFalse(result.getHeaders().get(Header.CONTENT_TYPE).isEmpty());
        assertTrue(
                result.getHeaders().getFirst(Header.CONTENT_TYPE).contains(MimeType.APPLICATION_JSON));
    }

    /**
     * Result is {@link YalsErrorJson}.
     *
     * @param result string with http response
     */
    public static void assertResultIsYalsErrorJson(final HttpResponse<String> result) {
        assertTrue(
                TestUtils.isValidErrorJson(result),
                "Response is not valid " + YalsErrorJson.class.getSimpleName());
    }

    /**
     * HTTP response body is not empty.
     *
     * @param response string with http response
     */
    public static void assertResponseBodyNotEmpty(final HttpResponse<String> response) {
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertNotEquals("", response.getBody().trim());
    }

    /**
     * Response has given mimeType.
     *
     * @param mimeType mime type to check
     * @param response response having mime-type
     */
    public static void assertContentType(final String mimeType, final HttpResponse<String> response) {
        assertNotNull(mimeType);
        assertNotNull(response);

        Headers headers = response.getHeaders();
        assertNotNull(headers);
        String contentType = headers.getFirst(Header.CONTENT_TYPE);
        String actualMimeType = extractMime(contentType);
        assertEquals(mimeType, actualMimeType);
    }

    /**
     * Content of request is not empty.
     *
     * @param response http response
     */
    public static void assertContentNotEmpty(final HttpResponse<String> response) {
        assertNotNull(response);
        Headers headers = response.getHeaders();
        assertNotNull(headers);
        String contentLengthStr = headers.getFirst(Header.CONTENT_LENGTH);

        assertNotNull(contentLengthStr);
        int contentLength = 0;
        try {
            contentLength = Integer.parseInt(contentLengthStr);
        } catch (NumberFormatException e) {
            fail(String.format("%s header value is not a number", Header.CONTENT_LENGTH));
        }
        assertTrue(contentLength > 0, "Content is empty");
    }

    /**
     * Defines URL of server to run tests against.
     *
     * @return string with Server+Port
     */
    public static String getTestUrl() {
        final int serverPort = Integer.parseInt(System.getProperty(App.Properties.SERVER_PORT, "8080"));
        final String localUrl;

        if(StringUtils.isNotBlank(System.getProperty(TestApp.Properties.TEST_URL, ""))) {
            return System.getProperty(TestApp.Properties.TEST_URL);
        }

        TestApp.RunMode runMode = TestApp.RunMode.valueOf(
                System.getProperty(TestApp.Properties.TEST_RUN_MODE, TestApp.RunMode.LOCAL.name()));

        switch (runMode) {
            case GRID:
                localUrl = System.getProperty(TestApp.Properties.TEST_URL);
                break;
            case LOCAL:
            default:
                localUrl = String.format("http://localhost:%d", serverPort);
                break;
        }

        return localUrl;
    }

    /**
     * Determines if tests are running locally (localhost).
     *
     * @return true if locally, false if not
     */
    public static boolean isLocalRun() {
        String testUrl = getTestUrl();
        String localhost = "localhost";

        return testUrl.contains(localhost);
    }

    /**
     * Hostname of executing machine.
     *
     * @return string with hostname
     */
    public static String hostName() {
        return HostIdentifier.getHostName();
    }

    /**
     * Provides current time for test naming.
     *
     * @return string with timestamp
     */
    public static String timeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd-HHmm");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    /**
     * Following needed because in may contain something like 'application/json;encoding=UTF8'.
     *
     * @param contentType Content-Type header like 'application/json;encoding=UTF8'
     * @return string which contains content type without encoding
     */
    private static String extractMime(String contentType) {
        assertNotNull(contentType);

        String[] contentTypeParts = contentType.split(";");
        if (contentTypeParts.length > 1) {
            return contentTypeParts[0];
        } else {
            return contentType;
        }
    }

    private static boolean isValidErrorJson(HttpResponse<String> result) {
        String body = result.getBody();
        try {
            YalsErrorJson errorJson = AppUtils.GSON.fromJson(body, YalsErrorJson.class);
            assertNotNull(errorJson);
            boolean hasNotEmptyMessageField = errorJson.getMessage() != null;
            boolean hasNotEmptyTimestampField = errorJson.getTimestamp() != null;

            return hasNotEmptyMessageField || hasNotEmptyTimestampField;
        } catch (Exception e) {
            return false;
        }
    }
}