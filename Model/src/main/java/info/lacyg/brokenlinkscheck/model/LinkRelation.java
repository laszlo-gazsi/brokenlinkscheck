package info.lacyg.brokenlinkscheck.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "LinkRelations")
public class LinkRelation extends AbstractPersistentObject
{
    public static final String P_PARENT_ID = "parent";

    public static final String P_CHILD_ID = "child";

    @Column(name = P_PARENT_ID)
    private Integer parent;

    @Column(name = P_CHILD_ID)
    private Integer child;

    public LinkRelation()
    {
    }

    public LinkRelation(Integer parent, Integer child)
    {
        this.parent = parent;
        this.child = child;
    }

    public Integer getParent()
    {
        return parent;
    }

    public void setParent(Integer parent)
    {
        this.parent = parent;
    }

    public Integer getChild()
    {
        return child;
    }

    public void setChild(Integer child)
    {
        this.child = child;
    }
}
