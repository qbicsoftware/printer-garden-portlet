package life.qbic.presenter;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import life.qbic.model.main.MyPortletUI;
import life.qbic.model.config.ConfigurationManagerFactory;
import life.qbic.model.database.Database;
import life.qbic.model.database.Query;
import life.qbic.model.tables.Table;
import life.qbic.model.tables.printer.PrinterFields;
import life.qbic.model.tables.printerProjectAssociation.PrinterProjectFields;
import life.qbic.model.tables.project.ProjectFields;
import life.qbic.view.MainView;
import life.qbic.view.forms.PrinterFormView;
import life.qbic.view.forms.PrinterProjectFormView;
import life.qbic.portal.liferayandvaadinhelpers.main.LiferayAndVaadinUtils;


import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainPresenter {

    private final MainView view;
    private final Database database;

    private static final Log log = LogFactoryUtil.getLog(MainPresenter.class.getName());


    public MainPresenter(MainView view, MyPortletUI ui){

        if (LiferayAndVaadinUtils.isLiferayPortlet()) {
            log.info(MyPortletUI.toolname + ": " +"Printer Garden is running on Liferay and user is logged in.");
            log.info(MyPortletUI.toolname + ": " +"UserID = " + LiferayAndVaadinUtils.getUser().getScreenName());
        }

        this.view = view;

        life.qbic.model.config.ConfigurationManager c = ConfigurationManagerFactory.getInstance();
        //some issue with added whitespaces, trim those
        this.database = new Database(c.getMysqlUser().trim(), c.getMysqlPass().trim(), "",
                "jdbc:mariadb://" + c.getMysqlHost().trim()+ ":" + c.getMysqlPort().trim() + "/" + c.getMysqlDB().trim());

        connectToDatabase();
        setUpListeners();

    }

    private void connectToDatabase(){
        try {
            log.debug(MyPortletUI.toolname + ": " +"Trying to connect to database.");
            this.database.connectToDatabase();
            log.info(MyPortletUI.toolname + ": " +"Connection to database was successful.");
        } catch (SQLException exp) {
            log.error(MyPortletUI.toolname + ": " + LiferayAndVaadinUtils.getUser().getScreenName() + " could not connect to database. Reason: " + exp.getMessage());
        }
    }

    private void setUpListeners(){
        addSelectionListener();
    }

    private void addSelectionListener(){
        this.view.getSelection().addValueChangeListener(valueChangeEvent -> {
            if (this.view.getSelection().getValue().equals("Printer")) {
                addPrinter();
                log.info(MyPortletUI.toolname + ": " +"Printer table was selected");
            }else if(this.view.getSelection().getValue().equals("Printer Project Association")){
                addPrinterProject();
                log.info(MyPortletUI.toolname + ": " +"Printer Project table was selected");
            }
        });
    }

    private void addPrinter(){
        try {
            log.debug(MyPortletUI.toolname + ": " +"Try to access and retrieve printer table");
            SQLContainer allExisIds = new SQLContainer(new FreeformQuery(
                    Query.selectFrom(Collections.singletonList(PrinterFields.ID.toString()),
                            Collections.singletonList(Table.labelprinter.toString()))+";",
                    database.getPool()));
            PrinterFormView form =new PrinterFormView(allExisIds);
            Grid grid = makeGridEditable(getPrinterGrid());
            PrinterPresenter presenter = new PrinterPresenter(form, database, grid);
            this.view.addGrid(grid, form);
            log.info(MyPortletUI.toolname + ": " +"Printer table was retrieved and set up successfully.");
        }catch(SQLException e){
            log.error(MyPortletUI.toolname + ": " +"Access and retrieval of printer table failed: " + e.getMessage());

        }
    }

    private Grid makeGridEditable(Grid grid){
        //Collect edits in printer grid
        grid.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) {
            }
            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) {
                reload(grid);
            }
        });
        return grid;
    }

    private void addPrinterProject(){
        try {
            log.debug(MyPortletUI.toolname + ": " +"Try to access and retrieve printer-project table");
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
            PrinterProjectFormView form = new PrinterProjectFormView(allExisIds, allExisPrinterNames, allExisProjectNames);
            Grid grid = getPrinterProjectGrid();
            PrinterProjectPresenter presenter = new PrinterProjectPresenter(form, database, grid);
            this.view.addGrid(grid, form);
            log.info(MyPortletUI.toolname + ": " +"Printer-project table was retrieved and set up successfully.");
        }catch(SQLException e){
            log.error(MyPortletUI.toolname + ": " +"Printer-project table could not be set up " + e.getMessage());

        }
    }

    private void reload(Grid grid){
        grid.clearSortOrder();
    }

    private Grid getPrinterGrid(){
        Grid printerGrid = new Grid();
        try {
            log.debug(MyPortletUI.toolname + ": " +"Try to load printer table.");
            SQLContainer tableLabelprinter = database.loadCompleteTable(Table.labelprinter.toString(), "id");
            printerGrid = loadTableToGrid(tableLabelprinter);
            printerGrid.setEditorEnabled(true);
            printerGrid.setEditorBuffered(true);
            log.info(MyPortletUI.toolname + ": " +"Successfully loaded printer table.");
        }catch(SQLException e){
            log.error(MyPortletUI.toolname + ": " +"Printer table could not be loaded: " + e.getMessage());

        }
        return printerGrid;
    }

    private Grid getPrinterProjectGrid(){
        Grid printerProjectAssociationGrid = new Grid();
        List<String> printerProjectFields = Arrays.asList(PrinterProjectFields.ID.toString(),
                PrinterFields.NAME.toString(),
                PrinterFields.LOCATION.toString(),
                ProjectFields.OPENBISID.toString(),
                PrinterProjectFields.STATUS.toString());

        List<String> location = Collections.singletonList(Table.printer_project_association.toString());

        try {
            log.debug(MyPortletUI.toolname + ": " +"Try to load and configure printer-project table with freeFormQueries");
            SQLContainer tablePrinterProjectAssociation = database.loadTableFromQuery(
                    Query.selectFrom(printerProjectFields, location) +
                            " " + Query.innerJoinOn(Table.labelprinter.toString(), PrinterProjectFields.PRINTER_ID.toString(), PrinterFields.ID.toString()) +
                            " " + Query.innerJoinOn(Table.projects.toString(), PrinterProjectFields.PROJECT_ID.toString(), ProjectFields.ID.toString()) +
                            " ORDER BY " + PrinterProjectFields.ID.toString() + ";");

            printerProjectAssociationGrid = loadTableToGrid(tablePrinterProjectAssociation);
            printerProjectAssociationGrid.setEditorEnabled(false);
            printerProjectAssociationGrid.setEditorBuffered(false);
            log.info(MyPortletUI.toolname + ": " +"Printer-project table was loaded successfully.");
        }catch(SQLException e){
            log.error(MyPortletUI.toolname + ": " +"Printer-project table could not be loaded: " + e.getMessage());
        }
        return printerProjectAssociationGrid;

    }

    private Grid loadTableToGrid(SQLContainer table){

        log.info(MyPortletUI.toolname + ": " +"Loading of table from Database was successful.");
        Grid grid = new Grid();
        grid.isEditorActive();
        grid.setContainerDataSource(table);
        grid.setSizeFull();
        return grid;
    }


}
