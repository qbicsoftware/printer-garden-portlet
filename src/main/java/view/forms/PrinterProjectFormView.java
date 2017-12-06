package view.forms;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import life.qbic.MyPortletUI;
import model.tables.printerProjectAssociation.PrinterProjectAssociation;
import model.tables.printerProjectAssociation.PrinterProjectStatus;


import java.sql.SQLException;


public class PrinterProjectFormView extends AFormView implements IFormView {

    private final ComboBox printerNameLocation = new ComboBox("Printer");
    private final ComboBox projectName = new ComboBox("Project Name");
    private final ComboBox status = new ComboBox("Status");

    private final SQLContainer allExisPrinterLocationNames;
    private final SQLContainer allExisProjectNames;

    public PrinterProjectFormView(MyPortletUI myUI, SQLContainer exisIds, SQLContainer allExisPrinterNames,
                                  SQLContainer allExisProjectNames) throws SQLException{
        super(myUI, exisIds);
        this.allExisPrinterLocationNames = allExisPrinterNames;
        this.allExisProjectNames = allExisProjectNames;

        specifyComponents();
        addComponentsToView();
    }

    @Override
    public void addComponentsToView() {
        setSizeUndefined();
        addComponents(printerNameLocation, projectName, status, getSaveButton(), getRowID(), getDeleteButton());
    }

    @Override
    public void specifyComponents() {

            setExistingPrinterNameLocationCombinations();
            setExistingOpenBisProjectNames();
            setStatus();

    }

    /**
     * Set valid ComboBox Selections from Table entries
     */

    private void setExistingPrinterNameLocationCombinations() {

        printerNameLocation.setContainerDataSource(this.allExisPrinterLocationNames);
        printerNameLocation.setItemCaptionPropertyId("name, location");
        printerNameLocation.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
        printerNameLocation.setItemCaption(0,"name, location");
        printerNameLocation.setNullSelectionAllowed(false);
        printerNameLocation.setFilteringMode(FilteringMode.CONTAINS);

    }

    private void setExistingOpenBisProjectNames(){

        projectName.setContainerDataSource(this.allExisProjectNames);
        projectName.setItemCaptionPropertyId("openbis_project_identifier");
        projectName.setItemCaptionMode(AbstractSelect.ItemCaptionMode.ITEM);
        projectName.setItemCaption(0,"openbis_project_identifier");
        projectName.setFilteringMode(FilteringMode.CONTAINS);
        projectName.setNullSelectionAllowed(false);

    }

    private void setStatus(){
        for(PrinterProjectStatus p : PrinterProjectStatus.values()){
            status.addItem(p);
        }
        status.setNullSelectionAllowed(false);
        status.setValue(PrinterProjectStatus.ACTIVE);
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
        getRowID().clear();
    }

    public ComboBox getPrinterNameLocation() {
        return printerNameLocation;
    }

    public ComboBox getProjectName() {
        return projectName;
    }

    public ComboBox getStatus() {
        return status;
    }
}