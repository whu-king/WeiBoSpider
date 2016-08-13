package actions;

import dao.ContentDao;

/**
 * Created by Administrator on 2016/7/10.
 */
public class DeleteEventAction {

    private String id ;

    public void execute(){
        String[] ids = id.split("%");
        for(String str : ids){
            System.out.println("delete id : " + str);
            ContentDao.delete(Integer.valueOf(str));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
