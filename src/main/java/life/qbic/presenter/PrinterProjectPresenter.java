package life.qbic.presenter;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.ui.Grid;
import life.qbic.model.database.Database;
import life.qbic.model.database.Query;
import life.qbic.model.tables.Table;
import life.qbic.model.tables.printer.PrinterFields;
import life.qbic.model.tables.printerProjectAssociation.PrinterProjectAssociation;
import life.qbic.model.tables.project.ProjectFields;
import life.qbic.utils.MyNotification;
import org.javatuples.Pair;
import life.qbic.view.forms.PrinterProjectFormView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PrinterProjectPresenter {

    private final PrinterProjectFormView form;
    private final Database database;
    private final Grid grid;

    private static final Log log = LogFactoryUtil.getLog(PrinterProjectPresenter.class.getName());


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
                MyNotification.notification("Information", "Please enter information!", "" );
                log.info("No information to safe was provided in the printer-project form.");

            }else{
                log.info("New entry is saved to printer-project table.");
                saveToPrinterProject(form.getFormEntries());
                reload();
                this.form.emptyForm();
            }
        });
    }

    private void saveToPrinterProject(PrinterProjectAssociation entry) {

        log.info("Try to save new entry with: \n" +
                "\tprinterName:\t" + entry.getPrinterName() + "\n" +
                "\tprojectName:\t" + entry.getProjectName()  + "\n" +
                "\tstatus:\t" + entry.getStatus());

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
            log.info("No information to delete was provided in the printer-project form.");
            MyNotification.notification("Information", "Please enter information!", "" );
        } else {
            log.info("Entry with ID " + this.form.getRowID().getItem(
                    this.form.getRowID().getValue()).toString().split(":")[2] +" is deleted");
            database.delete(Table.printer_project_association.toString(), this.form.getRowID().getItem(
                    this.form.getRowID().getValue()).toString().split(":")[2]);
            this.form.emptyForm();
            reload();

        }
    }
}
