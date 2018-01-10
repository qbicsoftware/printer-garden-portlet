package life.qbic.model.tables.printer;

/**
 * This enum @{@link PrinterType} shows currently supported printer types.
 *
 * @author fhanssen
 */
public enum PrinterType {
    LABELPRINTER("LABEL PRINTER"),
    A4_PRINTER("A4 PRINTER");

    private final String name;
    PrinterType(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
