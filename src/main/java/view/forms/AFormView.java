package view.forms;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import elemental.events.KeyboardEvent;
import life.qbic.MyPortletUI;

import java.sql.SQLException;


public class AFormView extends FormLayout{

    protected final MyPortletUI ui;
    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final ComboBox rowID = new ComboBox("ID");

    public AFormView(MyPortletUI ui, SQLContainer ids) throws SQLException{
        super();
        this.ui = ui;

        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.setClickShortcut(KeyboardEvent.KeyCode.ENTER);

        setExistingIDs(ids);

    }

    public ComboBox getRowID() {
        return rowID;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    private void setExistingIDs(SQLContainer allExisIds) throws SQLException {

        rowID.addItem("");
        rowID.setContainerDataSource(allExisIds);
        rowID.setItemCaptionPropertyId("id");
        rowID.setImmediate(true);
    }
}
