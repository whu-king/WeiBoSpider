package httpComponent;

import dao.PlaceGeoDao;
import dao.RunHibernate;
import json.PlaceGeo;
import org.apache.http.HttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.EncodeUtils;
import utils.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/7.
 */
public class BeiJingGeoSpider {

    private Map<String, String> headers;
    private List<PlaceGeo> geos;

    BeiJingGeoSpider() {
        this.geos = new ArrayList<PlaceGeo>();
        this.headers = new HashMap<String, String>();
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("Accept-Encoding", "gzip, deflate, sdch");
        headers.put("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cache-Control", "no-cache");
    }

    public void getGeo() {
        for (int pageCount = 1; pageCount < 211; pageCount++) {
            String basicUrl = "http://bjghw.gov.cn/query/business/query/queryTableAction$getAllGonggaoList.action?";
            String paramUrl = "nothing=nothing&pageBean.currentPage=" + pageCount + "&pageBean.itemsPerPage=15";
            headers.put("Host", "bjghw.gov.cn");
            HttpResponse response = HttpUtils.doGet(basicUrl + paramUrl, headers);
            String responseText = HttpUtils.getStringFromResponse(response);
            responseText = EncodeUtils.unicdoeToGB2312(responseText);
            System.out.println(responseText);
            parseHtml(responseText);
            if (geos.size() > 50) {
                PlaceGeoDao.save(geos);
                geos = new ArrayList<PlaceGeo>();
            }
        }

    }

    public void parseHtml(String html) {
        Document document = Jsoup.parse(html);
        Element body = document.body();
        Elements tables = body.select("table[cellspacing=1]");
        Element table = tables.first();
        Elements trs = table.select("tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            String name = tds.get(0).text();
            String place = tds.get(1).text();
            geos.add(new PlaceGeo(name, place));
            System.out.println("Name : " + name + " Place : " + place);
        }
    }

    public static void main(String args[]) {
        new BeiJingGeoSpider().getGeo();
    }


}
