package location.prepare;

import dao.ContentDao;
import ikanalyzer.ContentWithGeoFinder;
import ikanalyzer.IKAnalyzerUtil;
import json.PlaceGeo;
import json.WeiBoContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ContentLocationOperation {

    public static void main(String[] args) throws IOException {
        getLocation();
    }

    public static void regularTime() {
        List<WeiBoContent> contents = ContentDao.getAll();
        for (WeiBoContent content : contents) {
            content.setTime(content.getTime().replaceAll("custom:", ""));
            ContentDao.update(content);
        }
    }

    public static void showContentWithGeo() {
        List<WeiBoContent> contents = ContentDao.getAll();
        int count = 0;
        for (WeiBoContent content : contents) {
            if (content.getLng() != null && !content.getLng().equalsIgnoreCase("")) {
                System.out.println(content.getLat() + "," + content.getLng());
                System.out.println(content.getAddr());
                System.out.println("Content:" + content.getContent());
                System.out.println("___________________________________________________");
                count++;
            }

        }
        System.out.println(count);
    }

    public static void clearLocation() {
        List<WeiBoContent> contents = ContentDao.getAll();
        for (WeiBoContent content : contents) {
            if (content.getLat() != null && !content.getLat().equalsIgnoreCase("")) {
                content.setLng("");
                content.setLat("");
                ContentDao.update(content);
            }
        }
    }

    public static List<WeiBoContent> getLocation() throws IOException {
        List<WeiBoContent> contents = ContentDao.getAll();
        for (WeiBoContent content : contents) {
            if (content.getLat() == null || content.getLat().equalsIgnoreCase("")) {
                List<String> tokens = IKAnalyzerUtil.split(content.getContent());
                List<PlaceGeo> geos = new ArrayList<PlaceGeo>();
                for (String token : tokens) {
                    PlaceGeo geo = ContentWithGeoFinder.searchInDb(token);
                    if (geo != null) {
                        geos.add(geo);
                    }
                }
                if (geos.size() != 0) {
                    for (PlaceGeo geo : geos) {
                        try {
                            double lat = Double.valueOf(geo.getLat());
                            double lgn = Double.valueOf(geo.getLng());
                            content.setLng(geo.getLng());
                            content.setLat(geo.getLat());
                            content.setAddr(geo.getName());
                            content.setType("安全");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    if (content.getLat() != null && !content.getLat().equalsIgnoreCase(""))
                        ContentDao.update(content);
                }
            }
        }
        return contents;
    }
}
