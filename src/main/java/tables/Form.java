package tables;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import database.Query;
import elemental.events.KeyboardEvent;
import life.qbic.MyPortletUI;
import tables.printer.PrinterFields;

import java.sql.SQLException;
import java.util.Arrays;

//TODO maybe separate view and logic better
public abstract class Form<T> extends FormLayout {


    public MyPortletUI myUI;
    public Button saveButton = new Button("Save");
    public Button deleteButton = new Button("Delete");

    public ComboBox rowID = new ComboBox("ID");

    public Table table;

    public Form(MyPortletUI myUI, Table table){
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
                Query.selectFrom(Arrays.asList(table.toString()+ ".id"),
                        Arrays.asList(table.toString()))+";"
                , myUI.getDatabase().getPool()));

        rowID.addItem("");
        rowID.setContainerDataSource(allExisIds);
        rowID.setItemCaptionPropertyId("id");
        rowID.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_STARTSWITH);
        rowID.setImmediate(true);
    }

    private void deleteEntry(){
        if(rowID == null || rowID.isEmpty()){
            System.out.println("Please enter information");
        }else {
            myUI.delete(table.toString(), rowID.getItem(rowID.getValue()).toString().split(":")[2]);
            rowID.clear();
            myUI.reload(table);
        }
    }

    public abstract void specifyComponents();

    public abstract void addComponentsToView();

    public abstract void saveEntry();

    public abstract T getFormEntries();

    public abstract void emptyForm();



}
