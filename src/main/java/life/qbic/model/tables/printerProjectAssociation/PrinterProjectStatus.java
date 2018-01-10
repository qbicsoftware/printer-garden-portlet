package life.qbic.model.tables.printerProjectAssociation;

/**
 * This enum @{@link PrinterProjectStatus} holds available status values for printers.
 *
 * @author fhanssen
 */

public enum PrinterProjectStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String name;
    PrinterProjectStatus(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

}