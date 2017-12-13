package life.qbic.model.tables.printer;

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
