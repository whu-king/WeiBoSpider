package dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by Administrator on 2016/7/6.
 */
public class RunHibernate {

    private static SessionFactory sf;

    static {
        Configuration conf = new Configuration().configure();
        sf = conf.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return sf;
    }
}
