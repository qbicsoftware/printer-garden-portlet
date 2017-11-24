package tables.printerProjectAssociation;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.*;
import database.Query;
import life.qbic.MyPortletUI;
import tables.Form;
import tables.Table;
import tables.printer.PrinterFields;
import tables.project.ProjectFields;

import java.sql.SQLException;
import java.util.Arrays;

public class PrinterProjectAssociationForm extends Form{

    private ComboBox printerName = new ComboBox("Printer Name");
    private ComboBox printerLocation = new ComboBox("Printer Location");
    private ComboBox projectName = new ComboBox("Project Name");

    public PrinterProjectAssociationForm(MyPortletUI myUI) {

        super(myUI, Table.printer_project_association);
        addComponentsToView();
        specifyComponents();
    }

    @Override
    public void specifyComponents(){
        try {
            setExistingPrinterNames();
            setExistingPrinterLocations();
            setExistingOpenBisProjectNames();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addComponentsToView(){
        setSizeUndefined();
        addComponents(printerName, printerLocation, projectName, saveButton, rowID, deleteButton);
    }

    /**
     * Set valid ComboBox Selections from Table entries
     * @throws SQLException
     */

    private void setExistingPrinterNames() throws SQLException{
        SQLContainer allExisPrinterNames = new SQLContainer(new FreeformQuery(
                Query.selectDistinctFrom(Arrays.asList(PrinterFields.NAME.toString()),
                                                    Arrays.asList(Table.labelprinter.toString()))+";"
                , myUI.getDatabase().getPool()));
        printerName.addItem("");
        printerName.setContainerDataSource(allExisPrinterNames);
        printerName.setItemCaptionPropertyId("name");
        printerName.setNullSelectionAllowed(false);
        printerName.setImmediate(true);
    }

    private void setExistingPrinterLocations() throws SQLException{
        SQLContainer allExisPrinterLocations = new SQLContainer(new FreeformQuery(
                Query.selectDistinctFrom(Arrays.asList(PrinterFields.LOCATION.toString()),
                        Arrays.asList(Table.labelprinter.toString()))+";"
                , myUI.getDatabase().getPool()));
        printerLocation.addItem("");
        printerLocation.setContainerDataSource(allExisPrinterLocations);
        printerLocation.setItemCaptionPropertyId("location");
        printerLocation.setNullSelectionAllowed(false);
        printerLocation.setImmediate(true);

    }

    private void setExistingOpenBisProjectNames() throws SQLException{
        SQLContainer allExisProjectNames = new SQLContainer(new FreeformQuery(
                Query.selectDistinctFrom(Arrays.asList(ProjectFields.OPENBISID.toString()),
                        Arrays.asList(Table.projects.toString()))+";"
                , myUI.getDatabase().getPool()));
        projectName.addItem("");
        projectName.setCaption("Project Name");
        projectName.setContainerDataSource(allExisProjectNames);
        projectName.setItemCaptionPropertyId("openbis_project_identifier");
        projectName.setNullSelectionAllowed(false);
        projectName.setEnabled(true);
        projectName.setTextInputAllowed(true);
    }

    //TODO Below belongs to logic
    @Override
    public void saveEntry(){

        if (printerName == null || printerLocation == null || projectName.isEmpty()) {
            System.out.println("Please enter information here");
        }else{
            try{
                myUI.saveToPrinterProjectAssociation(getFormEntries());
            }catch(SQLException e){
                e.printStackTrace();
            }
            emptyForm();
            myUI.reload(this.table);
        }

    }

    @Override
    public PrinterProjectAssociation getFormEntries(){

        //More a hack than a solution, due to the parsing of the form entries
        String addedPrinterName = this.printerName.getItem(this.printerName.getValue()).toString().split(":")[2];
        String addedPrinterLocation = this.printerLocation.getItem(this.printerLocation.getValue()).toString().split(":")[2];
        String addedProjectName = projectName.getItem(projectName.getValue()).toString().split(":")[2];

        return new PrinterProjectAssociation(addedPrinterName, addedPrinterLocation, addedProjectName);
    }


    @Override
    public void emptyForm(){
        printerName.clear();
        printerLocation.clear();
        projectName.clear();
    }



}
