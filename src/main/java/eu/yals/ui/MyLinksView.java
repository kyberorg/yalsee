package eu.yals.ui;

import com.github.appreciated.app.layout.annotations.Caption;
import com.github.appreciated.app.layout.annotations.Icon;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.yals.Endpoint;
import eu.yals.models.LinkInfo;
import eu.yals.services.LinkInfoService;
import eu.yals.utils.AppUtils;

import static eu.yals.ui.MyLinksView.IDs.BANNER;
import static eu.yals.ui.MyLinksView.IDs.GRID;

@SpringComponent
@UIScope
@Route(value = Endpoint.UI.MY_LINKS_PAGE, layout = AppView.class)
@Caption("My Links")
@Icon(VaadinIcon.TABLE)
@PageTitle("Link shortener for friends: My saved links")
public class MyLinksView extends VerticalLayout {

    private final Span sessionBanner = new Span();
    private final Grid<LinkInfo> grid = new Grid<>(LinkInfo.class);

    private final LinkInfoService linkInfoService;

    /**
     * Creates {@link MyLinksView}.
     */
    public MyLinksView(LinkInfoService linkInfoService) {
        this.linkInfoService = linkInfoService;

        setId(MyLinksView.class.getSimpleName());
        init();
        applyLoadState();
    }

    private void init() {
        sessionBanner.setId(BANNER);
        grid.setId(GRID);

        grid.removeAllColumns();
        grid.addColumn(LinkInfo::getIdent).setHeader("Link");
        grid.addColumn(LinkInfo::getDescription).setHeader("Description");
        grid.addColumn(TemplateRenderer.<LinkInfo>of(
                "<div><img style='height: 22px; width: 22px;' src='[[item.qrcode]]'/></div>"
        )
                .withProperty("qrcode", LinkInfo::getQrCode))
                .setHeader("QR Code");

        grid.addItemClickListener(
                event -> showQRCodeModal(event.getItem().getQrCode()));

        add(sessionBanner, grid);
    }

    private void showQRCodeModal(String qrCode) {
        //TODO implement
        Notification.show(qrCode);
    }

    private void applyLoadState() {
        sessionBanner.setText("Those are links stored in current session. " +
                "Soon you will be able to store them permanently, once we introduce users");

        grid.setItems(linkInfoService.getAllRecordWithSession(AppUtils.getSessionId(VaadinSession.getCurrent())));
    }

    public static class IDs {
        public static final String BANNER = "banner";
        public static final String GRID = "grid";
    }
}
