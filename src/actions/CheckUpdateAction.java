package actions;

import com.opensymphony.xwork2.ActionContext;
import dao.ConfigDao;
import dao.ContentDao;
import json.Config;
import json.WeiBoContent;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/7/18.
 */
public class CheckUpdateAction {

    private int isUpdate;

    public void execute() throws Exception{
        Config conf = ConfigDao.get(0);
        ActionContext ac = ActionContext.getContext();
        System.out.println("isUpdate : " + conf.getIsUpdate());
        HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
        ac.getSession();
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(String.valueOf(conf.getIsUpdate()));
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }
}
