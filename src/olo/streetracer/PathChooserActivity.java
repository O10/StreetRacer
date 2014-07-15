
package olo.streetracer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import olo.database.Path;
import olo.database.PathsDbAdapter;
import olo.routemap.Route;
import olo.routemap.RouteMapFragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Aleksander Wojcik aleksander.k.wojcik@gmail.com
 * @since 7 lip 2014 11:43:29
 */
public class PathChooserActivity extends Activity {

    ListView pathsList = null;

    PathsDbAdapter pathsDBAdapter;

    private FrameLayout mapFrameLayout;

    private RouteMapFragment routeMapFragment;

    boolean mapVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pathchooseractivitylayout);
        pathsList = (ListView)findViewById(R.id.pathsList);
        mapFrameLayout = (FrameLayout)findViewById(R.id.mapFragmentLayout);
        pathsDBAdapter = new PathsDbAdapter(getApplicationContext());

        pathsDBAdapter.open();

        /*
         * ArrayList<Path> testArrayList = new ArrayList<Path>();
         * testArrayList.add(new Path("test", 5.25, "OLO", 1542, 21.2, null,
         * null)); testArrayList.add(new Path("tes2", 1.25, "BOLO", 1121242,
         * 212.2, null, null)); testArrayList.add(new Path(
         * "baaaaaaaaaaaaaaaaaaaaardzo dluga dessadadsadsadasdasdadadsadsa",
         * 1.25, "MASTER", 1121242, 212.2, null, null)); for (int i = 0; i <
         * testArrayList.size(); i++)
         * pathsDBAdapter.insertPath(testArrayList.get(i));
         */
        // generateRandomDatabase(100);
        LatLng one = new LatLng(49.993162, 20.105295);
        LatLng two = new LatLng(49.989961, 20.080833);
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        list.add(two);
        Path p = new Path("tutaj olo testuje", 1.1, "Olo", 1545, 60, one, list);
        pathsDBAdapter.insertPath(p);
        SharedPreferences settings = getSharedPreferences(FilterPreferencesActivity.PREFS_FILTER, 0);

        pathsList.setAdapter(new PathListViewAdapter(getApplicationContext(), pathsDBAdapter
                .getPaths(null, settings.getString("WHERE", null), null, null, null,
                        settings.getString("ORDERBYSQL", null), null)));

        ArrayList<LatLng> list2 = pathsDBAdapter.getCheckpoints(((PathListViewAdapter)pathsList
                .getAdapter()).curPaths.get(0).getId());
        for (int i = 0; i < list2.size(); i++) {
            Log.d("DATABASE",
                    Double.toString(list2.get(i).longitude)
                            + Double.toString(list2.get(i).latitude));
        }

        pathsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
                if (!mapVisible) {
                    routeMapFragment = new RouteMapFragment();
                    FragmentManager fragmentmanager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentmanager.beginTransaction();
                    fragmentTransaction.add(R.id.mapFragmentLayout, routeMapFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    fragmentmanager.executePendingTransactions();
                }

                Path m = (Path)pathsList.getItemAtPosition(position);
                m.setpathCord(pathsDBAdapter.getCheckpoints(m.getId()));
                ArrayList<LatLng> points = m.getAllLatLng();

                routeMapFragment.getMap().moveCamera(
                        CameraUpdateFactory.newLatLngZoom(points.get(0), 14));

                routeMapFragment.setRoute(new Route());

                routeMapFragment.getRoute().drawRoute(routeMapFragment.getMap(),
                        PathChooserActivity.this, points, false, "en", true);

            }

        });

        getFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {

                    @Override
                    public void onBackStackChanged() {
                        if (!mapVisible) {
                            mapFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                            mapVisible = true;
                        } else {
                            mapFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 0));
                            mapVisible = false;
                        }

                    }
                });

    }

    void generateRandomDatabase(int limit) {
        SecureRandom random = new SecureRandom();
        Random r = new Random();
        for (int i = 0; i < limit; i++) {
            String desc = new BigInteger(130, random).toString(32);
            double rating = 1000 * r.nextDouble();
            String bestnick = new BigInteger(130, random).toString(32);
            Log.d("DATABASE", bestnick);
            double distance = 1000 * r.nextDouble();
            double besttime = 1000 * r.nextDouble();
            Path p = new Path(desc, rating, bestnick, distance, besttime, null, null);
            pathsDBAdapter.insertPath(p);
        }
        Path p = new Path("tutaj olo testuje", 1.1, "Olo", 1545, 60, null, null);
        pathsDBAdapter.insertPath(p);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        Log.d("DATABASE", "Calling onPause");
        super.onPause();
        pathsDBAdapter.deleteTable();
        pathsDBAdapter.close();
        this.deleteDatabase("maindatabase.db");
    }

}
