
package olo.routemap;

import com.google.android.gms.maps.MapFragment;

/**
 * @author Olek
 * @since 14-07-2014 16:18:49
 */
public class RouteMapFragment extends MapFragment {

    private Route route = null;

    public RouteMapFragment() {

    }

    public void setRoute(Route route) {
        getMap().clear();
        this.route = route;
    }

    public Route getRoute() {
        return this.route;
    }

}
