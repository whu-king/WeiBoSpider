package ikanalyzer;

import dao.ContentDao;
import dao.PlaceGeoDao;
import json.PlaceGeo;
import json.WeiBoContent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class ContentWithGeoFinder {
    static List<PlaceGeo> geos;
    static{
        geos = PlaceGeoDao.getAllGeo();
    }

    public static PlaceGeo searchInDb(String text){
        PlaceGeo result = null;
       for(PlaceGeo geo : geos){
           if(isStringMatch(geo.getName(),text)){
               System.out.println("match : " + text);
               result = geo;
               break;
           }
       }
        return result;
    }

    private static boolean isStringMatch(String str1, String str2){
        if(str1.length() < str2.length()) return  false;
        float count = 0;
        for(int i = 0; i < str1.length() && i < str2.length(); i ++){
            if(str1.charAt(i) == str2.charAt(i)) count = count + 1;
            else break;
        }
        if(count/str1.length() > 0.6 && count/str2.length() > 0.6)
            return true;
        else return false;
    }

    public static List<WeiBoContent> getContentWithGeo(){
        List<WeiBoContent> contents = ContentDao.getAll();
        List<WeiBoContent> contentsWithGeos = new ArrayList<WeiBoContent>();
        int count = 0;
        for(WeiBoContent content : contents){
            if(content.getLng() != null && !content.getLng().equalsIgnoreCase("")){
//                System.out.println("lat : " + content.getLat() + "lgn:" + content.getLng());
//                System.out.println("Content:" + content.getContent());
//                System.out.println("___________________________________________________");
                count++;
//                System.out.println(content.getAddr());
                contentsWithGeos.add(content);
            }

        }
        System.out.println(count);
        return contentsWithGeos;
    }

    public static void main(String []args) throws IOException {
        String text = "#抓小偷为人民服#这些小偷太可恨了，塘沽区我昨天天桥街 刚在朝阳区海淀区地铁站被偷，我当时背着玫红色的小包，里面装着的黑色钱包被盗，里面有现金三千多元和银行卡两张。希望看到的网友能够举报线索。不能让更多的受害者被盗！！ |北京·地铁...";
        List<String> results = IKAnalyzerUtil.split(text);
        for(String str : results){
            searchInDb(str);
        }
    }
}
