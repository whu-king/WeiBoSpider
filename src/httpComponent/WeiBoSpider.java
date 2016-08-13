package httpComponent;

import dao.ContentDao;
import dao.RunHibernate;
import json.WeiBoContent;
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
 * Created by Administrator on 2016/7/5.
 */
public class WeiBoSpider {
    private LoginSina ls;
    private Map<String, String> headers;
    private final int ADDFOLLOWING = 1;
    private final int CANCELFOLLOWING = 2;
    private List<WeiBoContent> contents = new ArrayList<WeiBoContent>();

    public WeiBoSpider() {
    }

    public WeiBoSpider(LoginSina ls) {
        this.ls = ls;
        this.headers = new HashMap<String, String>();
        headers.put("Accept", "text/html, application/xhtml+xml, */*");
        headers.put("Accept-Language", "zh-cn");
        headers.put("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cache-Control", "no-cache");
        String cookieValue = HttpUtils.setCookie2String(ls.getCookies());
        headers.put("Cookie", cookieValue);
    }

    public String releaseTopic(String content) {
        this.headers.put("Referer", "http://weibo.com/");
        this.headers.put("Host", "weibo.com");
        this.headers.put("Content-Type", "application/x-www-form-urlencoded");
        this.headers.put("x-requested-with", "XMLHttpRequest");
        Map<String, String> params = new HashMap<String, String>();
        params.put("_t", "0");
        params.put("location", "home");
        params.put("module", "stissue");
        params.put("pic_id", "");
        params.put("text", content);
        String url = "http://weibo.com/aj/mblog/add?__rnd=" + System.currentTimeMillis();
        HttpResponse response = HttpUtils.doPost(url, headers, params);
        return HttpUtils.getStringFromResponse(response);
    }


    public String getPageByKeyword(String[] keywords) {

        String keywordsUri[] = new String[keywords.length];
        StringBuffer searchString = new StringBuffer();
        for (int i = 0; i < keywords.length; i++) {
            keywordsUri[i] = EncodeUtils.encodeURL(keywords[i], "UTF-8");
            String word;
            if (i == 0) {
                word = keywordsUri[i].replaceAll("%", "%25");
            } else {
                word = "%2520" + keywordsUri[i].replaceAll("%", "%25");
            }
            searchString.append(word);
        }
        for (int year = 2015; year < 2016; year++) {
            for (int month = 12; month < 13; month++) {
                String timescope = "custom:" + year + "-0" + month + "-01:" + year + "-0" + month + "-30";
                System.out.println(timescope + "_______");
                urlRequest(timescope, searchString.toString());
            }
        }


        return "";
    }

    public void urlRequest(String timescope, String searchString) {

        int pageCount = 50;

        String typeall = "1";
        String suball = "1";
        String extraCondition = "&typeall=" + typeall
                + "&suball=" + suball + "&timescope=" + timescope;

        for (int i = 1; i < pageCount; i++) {
            String url = "http://s.weibo.com/weibo/" + searchString + extraCondition + "&page=" + i;
            HttpResponse response = HttpUtils.doGet(url, headers);
            String responseText = HttpUtils.getStringFromResponse(response);
            responseText = EncodeUtils.unicdoeToGB2312(responseText);
            getWeiboContent(responseText, timescope);
        }
    }

    public void getWeiboContent(String html, String timescope) {
        try {
            int contentStart = html.indexOf("\"pl_weibo_direct\"");
            String mid = html.substring(contentStart + 1, html.length());
            contentStart = mid.indexOf("html");
            int contentEnd = mid.indexOf("<script>");
            mid = mid.substring(contentStart + 8, contentEnd - 8);
            String[] texts = mid.split("<p");
            for (int i = 0; i < texts.length; i++) {
                if (i != 0) {
                    String pContent = texts[i].split("p>")[0];
                    parseHtmlFragment("<p" + pContent + "p>", timescope);
                }
                if (contents.size() > 10) {
                    ContentDao.save(contents);
                    contents = new ArrayList<WeiBoContent>();
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Lose One WeiBoContent");
        }

    }

    public String parseHtmlFragment(String htmlString, String timescope) {
        Document document = Jsoup.parse(htmlString);
        Element body = document.body();
        Elements texts = body.select("p");
        for (Element text : texts) {
            String attr = text.attr("nick-name");
            String name = cutRubbishString(attr);
            String text1 = text.text();
            String content = cutRubbishString(text1);
            contents.add(new WeiBoContent(name, content, timescope));
//            System.out.println("name : " + name + "\n content : " + content);
        }
        return "";
    }

    private String cutRubbishString(String htmlString) {
        String s = htmlString.replaceAll("<[^>]+>", "");
        s = s.replaceAll("\"", "");
        s = s.replaceAll("\\\\", "");
//        s = s.replaceAll("/a","");
//        s = s.replaceAll("<\\/a>","");
//        s = s.replaceAll("<\\/em>","");
//        s = s.replaceAll("<\\/p>","");
//        s = s.replaceAll("<\\/i>","");
//        s = s.replaceAll("|","");
//        System.out.println(s);
        return s;
    }


    public String utf2gb(String utfString) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = utfString.indexOf("\\u", pos)) != -1) {
            sb.append(utfString.substring(pos, i));
            if (i + 5 < utfString.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(utfString.substring(i + 2, i + 6), 16));
            }
        }

        return sb.toString();
    }


    public static void main(String[] args) {
        LoginSina ls = new LoginSina("971069014@qq.com", "070906?");
        ls.dologinSina();
        ls.redirect();
        WeiBoSpider ws = new WeiBoSpider(ls);
        ws.getPageByKeyword(new String[]{"小偷"});
    }


}
