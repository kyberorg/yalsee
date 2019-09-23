package eu.yals.test;

public class TestApp {
    public static class Properties {
        public static final String TEST_URL = "testUrl";
        public static final String SERVER_PORT = "port";
    }

    public static class Selenide {
        static final String BROWSER = "selenide.browser";
        public static final String TIMEOUT = "selenide.timeout";
        public static final String REPORT_DIR = "yals.selenide.reportdir";
    }
}