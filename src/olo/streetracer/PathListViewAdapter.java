
package olo.streetracer;

import olo.database.Path;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author Aleksander Wojcik aleksander.k.wojcik@gmail.com
 * @since 8 lip 2014 13:00:56
 */
public class PathListViewAdapter extends BaseAdapter {

    ArrayList<Path> curPaths;

    LayoutInflater inflater;

    public PathListViewAdapter(Context context, ArrayList<Path> paths) {
        curPaths = paths;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {
        public TextView tVDescription, tVHowFar, tVDistane, tVBestTime, tVBestTimeNick, tVRating;
    }

    @Override
    public int getCount() {
        return curPaths.size();
    }

    @Override
    public Path getItem(int n) {
        return curPaths.get(n);
    }

    @Override
    public long getItemId(int n) {
        return n;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.singlelistview, viewGroup, false);
            holder.tVDescription = (TextView)convertView.findViewById(R.id.textViewDescription);
            holder.tVHowFar = (TextView)convertView.findViewById(R.id.textViewHowFar);
            holder.tVDistane = (TextView)convertView.findViewById(R.id.textViewDistance);
            holder.tVBestTime = (TextView)convertView.findViewById(R.id.textViewBestTime);
            holder.tVBestTimeNick = (TextView)convertView.findViewById(R.id.textViewBestTimeNick);
            holder.tVRating = (TextView)convertView.findViewById(R.id.textViewRating);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        Path curPath = curPaths.get(position);
        holder.tVDescription.setText(curPath.getDescription());
        holder.tVHowFar.setText("0"); // how far to be changed
        holder.tVDistane.setText(Double.toString(curPath.getDistance()));
        holder.tVBestTime.setText(Double.toString(curPath.getBesTime()));
        holder.tVBestTimeNick.setText(curPath.getBestTimeNick());
        holder.tVRating.setText(Double.toString(curPath.getRating()));
        return convertView;
    }
}
