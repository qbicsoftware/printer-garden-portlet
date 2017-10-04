package life.qbic;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import elemental.events.KeyboardEvent;

public class PrinterProjectForm extends FormLayout{

    private TextField printerId = new TextField("Printer ID");
    private TextField projectId = new TextField("Project ID");
    private TextField ID = new TextField("ID");
    private ComboBox status = new ComboBox("Status");
    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");

    private PrinterProjectAssociation printerProjectAssociation;
    private MyPortletUI myUI;

    public PrinterProjectForm(MyPortletUI myUI){
        this.myUI = myUI;

        setSizeUndefined();
        addComponents(printerId, projectId, status, saveButton, ID, deleteButton);
        for(PrinterStatus p : PrinterStatus.values()){
            status.addItem(p);
        }

        status.setNullSelectionAllowed(false);
        status.setValue(PrinterStatus.ACTIVE);

        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.setClickShortcut(KeyboardEvent.KeyCode.ENTER);

        saveButton.addClickListener(e -> this.save());
        deleteButton.addClickListener(e -> this.delete());

    }


    private void save(){

        printerProjectAssociation = new PrinterProjectAssociation();

        if (projectId == null || printerId == null || projectId.isEmpty() || printerId.isEmpty()) {
            System.out.println("Please enter information here");
        }else{
            printerProjectAssociation.setPrinterID(printerId.getValue());
            printerProjectAssociation.setProjectID(projectId.getValue());
            printerProjectAssociation.setStatus((PrinterStatus) status.getValue());
            myUI.savePrinterProjectAssociation(printerProjectAssociation);

            printerId.clear();
            projectId.clear();
            status.setNullSelectionAllowed(false);
            status.setValue(PrinterStatus.ACTIVE);

            myUI.updatePrinterProjectAssociation();

        }

    }

    private void delete(){

        if(ID == null || ID.isEmpty()){
            System.out.println("Please enter information");
        }else {
            myUI.delete(Table.printer_project_association.toString(), ID.getValue());
            ID.clear();
            myUI.updatePrinterProjectAssociation();
        }
    }
}
