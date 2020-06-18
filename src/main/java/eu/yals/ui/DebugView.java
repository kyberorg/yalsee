package eu.yals.ui;

import com.github.appreciated.app.layout.annotations.Caption;
import com.github.appreciated.app.layout.annotations.Icon;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import eu.yals.Endpoint;
import eu.yals.utils.push.PushMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import reactor.core.publisher.Flux;

@SpringComponent
@UIScope
@Route(value = Endpoint.UI.DEBUG_PAGE, layout = AppView.class)
@Caption("Debug Page")
@Icon(VaadinIcon.FLASK)
@PageTitle("Link shortener for friends: Debug Page")
public class DebugView extends Div {

    /**
     * Creates {@link DebugView}.
     */
    public DebugView(@Qualifier("flux") Flux<PushMessage> messageFlux) {
        setId(DebugView.class.getSimpleName());

        messageFlux.subscribe(pushMessage -> getUI().ifPresent(ui -> ui.access(
                () -> add(new Paragraph(pushMessage.getCommand().name()))
        )));
    }
}
