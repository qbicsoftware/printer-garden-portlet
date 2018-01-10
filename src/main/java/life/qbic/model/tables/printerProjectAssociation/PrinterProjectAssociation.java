package life.qbic.model.tables.printerProjectAssociation;

import life.qbic.model.tables.printer.Printer;

import java.io.Serializable;

/**
 * The class {@link PrinterProjectAssociation} provides all fields needed to add a new printer project association.
 *
 * @author fhanssen
 */
@SuppressWarnings("serial")
public class PrinterProjectAssociation implements Serializable, Cloneable {


    private final String printerName;
    private final String printerLocation;
    private final String projectName;
    private final PrinterProjectStatus status;

    public PrinterProjectAssociation(String printerName, String printerLocation, String projectName, PrinterProjectStatus status) {
        this.printerName = printerName;
        this.printerLocation = printerLocation;
        this.projectName = projectName;
        this.status = status;
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

    public PrinterProjectStatus getStatus(){
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrinterProjectAssociation that = (PrinterProjectAssociation) o;

        return printerName.equals(that.printerName) && printerLocation.equals(that.printerLocation) && projectName.equals(that.projectName)
                && status.equals(that.status);
    }

    @Override
    public PrinterProjectAssociation clone() throws CloneNotSupportedException {
        return (PrinterProjectAssociation) super.clone();
    }

    @Override
    public String toString() {
        return printerName + " " + printerLocation + " " + projectName + " " + status;
    }
}

