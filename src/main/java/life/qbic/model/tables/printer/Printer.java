package model.tables.printer;


import java.io.Serializable;

@SuppressWarnings("serial")
public class Printer implements Serializable, Cloneable {

    private String name = "";

    private String location = "";

    private String url = "";

    private final PrinterStatus status;

    private final PrinterType type;

    private String isAdmin = "";

    private String userGroup = "";

    public Printer (String name, String location, String url, PrinterStatus status, PrinterType type, Boolean isAdmin, String userGroup){
        this.name = name;
        this.location = location;
        this.url = url;
        this.status = status;
        this.type = type;
        setIsAdmin(isAdmin);
        this.userGroup = userGroup;

    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getUrl() {
        return url;
    }

    public PrinterStatus getStatus() {
        return status;
    }

    public PrinterType getType() {
        return type;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    private void setIsAdmin(Boolean isAdmin) {
        if (isAdmin){
            this.isAdmin = "1";
        }else{
            this.isAdmin = "0";
        }

    }

    public String getUserGroup() {
        return userGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Printer printer = (Printer) o;

        return name.equals(printer.name) &&
                location.equals(printer.location) &&
                url.equals(printer.url) &&
                status == printer.status &&
                type == printer.type &&
                isAdmin.equals(printer.isAdmin) &&
                userGroup.equals(printer.userGroup);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + isAdmin.hashCode();
        result = 31 * result + userGroup.hashCode();
        return result;
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

