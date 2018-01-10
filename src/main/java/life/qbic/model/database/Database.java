package life.qbic.model.database;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import life.qbic.model.main.MyPortletUI;
import life.qbic.utils.MyNotification;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Class {@link Database} provides access to a database system for authorized users.
 *
 * @author fhanssen
 */

public class Database {

    private final String driverName = "com.mysql.jdbc.Driver";
    private final String connectionURI;
    private JDBCConnectionPool pool;
    private final String user;
    private final String password;

    private static final Log log = LogFactoryUtil.getLog(Database.class.getName());

    public Database(String user, String password, String driverName, String connectionURI) {
        //this.driverName = driverName;
        this.connectionURI = connectionURI;
        this.user = user;
        this.password = password;
    }

    /**
     * Connects to the database via SimpleJDBConnection tool. The initial connection number is 5, max connections are 10.
     * @throws IllegalArgumentException
     * @throws SQLException
     */
    public void connectToDatabase() throws IllegalArgumentException, SQLException {
        if (pool == null) {
            pool = new SimpleJDBCConnectionPool(driverName, connectionURI, user, password, 5, 10);
        }
    }

    /**
     * Loads a specified table into into an SQlContainer.
     * @param tableName
     * @param primaryKey
     * @return SQLContainer with table data
     * @throws RuntimeException
     * @throws SQLException
     */
    public SQLContainer loadCompleteTable(String tableName, String primaryKey) throws RuntimeException, SQLException {
        TableQuery query = new TableQuery(tableName, pool);
        query.setVersionColumn(primaryKey);
        SQLContainer tableContent = new SQLContainer(query);
        tableContent.setAutoCommit(true);

        return tableContent;
    }

    /**
     * Provide a free form query as a string to load an entire table.
     * @param query
     * @return
     * @throws SQLException
     */
    public SQLContainer loadTableFromQuery(String query) throws SQLException{
        FreeformQuery freeformQuery = new FreeformQuery(query, pool);
        return new SQLContainer(freeformQuery);
    }

    /**
     * Deletes entries from @tableName via a specific row id. If now row id was specified, now entry will be deleted
     * @param tableName
     * @param rowId
     */
    public void delete(String tableName, String rowId) {

        if (rowId == null || rowId.isEmpty()) {
            log.info(MyPortletUI.toolname + ": " + "Provided ID from table " + tableName + " is null.");
            return;
        }
        String query = Query.deleteFromWhere(tableName, "id", rowId);
        executeFreeQuery(query);

    }

    /**
     * Save a new entry in a table.
     * @param tableName table to store in
     * @param entry Column names in table, that get values.
     * @param values Values added to the table.
     *               Values will be stored in the column that is specified at the same index of the entry list.
     * @param isStatement True if save command contains SELECT statements, false otherwise
     */
    public void save(String tableName, List<String> entry, List<String> values, boolean isStatement) {
        if (entry == null) {
            log.info(MyPortletUI.toolname + ": " + "Provided entry to safe is null.");
            return;
        }
        String query;
        if(isStatement){
            query = Query.insertIntoStatementValues(tableName, entry, values).concat(";");
        }else{
            query = Query.insertIntoSingleValues(tableName, entry, values).concat(";");
        }
        executeFreeQuery(query);
    }

    private void executeFreeQuery(String query){
        Connection conn = null;
        try {

            conn = pool.reserveConnection();
            Statement statement = conn.createStatement();

            statement.executeUpdate(query);
            statement.close();
            conn.setAutoCommit(true);
            conn.commit();
            log.info(MyPortletUI.toolname + ": " + "Query was executed successfully.");
        } catch (SQLException e) {
            MyNotification.notification("Error", "Database access failed.", "error");
            log.error(MyPortletUI.toolname + ": " + "Changes could not be executed on database: Query:\n" + query);
        } finally {
            pool.releaseConnection(conn);
            log.info(MyPortletUI.toolname + ": " + "Connection was released after free query execution.");
        }
    }

    public JDBCConnectionPool getPool() {
        return pool;
    }

}



