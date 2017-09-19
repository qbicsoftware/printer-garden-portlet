package life.qbic;

import com.vaadin.data.util.sqlcontainer.SQLContainer;

import java.sql.SQLException;


public interface ProjectDatabaseInterface {

    boolean connectToDatabase() throws IllegalArgumentException, SQLException;

    SQLContainer loadCompleteTableData(String tableName, String primaryKey) throws SQLException;

    void save(PrinterProjectAssociation entry);

    void delete(String id);
}
