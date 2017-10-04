package life.qbic;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

public class PrinterForm extends FormLayout{

    private TextField name = new TextField("Name");
    private TextField ID = new TextField("ID");
    private TextField location = new TextField("Location");
    private TextField url = new TextField("URL");
    private TextField userGroup = new TextField("User group");

    private ComboBox status = new ComboBox("Status");
    private ComboBox type = new ComboBox("Type");
    private CheckBox adminOnly = new CheckBox("Admin only");

    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");

    private Printer printer;
    private MyPortletUI myUI;

    public PrinterForm(MyPortletUI myUI){
        this.myUI = myUI;

        setSizeUndefined();
        addComponents(name, location, url, status, type, adminOnly, userGroup, saveButton, ID, deleteButton);

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

        adminOnly.setValue(false);

        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        //saveButton.setClickShortcut(KeyboardEvent.KeyCode.ENTER);

        saveButton.addClickListener(e -> this.save());
        deleteButton.addClickListener(e -> this.delete());

    }


    private void save(){
        printer = new Printer();

        if (name == null || location == null || url == null || status == null || type == null || adminOnly == null || userGroup == null
                || name.isEmpty() || location.isEmpty() || url.isEmpty() || userGroup.isEmpty()) {
            System.out.println(name.getValue() + " " + location.getValue() +" " + url.getValue() + " " +status.getValue() + " " +type.getValue() + " " +adminOnly.getValue() + " " +userGroup.getValue());
            System.out.println("Please enter information !");
        }else{

            printer.setName(name.getValue());
            printer.setLocation(location.getValue());
            printer.setUrl(url.getValue());
            printer.setStatus((PrinterStatus) status.getValue());
            printer.setType((PrinterType) type.getValue());
            printer.setIsAdmin(adminOnly.getValue());
            printer.setUserGroup(userGroup.getValue());

            myUI.savePrinter(printer);

            name.clear();
            location.clear();
            url.clear();
            status.setNullSelectionAllowed(false);
            status.setValue(PrinterStatus.ACTIVE);
            type.setNullSelectionAllowed(false);
            type.setValue(PrinterType.LABELPRINTER);

            adminOnly.setValue(false);
            userGroup.clear();

            myUI.refreshPrinter();

        }


    }

    private void delete(){

        if(ID == null || ID.isEmpty()){
            System.out.println("Please enter information");
        }else {
            myUI.delete(Table.labelprinter.toString(), ID.getValue());
            ID.clear();
            myUI.refreshPrinter();
        }
    }
}
