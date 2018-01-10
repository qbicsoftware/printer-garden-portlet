package life.qbic.model.tables.project;

import life.qbic.model.tables.Table;

/**
 * This enum @{@link ProjectFields} required project fields.
 *
 * @author fhanssen
 */
public enum ProjectFields {
    ID(Table.projects.toString() + ".id"),
    OPENBISID (Table.projects.toString() + ".openbis_project_identifier");


    private final String name;

    ProjectFields(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }

}
