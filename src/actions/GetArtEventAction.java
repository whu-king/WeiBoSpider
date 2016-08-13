package actions;

import com.opensymphony.xwork2.ActionContext;
import contentDealer.WeiBoContentFilter;
import dao.ConfigDao;
import ikanalyzer.ContentWithGeoFinder;
import json.Config;
import json.WeiBoContent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/19.
 */
public class GetArtEventAction {

    public void execute()throws Exception{
        List<String> events = new ArrayList<String>();
        Config conf = ConfigDao.get(0);
        conf.setIsUpdate(0);
        ConfigDao.update(conf);
        ActionContext ac = ActionContext.getContext();
        System.out.println("In GetArtAction");
        List<WeiBoContent> contents = ContentWithGeoFinder.getContentWithGeo();
        contents = WeiBoContentFilter.getArtEvent(contents);
        for(WeiBoContent content : contents){
            String jsonString = JSONObject.fromObject(content).toString();
            events.add(jsonString);
            System.out.println(jsonString);
        }
        HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(JSONArray.fromObject(events).toString());
    }
}
