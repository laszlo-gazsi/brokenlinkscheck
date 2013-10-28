package info.lacyg.brokenlinkscheck.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Tasks")
public class Task extends AbstractPersistentObject
{
    public static final String P_NAME = "name";

    public static final String P_FINISHED = "finished";

    public static final String P_FAST_MODE = "fast_mode";

    @Column(name = P_NAME, nullable = false)
    private String name;

    @Column(name = P_FINISHED)
    private boolean finished;

    @Column(name = P_FAST_MODE)
    private boolean fastMode;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isFinished()
    {
        return finished;
    }

    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }

    public boolean isFastMode()
    {
        return fastMode;
    }

    public void setFastMode(boolean fastMode)
    {
        this.fastMode = fastMode;
    }
}
