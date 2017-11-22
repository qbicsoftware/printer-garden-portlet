package tables;

public enum Table {
    printer_project_association("printer_project_association"),
    labelprinter("labelprinter"),
    projects("projects");

    private String name;

    Table(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
