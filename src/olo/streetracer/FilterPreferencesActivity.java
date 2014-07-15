
package olo.streetracer;

import olo.database.PathsDbAdapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * @author Aleksander Wojcik aleksander.k.wojcik@gmail.com
 * @since 8 lip 2014 19:26:06
 */
public class FilterPreferencesActivity extends Activity {
    public static String PREFS_FILTER = "filterpreferences";

    EditText eTMinBT, eTMaxBT, eTDescription, eTRating, eTminDistance, eTMaxDistance;

    boolean minTimeChanged = false, maxTimeChanged = false;

    RadioGroup orderBy, sortType;

    Button buttonOK, buttonCancel;

    double[] minTime, maxTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        minTime = new double[1];
        maxTime = new double[1];
        setContentView(R.layout.filteractivitylayout);
        eTMinBT = (EditText)findViewById(R.id.eTminBT);
        eTMaxBT = (EditText)findViewById(R.id.eTMaxBT);
        eTDescription = (EditText)findViewById(R.id.eTDescription);
        eTRating = (EditText)findViewById(R.id.eTRating);
        eTminDistance = (EditText)findViewById(R.id.eTMinDistance);
        eTMaxDistance = (EditText)findViewById(R.id.eTMaxDistance);
        buttonOK = (Button)findViewById(R.id.buttonOK);
        buttonCancel = (Button)findViewById(R.id.buttonCancel);
        orderBy = (RadioGroup)findViewById(R.id.radioGroupOrderBy);
        sortType = (RadioGroup)findViewById(R.id.radioGroupoOrderType);

        updateViewsWithPreferences();

        eTMinBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ExactTimePickerDialog ex = new ExactTimePickerDialog();
                ex.setEText(eTMinBT);
                ex.setTime(minTime);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ex.show(ft, "dialog");
                minTimeChanged = true;
            }
        });
        eTMaxBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ExactTimePickerDialog ex = new ExactTimePickerDialog();
                ex.setEText(eTMaxBT);
                ex.setTime(maxTime);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ex.show(ft, "dialog");
                maxTimeChanged = true;

            }
        });

        buttonOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                savePreferences();
                startActivity(new Intent(getApplicationContext(), PathChooserActivity.class));
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PathChooserActivity.class));
                finish();
            }
        });

    }

    void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_FILTER, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("DESCRIPTION", eTDescription.getText().toString());

        if (!eTminDistance.getText().toString().matches(""))
            editor.putLong("MINDISTANCE", Double.doubleToRawLongBits(Double
                    .parseDouble(eTminDistance.getText().toString())));
        else
            editor.putLong("MINDISTANCE", Double.doubleToRawLongBits(0));

        if (!eTMaxDistance.getText().toString().matches(""))
            editor.putLong("MAXDISTANCE", Double.doubleToRawLongBits(Double
                    .parseDouble(eTMaxDistance.getText().toString())));
        else
            editor.putLong("MAXDISTANCE", Double.doubleToRawLongBits(0));

        if (!eTRating.getText().toString().matches(""))
            editor.putLong("MINRATING",
                    Double.doubleToRawLongBits(Double.parseDouble(eTRating.getText().toString())));
        else
            editor.putLong("MINRATING", Double.doubleToRawLongBits(0));

        if (minTimeChanged)
            editor.putLong("MINTIME", Double.doubleToRawLongBits(minTime[0]));

        if (maxTimeChanged)
            editor.putLong("MAXTIME", Double.doubleToRawLongBits(maxTime[0]));

        editor.putInt("ORDERBY", orderBy.getCheckedRadioButtonId());

        editor.putInt("SORTTYPE", sortType.getCheckedRadioButtonId());

        String where = createWhereString();

        editor.putString("WHERE", where);

        String orderBy = createOrderByString();

        editor.putString("ORDERBYSQL", orderBy);

        editor.commit();

    }

    void updateViewsWithPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_FILTER, 0);

        eTDescription.setText(settings.getString("DESCRIPTION", ""));

        double doubles = Double.longBitsToDouble(settings.getLong("MINDISTANCE",
                Double.doubleToLongBits(0)));
        if (doubles != 0)
            eTminDistance.setText(Double.toString(doubles));

        doubles = Double.longBitsToDouble(settings.getLong("MAXDISTANCE",
                Double.doubleToLongBits(0)));
        if (doubles != 0)
            eTMaxDistance.setText(Double.toString(doubles));

        doubles = Double
                .longBitsToDouble(settings.getLong("MINRATING", Double.doubleToLongBits(0)));
        if (doubles != 0)
            eTRating.setText(Double.toString(doubles));

        int iD = settings.getInt("ORDERBY", -1);
        if (iD != -1)
            ((RadioButton)findViewById(iD)).setChecked(true);

        iD = settings.getInt("SORTTYPE", -1);
        if (iD != -1)
            ((RadioButton)findViewById(iD)).setChecked(true);

        doubles = Double.longBitsToDouble(settings.getLong("MINTIME", Double.doubleToLongBits(0)));
        if (doubles != 0)
            eTMinBT.setText(getTimeString(doubles));

        doubles = Double.longBitsToDouble(settings.getLong("MAXTIME", Double.doubleToLongBits(0)));
        if (doubles != 0)
            eTMaxBT.setText(getTimeString(doubles));

    }

    static public String getTimeString(double value) {
        String strVal = new String();
        boolean hasStarted = false;
        int intVal = (int)(value / (60 * 60));
        if (intVal != 0) {
            hasStarted = true;
            strVal += Integer.toString(intVal);
            value -= intVal * 60 * 60;
        }
        intVal = (int)(value / 60);
        if (intVal != 0 || hasStarted) {
            if (hasStarted)
                strVal += ":";
            hasStarted = true;
            strVal += Integer.toString(intVal);
            value -= intVal * 60;
        }
        intVal = (int)value;
        if (intVal != 0 || hasStarted) {
            if (hasStarted)
                strVal += ":";
            hasStarted = true;
            strVal += Integer.toString(intVal);
            value -= intVal;
        }
        intVal = (int)(value * 60);
        if (intVal != 0 || hasStarted) {
            if (hasStarted)
                strVal += ":";
            hasStarted = true;
            strVal += Integer.toString(intVal);
        }
        return strVal;
    }

    private String createWhereString() {
        String where = new String();
        boolean hasStared = false;
        if (!eTDescription.getText().toString().matches("")) {
            where += PathsDbAdapter.KEY_DESCRIPTION + " LIKE '%"
                    + eTDescription.getText().toString() + "%'";
            hasStared = true;
        }

        if (!eTminDistance.getText().toString().matches("")) {
            if (hasStared)
                where += " AND ";
            hasStared = true;
            where += PathsDbAdapter.KEY_DISTANCE + " > " + eTminDistance.getText().toString();
        }
        if (!eTMaxDistance.getText().toString().matches("")) {
            if (hasStared)
                where += " AND ";
            hasStared = true;
            where += PathsDbAdapter.KEY_DISTANCE + " < " + eTMaxDistance.getText().toString();
        }
        if (!eTRating.getText().toString().matches("")) {
            if (hasStared)
                where += " AND ";
            hasStared = true;
            where += PathsDbAdapter.KEY_RATING + " > " + eTRating.getText().toString();
        }
        if (!eTMinBT.getText().toString().matches("")) {
            if (hasStared)
                where += " AND ";
            hasStared = true;
            where += PathsDbAdapter.KEY_BESTTIME + " > " + Double.toString(minTime[0]);
        }
        if (!eTMaxBT.getText().toString().matches("")) {
            if (hasStared)
                where += " AND ";
            hasStared = true;
            where += PathsDbAdapter.KEY_BESTTIME + " < " + Double.toString(maxTime[0]);
        }

        return where;
    }

    private String createOrderByString() {
        String orderBy = new String();
        int Idchecked = this.orderBy.getCheckedRadioButtonId();
        if (Idchecked == R.id.raadiodescription)
            orderBy += PathsDbAdapter.KEY_DESCRIPTION;
        else if (Idchecked == R.id.radiorating)
            orderBy += PathsDbAdapter.KEY_RATING;
        else if (Idchecked == R.id.radiodistance)
            orderBy += PathsDbAdapter.KEY_DISTANCE;
        else if (Idchecked == R.id.radiobt)
            orderBy += PathsDbAdapter.KEY_BESTTIME;

        orderBy += " ";
        Idchecked = this.sortType.getCheckedRadioButtonId();
        if (Idchecked == R.id.raadiodesc)
            orderBy += "DESC";
        else
            orderBy += "ASC";

        return orderBy;
    }
}
