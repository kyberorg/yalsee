package eu.yals.ui;

import com.github.appreciated.app.layout.annotations.Caption;
import com.github.appreciated.app.layout.annotations.Icon;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.yals.Endpoint;

@SpringComponent
@UIScope
@Route(value = Endpoint.UI.MY_LINKS_PAGE, layout = AppView.class)
@Caption("Debug Page")
@Icon(VaadinIcon.TABLE)
@PageTitle("Link shortener for friends: My saved links")
public class MyLinksView extends VerticalLayout {

    private Span sessionLabel;
    /**
     * Creates {@link MyLinksView}.
     */
    public MyLinksView() {
        setId(MyLinksView.class.getSimpleName());
        init();
        applyLoadState();
    }

    private void init() {
        sessionLabel = new Span();
        add(sessionLabel);
    }

    private void applyLoadState() {
        if(VaadinSession.getCurrent() != null) {
            if(VaadinSession.getCurrent().getSession() != null) {
                sessionLabel.setText(VaadinSession.getCurrent().getSession().getId());
            }
        }
    }
}
