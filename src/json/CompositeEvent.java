package json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/9.
 */
public class CompositeEvent {

    private List<WeiBoContent> events = new ArrayList<WeiBoContent>();

    public List<WeiBoContent> getEvents() {
        return events;
    }

    public void setEvents(List<WeiBoContent> events) {
        this.events = events;
    }
}
