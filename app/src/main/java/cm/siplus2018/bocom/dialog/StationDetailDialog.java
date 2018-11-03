package cm.siplus2018.bocom.dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cm.siplus2018.bocom.R;
import cm.siplus2018.bocom.StationDetailActivity;
import cm.siplus2018.bocom.model.Station;


public class StationDetailDialog extends DialogFragment {
    private StationDetailDialog this_station_detail_dialog;
    private static Station station;
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static StationDetailDialog newInstance(Station stat, int num) {
        StationDetailDialog f = new StationDetailDialog();

        station = stat;
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this_station_detail_dialog = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String titre = "Voir les details de la station " + station.getName();
    	getDialog().setTitle(titre);
        final View fragment_dialog = inflater.inflate(R.layout.station_detail_dialog, container, false);
        TextView goto_station_detail = fragment_dialog.findViewById(R.id.goto_station_detail);
        goto_station_detail.setText(titre);
        Button button_cancel = (Button)fragment_dialog.findViewById(R.id.cancel);
        button_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				this_station_detail_dialog.dismiss();
			}
		});

        Button button_yes = (Button)fragment_dialog.findViewById(R.id.confirm);
        button_yes.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                this_station_detail_dialog.dismiss();
                Intent intent = new Intent(getActivity(), StationDetailActivity.class);
                intent.putExtra("id", station.getId());
                intent.putExtra("name", station.getName());
                intent.putExtra("latitude", station.getLatitude());
                intent.putExtra("longitude", station.getLongitude());
                getActivity().startActivity(intent);
            }
        });
        return fragment_dialog;
    }
}

