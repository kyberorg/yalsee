package eu.yals.test.ui.vaadin.usage;

import eu.yals.test.ui.vaadin.VaadinTest;
import eu.yals.test.ui.vaadin.pageobjects.HomeViewPageObject;
import eu.yals.test.ui.vaadin.pageobjects.external.*;
import eu.yals.test.utils.elements.YalsElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * Contains IDN URL multi step tests for Front page
 *
 * @since 2.5
 */
public class IdnTestIT extends VaadinTest {
  private HomeViewPageObject homeView;

  public void openUrl() {
    open("/");
    homeView = HomeViewPageObject.getPageObject(getDriver());
  }

  @Test
  public void russianUrl() {
    storeAndOpenSavedUrl("http://кто.рф");

    // verify that KtoRF opened
    YalsElement eggs = $$(KtoRf.DIV_EGGS);
    eggs.shouldExist();
  }

  @Test
  public void swedishUrl() {
    storeAndOpenSavedUrl("https://räksmörgås.josefsson.org");

    // verify that swedish site opened
    YalsElement h1 = $$(JosefssonOrg.H1);
    h1.shouldExist();
    h1.textHas(JosefssonOrg.H1_TEXT);
  }

  @Test
  public void finnishUrl() {
    storeAndOpenSavedUrl("https://sää.fi");
    YalsElement logo = $$(ForecaFi.LOGO);
    logo.shouldExist();
    logo.shouldHaveAttr("title", ForecaFi.LOGO_TITLE);
  }

  @Test
  public void arabicUrl() {
    storeAndOpenSavedUrl("http://موقع.وزارة-الاتصالات.مصر/");

    // verify that opens page of IT ministry of Egypt
    Assert.assertEquals(EgyptianMinistryOfIT.TITLE_TEXT, getPageTitle());
  }

  @Test
  public void taiwaneseUrl() {
    storeAndOpenSavedUrl("http://中文.tw/");

    YalsElement navTable = $$(ZhongwenTw.NAV_TABLE);
    navTable.shouldExist();
  }

  @Test
  public void polishUrl() {
    storeAndOpenSavedUrl("http://żółć.pl");
    Assert.assertEquals(ZolcPl.TITLE_TEXT, getPageTitle());
  }

  @Test
  public void germanUrl() {
    storeAndOpenSavedUrl("http://www.travemünde.de/");
    Assert.assertEquals(TravemundeDe.TITLE_TEXT, getPageTitle());
  }

  @Test
  public void estonianUrl() {
    storeAndOpenSavedUrl("https://sõnaveeb.ee");
    Assert.assertEquals(SonaveebEe.TITLE_TEXT, getPageTitle());
  }

  @Test
  public void multiLanguageUrl() {
    storeAndOpenSavedUrl("http://€.linux.it");

    //verify that opens Euro Linux Page
    YalsElement h1 = $$(EuroLinuxIt.H1);
    h1.shouldExist();
    h1.shouldHaveText(EuroLinuxIt.H1_TEXT);
  }

  private void storeAndOpenSavedUrl(String urlToStore) {
    openUrl();
    homeView.pasteValueInFormAndSubmitIt(urlToStore);
    String shortLink = homeView.getSavedUrl();
    open(shortLink);
  }

  private String getPageTitle() {
    return getDriver().getTitle();
  }
}