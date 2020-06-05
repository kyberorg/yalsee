package eu.yals.ui;

import com.github.appreciated.app.layout.annotations.Caption;
import com.github.appreciated.app.layout.annotations.Icon;
import com.google.zxing.WriterException;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.yals.Endpoint;
import eu.yals.models.LinkInfo;
import eu.yals.services.LinkInfoService;
import eu.yals.services.QRCodeService;
import eu.yals.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

import static eu.yals.ui.MyLinksView.IDs.BANNER;
import static eu.yals.ui.MyLinksView.IDs.GRID;

@Slf4j
@SpringComponent
@UIScope
@Route(value = Endpoint.UI.MY_LINKS_PAGE, layout = AppView.class)
@Caption("My Links")
@Icon(VaadinIcon.TABLE)
@PageTitle("Link shortener for friends: My saved links")
public class MyLinksView extends VerticalLayout {
    private final String TAG = MyLinksView.class.getSimpleName();

    private final Span sessionBanner = new Span();
    private final Grid<LinkInfo> grid = new Grid<>(LinkInfo.class);

    private final LinkInfoService linkInfoService;
    private final QRCodeService qrCodeService;

    /**
     * Creates {@link MyLinksView}.
     */
    public MyLinksView(LinkInfoService linkInfoService, QRCodeService qrCodeService) {
        this.linkInfoService = linkInfoService;
        this.qrCodeService = qrCodeService;

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
        grid.addComponentColumn(this::qrImage).setHeader("QR Code");

        add(sessionBanner, grid);
    }

    private Image qrImage(LinkInfo linkInfo) {
        Image image = new Image();
        image.setSrc(linkInfo.getQrCode());
        image.setAlt("QR Code");
        image.setId(Long.toString(linkInfo.getId()));
        image.addClickListener(this::onQRCodeClicked);
        return image;
    }

    private void onQRCodeClicked(ClickEvent<Image> imageClickEvent) {
        Optional<Image> bigQRCode = getBigQRCode(imageClickEvent);
        bigQRCode.ifPresentOrElse(qrCode -> {
            Dialog dialog = new Dialog();
            dialog.add(qrCode);
            dialog.open();
        }, this::showNoSuchLinkInfoNotification);
    }

    private void showNoSuchLinkInfoNotification() {
        Notification.show("Internal Error: No info about stored link found");
    }

    private Optional<Image> getBigQRCode(ClickEvent<Image> imageClickEvent) {
        Optional<String> linkInfoId = imageClickEvent.getSource().getId();
        if (linkInfoId.isPresent()) {
            Optional<LinkInfo> linkInfo = linkInfoService.getLinkInfoById(Long.parseLong(linkInfoId.get()));
            if (linkInfo.isPresent()) {
                String ident = linkInfo.get().getIdent();
                try {
                    String qrCode = qrCodeService.getQRCodeFromIdent(ident);
                    Image image = new Image();
                    image.setSrc(qrCode);
                    image.setAlt("QR Code");
                    return Optional.of(image);
                } catch (IOException | WriterException e) {
                    log.error("{} {}", TAG, e.getMessage(), e);
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
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
