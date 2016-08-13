package dao;

import json.Config;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Created by Administrator on 2016/7/18.
 */
public class ConfigDao {

    static SessionFactory sf;

    static {
        sf = RunHibernate.getSessionFactory();
    }

    public static Config get(int id){
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        Config conf = (Config)sess.get(Config.class, id);
        tran.commit();
        sess.close();
        return conf;
    }

    public static void update(Config conf){
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        sess.update(conf);
        tran.commit();
        sess.close();
    }
}
