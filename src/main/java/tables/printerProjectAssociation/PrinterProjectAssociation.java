package tables.printerProjectAssociation;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PrinterProjectAssociation implements Serializable, Cloneable {


    private String printerName = "";
    private String printerLocation = "";
    private String projectName = "";

    PrinterProjectAssociation(String printerName, String printerLocation, String projectName) {
        this.printerName = printerName;
        this.printerLocation = printerLocation;
        this.projectName = projectName;
    }

    public String getPrinterName() {
        return printerName;
    }

    public String getPrinterLocation() {

        return printerLocation;
    }

    public String getProjectName() {

        return projectName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrinterProjectAssociation that = (PrinterProjectAssociation) o;

        if (!printerName.equals(that.printerName)) return false;
        if (!printerLocation.equals(that.printerLocation)) return false;
        return projectName.equals(that.projectName);
    }

    @Override
    public PrinterProjectAssociation clone() throws CloneNotSupportedException {
        return (PrinterProjectAssociation) super.clone();
    }

    @Override
    public String toString() {
        return printerName + " " + printerLocation + " " + projectName;
    }
}

