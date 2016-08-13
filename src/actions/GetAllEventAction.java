package actions;

import com.opensymphony.xwork2.ActionContext;
import ikanalyzer.ContentWithGeoFinder;
import json.WeiBoContent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/8.
 */
public class GetAllEventAction {

    List<String> events = new ArrayList<String>();

    public String execute() throws IOException {
        ActionContext ac = ActionContext.getContext();
        List<WeiBoContent> contents = ContentWithGeoFinder.getContentWithGeo();
        for(WeiBoContent content : contents){
            String jsonString = JSONObject.fromObject(content).toString();
            events.add(jsonString);
            System.out.println(jsonString);
        }
        HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(JSONArray.fromObject(events).toString());
        return "success";
    }

}
