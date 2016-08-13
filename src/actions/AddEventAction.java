package actions;

import com.opensymphony.xwork2.ActionContext;
import dao.ContentDao;
import json.WeiBoContent;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2016/7/10.
 */
public class AddEventAction {


    private String username;
    private String content;
    private String time;
    private String lng;
    private String lat;
    private Integer id;
    private String type;
    private String address;

    public void execute() throws IOException {

        ActionContext ac = ActionContext.getContext();
        WeiBoContent con = new WeiBoContent();
        con.setAddr(this.address);
        con.setTime(this.time);
        con.setType(this.type);
        con.setContent(this.content);
        con.setLat(this.lat);
        con.setLng(this.lng);
        con.setUsername(this.username);
        long newconId = ContentDao.save(con);

        System.out.println("Add Id : " + newconId);
        HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
        ac.getSession();
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(String.valueOf(newconId));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
