package tables.printer;


import java.io.Serializable;

@SuppressWarnings("serial")
public class Printer implements Serializable, Cloneable {


    private Long ID;

    private String name = "";

    private String location = "";

    private String url = "";

    private PrinterStatus status;

    private PrinterType type;

    private String isAdmin = "";

    private String userGroup = "";


    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PrinterStatus getStatus() {
        return status;
    }

    public void setStatus(PrinterStatus status) {
        this.status = status;
    }

    public PrinterType getType() {
        return type;
    }

    public void setType(PrinterType type) {
        this.type = type;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        if (isAdmin){
            this.isAdmin = "1";
        }else{
            this.isAdmin = "0";
        }

    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public boolean isPersisted() {
        return ID != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.ID == null) {
            return false;
        }

        if (obj instanceof Printer && obj.getClass().equals(getClass())) {
            return this.ID.equals(((Printer) obj).ID);
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (ID == null ? 0 : ID.hashCode());
        return hash;
    }

    @Override
    public Printer clone() throws CloneNotSupportedException {
        return (Printer) super.clone();
    }

    @Override
    public String toString() {
        return (name + " " + location + " " + url + " " + status + " " + type.toString() + " " + isAdmin + " " + userGroup);
    }
}

