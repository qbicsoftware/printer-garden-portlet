package life.qbic;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import life.qbic.portal.liferayandvaadinhelpers.main.LiferayAndVaadinUtils;

import java.sql.SQLException;

@Theme("mytheme")
@SuppressWarnings("serial")
@Widgetset("life.qbic.AppWidgetSet")
public class MyPortletUI extends UI {

    private static Log log = LogFactoryUtil.getLog(MyPortletUI.class.getName());
    private PrinterForm printerForm = new PrinterForm(this);
    private SQLContainer table;
    private ProjectDatabaseInterface projectDatabase;
    private Grid grid;


    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout mainFrame = new VerticalLayout();
        final HorizontalLayout mainContent = new HorizontalLayout();

        projectDatabase = new ProjectDatabaseImpl();

        try {
            projectDatabase.connectToDatabase();
            log.info("Connection to SQL project database was successful.");
        } catch (SQLException exp) {
            log.error("Could not connect to SQL project database. Reason: " + exp.getMessage());
        }

        try {

            grid = loadDBtoGrid();

            mainContent.setSizeFull();
            grid.setSizeFull();
            mainContent.addComponents(grid, printerForm);
            mainContent.setExpandRatio(grid, 1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        mainFrame.addComponent(mainContent);
        setContent(mainFrame);
    }

    private Grid loadDBtoGrid() throws SQLException{
        table = projectDatabase.loadCompleteTableData("printer_project_association", "id");
        log.info("Loading of project database was successful.");
        Grid grid = new Grid();
        grid.setContainerDataSource(table);
        return grid;
    }

    public void save(PrinterProjectAssociation entry){
        projectDatabase.save(entry);
    }

    public void delete(String id){
        projectDatabase.delete(id);
    }

    public void update(){
        grid.clearSortOrder();
    }
}
