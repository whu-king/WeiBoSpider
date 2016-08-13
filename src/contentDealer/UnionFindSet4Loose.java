package contentDealer;

import json.WeiBoContent;
import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/14.
 */
public class UnionFindSet4Loose {

    public Map<Integer,Integer> father;

    public UnionFindSet4Loose(){

    }

    public void init(List<WeiBoContent> contents){
        father = new HashMap<Integer,Integer>();
        for(WeiBoContent content : contents){
            if(content.getFatherId() == null){
                father.put(content.getId(),0);
            }else{
                father.put(content.getId(),content.getFatherId());
            }
        }
    }



    public int find(int x){
        int y = father.get(x);
        if(y == 0){
            return x;
        }
        if(x == y)
            return x;
        else return find(y);
    }

    public void union(int x, int y){
        int fatherx = find(x);
        int fathery = find(y);
//        System.out.println();
//        System.out.println("X:" + x + " " + "Y: " + y);
//        System.out.println("father(X):" + fatherx + " " +  "father(Y):" + fathery);
        father.put(fatherx,fathery);
//        for(int i = 0 ; i < father.length; i++){
//            System.out.print(father[i] + " ");
//            if(i%10 == 0 ) System.out.println();
//        }
    }

    public List<Integer> getAllinOneSet(int x){
        List<Integer> indexs = new ArrayList<Integer>();
        int y = father.get(x);
        if(x == y){
            indexs.add(x);
            return indexs;
        }
        else {
            indexs.add(x);
            indexs.addAll(getAllinOneSet(y));
            return indexs;
        }

    }
}
