package dao;

import json.WeiBoContent;

/**
 * Created by Administrator on 2016/7/6.
 */
public class Test {

    public static void main(String[] args) {
        WeiBoContent content = new WeiBoContent();
        content.setUsername("丰台警事");
        content.setContent("#2016春夏平安行动#亲，偷了快递车电瓶的小偷被抓到了，您的快递很快到达，给好评哦快递员上楼送货，下楼就发现快递车被推到了荒草丛里，车上的电瓶不翼而飞。近日，丰台警方将专门盗窃快递车电瓶的嫌疑人刘某及收购赃物的胡某双双抓获。@平安北京丰台警方破");
        ContentDao.save(content);
    }
}
