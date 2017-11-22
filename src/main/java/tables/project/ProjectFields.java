package tables.project;

import tables.Table;

public enum ProjectFields {
    ID(Table.projects.toString() + ".id"),
    OPENBISID (Table.projects.toString() + ".openbis_project_identifier");


    String name;

    ProjectFields(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
