package life.qbic;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import elemental.events.KeyboardEvent;

public class PrinterForm extends FormLayout{

    private TextField name = new TextField("Name");
    private TextField ID = new TextField("ID");
    private TextField location = new TextField("Location");
    private TextField URL = new TextField("URL");
    private TextField userGroup = new TextField("User group");

    private ComboBox status = new ComboBox("Status");
    private ComboBox type = new ComboBox("Type");
    private ComboBox adminOnly = new ComboBox("Admin only");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");

    private PrinterProjectAssociation printerProjectAssociation;
    private MyPortletUI myUI;

    public PrinterForm(MyPortletUI myUI){
        this.myUI = myUI;

        setSizeUndefined();
        addComponents(name, location, URL, status, type, adminOnly, userGroup, saveButton, ID, deleteButton);

        for(PrinterStatus p : PrinterStatus.values()){
            status.addItem(p);
        }

        status.setNullSelectionAllowed(false);
        status.setValue(PrinterStatus.ACTIVE);

        for(PrinterType p : PrinterType.values()){
            type.addItem(p);
        }

        type.setNullSelectionAllowed(false);
        type.setValue(PrinterType.LABELPRINTER);

        adminOnly.addItem("True");
        adminOnly.addItem("False");
        adminOnly.setNullSelectionAllowed(false);
        adminOnly.setValue("True");

        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.setClickShortcut(KeyboardEvent.KeyCode.ENTER);

        saveButton.addClickListener(e -> this.save());
        deleteButton.addClickListener(e -> this.delete());

    }


    private void save(){



    }

    private void delete(){

        if(ID == null || ID.isEmpty()){
            System.out.println("Please enter information");
        }else {
            myUI.delete(Table.labelprinter.toString(), ID.getValue());
            ID.clear();
            //myUI.updatePrinterProjectAssociation();
        }
    }
}
