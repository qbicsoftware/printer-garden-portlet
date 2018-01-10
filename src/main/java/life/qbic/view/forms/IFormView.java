package life.qbic.view.forms;

/**
 * This interface {@link IFormView} specifies methods forms need.
 * @author fhanssen
 * @param <T> Either Printer or PrinterProjectAssociation, depending on what the form generates
 */
interface IFormView <T> {

    void specifyComponents();

    void addComponentsToView();

    T getFormEntries();

    void emptyForm();
}
