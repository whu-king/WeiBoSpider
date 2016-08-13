package location.prepare;

import dao.PlaceGeoDao;
import json.PlaceGeo;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpResponse;
import utils.HttpUtils;
import utils.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/7.
 */
public class GeoInfoOperation {

    public static void main(String[] args)throws Exception{
       getGeoLocation();
    }

    public static void regularGeoName(){
        List<PlaceGeo> geos = PlaceGeoDao.getAllGeo();
        for(PlaceGeo geo : geos){
            String name = geo.getName();
            geo.setName(name.trim().replaceAll(" ", "").replaceAll("。", ""));
            String placeName = geo.getName();
            geo.setGeoPlace(placeName.trim().replaceAll(" ","").replaceAll("。",""));
        }
        PlaceGeoDao.save( geos);
    }

    public static void cutSingleNameGeo(){
        List<PlaceGeo> geos = PlaceGeoDao.getAllGeo();
        List<Long> deleteID = new ArrayList<Long>();
        for(PlaceGeo geo : geos){
            if(geo.getName().length() == 1){
                deleteID.add(geo.getId());
            }
        }
        for(Long id : deleteID){
            System.out.println(id);
            PlaceGeoDao.deleteById(id);
        }

    }

    public static void getGeoLocation(){
        List<PlaceGeo> geos = PlaceGeoDao.getAllGeo();
        Map<String,String> headers = new HashedMap(),params = new HashedMap();
        headers.put("Accept", "text/html, application/xhtml+xml, */*");
        headers.put("Accept-Language", "zh-cn");
        headers.put("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0; BOIE9;ZHCN");
        headers.put("Connection", "Keep-Alive");
        headers.put("Cache-Control", "no-cache");
        for(PlaceGeo geo : geos){
            if(geo.getLat() == null || geo.getLng() == null ||
                    geo.getLat().equalsIgnoreCase("") || geo.getLng().equalsIgnoreCase("")){
                String name = geo.getGeoPlace();
                String url="http://api.map.baidu.com/geocoder/v2/?address=" + name +  "&output=json&ak=XXPFtqDXGNnuNWmIfNMpDVuoUMoRVG8T";
                HttpResponse response= HttpUtils.doGet(url, headers);
                JSONArray arr = JSONArray.fromObject("[" + HttpUtils.getStringFromResponse(response) + "]");
                String status = arr.getJSONObject(0).getString("status");
                String lng = "";
                String lat = "";
                try{
                    if(arr.getJSONObject(0).getJSONObject("result").getJSONObject("location").getString("lng") != null){
                        lng = arr.getJSONObject(0).getJSONObject("result").getJSONObject("location").getString("lng");
                    }
                    if(arr.getJSONObject(0).getJSONObject("result").getJSONObject("location").getString("lat") != null){
                        lat =  arr.getJSONObject(0).getJSONObject("result").getJSONObject("location").getString("lat");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("Status:" + status + "lng:" + lng + "lat:" + lat);
                geo.setLat(lat);
                geo.setLng(lng);
                PlaceGeoDao.update( geo);
            }
        }

    }


    public static void BeiJing(){
        List<PlaceGeo> geos = PlaceGeoDao.getAllGeo();
        StringBuffer geoDic = new StringBuffer();
        for(PlaceGeo geo : geos){
            String name = geo.getName();
            geoDic.append(name + "\n");
        }
        Utils.writeFileFromString("C:\\geoDic.txt", geoDic.toString());
    }

    public static void ChinaCity() throws FileNotFoundException {
        BufferedWriter writer=null;
        BufferedReader reader=null;
        String filename = "c:\\new.txt";
        if(filename==null || filename.trim().length()==0)
            filename="tmp.txt";
        File file=new File(filename);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String city = "";
        String tmp = "";
        reader = new BufferedReader(new FileReader("c:\\sheng.txt"));
        StringBuffer buffer=new StringBuffer();
        try {
            while((tmp=reader.readLine())!=null){
                if(!tmp.equalsIgnoreCase("")) buffer.append(tmp+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        city = buffer.toString();


        try {
            writer=new BufferedWriter(new FileWriter(file));
            reader=new BufferedReader(new StringReader(city));
             tmp=null;
             buffer=new StringBuffer();
            while((tmp=reader.readLine())!=null){
                if(!tmp.equalsIgnoreCase("")) buffer.append(tmp+"\n");
            }

            writer.write(buffer.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void importGeoIntoDB() throws FileNotFoundException {
        BufferedWriter writer=null;
        BufferedReader reader=null;
        List<PlaceGeo> geos = new ArrayList<PlaceGeo>();
        String city = "";
        String tmp = "";
        reader = new BufferedReader(new FileReader("c:\\street.txt"));
        try {
            while((tmp=reader.readLine())!=null){
                if(geos.size() > 30) {
                    PlaceGeoDao.save( geos);
                    geos = new ArrayList<PlaceGeo>();
                }
                if(!tmp.equalsIgnoreCase("")) {
                    System.out.println(tmp);
                    geos.add(new PlaceGeo(tmp.trim(), tmp.trim()));
                }
            }
            PlaceGeoDao.save( geos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
