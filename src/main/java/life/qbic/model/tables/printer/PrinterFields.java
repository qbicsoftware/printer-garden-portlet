package life.qbic.model.tables.printer;

import life.qbic.model.tables.Table;

/**
 * This enum @{@link PrinterFields} holds fields found in printer table view entries.
 *
 * @author fhanssen
 */

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
