package life.qbic.view;

import com.vaadin.ui.*;
import life.qbic.MyPortletUI;
import view.forms.AFormView;

public class MainView {

    private final OptionGroup selection;
    private final VerticalLayout rootFrame;
    private final MyPortletUI ui;
    public MainView(MyPortletUI ui){
        this.selection = new OptionGroup("Select: ");
        this.selection.addItems("Printer", "Printer Project Association");
        this.rootFrame = new VerticalLayout();
        this.ui = ui;

        //Place buttons in the middle
        //this.rootFrame.setSizeFull();
        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(this.selection);
        layout.setComponentAlignment(selection, Alignment.MIDDLE_CENTER);
        this.rootFrame.addComponent(layout);
        this.rootFrame.setComponentAlignment(layout, Alignment.MIDDLE_CENTER );

        this.ui.setContent(this.rootFrame);
    }

    public OptionGroup getSelection() {
        return selection;
    }

    public void addGrid(Grid grid, AFormView form){

        VerticalLayout content = new VerticalLayout();
        content.setSizeFull();
        content.addComponents(grid, form);
        content.setExpandRatio(grid, 1);
        setRootFrame(content);

    }

    private void setRootFrame(VerticalLayout content){
        rootFrame.removeAllComponents();
        VerticalLayout layout = new VerticalLayout();
        layout.addComponents(selection,content);
        rootFrame.addComponent(layout);
        rootFrame.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
        this.ui.setContent(rootFrame);
    }



}
