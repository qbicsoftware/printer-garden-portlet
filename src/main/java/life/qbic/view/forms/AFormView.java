package life.qbic.view.forms;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.ValoTheme;
import elemental.events.KeyboardEvent;

/**
 * This class {@link AFormView} holds fields and methods all forms need: save button, delete button and a row id.
 * @author fhanssen
 */
public class AFormView extends FormLayout{

    private final Button saveButton = new Button("Save");
    private final Button deleteButton = new Button("Delete");
    private final ComboBox rowID = new ComboBox("ID");

    AFormView(SQLContainer ids) {
        super();

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

    public void setExistingIDs(SQLContainer allExisIds){

        rowID.setContainerDataSource(allExisIds);
        rowID.setItemCaptionPropertyId("id");
        rowID.setImmediate(true);
    }
}
