package life.qbic.view.forms;

interface IFormView <T> {

    void specifyComponents();

    void addComponentsToView();

    T getFormEntries();

    void emptyForm();
}
