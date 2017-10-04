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

import java.sql.SQLException;

@Theme("mytheme")
@SuppressWarnings("serial")
@Widgetset("life.qbic.AppWidgetSet")
public class MyPortletUI extends UI {

    private static Log log = LogFactoryUtil.getLog(MyPortletUI.class.getName());
    private PrinterProjectForm printerProjectForm = new PrinterProjectForm(this);
    private PrinterForm projectForm = new PrinterForm(this);
    private SQLContainer table;
    private SQLContainer table2;
    private Database database;
    private Grid gridPrinterProjectAsso;
    private Grid gridPrinter;

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout mainFrame = new VerticalLayout();
        final HorizontalLayout contentPrinterProject = new HorizontalLayout();
        final HorizontalLayout contentPrinter = new HorizontalLayout();

        database = new Database();
        connectToDatabase();

        try {


            table2 = database.loadCompleteTableData(Table.labelprinter.toString(), "id");
            gridPrinter = loadDBtoGrid(table2);

            table = database.loadCompleteTableData(Table.printer_project_association.toString(), "id");
            table.addReference(table2, "printer_id", "name");
            gridPrinterProjectAsso = loadDBtoGrid(table);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        contentPrinter.setSizeFull();
        contentPrinter.addComponents(gridPrinter, projectForm);
        contentPrinter.setExpandRatio(gridPrinter, 1);

        gridPrinter.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                System.out.println("pre");
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                refreshPrinter();
                System.out.println("post");
            }
        });


        contentPrinterProject.setSizeFull();
        contentPrinterProject.addComponents(gridPrinterProjectAsso, printerProjectForm);
        contentPrinterProject.setExpandRatio(gridPrinterProjectAsso, 1);

        gridPrinterProjectAsso.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                System.out.println("pre");
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                updatePrinterProjectAssociation();
                System.out.println("post");
            }
        });

        contentPrinter.setSizeFull();
        contentPrinter.addComponents(gridPrinter, projectForm);
        contentPrinter.setExpandRatio(gridPrinter, 1);

        gridPrinter.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                System.out.println("pre");
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                refreshPrinter();
                System.out.println("post");
            }
        });

        mainFrame.addComponents(contentPrinterProject, contentPrinter);
        setContent(mainFrame);
    }

    private Grid loadDBtoGrid(SQLContainer table) throws SQLException{


        log.info("Loading of project database was successful.");
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
            log.info("Connection to SQL project database was successful.");
        } catch (SQLException exp) {
            log.error("Could not connect to SQL project database. Reason: " + exp.getMessage());
        }
    }


    /**
     * Saves new printer entry
     * @param entry new printer_project_association
     */
    public void savePrinterProjectAssociation(PrinterProjectAssociation entry){
        database.save(Table.printer_project_association.toString()," (printer_id, project_id, status) ", "(" + entry.getPrinterID()
                + ", " + entry.getProjectID() + ", '" + entry.getStatus() + "')");
    }


    public void savePrinter(Printer entry){
        database.save(Table.labelprinter.toString()," (name, location, url, status, type, admin_only, user_group) ", "('" + entry.getName()
                + "', '" + entry.getLocation() +  "', '" + entry.getUrl() + "', '" + entry.getStatus() + "', '"  + entry.getType() + "', '"
                + entry.getIsAdmin() + "', '" + entry.getUserGroup() + "')");
    }

    public void delete(String tableName, String id){
        database.delete(tableName, id);
    }

    public void updatePrinterProjectAssociation(){
        gridPrinterProjectAsso.clearSortOrder();
    }

    public void refreshPrinter(){
        gridPrinter.clearSortOrder();
    }

}
