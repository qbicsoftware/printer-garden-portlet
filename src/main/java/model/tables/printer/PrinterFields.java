package model.tables.printer;

import model.tables.Table;

public enum PrinterFields {
    ID(Table.labelprinter.toString() + ".id"),
    NAME(Table.labelprinter.toString() + ".name"),
    LOCATION(Table.labelprinter.toString() + ".location"),
    URL(Table.labelprinter.toString() + ".url"),
    STATUS(Table.labelprinter.toString() + ".status"),
    TYPE(Table.labelprinter.toString() + ".type"),
    ADMIN_ONLY(Table.labelprinter.toString() + ".admin_only"),
    USER_GROUP(Table.labelprinter.toString() + ".user_group");

    private final String name;
    PrinterFields(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
