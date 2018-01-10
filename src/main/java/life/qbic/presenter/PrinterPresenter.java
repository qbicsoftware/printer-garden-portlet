package life.qbic.presenter;

import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import life.qbic.model.database.Database;
import life.qbic.model.database.Query;
import life.qbic.model.tables.Table;
import life.qbic.model.tables.printer.Printer;
import life.qbic.utils.MyNotification;
import life.qbic.utils.URLValidator;
import life.qbic.view.forms.PrinterFormView;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class PrinterPresenter {

    private final PrinterFormView form;
    private final Database database;
    private final Grid grid;

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

    private void saveButtonListener() {
        this.form.getSaveButton().addClickListener(clickEvent -> {
            if (isInvalidForm()) {
                MyNotification.notification("Information", "Please enter information!", "" );
//                Notification notification = new Notification("Please enter information!", Notification.Type.HUMANIZED_MESSAGE);
//                notification.setDelayMsec(30);
//                notification.show(Page.getCurrent());
            } else {
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

    private void saveToPrinter(Printer entry){
        List<String> entries;
        List<String> values;

        //Database wants unique tupels of(name,location)
        if(isNameAndLocationUnique(entry.getName(), entry.getLocation())) {
            //also the url/ipadress should have the correct format
            if(URLValidator.validate(entry.getUrl())) {
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
                MyNotification.notification("Error", "Please enter a correctly formatted URL!", "error");
//                Notification notification = new Notification("Please enter a correctly formatted URL!", Notification.Type.ERROR_MESSAGE);
//                notification.show(Page.getCurrent());
            }
        }else{
            MyNotification.notification("Error", "(Name, Location) is already assigned. Please use a unique tuple!", "error");
//            Notification notification = new Notification("(Name, Location) is already assigned. Please use a unique tuple!", Notification.Type.ERROR_MESSAGE);
//            notification.show(Page.getCurrent());
        }
    }

    private Boolean isNameAndLocationUnique(String name, String location) {
        try {
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
        }catch(SQLException e){
            System.out.println(e.getMessage());

        }
        return true;
    }


    private void reload() {
        grid.clearSortOrder();
    }

    private void deleteButtonListener() {
        this.form.getDeleteButton().addClickListener(clickEvent -> deleteEntry());
    }

    private void deleteEntry() {
        if (this.form.getRowID() == null || this.form.getRowID().isEmpty()) {
            MyNotification.notification("Information", "Please enter information!", "" );
//            Notification notification = new Notification("Please enter information!", Notification.Type.HUMANIZED_MESSAGE);
//            notification.setDelayMsec(30);
//            notification.setPosition(Position.MIDDLE_CENTER);
//
//            notification.show(Page.getCurrent());
        } else {
            database.delete(Table.labelprinter.toString(), this.form.getRowID().getItem(
                    this.form.getRowID().getValue()).toString().split(":")[2]);
            this.form.emptyForm();
            reload();

        }
    }
}
