package contentDealer;

import json.WeiBoContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/9.
 * index of array is not useful
 */

public class UnionFindSet {

    public int[] father;

    public UnionFindSet(){

    }

    public void init (int size){
        father = new int[size];
        for(int i = 0; i < size; i++){
            father[i] = i;
        }
    }

    public void init(List<WeiBoContent> contents){
        father = new int[contents.get(contents.size()-1).getId() + 1];
        for(WeiBoContent content : contents){
            if(content.getFatherId() == null){
                father[content.getId()] = 0;
            }else{
                father[content.getId()] = content.getFatherId();
            }

        }
    }



    public int find(int x){
        int y = father[x];
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
        this.father[fatherx] = fathery;
//        for(int i = 0 ; i < father.length; i++){
//            System.out.print(father[i] + " ");
//            if(i%10 == 0 ) System.out.println();
//        }
    }

    public List<Integer> getAllinOneSet(int x){
        List<Integer> indexs = new ArrayList<Integer>();
        int y = father[x];
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
