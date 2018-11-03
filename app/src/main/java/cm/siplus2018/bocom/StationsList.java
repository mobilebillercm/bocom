package cm.siplus2018.bocom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cm.siplus2018.bocom.adapter.StationAdapter;
import cm.siplus2018.bocom.model.Station;
import cm.siplus2018.bocom.utils.Util;

public class StationsList extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView stations_list;
    private StationAdapter stationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.station_list));

        stations_list = findViewById(R.id.stations_list);

        stationAdapter = new StationAdapter(this, Util.stations);
        stations_list.setAdapter(stationAdapter);
        stations_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Station station = Util.stations.get(position);
                Intent intent = new Intent(getApplicationContext(), StationDetailActivity.class);
                intent.putExtra("id", station.getId());
                intent.putExtra("name", station.getName());
                intent.putExtra("latitude", station.getLatitude());
                intent.putExtra("longitude", station.getLongitude());
                startActivity(intent);
            }
        });

    }
}
