package life.qbic.view.forms;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.*;
import life.qbic.MyPortletUI;
import life.qbic.model.tables.printer.Printer;
import life.qbic.model.tables.printer.PrinterStatus;
import life.qbic.model.tables.printer.PrinterType;
import java.sql.SQLException;


public class PrinterFormView extends AFormView implements IFormView {

    private final TextField name = new TextField("Name");
    private final TextField location = new TextField("Location");
    private final TextField url = new TextField("URL");
    private final TextField userGroup = new TextField("User group");

    private final ComboBox status = new ComboBox("Status");
    private final ComboBox type = new ComboBox("Type");
    private final CheckBox adminOnly = new CheckBox("Admin only");

    private final HorizontalLayout options = new HorizontalLayout();
    private final VerticalLayout saveForm = new VerticalLayout();
    private final VerticalLayout deleteForm = new VerticalLayout();


    public PrinterFormView(SQLContainer exisIDs) {
        super(exisIDs);
        specifyComponents();
        addComponentsToView();
    }

    @Override
    public void addComponentsToView() {
        saveForm.addComponents(name, location, url, status, type, adminOnly, userGroup,
                getSaveButton());
        saveForm.setSpacing(true);
        deleteForm.addComponents(getRowID(), getDeleteButton());
        deleteForm.setSpacing(true);
        options.addComponents(saveForm, deleteForm);
        options.setSpacing(true);
        addComponent(options);
    }

    @Override
    public void specifyComponents() {
        for(PrinterStatus p : PrinterStatus.values()){
            status.addItem(p);
        }
        status.setNullSelectionAllowed(false);
        status.setValue(PrinterStatus.ACTIVE);

        for(PrinterType p : PrinterType.values()){
            type.addItem(p);
        }
        type.setNullSelectionAllowed(false);
        type.setValue(PrinterType.LABELPRINTER);

        adminOnly.setValue(false);
    }

    @Override
    public Printer getFormEntries() {
        return new Printer(name.getValue(), location.getValue(), url.getValue(),
                (PrinterStatus) status.getValue(), (PrinterType) type.getValue(), adminOnly.getValue(),
                userGroup.getValue());
    }

    @Override
    public void emptyForm() {
        name.clear();
        location.clear();
        url.clear();
        status.setNullSelectionAllowed(false);
        status.setValue(PrinterStatus.ACTIVE);
        type.setNullSelectionAllowed(false);
        type.setValue(PrinterType.LABELPRINTER);

        adminOnly.setValue(false);
        userGroup.clear();
        getRowID().clear();
    }


    public TextField getName() {
        return name;
    }

    public TextField getLocation() {
        return location;
    }

    public TextField getUrl() {
        return url;
    }

    public TextField getUserGroup() {
        return userGroup;
    }

    public ComboBox getStatus() {
        return status;
    }

    public ComboBox getType() {
        return type;
    }

    public CheckBox getAdminOnly() {
        return adminOnly;
    }
}
