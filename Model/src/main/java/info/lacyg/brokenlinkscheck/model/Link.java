package info.lacyg.brokenlinkscheck.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Links")
public class Link extends AbstractPersistentObject
{
    public static final String P_URL = "url";

    public static final String P_HTTP_RESPONSE = "httpResponse";

    public static final String P_TASK_ID = "taskID";

    @Column(name = P_URL, nullable = false)
    private String url;

    @Column(name = P_HTTP_RESPONSE)
    private Integer httpResponse;

    @Column(name = P_TASK_ID)
    private Integer taskID;

    public Link()
    {
    }

    public Link(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public Integer getHttpResponse()
    {
        return httpResponse;
    }

    public void setHttpResponse(Integer httpResponse)
    {
        this.httpResponse = httpResponse;
    }

    public Integer getTaskID()
    {
        return taskID;
    }

    public void setTaskID(Integer taskID)
    {
        this.taskID = taskID;
    }

    public String toString()
    {
        return this.url;
    }
}
