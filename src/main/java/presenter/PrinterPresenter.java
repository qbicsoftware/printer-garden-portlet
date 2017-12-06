package presenter;

import com.vaadin.ui.Grid;
import model.database.Database;
import model.tables.Table;
import model.tables.printer.Printer;
import view.forms.PrinterFormView;

import java.util.Arrays;
import java.util.List;

public class PrinterPresenter {

    private final PrinterFormView form;
    private final Database database;
    private final Grid grid;

    public PrinterPresenter(PrinterFormView form, Database database, Grid grid) {
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
                //TODO show alert
                System.out.println("Please enter information !");
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

    private void saveToPrinter(Printer entry) {
        List<String> entries;
        List<String> values;
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
    }

    private void reload() {
        grid.clearSortOrder();
    }

    private void deleteButtonListener() {
        this.form.getDeleteButton().addClickListener(clickEvent -> {
            deleteEntry();
        });
    }

    private void deleteEntry() {
        if (this.form.getRowID() == null || this.form.getRowID().isEmpty()) {
            System.out.println("Please enter information");
        } else {
            database.delete(Table.labelprinter.toString(), this.form.getRowID().getItem(
                    this.form.getRowID().getValue()).toString().split(":")[2]);
            this.form.emptyForm();
            reload();

        }
    }
}
