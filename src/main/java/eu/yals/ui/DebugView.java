package eu.yals.ui;

import com.github.appreciated.app.layout.annotations.Caption;
import com.github.appreciated.app.layout.annotations.Icon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.vaadin.olli.ClipboardHelper;

@SpringComponent
@UIScope
@Route(value = "debug", layout = AppView.class)
@Caption("Debug Page")
@Icon(VaadinIcon.DASHBOARD)
@PageTitle("Link shortener for friends - Debug Page")
public class DebugView extends Div {
  public DebugView() {
    Button button = new Button("click this button to copy some stuff to the clipboard");
    ClipboardHelper clipboardHelper = new ClipboardHelper("some stuff", button);
    add(clipboardHelper);
  }
}
