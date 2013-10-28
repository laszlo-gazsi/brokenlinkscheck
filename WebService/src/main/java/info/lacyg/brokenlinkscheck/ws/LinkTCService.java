package info.lacyg.brokenlinkscheck.ws;

import info.lacyg.brokenlinkscheck.application.ApplicationContext;
import info.lacyg.brokenlinkscheck.database.IDatabaseReadService;
import info.lacyg.brokenlinkscheck.model.Link;
import info.lacyg.brokenlinkscheck.model.LinkRelation;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService
public class LinkTCService
{

    public static final String YES = "yes";

    public static final String NO = "no";

    public static final String DATA_SEPARATOR = "|";

    public static final String ROW_SEPARATOR = "*";

    @WebMethod
    public String getAllLinks(int taskId, int startPage, int pageSize)
    {
        IDatabaseReadService<Link> readService = ApplicationContext.getDatabaseReadService(Link.class);

        SimpleExpression fromTaskCriterion = Restrictions.eq(Link.P_TASK_ID, taskId);
        int count = readService.getRowCount(fromTaskCriterion);
        List<Link> links = readService.getAll(startPage * pageSize, pageSize, fromTaskCriterion);

        return generateResponse(startPage, pageSize, count, links);
    }

    @WebMethod
    public String getBrokenLinks(int taskId, int startPage, int pageSize)
    {
        IDatabaseReadService<Link> readService = ApplicationContext.getDatabaseReadService(Link.class);

        SimpleExpression fromTaskCriterion = Restrictions.eq(Link.P_TASK_ID, taskId);
        SimpleExpression checkedLinkCriterion = Restrictions.ne(Link.P_HTTP_RESPONSE, -1);
        SimpleExpression brokenLinkCriterion = Restrictions.ne(Link.P_HTTP_RESPONSE, 200);
        int count = readService.getRowCount(fromTaskCriterion, checkedLinkCriterion, brokenLinkCriterion);
        List<Link> links = readService.getAll(startPage * pageSize, pageSize, fromTaskCriterion, checkedLinkCriterion, brokenLinkCriterion);

        return generateResponse(startPage, pageSize, count, links);
    }

    @WebMethod
    public String getParentLinks(int linkId)
    {
        IDatabaseReadService<Link> linkReadService = ApplicationContext.getDatabaseReadService(Link.class);
        IDatabaseReadService<LinkRelation> linkRelationReadService = ApplicationContext.getDatabaseReadService(LinkRelation.class);

        List<LinkRelation> linkRelations = linkRelationReadService.getAll(null, null, Restrictions.eq(LinkRelation.P_CHILD_ID, linkId));
        List<Integer> parentIds = new ArrayList();
        for (LinkRelation linkRelation : linkRelations)
        {
            parentIds.add(linkRelation.getParent());
        }

        List<Link> links = linkReadService.getAll(null, null, Restrictions.in(Link.P_ID, parentIds));

        StringBuilder stringBuilder = new StringBuilder();

        for (Link parentLink : links)
        {
            stringBuilder.append(generateLinkRow(parentLink));
        }

        return stringBuilder.toString();
    }

    private String generateResponse(int start, int limit, int count, List<Link> links)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getPaginationInfo(start, limit, count));

        for (Link link : links)
        {
            stringBuilder.append(generateLinkRow(link));
        }

        return stringBuilder.toString();
    }

    private String getPaginationInfo(int start, int limit, int count)
    {
        String result = "";

        result += (start > 0) ? YES : NO;
        result += DATA_SEPARATOR;
        result += (count > (start + 1) * limit) ? YES : NO;
        result += ROW_SEPARATOR;

        return result;
    }

    private String generateLinkRow(Link link)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(link.getID());
        stringBuilder.append(DATA_SEPARATOR);
        stringBuilder.append(link.getUrl());
        stringBuilder.append(DATA_SEPARATOR);
        stringBuilder.append(getLinkClass(link));
        stringBuilder.append(DATA_SEPARATOR);
        stringBuilder.append((link.getHttpResponse() != null) ? link.getHttpResponse() : "");
        stringBuilder.append(ROW_SEPARATOR);

        return stringBuilder.toString();
    }

    private String getLinkClass(Link link)
    {
        String linkClass;
        Integer httpResponse = link.getHttpResponse();

        if (httpResponse == null)
        {
            linkClass = "unchecked";
        }
        else if (httpResponse == 200)
        {
            linkClass = "ok";
        }
        else if (httpResponse == 301 || httpResponse == 302)
        {
            linkClass = "warning";
        }
        else
        {
            linkClass = "broken";
        }

        return linkClass;
    }
}
