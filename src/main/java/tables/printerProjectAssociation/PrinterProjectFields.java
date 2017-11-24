package tables.printerProjectAssociation;

import tables.Table;

public enum PrinterProjectFields {
    ID(Table.printer_project_association + ".id"),
    PRINTER_ID(Table.printer_project_association + ".printer_id"),
    PROJECT_ID(Table.printer_project_association + ".project_id"),
    STATUS(Table.printer_project_association + ".status");

    private final String name;
    PrinterProjectFields(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
