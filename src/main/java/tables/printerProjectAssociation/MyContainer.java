package tables.printerProjectAssociation;

import java.io.Serializable;

public class MyContainer implements Serializable {

    private String location = "";
    private String name = "";

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return name + " " + location;
    }
}
