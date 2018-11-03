package cm.siplus2018.bocom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cm.siplus2018.bocom.R;
import cm.siplus2018.bocom.model.Product;
import cm.siplus2018.bocom.model.Service;

/**
 * Created by nkalla on 02/11/18.
 */

public class ServiceAdapter extends BaseAdapter{
    private Context context;
    private List<Service> services;

    public ServiceAdapter(Context context, List<Service> services) {
        this.context = context;
        this.services = services;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Object getItem(int position) {
        return services.get(position);
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
            convertView=li.inflate(R.layout.service_layout, null);
        }

        TextView service_name = convertView.findViewById(R.id.service_name);
        service_name.setText(services.get(position).getName());
        TextView service_id = convertView.findViewById(R.id.service_id);
        service_id.setText(" " + services.get(position).getServiceid());
        TextView service_description = convertView.findViewById(R.id.service_description);
        service_description.setText(services.get(position).getDescription());
        return convertView;
    }
}
