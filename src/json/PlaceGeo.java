package json;

/**
 * Created by Administrator on 2016/7/7.
 */
public class PlaceGeo {

    private long id;
    private String name;
    private String geoPlace;
    private String lng;
    private String lat;

    public PlaceGeo() {
    }

    public PlaceGeo(String name, String place) {
        this.name = name;
        this.geoPlace = place;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getGeoPlace() {
        return geoPlace;
    }

    public void setGeoPlace(String geoPlace) {
        this.geoPlace = geoPlace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
