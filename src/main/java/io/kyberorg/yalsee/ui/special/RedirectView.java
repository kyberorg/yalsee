package io.kyberorg.yalsee.ui.special;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.kyberorg.yalsee.Endpoint;
import io.kyberorg.yalsee.constants.App;
import io.kyberorg.yalsee.constants.Header;
import io.kyberorg.yalsee.exception.NeedForRedirectException;
import io.kyberorg.yalsee.ui.MainView;
import io.kyberorg.yalsee.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static io.kyberorg.yalsee.constants.HttpCode.*;

@SuppressWarnings("FieldCanBeLocal")
@Slf4j
@SpringComponent
@UIScope
@CssImport("./css/common_styles.css")
@Route(value = Endpoint.TNT.REDIRECTOR, layout = MainView.class)
@PageTitle("Yalsee: Redirect Page")
public class RedirectView extends VerticalLayout implements HasErrorParameter<NeedForRedirectException> {
    private static final String TAG = "[" + RedirectView.class.getSimpleName() + "]";

    private final Span firstTextLine = new Span("According to our records");
    private final Anchor originLink = new Anchor();

    private final Span secondTextLine = new Span("is short link for");
    private final Anchor targetLink = new Anchor();

    private final Span redirectLine = new Span();
    private final Span rdrPreText = new Span("You will be redirected in ");
    private final Span rdrCounter = new Span();
    private final Span rdrUnit = new Span(" seconds... ");
    private final Span rdrClickText = new Span("or click ");
    private final Anchor rdrHereLink = new Anchor();
    private final Span rdrPostText = new Span(", if you too busy to wait.");

    private final Span nbLine = new Span();
    private final Span nb = new Span("NB! ");
    private final Span nbPreText = new Span("You can add ");
    private final Span nbSymbol = new Span();
    private final Span nbPostText = new Span(" symbol to your short link to bypass this page.");

    private final Page page = UI.getCurrent().getPage();
    private FeederThread thread;

    private String origin;
    private String target;

    private final AppUtils appUtils;

    /**
     * Creates {@link RedirectView}.
     */
    public RedirectView(AppUtils appUtils) {
        this.appUtils = appUtils;

        init();
        applyStyle();
    }

    private void init() {
        setId(IDs.VIEW_ID);
        originLink.setId(IDs.ORIGIN_LINK_ID);
        targetLink.setId(IDs.TARGET_LINK_ID);

        rdrCounter.setText(appUtils.getRedirectPageTimeout() + "");
        rdrCounter.setId(IDs.COUNTER_ID);

        rdrHereLink.setText("here");
        rdrHereLink.setId(IDs.HERE_LINK_ID);

        nbSymbol.setText(appUtils.getRedirectPageBypassSymbol());
        nbSymbol.setId(IDs.BYPASS_SYMBOL_ID);

        redirectLine.add(rdrPreText, rdrCounter, rdrUnit, rdrClickText, rdrHereLink, rdrPostText);
        nbLine.add(nb, nbPreText, nbSymbol, nbPostText);
        add(firstTextLine, originLink, secondTextLine, targetLink, redirectLine, nbLine);
    }

    private void applyStyle() {
        nb.addClassName("bold");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        thread = new FeederThread(attachEvent.getUI(), this);
        thread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        thread.interrupt();
        thread = null;
    }

    @Override
    public int setErrorParameter(final BeforeEnterEvent event,
                                 final ErrorParameter<NeedForRedirectException> parameter) {
        String message = parameter.getCustomMessage();
        String[] parts = message.split(App.URL_SAFE_SEPARATOR);
        if (parts.length != 2) {
            log.error("Something wrong with received message {} it's length {}, but only 2 parts are excepted",
                    message, parts.length);
            return STATUS_500;
        }

        this.origin = appUtils.getShortUrl() + "/" + parts[0];
        this.target = parts[1];

        if(shouldSkipRedirectPage()) {
            log.info("{} skipping redirect page for {}", TAG, this.origin);
            return doHeaderRedirect(target);
        }

        originLink.setText(this.origin);
        originLink.setHref(this.origin);

        targetLink.setText(this.target);
        targetLink.setHref(this.target);

        rdrHereLink.setHref(this.target);
        return STATUS_200;
    }

    private boolean shouldSkipRedirectPage() {
        //TODO add if link has owner -> true
        return appUtils.hasRedirectPageBypassSymbol(this.origin);
    }

    private void doJSRedirect(String target) {
        if (Objects.nonNull(this.page)) {
            this.page.setLocation(AppUtils.covertUnicodeToAscii(target));
        }
    }

    private int doHeaderRedirect(String target) {
        if (StringUtils.isNotBlank(target)) {
            VaadinResponse.getCurrent().setHeader(Header.LOCATION, AppUtils.covertUnicodeToAscii(target));
            return STATUS_302;
        } else {
            log.error("{} Target is empty", TAG);
            return STATUS_500;
        }
    }

    private static final class FeederThread extends Thread {
        private final UI ui;
        private final RedirectView view;

        private int count = 0;

        public FeederThread(UI ui, RedirectView view) {
            this.ui = ui;
            this.view = view;
        }

        @Override
        public void run() {
            try {
                int redirectTimeout = view.appUtils.getRedirectPageTimeout();
                while (count < redirectTimeout ) {
                    TimeUnit.SECONDS.sleep(1);
                    count++;

                    int secondsRemains = redirectTimeout - count;
                    ui.access(() -> view.rdrCounter.setText(secondsRemains + ""));
                }
                ui.access(() -> view.doJSRedirect(view.target));
            } catch (InterruptedException e) {
                log.error("{} while waiting for redirect", e.getMessage());
            }
        }
    }

    private static class IDs {
        public static final String VIEW_ID = "RedirectView";
        public static final String ORIGIN_LINK_ID = "originLink";
        public static final String TARGET_LINK_ID = "targetLink";
        public static final String HERE_LINK_ID = "hereLink";
        public static final String BYPASS_SYMBOL_ID = "bypassSymbol";
        public static final String COUNTER_ID = "counter";
    }
}