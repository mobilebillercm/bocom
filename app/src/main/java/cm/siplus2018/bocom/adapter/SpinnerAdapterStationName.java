package cm.siplus2018.bocom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cm.siplus2018.bocom.R;
import cm.siplus2018.bocom.model.Station;

public class SpinnerAdapterStationName extends ArrayAdapter {

    private Context context;
    private List<Station> values;

    public SpinnerAdapterStationName(Context context, List<Station> values) {
        super(context, R.layout.custom_item_spinner_station_name, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater li=(LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=li.inflate(R.layout.custom_item_spinner_station_name, null);
        }

        TextView textView_name = (TextView)convertView.findViewById(R.id.save_position_station_name);
        textView_name.setText(values.get(position).getName());

        /*TextView textView_id = (TextView)convertView.findViewById(R.id.save_position_commune_id);
        textView_id.setText("" + values.get(position).getId());

        TextView textView_code = (TextView)convertView.findViewById(R.id.save_position_commune_code);
        textView_code.setText(values.get(position).getCode());

        TextView textView_departement_id = (TextView)convertView.findViewById(R.id.save_position_commune_departement_id);
        textView_departement_id.setText("" + values.get(position).getDepartement_id());*/

       /* if (position == 0){
            ImageView imageView = (ImageView)convertView.findViewById(R.id.iconItemCommune);
            imageView.setVisibility(View.GONE);
        }*/
        return convertView;
    }
}
