package io.kyberorg.yalsee.test.ui.usage;

import com.codeborne.selenide.SelenideElement;
import io.kyberorg.yalsee.test.pageobjects.HomePageObject;
import io.kyberorg.yalsee.test.pageobjects.external.*;
import io.kyberorg.yalsee.test.ui.SelenideTest;
import io.kyberorg.yalsee.test.utils.SelenideUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.kyberorg.yalsee.test.pageobjects.VaadinPageObject.waitForVaadin;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains IDN URL multi step tests for Front page.
 *
 * @since 2.5
 */
@SpringBootTest
@SuppressWarnings("SpellCheckingInspection")
public class IdnTest extends SelenideTest {

    @BeforeEach
    public void beforeTest() {
        tuneDriverWithCapabilities();
        open("/");
        waitForVaadin();
    }

    @Test
    public void russianUrl() {
        HomePageObject.storeAndOpenSavedUrl("http://кто.рф");
        // verify that KtoRF opened
        SelenideElement eggs = $(KtoRf.DIV_EGGS);
        eggs.should(exist);
    }

    @Test
    public void finnishUrl() {
        HomePageObject.storeAndOpenSavedUrl("https://sää.fi");
        SelenideElement logo = $(ForecaFi.LOGO);
        logo.should(exist);
        logo.shouldHave(attribute("title", ForecaFi.LOGO_TITLE));
    }

    @Test
    public void arabicUrl() {
        HomePageObject.storeAndOpenSavedUrl("https://www.101domain.com/عرب.htm");

        //needed because site site loads way too long
        SelenideUtils.waitUntilSiteLoads(40);

        // verify that opens page of Registation of arabic names
        SelenideElement uniqueClassElement = $(ArabUrlRegistrar.MAIN_CLASS);
        uniqueClassElement.should(exist);
    }

    @Test
    public void taiwaneseUrl() {
        HomePageObject.storeAndOpenSavedUrl("http://中文.tw/");

        //needed because site site loads way too long
        SelenideUtils.waitUntilSiteLoads(30);

        SelenideElement navTable = $(ZhongwenTw.NAV_TABLE);
        navTable.should(exist);
    }

    @Test
    public void germanUrl() {
        HomePageObject.storeAndOpenSavedUrl("http://www.travemünde.de/");
        assertEquals(TravemundeDe.TITLE_TEXT, SelenideUtils.getPageTitle());
    }

    @Test
    public void estonianUrl() {
        HomePageObject.storeAndOpenSavedUrl("https://sõnaveeb.ee");
        assertEquals(SonaveebEe.TITLE_TEXT, SelenideUtils.getPageTitle());
    }

    /**
     * Multiple languages.
     */
    @Test
    public void multiLanguageUrl() {
        HomePageObject.storeAndOpenSavedUrl("http://€.linux.it");

        // verify that opens Euro Linux Page
        SelenideElement h1 = $(EuroLinuxIt.H1);
        h1.should(exist);
        h1.shouldHave(text(EuroLinuxIt.H1_TEXT));
    }

}