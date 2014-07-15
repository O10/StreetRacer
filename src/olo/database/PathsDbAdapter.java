
package olo.database;

import com.google.android.gms.maps.model.LatLng;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * @author Aleksander Wojcik aleksander.k.wojcik@gmail.com
 * @since 7 lip 2014 12:04:57
 */
public class PathsDbAdapter {
    private static final String DEBUG_TAG = "DATABASE";

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "maindatabase.db";

    private static final String DB_PATHS_TABLE = "paths";

    public static final String KEY_ID = "_id";

    public static final int ID_COLUMN = 0;

    public static final String KEY_DESCRIPTION = "description";

    public static final int DESCRIPTION_COLUMN = 1;

    public static final String KEY_BESTTIME = "besttime";

    public static final int BESTTIME_COLUMN = 2;

    public static final String KEY_BESTTIMENICK = "besttimenick";

    public static final int BESTTIMENICK_COLUMN = 3;

    public static final String KEY_RATING = "rating";

    public static final int RATING_COLUMN = 4;

    public static final String KEY_DISTANCE = "distance";

    public static final int DISTANCE_COLUMN = 5;

    public static final String KEY_STARTX = "startx";

    public static final int STARTX_COLUMN = 6;

    public static final String KEY_STARTY = "starty";

    public static final int STARTY_COLUMN = 7;

    public static final String KEY_POSITION = "position";

    public static final int POSTION_COLUMN = 1;

    public static final String KEY_XCORD = "xcord";

    public static final int XCORD_COLUMN = 2;

    public static final String KEY_YCORD = "ycord";

    public static final int YCORD_COLUMN = 3;

    private static final String DB_CREATE_PATHS_TABLE = "CREATE TABLE " + DB_PATHS_TABLE + "( "
            + KEY_ID + " " + "INTEGER PRIMARY KEY AUTOINCREMENT" + ", " + KEY_DESCRIPTION + " "
            + "TEXT NOT NULL" + ", " + KEY_BESTTIME + " " + "Double DEFAULT 0" + ", "
            + KEY_BESTTIMENICK + " " + "TEXT DEFAULT NULL" + "," + KEY_RATING + " "
            + "Double DEFAULT 0" + "," + KEY_DISTANCE + " " + "Double DEFAULT 0" + "," + KEY_STARTX
            + " " + "Double DEFAULT 0" + "," + KEY_STARTY + " " + "Double DEFAULT 0" + ");";

    private static final String DROP_PATHS_TABLE = "DROP TABLE IF EXISTS " + DB_PATHS_TABLE;

    private SQLiteDatabase db;

    private Context context;

