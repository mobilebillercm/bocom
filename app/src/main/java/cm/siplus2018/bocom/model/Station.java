package cm.siplus2018.bocom.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Ange_Douki on 08/01/2017.
 */

public class Station implements ClusterItem {
    private int id;
    private String name;
    private  double latitude, longitude;

    public Station(int id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Station(int id, LatLng position, String name) {
        this.id = id;
        this.name = name;
        this.latitude = position.latitude;
        this.longitude = position.longitude;
    }
    public Station(LatLng position, String name) {
        this.name = name;
        this.latitude = position.latitude;
        this.longitude = position.longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public int getProfilePhoto() {
        return profilePhoto;
    }*/

   /* public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }*/

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(this.latitude, this.longitude);
    }

    public String toString(){
        return this.getName();
    }
}
