package tables.printerProjectAssociation;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import database.Query;
import life.qbic.MyPortletUI;
import tables.AForm;
import tables.IForm;
import tables.Table;
import tables.printer.PrinterFields;
import tables.project.ProjectFields;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;


public class PrinterProjectAssociationForm extends AForm implements IForm{

    private final ComboBox printerNameLocation = new ComboBox("Printer Name");
    private final ComboBox projectName = new ComboBox("Project Name");
    private final ComboBox status = new ComboBox("Status");

    public PrinterProjectAssociationForm(MyPortletUI myUI) {

        super(myUI, Table.printer_project_association);
        specifyComponents();
        addComponentsToView();

    }

    @Override
    public void specifyComponents(){
        try {
            setExistingPrinterNameLocationCombinations();
            setExistingOpenBisProjectNames();
            setStatus();

        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addComponentsToView(){
        setSizeUndefined();
        addComponents(printerNameLocation, projectName, status, saveButton, rowID, deleteButton);
    }

    /**
     * Set valid ComboBox Selections from Table entries
     */

    private void setExistingPrinterNameLocationCombinations() throws SQLException{
        SQLContainer allExisPrinterNames = new SQLContainer(new FreeformQuery(
                Query.selectFrom(Arrays.asList(PrinterFields.NAME.toString(), PrinterFields.LOCATION.toString()),
                                                    Collections.singletonList(Table.labelprinter.toString()))+";"
                , myUI.getDatabase().getPool()));
        printerNameLocation.setContainerDataSource(allExisPrinterNames);
        //printerNameLocation.setItemCaptionPropertyId("name, location");
        printerNameLocation.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
        printerNameLocation.setItemCaption(0,"name, location");
        printerNameLocation.setNullSelectionAllowed(false);
    }


    private void setExistingOpenBisProjectNames() throws SQLException{
        SQLContainer allExisProjectNames = new SQLContainer(new FreeformQuery(
                Query.selectDistinctFrom(Collections.singletonList(ProjectFields.OPENBISID.toString()),
                        Collections.singletonList(Table.projects.toString()))+";"
                , myUI.getDatabase().getPool()));

        projectName.setContainerDataSource(allExisProjectNames);
        projectName.setItemCaptionPropertyId("openbis_project_identifier");
        projectName.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
        projectName.setItemCaption(0,"openbis_project_identifier");
        projectName.setFilteringMode(FilteringMode.CONTAINS);
        projectName.setNullSelectionAllowed(false);

    }

    private void setStatus() throws SQLException{
        for(PrinterProjectStatus p : PrinterProjectStatus.values()){
            status.addItem(p);
        }
        status.setNullSelectionAllowed(false);
        status.setValue(PrinterProjectStatus.ACTIVE);
    }

    //TODO Below belongs to logic
    @Override
    public void saveEntry(){

        if (printerNameLocation == null || projectName.isEmpty()) {
            System.out.println("Please enter information here");
        }else{

            myUI.saveToPrinterProjectAssociation(getFormEntries());
            emptyForm();
            myUI.reload(this.table);
        }

    }

    @Override
    public PrinterProjectAssociation getFormEntries(){

        //More a hack than a solution, due to the parsing of the form entries
        //TODO apparently this to string method is deprecated
        String addedPrinterName = this.printerNameLocation.getItem(this.printerNameLocation.getValue()).toString().split(":")[2].split("\\|")[0];
        String addedPrinterLocation = this.printerNameLocation.getItem(this.printerNameLocation.getValue()).toString().split(":")[3];
        String addedProjectName = projectName.getItem(projectName.getValue()).toString().split(":")[2];

        return new PrinterProjectAssociation(addedPrinterName, addedPrinterLocation, addedProjectName, (PrinterProjectStatus) status.getValue());
    }


    @Override
    public void emptyForm(){
        printerNameLocation.clear();
        projectName.clear();
    }



}
