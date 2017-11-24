package tables;

public interface IForm<T> {

    void specifyComponents();

    void addComponentsToView();

    void saveEntry();

    T getFormEntries();

    void emptyForm();
}
