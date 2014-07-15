
package olo.database;

import com.google.android.gms.maps.model.LatLng;

import android.util.Log;

import java.util.ArrayList;

/**
 * @author Aleksander Wojcik aleksander.k.wojcik@gmail.com
 * @since 7 lip 2014 12:36:19
 */
public class Path {
    private String description = null;

    private String bestTimeNick = null;

    private ArrayList<LatLng> pathCord = null;

    private double rating = 0, bestTime = 0, distance = 0;

    LatLng startPoint;

    private int Id = 0;

    public Path() {
    }

    public Path(String description, double rating, String bestTimeNick, double distance,
            double bestTime, LatLng start, ArrayList<LatLng> pathCord) {
        this.description = description;
        this.rating = rating;
        this.pathCord = pathCord;
        this.bestTimeNick = bestTimeNick;
        this.bestTime = bestTime;
        this.startPoint = start;
        this.distance = distance;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public void setbesTimeNick(String newbestTimeNick) {
        this.bestTimeNick = newbestTimeNick;
    }

    public void setBestTime(double newBestTime) {
        this.bestTime = newBestTime;
    }

    public void setpathCord(ArrayList<LatLng> newPathCord) {
        this.pathCord = newPathCord;
    }

    public void setRating(double newRating) {
        this.rating = newRating;
    }

    public void setId(int newId) {
        this.Id = newId;
    }

    public void setStart(LatLng newStart) {
        this.startPoint = newStart;
    }

    public void setDistance(double newDistance) {
        this.distance = newDistance;
    }

    public String getDescription() {
        return this.description;
    }

    public String getBestTimeNick() {
        return this.bestTimeNick;
    }

    public double getBesTime() {
        return this.bestTime;
    }

    public int getId() {
        return this.Id;
    }

    public ArrayList<LatLng> getPathCord() {
        return this.pathCord;
    }

    public double getRating() {
        return this.rating;
    }

    public LatLng getStart() {
        return this.startPoint;
    }

    public double getDistance() {
        return this.distance;
    }

    public ArrayList<LatLng> getAllLatLng() {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        if (startPoint != null) {
            list.add(startPoint);
            Log.d("DATABASE", "GETSTART NOT NULL");

        }
        if (pathCord != null) {
            Log.d("DATABASE", "GetPathCord NOT NULL");
            ArrayList<LatLng> t = pathCord;
            for (LatLng obj : t)
                list.add(obj);
        }
        return list;
    }

}
