package eu.yals.ui;

import com.github.appreciated.app.layout.annotations.Caption;
import com.github.appreciated.app.layout.annotations.Icon;
import com.google.zxing.WriterException;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.yals.Endpoint;
import eu.yals.models.LinkInfo;
import eu.yals.result.GetResult;
import eu.yals.services.LinkInfoService;
import eu.yals.services.LinkService;
import eu.yals.services.QRCodeService;
import eu.yals.utils.AppUtils;
import eu.yals.utils.push.Broadcaster;
import eu.yals.utils.push.Push;
import eu.yals.utils.push.PushCommand;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

import static eu.yals.ui.MyLinksView.IDs.BANNER;
import static eu.yals.ui.MyLinksView.IDs.GRID;
import static eu.yals.utils.push.PushCommand.LINK_SAVED;

@Slf4j
@SpringComponent
@UIScope
@Route(value = Endpoint.UI.MY_LINKS_PAGE, layout = AppView.class)
@Caption("My Links")
@Icon(VaadinIcon.TABLE)
@PageTitle("Link shortener for friends: My saved links")
public class MyLinksView extends VerticalLayout {
    private final String TAG = "[" + MyLinksView.class.getSimpleName() + "]";

    private final Span sessionBanner = new Span();
    private final GridPro<LinkInfo> grid = new GridPro<>(LinkInfo.class);

    private final LinkInfoService linkInfoService;
    private final QRCodeService qrCodeService;
    private final LinkService linkService;
    private Registration broadcasterRegistration;

    /**
     * Creates {@link MyLinksView}.
     */
    public MyLinksView(LinkInfoService linkInfoService, QRCodeService qrCodeService, LinkService linkService) {
        this.linkInfoService = linkInfoService;
        this.qrCodeService = qrCodeService;
        this.linkService = linkService;

        setId(MyLinksView.class.getSimpleName());
        init();
        applyLoadState();
    }

    private void init() {
        sessionBanner.setId(BANNER);
        grid.setId(GRID);
        grid.setSingleCellEdit(true);

        grid.removeAllColumns();
        grid.addColumn(LinkInfo::getIdent).setHeader("Link");
        grid.addEditColumn(LinkInfo::getDescription).text(this::updateLinkInfo)
                .setHeader("Description");
        grid.addComponentColumn(this::qrImage).setHeader("QR Code");

        // You can use any renderer for the item details. By default, the
// details are opened and closed by clicking the rows.
        grid.setItemDetailsRenderer(TemplateRenderer.<LinkInfo>of(
                "<div class='custom-details' style='border: 1px solid gray; padding: 10px; width: 100%; box-sizing: border-box;'>"
                        + "<div><b>[[item.longLink]]</b><br>" +
                        "<div>Created: [[item.created]], Updated: [[item.updated]]</div>" +
                        "</div>"
                        + "</div>")
                .withProperty("longLink", this::getLongLink)
                .withProperty("created", linkInfo -> linkInfo.getCreated().toString())
                .withProperty("updated", linkInfo -> linkInfo.getUpdated().toString())
                // This is now how we open the details
                .withEventHandler("handleClick", person -> grid.getDataProvider().refreshItem(person)));

        add(sessionBanner, grid);
    }

    private void applyLoadState() {
        sessionBanner.setText("Those are links stored in current session. " +
                "Soon you will be able to store them permanently, once we introduce users");

        updateGrid();
    }

    @Override
    protected void onAttach(final AttachEvent attachEvent) {
        UI ui = attachEvent.getUI();
        broadcasterRegistration = Broadcaster.register(message -> ui.access(() -> {
            log.debug("{} Push received. {} ID: {}, Message: {}",
                    TAG, MyLinksView.class.getSimpleName(), ui.getUIId(), message);

            Push push = Push.fromMessage(message);
            PushCommand command = push.getPushCommand();
            if (command == LINK_SAVED) {
                updateGrid();
            } else {
                log.warn("{} got unknown push command: '{}'", TAG, push.getPushCommand());
            }
        }));
    }

    private void updateGrid() {
        grid.setItems(linkInfoService.getAllRecordWithSession(AppUtils.getSessionId(VaadinSession.getCurrent())));
    }

    @Override
    protected void onDetach(final DetachEvent detachEvent) {
        // Cleanup
        broadcasterRegistration.remove();
        broadcasterRegistration = null;
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

    private Object getLongLink(LinkInfo linkInfo) {
        GetResult result = linkService.getLink(linkInfo.getIdent());
        if (result instanceof GetResult.Success) {
            return ((GetResult.Success) result).getLink();
        } else {
            return "";
        }
    }


    private void updateLinkInfo(LinkInfo linkInfoItem, String newValue) {
        linkInfoItem.setDescription(newValue);
        linkInfoService.update(linkInfoItem);
    }

    public static class IDs {
        public static final String BANNER = "banner";
        public static final String GRID = "grid";
    }
}
