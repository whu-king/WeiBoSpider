package dao;

import json.WeiBoContent;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class ContentDao {

    static SessionFactory sf;

    static {
        sf = RunHibernate.getSessionFactory();
    }

    public static long save(WeiBoContent content) {
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        sess.save(content);
        tran.commit();
        sess.close();
        return content.getId();
    }

    public static void save(List<WeiBoContent> contents) {
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        for (WeiBoContent content : contents) {
            sess.save(content);
        }
        try {
            tran.commit();
        } catch (GenericJDBCException e) {
            System.out.println("insert fail one time");
        }
        sess.close();
    }

    public static List<WeiBoContent> getAll() {
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        Query query = sess.createQuery("from WeiBoContent ");
        List list = query.list();
        tran.commit();
        sess.close();
        return (List<WeiBoContent>) list;
    }

    public static void delete(Integer id){
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        sess.delete(sess.get(WeiBoContent.class,id));
        tran.commit();
        sess.close();
    }

    public static void update(WeiBoContent content) {
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        sess.update(content);
        tran.commit();
        sess.close();
    }
}
