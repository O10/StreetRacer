
package olo.streetracer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * @author Aleksander Wojcik aleksander.k.wojcik@gmail.com
 * @author Ready4S
 * @since 9 lip 2014 12:11:25
 */
public class ExactTimePickerDialog extends DialogFragment {

    NumberPicker nPHours, nPMins, nPSeconds, nPMilis;

    EditText eText = null;

    double[] time;

    public ExactTimePickerDialog() {
    }

    void setEText(EditText eText) {
        this.eText = eText;
    }

    void setTime(double[] time) {
        this.time = time;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View fullL = inflater.inflate(R.layout.timepicklayout, null);

        nPHours = (NumberPicker)fullL.findViewById(R.id.nPHours);
        nPHours.setMinValue(0);
        nPHours.setMaxValue(23);
        nPMins = (NumberPicker)fullL.findViewById(R.id.nPMins);
        nPMins.setMinValue(0);
        nPMins.setMaxValue(59);
        nPSeconds = (NumberPicker)fullL.findViewById(R.id.nPS);
        nPSeconds.setMinValue(0);
        nPSeconds.setMaxValue(59);
        nPMilis = (NumberPicker)fullL.findViewById(R.id.nPMss);
        nPMilis.setMinValue(0);
        nPMilis.setMaxValue(59);

        builder.setTitle("Choose Time").setView(fullL)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (eText != null && time != null) {
                            double fulltime = 0;
                            String text = new String("");
                            boolean isS = false;
                            if (nPHours.getValue() != 0) {
                                isS = true;
                                text = Integer.toString(nPHours.getValue());
                                fulltime += nPHours.getValue() * 60 * 60;
                            }
                            if (nPMins.getValue() != 0 || isS) {
                                if (isS)
                                    text = text + ":";
                                isS = true;
                                text = text + Integer.toString(nPMins.getValue());
                                fulltime += nPMins.getValue() * 60;
                            }
                            if (nPSeconds.getValue() != 0 || isS) {
                                if (isS)
                                    text = text + ":";
                                isS = true;
                                text = text + Integer.toString(nPSeconds.getValue());
                                fulltime += nPSeconds.getValue();
                            }
                            if (nPMilis.getValue() != 0 || isS) {
                                if (isS)
                                    text = text + ":";
                                isS = true;
                                text = text + Integer.toString(nPMilis.getValue());
                                fulltime += nPMilis.getValue() / 60.0;
                            }
                            eText.setText(text);
                            time[0] = fulltime;
                        }

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}
