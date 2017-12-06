package presenter;

import com.vaadin.ui.Grid;
import javafx.util.Pair;
import model.database.Database;
import model.database.Query;
import model.tables.Table;
import model.tables.printer.PrinterFields;
import model.tables.printerProjectAssociation.PrinterProjectAssociation;
import model.tables.project.ProjectFields;
import view.forms.PrinterProjectFormView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PrinterProjectPresenter {

    private final PrinterProjectFormView form;
    private final Database database;
    private final Grid grid;
    PrinterProjectPresenter(PrinterProjectFormView form, Database database, Grid grid){
        this.form = form;
        this.database = database;
        this.grid = grid;

        setUpListener();
    }

    private void setUpListener(){
        saveButtonListener();
        deleteButtonListener();

    }

    private void saveButtonListener(){
        this.form.getSaveButton().addClickListener(clickEvent -> {
            if (this.form.getPrinterNameLocation() == null || this.form.getProjectName().isEmpty()) {
                //TODO show alert
                System.out.println("Please enter information !");
            }else{
                saveToPrinterProject(form.getFormEntries());
                reload();
                this.form.emptyForm();
            }
        });
    }

    private void saveToPrinterProject(PrinterProjectAssociation entry) {

        List<String> entries = Arrays.asList("printer_id", "project_id", "status");

        String selectPrinterId = Query.selectFromWhereAnd(Collections.singletonList(PrinterFields.ID.toString()),
                Collections.singletonList(Table.labelprinter.toString()),
                Arrays.asList(new Pair<>(PrinterFields.NAME.toString(), entry.getPrinterName()),
                        new Pair<>(PrinterFields.LOCATION.toString(), entry.getPrinterLocation())));
        String selectProjectId = Query.selectFromWhereAnd(Collections.singletonList(ProjectFields.ID.toString()),
                Collections.singletonList(Table.projects.toString()),
                Collections.singletonList(new Pair<>(ProjectFields.OPENBISID.toString(), entry.getProjectName())));

        database.save(Table.printer_project_association.toString(), entries, Arrays.asList(
                "(" + selectPrinterId + ")", "(" + selectProjectId + ")", "'" + entry.getStatus().toString() + "'"), false);

    }

    private void reload(){
        grid.clearSortOrder();
    }

    private void deleteButtonListener() {
        this.form.getDeleteButton().addClickListener(clickEvent -> {
            deleteEntry();
            reload();
            this.form.emptyForm();
        });
    }

    private void deleteEntry() {
        if (this.form.getRowID() == null || this.form.getRowID().isEmpty()) {
            System.out.println("Please enter information");
        } else {
            database.delete(Table.printer_project_association.toString(), this.form.getRowID().getItem(
                    this.form.getRowID().getValue()).toString().split(":")[2]);
            this.form.emptyForm();
            reload();

        }
    }
}
