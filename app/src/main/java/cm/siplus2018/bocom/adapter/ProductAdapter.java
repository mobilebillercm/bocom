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
import cm.siplus2018.bocom.model.Station;

/**
 * Created by nkalla on 02/11/18.
 */

public class ProductAdapter extends BaseAdapter{
    private Context context;
    private List<Product> products;

    public ProductAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
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
            convertView=li.inflate(R.layout.product_layout, null);
        }

        TextView product_name = convertView.findViewById(R.id.product_name);
        product_name.setText(products.get(position).getName());
        TextView product_id = convertView.findViewById(R.id.product_id);
        product_id.setText(" " + products.get(position).getProductid());
        TextView product_description = convertView.findViewById(R.id.product_description);
        product_description.setText(products.get(position).getDescription());
        return convertView;
    }
}
