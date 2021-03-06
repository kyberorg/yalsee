package io.kyberorg.yalsee;

/**
 * List of application endpoints.
 *
 * @since 2.0
 */
public final class Endpoint {
    private Endpoint() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Endpoints accessible via browser.
     */
    public static class UI {
        /**
         * Application home page.
         */
        public static final String HOME_PAGE = "";

        /**
         * Page that will appear, when user hits location which is not exist nor served by application.
         */
        public static final String PAGE_404 = "errors/404page";

        /**
         * Page that will appear, when user hits ident which is not exist.
         */
        public static final String IDENT_404 = "errors/404ident";

        /**
         * Page that will shown when application hits server-side error.
         */
        public static final String ERROR_PAGE_500 = "errors/500";

        /**
         * Page that will shown when application hits fatal error and became inaccessible.
         */
        public static final String ERROR_PAGE_503 = "errors/503";

        /**
         * Page for debugging staff (avoid adding it in PROD mode).
         */
        public static final String DEBUG_PAGE = "debug";

        /**
         * Page for displaying software info (avoid adding it in PROD mode).
         */
        public static final String APP_INFO_PAGE = "appInfo";
    }

    /**
     * API Endpoints.
     */
    public static class Api {
        /**
         * API for manipulating with links.
         * <p>
         * /api/links
         */
        public static final String LINKS_API = "/api/links";

        /**
         * API for deleting links.
         * <p>
         * DELETE /api/links/{ident}
         */
        public static final String DELETE_LINKS_API = LINKS_API + "/{ident}";

        /**
         * API for storing links.
         * <p>
         * POST /api/store
         */
        public static final String STORE_API = "/api/store";

        /**
         * API for Getting links.
         * <p>
         * GET /api/link/{ident}
         */
        public static final String LINK_API = "/api/link";

        /**
         * Mattermost API.
         * <p>
         * POST /api/mm
         */
        public static final String MM_API = "/api/mm";

        /**
         * Show availability of Telegram API.
         * <p>
         * GET /api/tg/status
         */
        public static final String TELEGRAM_STATUS_API = "/api/tg/status";

        /**
         * QR Code API.
         * <p>
         * GET /api/qrCode/{ident}/{size}
         */
        public static final String QR_CODE_API = "/api/qrCode";

        /**
         * Page 404 for API requests.
         */
        public static final String PAGE_404 = "/errors/404api";

    }

    public static class Static {
        /**
         * Old good file for search engines.
         */
        public static final String ROBOTS_TXT = "/robots.txt";

        /**
         * Because there are not only robots behind the scenes.
         */
        public static final String HUMANS_TXT = "/humans.txt";

        /**
         * Application icon.
         */
        public static final String FAVICON_ICO = "/favicon.ico";

        /**
         * Sitemap, which is needed for good SEO.
         */
        public static final String SITEMAP_XML = "/sitemap.xml";

        /**
         * Application offline page.
         */
        public static final String APP_OFFLINE_PAGE = "offline-page.html";
    }

    /**
     * Tech and temp.
     */
    public static class TNT {
        /**
         * Endpoint for redirecting to long links. Not intended to be accessed directly
         */
        public static final String REDIRECTOR = "redirector";

        /**
         * Catch-all errors endpoint. Served by:
         * <p>
         * {@link YalseeErrorController}
         */
        public static final String ERROR_PAGE = "/error";

        /**
         * Serves offline page with status 503.
         * <p>
         * {@link AppOfflineController}
         */
        public static final String APP_OFFLINE = "/app-offline";

        /**
         * Server Error Loopback View. Intended to be used only within {@link ServerErrorView}.
         */
        public static final String SERVER_ERROR_LOOP = "server-error-loop";

        /**
         * Site Preview image for SEO.
         */
        public static final String PREVIEW_IMAGE = "/preview.png";
    }

    /**
     * Endpoints or values used in Application tests only.
     */
    public static class ForTests {
        /**
         * Just slash symbol (/). Application base.
         */
        public static final String SLASH_BASE = "/";
        /**
         * Path for getting link.
         */
        public static final String LINK_API = Api.LINK_API + "/";
        /**
         * Path for retrieving QR code.
         */
        public static final String QR_CODE_API = Api.QR_CODE_API + "/";

        // those two endpoints are used only in tests to simulate application error
        /**
         * General endpoint, which always produces error.
         */
        public static final String FAIL_ENDPOINT = "/failPoint";
        /**
         * API endpoint, which always produces error.
         */
        public static final String FAIL_API_ENDPOINT = "/api/failPoint";
    }
}
