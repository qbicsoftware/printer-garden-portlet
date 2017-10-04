package database;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {

    private String driverName = "com.mysql.jdbc.Driver";

    private String connectionURI = "jdbc:mysql://portal-testing.am10.uni-tuebingen.de:3306/qbic_usermanagement_db";

    JDBCConnectionPool pool;

    String tableName;

    private String user;

    private String password;

    static Log log = LogFactoryUtil.getLog(Database.class.getName());

    public Database() {
        user = "mariadbuser";
        password = "dZAmDa9-Ysq_Zv1AGygQ";
    }

    public boolean connectToDatabase() throws IllegalArgumentException, SQLException {
        if (pool == null) {
            pool = new SimpleJDBCConnectionPool(driverName, connectionURI, user, password, 5, 10);
            return true;
        }
        return false;

    }

    public SQLContainer loadCompleteTableData(String tableName, String primaryKey) throws RuntimeException, SQLException {
        this.tableName = tableName; //fix this later
        TableQuery query = new TableQuery(this.tableName, pool);
        query.setVersionColumn(primaryKey);
        SQLContainer tableContent = new SQLContainer(query);
        tableContent.setAutoCommit(true);

//        addGeneratedColumn("printer_id", new Table.ColumnGenerator(){
//            public Component generateCell(Table source, Object itemID, Object columnID){
//                if(getItem(itemID).getItemProperty("printer_id").getValue() != null){
//                    Label l = new Label();
//                    int printerID = (Integer) getItem(itemID).getItemProperty("printer_id").getValue();
//                    l.setValue(app.getDbHelp().getPrinterName(printerID));
//                    l.setSizeUndefined();
//                    return l;
//                }
//                return null;
//            }
//        });

        return tableContent;
    }

    public void delete(String tableName, String id) {

        if (id == null || id.isEmpty()) {
            log.info("ID is null.");
            return;
        }
        Connection conn = null;
        try {

            conn = pool.reserveConnection();

            Statement statement = conn.createStatement();

            String query = "DELETE FROM " + tableName + " WHERE `id`=" + id;

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

    public void save(String tableName, String entry, String values) {
        if (entry == null) {
            log.info("Entry is null.");
            return;
        }
        Connection conn = null;
        try {

            conn = pool.reserveConnection();

            Statement statement = conn.createStatement();

            String query = "INSERT INTO " + tableName + entry + " VALUES " + values;
            System.out.println(query);
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



