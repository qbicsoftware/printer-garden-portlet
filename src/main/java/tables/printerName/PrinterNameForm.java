package tables.printerName;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import elemental.events.KeyboardEvent;
import life.qbic.MyPortletUI;
import tables.Table;

import java.sql.SQLException;

public class PrinterNameForm extends FormLayout{

    private ComboBox printerName = new ComboBox("Printer Name");
    private ComboBox printerLocation = new ComboBox("Project Location");
    private ComboBox projectName = new ComboBox("Project Name");

    private ComboBox associationID = new ComboBox("ID");
    private Button saveButton = new Button("Save");
    private Button deleteButton = new Button("Delete");

    private PrinterName printerNameObject;
    private MyPortletUI myUI;

    public PrinterNameForm(MyPortletUI myUI){
        this.myUI = myUI;


        setSizeUndefined();
        addComponents(printerName, printerLocation, projectName, saveButton, associationID, deleteButton);
        try {
            SQLContainer allExisPrinterNames = new SQLContainer(new FreeformQuery("SELECT DISTINCT labelprinter.name FROM labelprinter;", myUI.getDatabase().getPool()));
            printerName.addItem("");
            printerName.setContainerDataSource(allExisPrinterNames);
            printerName.setItemCaptionPropertyId("name");
            printerName.setNullSelectionAllowed(false);
            printerName.setImmediate(true);

            SQLContainer allExisPrinterLocations = new SQLContainer(new FreeformQuery("SELECT DISTINCT labelprinter.location FROM labelprinter;", myUI.getDatabase().getPool()));
            printerLocation.addItem("");
            printerLocation.setContainerDataSource(allExisPrinterLocations);
            printerLocation.setItemCaptionPropertyId("location");
            printerLocation.setNullSelectionAllowed(false);
            printerLocation.setImmediate(true);

            SQLContainer allExisProjectNames = new SQLContainer(new FreeformQuery("SELECT DISTINCT projects.openbis_project_identifier FROM projects;", myUI.getDatabase().getPool()));
            projectName.addItem("");
            projectName.setCaption("Project Name");
            projectName.setContainerDataSource(allExisProjectNames);
            projectName.setItemCaptionPropertyId("openbis_project_identifier");
            projectName.setNullSelectionAllowed(false);
            //projectName.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_STARTSWITH);
            //projectName.setImmediate(true);
            projectName.setEnabled(true);
            projectName.setTextInputAllowed(true);

            SQLContainer allExisPrinProjIds = new SQLContainer(new FreeformQuery("SELECT printer_project_association.id FROM printer_project_association;", myUI.getDatabase().getPool()));
            associationID.addItem("");
            associationID.setContainerDataSource(allExisPrinProjIds);
            associationID.setItemCaptionPropertyId("id");
            associationID.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_STARTSWITH);
            associationID.setImmediate(true);


        }catch(SQLException e){
            e.printStackTrace();
        }
        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.setClickShortcut(KeyboardEvent.KeyCode.ENTER);

        saveButton.addClickListener(e -> this.save());
        deleteButton.addClickListener(e -> this.delete());

    }


    private void save(){

        printerNameObject = new PrinterName();

        if (printerName == null || printerLocation == null || projectName.isEmpty()) {
            System.out.println("Please enter information here");
        }else{

            //More a hack than a solution
            printerNameObject.setPrinterName(printerName.getItem(printerName.getValue()).toString().split(":")[2]);
            printerNameObject.setPrinterLocation(printerLocation.getItem(printerLocation.getValue()).toString().split(":")[2]);
            printerNameObject.setProjectName(projectName.getItem(projectName.getValue()).toString().split(":")[2]);

            try{
                myUI.savePrinterName(printerNameObject);
            }catch(SQLException e){
                e.printStackTrace();
            }

            printerName.clear();
            printerLocation.clear();
            projectName.clear();

            myUI.reloadPrinterName();

        }

    }

    private void delete(){

        if(associationID == null || associationID.isEmpty()){
            System.out.println("Please enter information");
        }else {
            myUI.delete(Table.printer_project_association.toString(), associationID.getItem(associationID.getValue()).toString().split(":")[2]);
            associationID.clear();
            myUI.reloadPrinterName();
        }
    }
}
