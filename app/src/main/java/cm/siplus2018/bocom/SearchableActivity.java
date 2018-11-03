package cm.siplus2018.bocom;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cm.siplus2018.bocom.model.Station;
import cm.siplus2018.bocom.utils.Util;

public class SearchableActivity extends AppCompatActivity {
    private List<Station> listeSecondaireStation;
    private Station selectedStation;
    //private String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    public void  doMySearch(String query){
        Log.e("QUERY QUERY", query);
        listeSecondaireStation = new ArrayList<Station>();
        String userInput = query.toString();
        for (Station station: Util.stations){

            if(station.getName().toUpperCase().contains(query.toString().toUpperCase()) ||
                    station.getName().toLowerCase().contains(query.toString().toLowerCase())){
                listeSecondaireStation.add(station);
            }
        }
        if (listeSecondaireStation.size() > 0) selectedStation = listeSecondaireStation.get(0);
        else selectedStation = null;
        /*spinnerAdapterStationName =
                new SpinnerAdapterStationName(SaveStation.this, listeSecondaireStation);
        edit_autocomplete_station_name.setAdapter(spinnerAdapterStationName);*/
    }
}
