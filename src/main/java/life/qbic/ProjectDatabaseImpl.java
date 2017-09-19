package life.qbic;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ProjectDatabaseImpl implements ProjectDatabaseInterface{

    private String driverName = "com.mysql.jdbc.Driver";

    private String connectionURI = "jdbc:mysql://portal-testing.am10.uni-tuebingen.de:3306/qbic_usermanagement_db";

    private JDBCConnectionPool pool;

    private String user;

    private String password;

    private String tableName;

    private static Log log = LogFactoryUtil.getLog(ProjectDatabaseImpl.class.getName());

    public ProjectDatabaseImpl(){
        user = "mariadbuser";
        password = "dZAmDa9-Ysq_Zv1AGygQ";
    }

    @Override
    public boolean connectToDatabase() throws IllegalArgumentException, SQLException {
        if (pool == null) {
            pool = new SimpleJDBCConnectionPool(driverName, connectionURI, user, password, 5, 10);
            return true;
        }
        return false;

    }

    @Override
    public SQLContainer loadCompleteTableData(String tableName, String primaryKey) throws RuntimeException, SQLException {
        this.tableName = tableName; //fix this later
        TableQuery query = new TableQuery(this.tableName, pool);
        query.setVersionColumn(primaryKey);
        SQLContainer tableContent = new SQLContainer(query);
        tableContent.setAutoCommit(true);
        return tableContent;
    }

    @Override
    public void save(PrinterProjectAssociation entry) {
        if (entry == null) {
            log.info("Entry is null.");
            return;
        }
        Connection conn = null;
        try {

            conn = pool.reserveConnection();

            Statement statement = conn.createStatement();

            String query = "INSERT INTO " + tableName + " (printer_id, project_id, status) VALUES (" + entry.getPrinterID()
            + ", " + entry.getProjectID() + ", '" + entry.getStatus() + "')";

            statement.executeUpdate(query);
            statement.close();
            conn.setAutoCommit(true);
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }
    }

    @Override
    public void delete(String id) {

        if (id == null || id.isEmpty()) {
            log.info("ID is null.");
            return;
        }
        Connection conn = null;
        try {

            conn = pool.reserveConnection();

            Statement statement = conn.createStatement();

            String query = "DELETE FROM " + this.tableName + " WHERE `id`=" + id;

            statement.executeUpdate(query);
            statement.close();
            conn.setAutoCommit(true);
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.releaseConnection(conn);
        }

    }
}
