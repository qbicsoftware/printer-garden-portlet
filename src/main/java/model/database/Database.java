package model.database;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class Database {

    private final String driverName = "com.mysql.jdbc.Driver";
    private final String connectionURI = "jdbc:mysql://portal-testing.am10.uni-tuebingen.de:3306/qbic_usermanagement_db";

    private JDBCConnectionPool pool;
    private final String user;
    private final String password;

    private static final Log log = LogFactoryUtil.getLog(Database.class.getName());

    public Database() {
        user = "mariadbuser";
        password = "dZAmDa9-Ysq_Zv1AGygQ";
    }

    public void connectToDatabase() throws IllegalArgumentException, SQLException {
        if (pool == null) {
            pool = new SimpleJDBCConnectionPool(driverName, connectionURI, user, password, 5, 10);
        }
    }

    public SQLContainer loadCompleteTable(String tableName, String primaryKey) throws RuntimeException, SQLException {
        TableQuery query = new TableQuery(tableName, pool);
        query.setVersionColumn(primaryKey);
        SQLContainer tableContent = new SQLContainer(query);
        tableContent.setAutoCommit(true);

        return tableContent;
    }

    public SQLContainer loadTableFromQuery(String query) throws SQLException{
        FreeformQuery freeformQuery = new FreeformQuery(query, pool);
        return new SQLContainer(freeformQuery);
    }

    public void delete(String tableName, String rowId) {

        if (rowId == null || rowId.isEmpty()) {
            log.info("ID is null.");
            return;
        }
        String query = Query.deleteFromWhere(tableName, "id", rowId);
        executeFreeQuery(query);

    }

    public void save(String tableName, List<String> entry, List<String> values, boolean isStatement) {
        if (entry == null) {
            log.info("Entry is null.");
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

    public void executeFreeQuery(String query){
        Connection conn = null;
        try {

            conn = pool.reserveConnection();
            Statement statement = conn.createStatement();
            System.out.println(query);

            statement.executeUpdate(query);
            statement.close();
            conn.setAutoCommit(true);
            conn.commit();

        } catch (SQLException e) {
            log.error("Changes could not be executed on database. Reason: " + e.getMessage());
        } finally {
            pool.releaseConnection(conn);
        }
    }

    public JDBCConnectionPool getPool() {
        return pool;
    }

}