    private DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_PATHS_TABLE);
            Log.d(DEBUG_TAG, DB_CREATE_PATHS_TABLE);
            Log.d(DEBUG_TAG, "Entering on create");
            Log.d(DEBUG_TAG, "Table " + DB_PATHS_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_PATHS_TABLE);
            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_PATHS_TABLE + " updated from ver." + oldVersion
                    + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");
            onCreate(db);

        }

    }

    public PathsDbAdapter(Context context) {
        this.context = context;

    }

    public PathsDbAdapter open() {
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertPath(Path newPath) {
        Log.d(DEBUG_TAG, "Inserting new Path");
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_DESCRIPTION, newPath.getDescription());
        newValues.put(KEY_BESTTIME, newPath.getBesTime());
        newValues.put(KEY_BESTTIMENICK, newPath.getBestTimeNick());
        newValues.put(KEY_DISTANCE, newPath.getDistance());
        newValues.put(KEY_RATING, newPath.getRating());
        if (newPath.getStart() != null) {
            newValues.put(KEY_STARTX, newPath.getStart().longitude);
            newValues.put(KEY_STARTY, newPath.getStart().latitude);
        }
        if (newPath.getPathCord() != null) {
            Log.d("DATABASE", "Inserting Cordinates");
            int id = 0;
            String[] col = {
                KEY_ID
            };
            Cursor cur = db.query(DB_PATHS_TABLE, col, null, null, null, null, KEY_ID + " DESC",
                    "1");
            if (cur.moveToFirst())
                id = cur.getInt(ID_COLUMN) + 1;
            createCheckpointTable(id, newPath.getPathCord());
        }
        return db.insert(DB_PATHS_TABLE, null, newValues);
    }

    public boolean updatePath(Path updatedPath) {
        String where = KEY_ID + "=" + updatedPath.getId();
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_DESCRIPTION, updatedPath.getDescription());
        newValues.put(KEY_BESTTIME, updatedPath.getBesTime());
        newValues.put(KEY_BESTTIMENICK, updatedPath.getBestTimeNick());
        newValues.put(KEY_DISTANCE, updatedPath.getDistance());
        newValues.put(KEY_RATING, updatedPath.getRating());
        if (updatedPath.getStart() != null) {
            newValues.put(KEY_STARTX, updatedPath.getStart().longitude);
            newValues.put(KEY_STARTY, updatedPath.getStart().latitude);
        }
        return db.update(DB_PATHS_TABLE, newValues, where, null) > 0;
    }

    public boolean deletePath(long id) {
        String where = KEY_ID + "=" + id;
        return db.delete(DB_PATHS_TABLE, where, null) > 0;
    }

    public Cursor getWholeTable() {
        return db.query(DB_PATHS_TABLE, null, null, null, null, null, null);
    }

    public ArrayList<Path> getPaths(String[] columns, String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy, String limit) {
        Cursor cursor = db.query(false, DB_PATHS_TABLE, columns, selection, selectionArgs, groupBy,
                having, orderBy, limit, null);
        ArrayList<Path> paths = new ArrayList<Path>();
        String description, bestTimeNick;
        double rating, distance, bestTime;
        LatLng startCord;

        while (cursor.moveToNext()) {
            description = cursor.getString(DESCRIPTION_COLUMN);
            bestTimeNick = cursor.getString(BESTTIMENICK_COLUMN);
            rating = cursor.getDouble(RATING_COLUMN);
            distance = cursor.getDouble(DISTANCE_COLUMN);
            bestTime = cursor.getDouble(BESTTIME_COLUMN);
            startCord = new LatLng(cursor.getDouble(STARTY_COLUMN), cursor.getDouble(STARTX_COLUMN));
            paths.add(new Path(description, rating, bestTimeNick, distance, bestTime, startCord,
                    null));
        }
        return paths;

    }

    public ArrayList<LatLng> getCheckpoints(int ID) {
        ArrayList<LatLng> checkpoints = new ArrayList<LatLng>();
        String[] cols = {
                KEY_XCORD, KEY_YCORD
        };
        Cursor cur = db.query("checkpoints_" + Integer.toString(ID), cols, null, null, null, null,
                KEY_POSITION + " ASC");
        while (cur.moveToNext()) {
            checkpoints.add(new LatLng(cur.getDouble(1), cur.getDouble(0)));
        }

        return checkpoints;
    }

    void createCheckpointTable(int pathID, ArrayList<LatLng> coordinates) {
        String tablename = new String("checkpoints_" + Integer.toString(pathID));
        db.execSQL("DROP TABLE IF EXISTS " + tablename);
        Log.d("DATABASE", "Creating table " + tablename);

        String createS = new String("CREATE TABLE " + tablename
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_POSITION + " Integer default 0,"
                + KEY_XCORD + " Double default 0," + KEY_YCORD + " Double default 0);");
        db.execSQL(createS);

        for (int i = 0; i < coordinates.size(); i++) {
            ContentValues newVal = new ContentValues();
            newVal.put(KEY_POSITION, i);
            newVal.put(KEY_XCORD, coordinates.get(i).longitude);
            newVal.put(KEY_YCORD, coordinates.get(i).latitude);
            db.insert(tablename, null, newVal);
        }
    }

    public boolean deleteTable() {
        return db.delete(DB_PATHS_TABLE, null, null) > 0;

    }

}
