package json;

/**
 * Created by Administrator on 2016/7/6.
 */
public class WeiBoContent {

    private String username;
    private String content;
    private String time;
    private String lng;
    private String lat;
    private Integer id;
    private String type;
    private String addr;
    private Integer fatherId = 0;
    private String isShow = "yes";

    public WeiBoContent() {
    }


    public WeiBoContent(String name, String content, String timescope) {
        this.username = name;
        this.content = content;
        this.time = timescope;
    }

    public WeiBoContent(String name, String content, String timescope, String lgn, String lat) {
        this.username = name;
        this.content = content;
        this.time = timescope;
        this.lng = lgn;
        this.lat = lat;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String address) {
        this.addr = address;
    }

    public Integer getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lgt) {
        this.lat = lgt;
    }

    public Integer getFatherId() {
        return fatherId;
    }

    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }
}
