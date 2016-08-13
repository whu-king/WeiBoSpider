package dao;

import json.PlaceGeo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.GenericJDBCException;

import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class PlaceGeoDao {

    static SessionFactory sf;

    static {
        sf = RunHibernate.getSessionFactory();
    }

    public static void save(List<PlaceGeo> geos) {
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        for (PlaceGeo geo : geos) {
            sess.save(geo);
        }
        try {
            tran.commit();
        } catch (GenericJDBCException e) {
            e.printStackTrace();
            System.out.println();
        }
        sess.close();
    }

    public static void update(PlaceGeo geo) {
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        sess.update(geo);
        try {
            tran.commit();
        } catch (GenericJDBCException e) {
            e.printStackTrace();
            System.out.println();
        }
        sess.close();
    }

    public static List<PlaceGeo> getAllGeo() {
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        Query query = sess.createQuery("from PlaceGeo");
        List list = query.list();
        tran.commit();
        sess.close();
        return (List<PlaceGeo>) list;
    }

    public static PlaceGeo getPlaceGeoById(long id) {
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        PlaceGeo geo = (PlaceGeo) sess.get(PlaceGeo.class, id);
        tran.commit();
        sess.close();
        return geo;
    }

    public static void deleteById(Long id) {
        Session sess = sf.openSession();
        Transaction tran = sess.beginTransaction();
        sess.delete(getPlaceGeoById(id));
        tran.commit();
        sess.close();
    }
}
