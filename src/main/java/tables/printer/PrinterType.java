package tables.printer;

public enum PrinterType {
    LABELPRINTER("LABEL PRINTER");

    private String name;
    PrinterType(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
