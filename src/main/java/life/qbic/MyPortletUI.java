package life.qbic;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import database.Database;
import database.Query;
import javafx.util.Pair;
import tables.AForm;
import tables.printer.Printer;
import tables.printer.PrinterFields;
import tables.printer.PrinterForm;
import tables.printerProjectAssociation.PrinterProjectAssociation;
import tables.printerProjectAssociation.PrinterProjectAssociationForm;
import tables.Table;
import tables.printerProjectAssociation.PrinterProjectFields;
import tables.project.ProjectFields;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Theme("mytheme")
@SuppressWarnings("serial")
@Widgetset("life.qbic.AppWidgetSet")
public class MyPortletUI extends UI {

    private static final Log log = LogFactoryUtil.getLog(MyPortletUI.class.getName());

    private Database database;

    private Grid gridPrinter;
    private Grid gridPrinterProjectAssociation;

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout mainFrame = new VerticalLayout();
        connectToDatabase();

        try {
            setData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final HorizontalLayout contentPrinter = addPrinterGrid();
        final HorizontalLayout contentPrinterProjectAssociation = addPrinterProjectAssociationGrid();
        mainFrame.addComponents(contentPrinter, contentPrinterProjectAssociation);
        setContent(mainFrame);
    }


    public void saveToPrinter(Printer entry){
        List<String> entries = Arrays.asList("name", "location", "url", "status", "type", "admin_only", "user_group");
        List<String> values = Arrays.asList(entry.getName(), entry.getLocation(), entry.getUrl(), entry.getStatus().toString(),
                entry.getType().toString(), entry.getIsAdmin(), entry.getUserGroup());
        database.save(Table.labelprinter.toString(), entries, values, false);
    }

    public void saveToPrinterProjectAssociation(PrinterProjectAssociation entry){

        List<String> entries = Arrays.asList("printer_id", "project_id", "status");

        String selectPrinterId = Query.selectFromWhereAnd(Collections.singletonList(PrinterFields.ID.toString()),
                                                            Collections.singletonList(Table.labelprinter.toString()),
                                                          Arrays.asList(new Pair<>(PrinterFields.NAME.toString(), entry.getPrinterName()),
                                                                        new Pair<>(PrinterFields.LOCATION.toString(), entry.getPrinterLocation())));
        String selectProjectId = Query.selectFromWhereAnd(Collections.singletonList(ProjectFields.ID.toString()),
                                                            Collections.singletonList(Table.projects.toString()),
                                                            Collections.singletonList(new Pair<>(ProjectFields.OPENBISID.toString(), entry.getProjectName())));
        //TODO ask andreas about status field: pot have to update form if is ppa attribute
        String selectPrinterProjectStatus = Query.selectFromWhereAnd(Collections.singletonList(PrinterFields.STATUS.toString()),
                                                                        Collections.singletonList(Table.labelprinter.toString()),
                                                              Arrays.asList(new Pair<>(PrinterFields.NAME.toString(), entry.getPrinterName()),
                                                                            new Pair<>(PrinterFields.LOCATION.toString(), entry.getPrinterLocation())));

        database.save(Table.printer_project_association.toString(), entries, Arrays.asList(
                selectPrinterId,selectProjectId,selectPrinterProjectStatus), true);

    }

    public void delete(String tableName, String id){
        database.delete(tableName, id);
    }

    public void reload(Table table){
        if(table.toString().equals(Table.labelprinter.toString())){
            gridPrinter.clearSortOrder();
        }else if(table.toString().equals(Table.printer_project_association.toString())){
            gridPrinterProjectAssociation.clearSortOrder();
        }
    }

    public Database getDatabase() {
        return database;
    }

    private void connectToDatabase(){
        this.database = new Database();
        try {
            this.database.connectToDatabase();
            log.info("Connection to SQL database was successful.");
        } catch (SQLException exp) {
            log.error("Could not connect to SQL database. Reason: " + exp.getMessage());
        }
    }

    private void setData() throws SQLException{

        SQLContainer tableLabelprinter = database.loadCompleteTable(Table.labelprinter.toString(), "id");
        gridPrinter = loadTableToGrid(tableLabelprinter);
        gridPrinter.setEditorEnabled(true);
        gridPrinter.setEditorBuffered(true);

        List<String> printerProjectFields = Arrays.asList(PrinterProjectFields.ID.toString(),
                PrinterProjectFields.PRINTER_ID.toString(),
                PrinterFields.NAME.toString(),
                PrinterFields.LOCATION.toString(),
                PrinterProjectFields.PROJECT_ID.toString(),
                ProjectFields.OPENBISID.toString(),
                PrinterFields.STATUS.toString());

        List<String> location = Collections.singletonList(Table.printer_project_association.toString());

        SQLContainer tablePrinterProjectAssociation = database.loadTableFromQuery(
                Query.selectFrom(printerProjectFields, location) +
                " " + Query.innerJoinOn(Table.labelprinter.toString(), PrinterProjectFields.PRINTER_ID.toString(), PrinterFields.ID.toString()) +
                " " + Query.innerJoinOn(Table.projects.toString(), PrinterProjectFields.PROJECT_ID.toString(), ProjectFields.ID.toString()) +
                " ORDER BY " + PrinterProjectFields.ID.toString() + ";");

        gridPrinterProjectAssociation = loadTableToGrid(tablePrinterProjectAssociation);
        gridPrinterProjectAssociation.setEditorEnabled(false);
        gridPrinterProjectAssociation.setEditorBuffered(false);

    }

    private HorizontalLayout addPrinterGrid(){
        HorizontalLayout contentPrinter = new HorizontalLayout();
        contentPrinter.setSizeFull();
        AForm projectForm = new PrinterForm(this);
        contentPrinter.addComponents(gridPrinter, projectForm);
        contentPrinter.setExpandRatio(gridPrinter, 1);

        //Collect edits in printer grid
        gridPrinter.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                reload(Table.labelprinter);
            }
        });

        return contentPrinter;

    }

    private HorizontalLayout addPrinterProjectAssociationGrid(){

        final HorizontalLayout contentPrinterProjectAssociation = new HorizontalLayout();

        contentPrinterProjectAssociation.setSizeFull();
        AForm printerProjectAssociationForm = new PrinterProjectAssociationForm(this);
        contentPrinterProjectAssociation.addComponents(gridPrinterProjectAssociation, printerProjectAssociationForm);
        contentPrinterProjectAssociation.setExpandRatio(gridPrinterProjectAssociation, 1);

        return contentPrinterProjectAssociation;
    }

    private Grid loadTableToGrid(SQLContainer table){

        log.info("Loading of table from database was successful.");
        Grid grid = new Grid();
        grid.isEditorActive();
        grid.setContainerDataSource(table);
        grid.setSizeFull();

        return grid;
    }
}
