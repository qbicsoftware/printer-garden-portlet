package view.forms;

public interface IFormView <T> {

    void specifyComponents();

    void addComponentsToView();

    T getFormEntries();

    void emptyForm();
}
