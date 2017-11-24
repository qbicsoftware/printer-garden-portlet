package tables;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import database.Query;
import elemental.events.KeyboardEvent;
import life.qbic.MyPortletUI;
import tables.printerProjectAssociation.PrinterProjectFields;

import java.sql.SQLException;
import java.util.Collections;

//TODO maybe separate view and logic better
public abstract class AForm extends FormLayout implements IForm {


    protected final MyPortletUI myUI;
    protected final Button saveButton = new Button("Save");
    protected final Button deleteButton = new Button("Delete");

    protected final ComboBox rowID = new ComboBox("ID");

    protected final Table table;

    protected AForm(MyPortletUI myUI, Table table){
        super();
        this.myUI = myUI;
        this.table = table;

        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.setClickShortcut(KeyboardEvent.KeyCode.ENTER);

        try {
            setExistingIDs();
        }catch(SQLException e){
            e.printStackTrace();
        }
        addButtonLogic();
    }


    private void addButtonLogic(){
        saveButton.addClickListener(e -> this.saveEntry());
        deleteButton.addClickListener(e -> this.deleteEntry());
    }

    private void setExistingIDs() throws SQLException{
        //Have to be distinct, they are a primary keys
        SQLContainer allExisIds = new SQLContainer(new FreeformQuery(
                Query.selectFrom(Collections.singletonList(table.toString()+ ".id"),
                                            Collections.singletonList(table.toString()))+";",
                                            myUI.getDatabase().getPool()));

        rowID.addItem("");
        rowID.setContainerDataSource(allExisIds);
        rowID.setItemCaptionPropertyId("id");
        rowID.setImmediate(true);
    }

    private void deleteEntry(){
        if(rowID == null || rowID.isEmpty()){
            System.out.println("Please enter information");
        }else {
            if(table.equals(Table.labelprinter)){
                String deletion = Query.deleteFromWhere(Table.printer_project_association.toString(),
                                                        PrinterProjectFields.PRINTER_ID.toString(),
                                                        rowID.getItem(rowID.getValue()).toString().split(":")[2]);
                myUI.getDatabase().executeFreeQuery(deletion);
                myUI.reload(Table.printer_project_association);
            }
            myUI.delete(table.toString(), rowID.getItem(rowID.getValue()).toString().split(":")[2]);
            rowID.clear();
            myUI.reload(table);
        }
    }


}
