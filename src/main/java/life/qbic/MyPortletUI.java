package life.qbic;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import database.DatabaseAbstract;

import java.sql.SQLException;

@Theme("mytheme")
@SuppressWarnings("serial")
@Widgetset("life.qbic.AppWidgetSet")
public class MyPortletUI extends UI {

    private static Log log = LogFactoryUtil.getLog(MyPortletUI.class.getName());
    private PrinterProjectForm printerProjectForm = new PrinterProjectForm(this);
    private PrinterForm projectForm = new PrinterForm(this);
    private SQLContainer table;
    private DatabaseAbstract database;
    private Grid gridPrinterProjectAsso;
    private Grid gridPrinter;

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout mainFrame = new VerticalLayout();
        final HorizontalLayout contentPrinterProject = new HorizontalLayout();
        final HorizontalLayout contentPrinter = new HorizontalLayout();

        database = new DatabaseAbstract();
        connectToDatabase();

        try {
            gridPrinterProjectAsso = loadDBtoGrid(Table.printer_project_association.toString(), "id");
            gridPrinter = loadDBtoGrid(Table.labelprinter.toString(), "id");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //gridPrinterProjectAsso.addRow("","","");
        contentPrinterProject.setSizeFull();
        contentPrinterProject.addComponents(gridPrinterProjectAsso, printerProjectForm);
        contentPrinterProject.setExpandRatio(gridPrinterProjectAsso, 1);

        contentPrinter.setSizeFull();
        contentPrinter.addComponents(gridPrinter, projectForm);
        contentPrinter.setExpandRatio(gridPrinter, 1);

        mainFrame.addComponents(contentPrinterProject, contentPrinter);
        setContent(mainFrame);
    }

    private Grid loadDBtoGrid(String tableName, String primaryKey) throws SQLException{
        table = database.loadCompleteTableData(tableName, primaryKey);
        log.info("Loading of project database was successful.");
        Grid grid = new Grid();
        grid.setEditorEnabled(true);
        grid.setEditorBuffered(false);
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
        database.save(Table.printer_project_association.toString(),"(printer_id, project_id, status)", "(" + entry.getPrinterID()
                + ", " + entry.getProjectID() + ", '" + entry.getStatus() + "')");
    }



    public void delete(String tableName, String id){
        database.delete(tableName, id);
    }



    public void updatePrinterProjectAssociation(){
        gridPrinterProjectAsso.clearSortOrder();
    }
}
