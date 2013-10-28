package info.lacyg.brokenlinkscheck.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractPersistentObject
{
    public static final String P_ID = "ID";

    @Id()
    @GeneratedValue()
    @Column(name = P_ID)
    private int ID;

    public int getID()
    {
        return ID;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }
}
