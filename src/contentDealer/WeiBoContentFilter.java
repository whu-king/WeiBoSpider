package contentDealer;

import dao.ContentDao;
import ikanalyzer.ContentWithGeoFinder;
import ikanalyzer.IKAnalyzerUtil;
import ikanalyzer.SimilarityCalculator;
import json.CompositeEvent;
import json.WeiBoContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Administrator on 2016/7/9.
 */
public class WeiBoContentFilter {

    public static List<WeiBoContent> cutDouplicateContent(List<WeiBoContent> contents) throws  Exception{
        List<WeiBoContent> newcontent = new ArrayList<WeiBoContent>();
        for(int i = 0; i < contents.size(); i++){
            if(contents.get(i).getIsShow() == null || contents.get(i).getIsShow().equalsIgnoreCase("yes")){
                Vector<String> v1 = new Vector<>(IKAnalyzerUtil.split(contents.get(i).getContent()));
                boolean isDouplicate = false;
                for(int j = i + 1; j < contents.size(); j++){
                    Vector<String> v2 = new Vector<>(IKAnalyzerUtil.split(contents.get(j).getContent()));
                    double simi = SimilarityCalculator.getSimilarity(v1, v2);
                    if(simi < 0.8)  {
                        isDouplicate = false;
                    } else {
                        isDouplicate = true;
                        contents.get(i).setIsShow("no");
                        ContentDao.update(contents.get(i));
                        break;
                    }
                }
                if(isDouplicate == false) {
                    contents.get(i).setIsShow("yes");
                    newcontent.add(contents.get(i));
                    ContentDao.update(contents.get(i));
                }
            }
        }
        return newcontent;
    }

    public static List<WeiBoContent> hideSameContent(List<WeiBoContent> contents){
        List<WeiBoContent> newcontent = new ArrayList<WeiBoContent>();
        for(WeiBoContent content : contents){
            if(content.getIsShow().equalsIgnoreCase("yes")){
                newcontent.add(content);
            }
        }
        return newcontent;
    }

    public static List<WeiBoContent> getContentInBeijing(List<WeiBoContent> contents)throws Exception{
        List<WeiBoContent> newcontent = new ArrayList<WeiBoContent>();
        for(WeiBoContent content : contents){
            if(39.26 < Double.valueOf(content.getLat()) && Double.valueOf(content.getLat()) < 41.03 &&
                    115.25 < Double.valueOf(content.getLng()) && Double.valueOf(content.getLng()) <  117.30){
                newcontent.add(content);
            }
        }
        return newcontent;
    }

    public static  List<WeiBoContent> getArtEvent(List<WeiBoContent> contents)throws Exception{
        List<WeiBoContent> newcontent = new ArrayList<WeiBoContent>();
        for(WeiBoContent content : contents){
            if(content.getType().equalsIgnoreCase("艺术")){
                newcontent.add(content);
            }
        }
        return newcontent;
    }

    public static  List<WeiBoContent> getSafeEvent(List<WeiBoContent> contents)throws Exception{
        List<WeiBoContent> newcontent = new ArrayList<WeiBoContent>();
        for(WeiBoContent content : contents){
            if(content.getType().equalsIgnoreCase("安全")){
                newcontent.add(content);
            }
        }
        return newcontent;
    }


    public static UnionFindSet4Loose makeUFSFromList(List<WeiBoContent> contents){
        UnionFindSet4Loose ufs = new UnionFindSet4Loose();
        ufs.init(contents);
        for(int i = contents.size() - 1; i > -1; i--){
            WeiBoContent con1 = contents.get(i);
            if(con1.getFatherId() == null || con1.getFatherId() == 0){
                boolean isFindSame = false;
                for(int j = i - 1; j > 0; j--){
                    WeiBoContent con2 = contents.get(j);
                    if(ufs.find(con1.getId()) != ufs.find(con2.getId()) && ufs.find(con2.getId()) != ufs.find(con1.getId())){
                        if(con1.getLat().trim().equalsIgnoreCase(con2.getLat().trim()) &&
                                con1.getLng().trim().equalsIgnoreCase(con2.getLng().trim())){
                            ufs.union(con1.getId(),con2.getId());
                            isFindSame = true;
                            break;
                        }
                    }
                }
                if(!isFindSame) ufs.father.put(con1.getId(),con1.getId());
            }
        }
        for(Map.Entry<Integer,Integer> entry : ufs.father.entrySet()){
            int key = entry.getKey();
            int value = entry.getValue();
            WeiBoContent content = getContentById(contents,key);
            if(content.getFatherId() == null || content.getFatherId() != value){
                content.setFatherId(value);
                ContentDao.update(content);
                }

        }
        return ufs;
    }

