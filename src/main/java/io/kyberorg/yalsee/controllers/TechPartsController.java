package io.kyberorg.yalsee.controllers;

import com.beust.jcommander.Strings;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import io.kyberorg.yalsee.Endpoint;
import io.kyberorg.yalsee.constants.App;
import io.kyberorg.yalsee.constants.MimeType;
import io.kyberorg.yalsee.utils.AppUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.util.Date;

import static io.kyberorg.yalsee.constants.HttpCode.STATUS_500;

/**
 * Handles tech resources. Currently only Fail endpoint for tests.
 *
 * @since 2.0
 */
@Controller
public class TechPartsController {

    private static final double HIGH_PRIORITY = 1.0;
    private static final double APP_INFO_PAGE_PRIORITY = 0.7;
    private final AppUtils appUtils;

    /**
     * Standard Spring constructor for Spring Boot autowiring.
     *
     * @param appUtils application utils
     */
    public TechPartsController(final AppUtils appUtils) {
        this.appUtils = appUtils;
    }

    /**
     * This endpoint meant to be used only in application tests for simulating application fails.
     *
     * @return always throws RuntimeException
     */
    @RequestMapping(method = RequestMethod.GET,
            value = {Endpoint.ForTests.FAIL_ENDPOINT, Endpoint.ForTests.FAIL_API_ENDPOINT})
    public String iWillAlwaysFail() {
        throw new RuntimeException("I will always fail");
    }

    /**
     * Generates sitemap.xml dynamically.
     *
     * @param response response object to write status to
     * @return string with generated XML
     */
    @RequestMapping(method = RequestMethod.GET, value = Endpoint.Static.SITEMAP_XML,
            produces = MimeType.APPLICATION_XML)
    public @ResponseBody String getSitemap(final HttpServletResponse response) {
        String baseUrl = appUtils.getServerUrl();
        WebSitemapGenerator sitemapGenerator;

        try {
            sitemapGenerator = new WebSitemapGenerator(baseUrl);
            WebSitemapUrl mainPage = new WebSitemapUrl.Options(baseUrl + "/" +  Endpoint.UI.HOME_PAGE)
                    .lastMod(new Date()).priority(HIGH_PRIORITY).changeFreq(ChangeFreq.ALWAYS)
                    .build();

            WebSitemapUrl appInfo = new WebSitemapUrl.Options(baseUrl + "/" + Endpoint.UI.APP_INFO_PAGE)
                    .lastMod(new Date()).priority(APP_INFO_PAGE_PRIORITY).changeFreq(ChangeFreq.WEEKLY)
                    .build();

            sitemapGenerator.addUrl(mainPage).addUrl(appInfo);
        } catch (MalformedURLException e) {
            response.setStatus(STATUS_500);
            throw new RuntimeException("Server URL is unconfigured - cannot generate Sitemap.xml");
        }

        response.setContentType(MimeType.APPLICATION_XML);
        return Strings.join("", sitemapGenerator.writeAsStrings());
    }

    /**
     * Provides robots.txt in dynamic way.
     *
     * @return robots.txt content
     */
    @RequestMapping(method = RequestMethod.GET, value = Endpoint.Static.ROBOTS_TXT, produces = MimeType.TEXT_PLAIN)
    public @ResponseBody String getRobotsTxt() {
        StringBuilder builder = new StringBuilder();
        builder.append("User-agent: ").append("*").append(App.NEW_LINE);
        builder.append("Crawl-delay: ").append("1000").append(App.NEW_LINE);

        if (appUtils.areCrawlersAllowed()) {
            builder.append("Allow: ");
        } else {
           builder.append("Disallow: ");
        }
        builder.append("/").append(App.NEW_LINE);

        builder.append("Disallow: ").append("/vaadinServlet/").append(App.NEW_LINE);
        builder.append("Sitemap: ").append(appUtils.getServerUrl()).append(Endpoint.Static.SITEMAP_XML);
        return builder.toString();
    }

}
