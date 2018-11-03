package cm.siplus2018.bocom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cm.siplus2018.bocom.R;
import cm.siplus2018.bocom.model.Station;

/**
 * Created by nkalla on 01/11/18.
 */

public class StationAdapter extends BaseAdapter {
    private Context context;
    private List<Station> stations;

    public StationAdapter(Context context, List<Station> stations) {
        this.context = context;
        this.stations = stations;
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public Object getItem(int position) {
        return stations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li=(LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=li.inflate(R.layout.station_layout, null);
        }

        TextView station_name = convertView.findViewById(R.id.station_name);
        station_name.setText(stations.get(position).getName());
        TextView station_id = convertView.findViewById(R.id.station_id);
        station_id.setText(" " + stations.get(position).getId());
        TextView station_lat_long = convertView.findViewById(R.id.station_lat_long);
        station_lat_long.setText(stations.get(position).getLatitude() + " - " + stations.get(position).getLongitude());
        return convertView;
    }
}
