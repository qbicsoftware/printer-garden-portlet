package life.qbic.presenter;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.Grid;
import life.qbic.model.database.Database;
import life.qbic.model.database.Query;
import life.qbic.model.main.MyPortletUI;
import life.qbic.model.tables.Table;
import life.qbic.model.tables.printer.Printer;
import life.qbic.model.tables.printer.PrinterFields;
import life.qbic.utils.MyNotification;
import life.qbic.utils.URLValidator;
import life.qbic.view.forms.PrinterFormView;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This class {@link PrinterPresenter} handles operations in the printer table view.
 *
 * @author fhanssen
 */
class PrinterPresenter {

    private final PrinterFormView form;
    private final Database database;
    private final Grid grid;

    private static final Log log = LogFactoryUtil.getLog(PrinterPresenter.class.getName());


    PrinterPresenter(PrinterFormView form, Database database, Grid grid) {
        this.form = form;
        this.database = database;
        this.grid = grid;

        setUpListener();
    }

    private void setUpListener() {
        saveButtonListener();
        deleteButtonListener();
    }

    /**
     * When the save button is pressed, all values of the form fields are read.
     * If they are null or required fields are empty, the user is notified.
     */
    private void saveButtonListener() {
        this.form.getSaveButton().addClickListener(clickEvent -> {
            if (isInvalidForm()) {
                MyNotification.notification("Information", "Please enter information!", "" );
                log.info(MyPortletUI.toolname + ": " +"No information to safe was provided in the printer form.");
            } else {
                log.info(MyPortletUI.toolname + ": " +"New entry is saved to printer table.");
                saveToPrinter(form.getFormEntries());
                reload();
                this.form.emptyForm();
            }
        });
    }

    private Boolean isInvalidForm() {
        return this.form.getName() == null || this.form.getLocation() == null || this.form.getUrl() == null
                || this.form.getStatus() == null || this.form.getType() == null || this.form.getAdminOnly() == null
                || this.form.getUserGroup() == null || this.form.getName().isEmpty() || this.form.getLocation().isEmpty()
                || this.form.getUrl().isEmpty();
    }

    /**
     * A new entry is saved to a printer iff the tuple(name, location) does not appear in the table and
     * if a valid URL is added
     * @param entry new Printer
     */
    private void saveToPrinter(Printer entry){
        List<String> entries;
        List<String> values;

        //Database wants unique tupels of(name,location)
        if(isNameAndLocationUnique(entry.getName(), entry.getLocation())) {
            //also the url/ipadress should have the correct format
            if(URLValidator.validate(entry.getUrl())) {
                log.info(MyPortletUI.toolname + ": " +"Try to save new entry with \n" +
                        "\tname:\t" + entry.getName() + "\n" +
                        "\tlocation:\t" + entry.getLocation()  + "\n" +
                        "\turl:\t" + entry.getUrl() + "\n" +
                        "\tstatus:\t" + entry.getStatus() + "\n" +
                        "\ttype:\t" + entry.getType()  + "\n" +
                        "\tadmin_only:\t" + entry.getIsAdmin() + "\n" +
                        "\tuser_group:\t" + entry.getUserGroup());

                if (entry.getUserGroup().isEmpty()) {
                    entries = Arrays.asList("name", "location", "url", "status", "type", "admin_only");
                    values = Arrays.asList("'" + entry.getName() + "'", "'" + entry.getLocation() + "'", "'" + entry.getUrl() + "'", "'" + entry.getStatus().toString() + "'",
                            "'" + entry.getType().toString() + "'", "'" + entry.getIsAdmin() + "'");
                } else {
                    entries = Arrays.asList("name", "location", "url", "status", "type", "admin_only", "user_group");
                    values = Arrays.asList("'" + entry.getName() + "'", "'" + entry.getLocation() + "'", "'" + entry.getUrl() + "'", "'" + entry.getStatus().toString() + "'",
                            "'" + entry.getType().toString() + "'", "'" + entry.getIsAdmin() + "'", "'" + entry.getUserGroup() + "'");
                }

                database.save(Table.labelprinter.toString(), entries, values, false);
            }else{
                log.error(MyPortletUI.toolname + ": " +"Provided URL is has incorrect format.");
                MyNotification.notification("Error", "Please enter a correctly formatted URL!", "error");
            }
        }else{
            log.error(MyPortletUI.toolname + ": " +"(Name, Location) is already assigned. Tuple has to be unique.");
            MyNotification.notification("Error", "(Name, Location) is already assigned. Please use a unique tuple!", "error");
        }
    }

    /**
     * Checks if the tuple(name, location) appears in the table
     * @param name printer name
     * @param location printer location
     * @return true: tuple has not been found in table, false: else
     */
    private Boolean isNameAndLocationUnique(String name, String location) {
        try {
            log.debug(MyPortletUI.toolname + ": " +"Try to validate if (name, location) tuple is unique");
            SQLContainer currentPrinterNameLocations = database.loadTableFromQuery(
                    Query.selectFrom(Arrays.asList("name", "location"), Collections.singletonList(Table.labelprinter.toString())) + ";");

            Collection<?> currentNameIDs = currentPrinterNameLocations.getItemIds();
            for (Object itemID : currentNameIDs) {
                Property nameProp = currentPrinterNameLocations.getContainerProperty(itemID, "name");
                Property locProp = currentPrinterNameLocations.getContainerProperty(itemID, "location");
                String currName = nameProp.getValue().toString();
                String currLoc = locProp.getValue().toString();

                if(name.equals(currName) && location.equals(currLoc)){
                    //If tuple already exists return false
                    return false;
                }
            }
            log.info(MyPortletUI.toolname + ": " +"Tuple validation was successful.");
        }catch(SQLException e){
            MyNotification.notification("Error", "Database access failed.", "error");
            log.error(MyPortletUI.toolname + ": " +"Tuple validation failed: " + e.getMessage());
        }
        return true;
    }

    /**
     * Reloads table views and the delete id combobox, which is dependent on the current table entries.
     */
    private void reload() {
        grid.clearSortOrder();
        try {
            SQLContainer allExisIds = new SQLContainer(new FreeformQuery(
                    Query.selectFrom(Collections.singletonList(PrinterFields.ID.toString()),
                            Collections.singletonList(Table.labelprinter.toString())) + ";",
                    database.getPool()));
            form.setExistingIDs(allExisIds);
        }catch(SQLException e){
            MyNotification.notification("Error", "Database access failed.", "error");
            log.error(MyPortletUI.toolname + ": " +"ID combobox values could not be updated: " + e.getMessage());
        }
    }

    private void deleteButtonListener() {
        this.form.getDeleteButton().addClickListener(clickEvent -> deleteEntry());
    }

    /**
     * An entry belonging to a selected ID is deleted.
     */
    private void deleteEntry() {
        if (this.form.getRowID() == null || this.form.getRowID().isEmpty()) {
            log.info(MyPortletUI.toolname + ": " +"No information to delete was provided in the printer form.");
            MyNotification.notification("Information", "Please enter information!", "" );
        } else {
            log.info(MyPortletUI.toolname + ": " +"Entry with ID " + this.form.getRowID().getItem(
                    this.form.getRowID().getValue()).toString().split(":")[2] +" is deleted");
            database.delete(Table.labelprinter.toString(), this.form.getRowID().getItem(
                    this.form.getRowID().getValue()).toString().split(":")[2]);
            this.form.emptyForm();
            reload();

        }
    }
}