    public static UnionFindSet makeUFSFromList2(List<WeiBoContent> contents){
        UnionFindSet ufs = new UnionFindSet();
        ufs.init(contents);
        for(int i = contents.size() - 1; i > -1; i--){
            WeiBoContent con1 = contents.get(i);
            if(con1.getFatherId() == null || con1.getFatherId() == 0){
                boolean isFindSame = false;
                for(int j = i - 1; j > 0; j--){
                    WeiBoContent con2 = contents.get(j);
                    if(ufs.find(con1.getId()) != ufs.find(con2.getId()) && ufs.find(con2.getId()) != ufs.find(con1.getId())){
                        if(con1.getLat().trim().equalsIgnoreCase(con2.getLat().trim()) &&
                                con1.getLng().trim().equalsIgnoreCase(con2.getLng().trim())){
                            ufs.union(con1.getId(),con2.getId());
                            isFindSame = true;
                            break;
                        }
                    }
                }
                if(!isFindSame) ufs.father[con1.getId()] = con1.getId();
            }
        }
        int index = 0;
        for(int i : ufs.father){
            if(i != 0){
                WeiBoContent content = getContentById(contents,index);
                if(content.getFatherId() == null || content.getFatherId() != i){
                    content.setFatherId(i);
                    ContentDao.update(content);
                }
            }
            index ++;
        }
        return ufs;
    }

    public static WeiBoContent getContentById(List<WeiBoContent> contents, int id ){
        for(WeiBoContent content : contents){
            if(content.getId() == id) return content;
        }
        return null;
    }

    public static void main(String[] args)throws Exception{
//        List<WeiBoContent> contents = ContentWithGeoFinder.getContentWithGeo();
////        contents = WeiBoContentFilter.getContentInBeijing(contents);
//        contents = WeiBoContentFilter.cutDouplicateContent(contents);
//        UnionFindSet ufs = WeiBoContentFilter.makeUFSFromList(contents);
//        for(int i=0; i < ufs.father.length; i++){
//            System.out.print(ufs.father[i] + " ");
//            if(i%10 == 0 ) System.out.println();
//        }
//        //ufs数组全部失效
//        List<Integer> usedEventIndex = new ArrayList<Integer>();
//        List<WeiBoContent> singles = new ArrayList<WeiBoContent>();
//        List<CompositeEvent> composites = new ArrayList<CompositeEvent>();
//
//        for(int index = 0; index < ufs.father.length; index++){
//            boolean isUsed = false;
//            for(int used : usedEventIndex){
//                if(used == ufs.find(index)) {
//                    isUsed = true;
//                    break;
//                }
//            }
//            if(isUsed) continue;
//            if(ufs.find(index) == index) singles.add(contents.get(index));
//            else {
//                usedEventIndex.add(ufs.find(index));
//                CompositeEvent compositeEvent = new CompositeEvent();
//                for(int i : ufs.getAllinOneSet(index)){
//                    compositeEvent.getEvents().add(contents.get(i));
//                }
//                composites.add(compositeEvent);
//            }
//        }
//
//        System.out.println("SINGLE.................");
//        for(WeiBoContent content : singles){
//            System.out.println(content.getContent());
//            System.out.println(content.getAddr());
//            System.out.println(content.getLat() + "," + content.getLng());
//            System.out.println("____________________________");
//        }
//        System.out.println("COMPOSITE............................");
//        for(CompositeEvent com : composites){
//            System.out.println("_____________composite_Start_______________");
//            for(WeiBoContent content : com.getEvents()){
//                System.out.println(content.getContent());
//                System.out.println(content.getAddr());
//                System.out.println(content.getLat() + "," + content.getLng());
//                System.out.println("____________________________");
//            }
//            System.out.println("____________composite_End________________");
//        }
    }
}
