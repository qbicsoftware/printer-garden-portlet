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
import tables.printer.Printer;
import tables.printer.PrinterForm;
import tables.printerName.PrinterName;
import tables.printerName.PrinterNameForm;
import tables.printerProjectAssociation.PrinterProjectAssociation;
import tables.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Theme("mytheme")
@SuppressWarnings("serial")
@Widgetset("life.qbic.AppWidgetSet")
public class MyPortletUI extends UI {

    private static Log log = LogFactoryUtil.getLog(MyPortletUI.class.getName());
    private PrinterForm projectForm;
    private PrinterNameForm printerNameForm;
    private SQLContainer tableLabelprinter;
    private SQLContainer tablePrinterName;
    private Database database;
    private Grid gridPrinterProjectAsso = new Grid();
    private Grid gridPrinter;
    private Grid gridPrinterName;

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout mainFrame = new VerticalLayout();
        final HorizontalLayout contentPrinter = new HorizontalLayout();
        final HorizontalLayout contentPrinterName = new HorizontalLayout();

        database = new Database();
        connectToDatabase();

        try {

            tableLabelprinter = database.loadCompleteTableData(tables.Table.labelprinter.toString(), "id");
            tableLabelprinter.setAutoCommit(true);
            gridPrinter = loadDBtoGrid(tableLabelprinter);


            tablePrinterName = database.loadNames();
            gridPrinterName = loadDBtoGrid(tablePrinterName);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        contentPrinter.setSizeFull();
        projectForm = new PrinterForm(this);
        contentPrinter.addComponents(gridPrinter, projectForm);
        contentPrinter.setExpandRatio(gridPrinter, 1);

        gridPrinter.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                System.out.println("pre");
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                reloadPrinter();
                System.out.println("post");
            }
        });


        contentPrinterName.setSizeFull();
        printerNameForm = new PrinterNameForm(this);
        gridPrinterName.setEditorEnabled(false);
        gridPrinterName.setEditorBuffered(false);
        contentPrinterName.addComponents(gridPrinterName, printerNameForm);
        contentPrinterName.setExpandRatio(gridPrinterName, 1);

        mainFrame.addComponents(contentPrinter, contentPrinterName);
        setContent(mainFrame);
    }

    private Grid loadDBtoGrid(SQLContainer table) throws SQLException{


        log.info("Loading of tables.project database was successful.");
        Grid grid = new Grid();
        grid.setEditorEnabled(true);
        grid.setEditorBuffered(true);
        grid.isEditorActive();
        grid.setContainerDataSource(table);
        grid.setSizeFull();

        return grid;
    }

    private void connectToDatabase(){
        try {
            database.connectToDatabase();
            log.info("Connection to SQL tables.project database was successful.");
        } catch (SQLException exp) {
            log.error("Could not connect to SQL tables.project database. Reason: " + exp.getMessage());
        }
    }


    /**
     * Saves new printer_project_association entry
     * @param entry new printer_project_association
     */
    public void savePrinterProjectAssociation(PrinterProjectAssociation entry){
        database.save(tables.Table.printer_project_association.toString()," (printer_id, project_id, status) ", "(" + entry.getPrinterID()
                + ", " + entry.getProjectID() + ", '" + entry.getStatus() + "')");
    }

    public void savePrinter(Printer entry){
        database.save(Table.labelprinter.toString()," (name, location, url, status, type, admin_only, user_group) ", "('" + entry.getName()
                + "', '" + entry.getLocation() +  "', '" + entry.getUrl() + "', '" + entry.getStatus() + "', '"  + entry.getType() + "', '"
                + entry.getIsAdmin() + "', '" + entry.getUserGroup() + "')");
    }

    public void savePrinterName(PrinterName entry) throws SQLException{

        if (entry == null) {
            log.info("Entry is null.");
            return;
        }
        Connection conn = null;
        try {

            conn = database.getPool().reserveConnection();

            Statement statement = conn.createStatement();

            String query = "INSERT INTO printer_project_association (printer_id, project_id, status) VALUES (" +
                    "(SELECT labelprinter.id  FROM labelprinter WHERE labelprinter.name = '" + entry.getPrinterName() + "' AND labelprinter.location = '"+ entry.getPrinterLocation() +"' ),"+
                    "(SELECT projects.id  FROM projects WHERE projects.openbis_project_identifier = '"+ entry.getProjectName() +"'),"+
                    "(SELECT labelprinter.id  FROM labelprinter WHERE labelprinter.name = '"+entry.getPrinterName()+"' AND labelprinter.location = '"+entry.getPrinterLocation()+"'));";
            System.out.println(query);
            statement.executeUpdate(query);
            statement.close();
            conn.setAutoCommit(true);
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            database.getPool().releaseConnection(conn);
        }
    }

    public void delete(String tableName, String id){
        database.delete(tableName, id);
    }

    public void reloadPrinterProjectAssociation(){
        gridPrinterProjectAsso.clearSortOrder();
    }

    public void reloadPrinter(){
        gridPrinter.clearSortOrder();
    }

    public void reloadPrinterName(){
        gridPrinterName.clearSortOrder();
    }

    public Database getDatabase() {
        return database;
    }
}
