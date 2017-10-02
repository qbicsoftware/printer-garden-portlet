package life.qbic;

import java.io.Serializable;

public class Project implements Serializable, Cloneable{

    private Long ID;

    private String openbisProjectID = "";

    private String shortTitle = "";

    private String longDescription = "";

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }


    /**
     * Get the value of openbis_project_id
     *
     * @return the value of openbis_project_id
     */
    public String getOpenbisProjectID() {
        return openbisProjectID;
    }

    /**
     * Set the value of openbisProjectID
     *
     * @param openbisProjectID new value of openbisProjectID
     */
    public void setOpenbisProjectID(String openbisProjectID) {
        this.openbisProjectID = openbisProjectID;
    }


    /**
     * Get the value of shortTitle
     *
     * @return the value of shortTitle
     */
    public String getShortTitle() {
        return shortTitle;
    }

    /**
     * Set the value of shortTitle
     *
     * @param shortTitle new value of shortTitle
     */
    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    /**
     * Get the value of longDescription
     *
     * @return the value of longDescription
     */
    public String getLongDescription() {
        return longDescription;
    }

    /**
     * Set the value of longDescription
     *
     * @param longDescription new value of longDescription
     */
    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
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

        if (obj instanceof Project && obj.getClass().equals(getClass())) {
            return this.ID.equals(((Project) obj).ID);
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
    public Project clone() throws CloneNotSupportedException {
        return (Project) super.clone();
    }

    @Override
    public String toString() {
        return openbisProjectID + " " + shortTitle + " " + longDescription;
    }
}
