package life.qbic.model.tables.printer;


/**
 * This enum @{@link PrinterStatus} holds available status values for printers.
 *
 * @author fhanssen
 */
public enum PrinterStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String name;
    PrinterStatus(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
