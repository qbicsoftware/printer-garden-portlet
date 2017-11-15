package tables.printerProjectAssociation;


import tables.printer.PrinterStatus;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PrinterProjectAssociation implements Serializable, Cloneable {


    private Long ID;

    private String printerID = "";

    private String projectID = "";

    private PrinterStatus status;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }


    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public PrinterStatus getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(PrinterStatus status) {
        this.status = status;
    }


    /**
     * Get the value of printer_id
     *
     * @return the value of printer_id
     */
    public String getPrinterID() {
        return printerID;
    }

    /**
     * Set the value of printerID
     *
     * @param printerID new value of printerID
     */
    public void setPrinterID(String printerID) {
        this.printerID = printerID;
    }

    /**
     * Get the value of projectID
     *
     * @return the value of projectID
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Set the value of projectID
     *
     * @param projectID new value of projectID
     */
    public void setProjectID(String projectID) {
        this.projectID = projectID;
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

        if (obj instanceof PrinterProjectAssociation && obj.getClass().equals(getClass())) {
            return this.ID.equals(((PrinterProjectAssociation) obj).ID);
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
    public PrinterProjectAssociation clone() throws CloneNotSupportedException {
        return (PrinterProjectAssociation) super.clone();
    }

    @Override
    public String toString() {
        return printerID + " " + projectID + " " + status;
    }
}

