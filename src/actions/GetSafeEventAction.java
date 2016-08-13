package actions;

import com.opensymphony.xwork2.ActionContext;
import contentDealer.WeiBoContentFilter;
import dao.ConfigDao;
import dao.ContentDao;
import ikanalyzer.ContentWithGeoFinder;
import json.CompositeEvent;
import contentDealer.UnionFindSet;
import json.Config;
import json.WeiBoContent;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/9.
 */
public class GetSafeEventAction {

    List<String> events = new ArrayList<String>();

    public void execute() throws Exception {
        Config conf = ConfigDao.get(0);
        conf.setIsUpdate(0);
        ConfigDao.update(conf);
        ActionContext ac = ActionContext.getContext();
        System.out.println("In GetBeijingEventAction");
        List<WeiBoContent> contents = ContentWithGeoFinder.getContentWithGeo();
//        contents = WeiBoContentFilter.getContentInBeijing(contents);
        contents = WeiBoContentFilter.getSafeEvent(contents);
        contents = WeiBoContentFilter.hideSameContent(contents);
        long timebefore = System.currentTimeMillis();
        UnionFindSet ufs = WeiBoContentFilter.makeUFSFromList2(contents);

//        for(int i=0; i < ufs.father.length; i++){
//            System.out.print(ufs.father[i] + " ");
//            if(i%10 == 0 ) System.out.println();
//        }

        List<Integer> usedEventIndex = new ArrayList<Integer>();
        List<WeiBoContent> singles = new ArrayList<WeiBoContent>();
        List<CompositeEvent> composites = new ArrayList<CompositeEvent>();

        for(int index = ufs.father.length - 1; index > 0 ; index--){
            if(ufs.father[index] == 0){
                continue;
            }
            boolean isUsed = false;
            for(int used : usedEventIndex){
                if(used == ufs.find(index)) {
                    isUsed = true;
                    break;
                }
            }
            if(isUsed) continue;
            if(ufs.find(index) == index) singles.add(WeiBoContentFilter.getContentById(contents,index));
            else {
                usedEventIndex.add(ufs.find(index));
                CompositeEvent compositeEvent = new CompositeEvent();
                for(int i : ufs.getAllinOneSet(index)){
                    compositeEvent.getEvents().add(WeiBoContentFilter.getContentById(contents,i));
                }
                composites.add(compositeEvent);
            }
        }

        for(WeiBoContent content : singles){
            String jsonString = JSONObject.fromObject(content).toString();
            events.add(jsonString);
            System.out.println(jsonString);
        }
        for(CompositeEvent com : composites){
            String jsonString = JSONObject.fromObject(com).toString();
            events.add(jsonString);
            System.out.println(jsonString);
        }
        System.out.println("cost Time : " + (System.currentTimeMillis() - timebefore));
        HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
        ac.getSession();
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(JSONArray.fromObject(events).toString());
//        response.getWriter().write(JSONArray.fromObject(composites).toString());

    }


}
