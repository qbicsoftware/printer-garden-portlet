package presenter;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.Grid;
import life.qbic.MyPortletUI;
import model.config.ConfigurationManagerFactory;
import model.database.Database;
import model.database.Query;
import model.tables.Table;
import model.tables.printer.PrinterFields;
import model.tables.printerProjectAssociation.PrinterProjectFields;
import model.tables.project.ProjectFields;
import view.MainView;
import view.forms.PrinterFormView;
import view.forms.PrinterProjectFormView;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainPresenter {

    private final MainView view;
    private final Database database;
    private final MyPortletUI ui;

    private static final Log log = LogFactoryUtil.getLog(MainPresenter.class.getName());


    public MainPresenter(MainView view, MyPortletUI ui){
        this.view = view;

        model.config.ConfigurationManager c = ConfigurationManagerFactory.getInstance();
        //some issue with added whitespaces, trim those
        this.database = new Database(c.getMysqlUser().trim(), c.getMysqlPass(), "",
                "jdbc:mariadb://" + c.getMysqlHost().trim()+ ":" + c.getMysqlPort().trim() + "/" + c.getMysqlDB().trim());
        this.ui = ui;

        connectToDatabase();
        setUpListeners();
    }

    private void connectToDatabase(){
        try {
            this.database.connectToDatabase();
            log.info("Connection to SQL model.database was successful.");
        } catch (SQLException exp) {
            log.error("Could not connect to SQL model.database. Reason: " + exp.getMessage());
        }
    }

    private void setUpListeners(){
        addSelectionListener();
    }

    private void addSelectionListener(){
        this.view.getSelection().addValueChangeListener(valueChangeEvent -> {
            if (this.view.getSelection().getValue().equals("Printer")) {
                addPrinter();

            }else if(this.view.getSelection().getValue().equals("Printer Project Association")){
                addPrinterProject();
            }
        });
    }

    private void addPrinter(){
        try {
            SQLContainer allExisIds = new SQLContainer(new FreeformQuery(
                    Query.selectFrom(Collections.singletonList(PrinterFields.ID.toString()),
                            Collections.singletonList(Table.labelprinter.toString()))+";",
                    database.getPool()));
            PrinterFormView form =new PrinterFormView(this.ui, allExisIds);
            Grid grid = makeGridEditable(getPrinterGrid());
            PrinterPresenter presenter = new PrinterPresenter(form, database, grid);
            this.view.addGrid(grid, form);
        }catch(SQLException e){
            log.error("Could not connect to SQL model.database. Reason: " + e.getMessage());

        }
    }

    private Grid makeGridEditable(Grid grid){
        //Collect edits in printer grid
        grid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
            }
            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                reload(grid);
            }
        });
        return grid;
    }

    private void addPrinterProject(){
        try {
            SQLContainer allExisIds = new SQLContainer(new FreeformQuery(
                    Query.selectFrom(Collections.singletonList(PrinterProjectFields.ID.toString()),
                            Collections.singletonList(Table.printer_project_association.toString()))+";",
                    database.getPool()));
            SQLContainer allExisProjectNames = new SQLContainer(new FreeformQuery(
                    Query.selectDistinctFrom(Collections.singletonList(ProjectFields.OPENBISID.toString()),
                            Collections.singletonList(Table.projects.toString())) + ";"
                    , database.getPool()));
            SQLContainer allExisPrinterNames = new SQLContainer(new FreeformQuery(
                    Query.selectFrom(Arrays.asList(PrinterFields.NAME.toString(), PrinterFields.LOCATION.toString()),
                            Collections.singletonList(Table.labelprinter.toString()))+";"
                    , database.getPool()));
            PrinterProjectFormView form = new PrinterProjectFormView(this.ui,allExisIds, allExisPrinterNames, allExisProjectNames);
            Grid grid = getPrinterProjectGrid();
            PrinterProjectPresenter presenter = new PrinterProjectPresenter(form, database, grid);
            this.view.addGrid(grid, form);

        }catch(SQLException e){
            log.error("Could not connect to SQL model.database. Reason: " + e.getMessage());

        }
    }

    private void reload(Grid grid){
        grid.clearSortOrder();
    }

    private Grid getPrinterGrid(){
        Grid printerGrid = new Grid();
        try {
            SQLContainer tableLabelprinter = database.loadCompleteTable(Table.labelprinter.toString(), "id");
            printerGrid = loadTableToGrid(tableLabelprinter);
            printerGrid.setEditorEnabled(true);
            printerGrid.setEditorBuffered(true);
        }catch(SQLException e){
            log.error("Could not connect to SQL database. Reason: " + e.getMessage());

        }
        return printerGrid;
    }

    private Grid getPrinterProjectGrid(){
        Grid printerProjectAssociationGrid = new Grid();
        List<String> printerProjectFields = Arrays.asList(PrinterProjectFields.ID.toString(),
                PrinterProjectFields.PRINTER_ID.toString(),
                PrinterFields.NAME.toString(),
                PrinterFields.LOCATION.toString(),
                PrinterProjectFields.PROJECT_ID.toString(),
                ProjectFields.OPENBISID.toString(),
                PrinterProjectFields.STATUS.toString());

        List<String> location = Collections.singletonList(Table.printer_project_association.toString());

        try {
            SQLContainer tablePrinterProjectAssociation = database.loadTableFromQuery(
                    Query.selectFrom(printerProjectFields, location) +
                            " " + Query.innerJoinOn(Table.labelprinter.toString(), PrinterProjectFields.PRINTER_ID.toString(), PrinterFields.ID.toString()) +
                            " " + Query.innerJoinOn(Table.projects.toString(), PrinterProjectFields.PROJECT_ID.toString(), ProjectFields.ID.toString()) +
                            " ORDER BY " + PrinterProjectFields.ID.toString() + ";");

            printerProjectAssociationGrid = loadTableToGrid(tablePrinterProjectAssociation);
            printerProjectAssociationGrid.setEditorEnabled(false);
            printerProjectAssociationGrid.setEditorBuffered(false);
        }catch(SQLException e){
            log.error("Could not connect to SQL model.database. Reason: " + e.getMessage());
        }
        return printerProjectAssociationGrid;

    }

    private Grid loadTableToGrid(SQLContainer table){

        log.info("Loading of table from Database was successful.");
        Grid grid = new Grid();
        grid.isEditorActive();
        grid.setContainerDataSource(table);
        grid.setSizeFull();
        return grid;
    }


}
