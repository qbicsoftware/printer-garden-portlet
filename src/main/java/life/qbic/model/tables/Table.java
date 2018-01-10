package life.qbic.model.tables;

/**
 * This enum {@link Table} holds currently used tablenames.
 * @author fhanssen
 */
public enum Table {
    printer_project_association("printer_project_association"),
    labelprinter("labelprinter"),
    projects("projects");

    private final String name;

    Table(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
