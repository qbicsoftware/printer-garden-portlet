package tables.printer;

import com.vaadin.ui.*;
import life.qbic.MyPortletUI;
import tables.Form;
import tables.Table;


public class PrinterForm extends Form{

    private TextField name = new TextField("Name");
    private TextField location = new TextField("Location");
    private TextField url = new TextField("URL");
    private TextField userGroup = new TextField("User group");

    private ComboBox status = new ComboBox("Status");
    private ComboBox type = new ComboBox("Type");
    private CheckBox adminOnly = new CheckBox("Admin only");


    public PrinterForm(MyPortletUI myUI){
        super(myUI, Table.labelprinter);
        addComponentsToView();
        specifyComponents();
    }

    @Override
    public void addComponentsToView() {
        setSizeUndefined();
        addComponents(name, location, url, status, type, adminOnly, userGroup, saveButton, rowID, deleteButton);
    }

    @Override
    public void specifyComponents() {
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

    }

    //TODO put below in logic class
    @Override
    public void saveEntry() {

        if (name == null || location == null || url == null || status == null || type == null || adminOnly == null || userGroup == null
                || name.isEmpty() || location.isEmpty() || url.isEmpty() || userGroup.isEmpty()) {
            System.out.println("Please enter information !");
        }else{

            myUI.saveToPrinter(getFormEntries());
            myUI.reload(this.table);
            emptyForm();
        }
    }

    @Override
    public Printer getFormEntries() {
        Printer printer = new Printer(name.getValue(), location.getValue(), url.getValue(),
                (PrinterStatus) status.getValue(), (PrinterType) type.getValue(), adminOnly.getValue(),
                userGroup.getValue());
        return printer;
    }

    @Override
    public void emptyForm() {
        name.clear();
        location.clear();
        url.clear();
        status.setNullSelectionAllowed(false);
        status.setValue(PrinterStatus.ACTIVE);
        type.setNullSelectionAllowed(false);
        type.setValue(PrinterType.LABELPRINTER);

        adminOnly.setValue(false);
        userGroup.clear();
    }

}
