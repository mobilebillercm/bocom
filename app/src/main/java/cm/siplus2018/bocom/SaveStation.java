package cm.siplus2018.bocom;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cm.siplus2018.bocom.adapter.SpinnerAdapterStationName;
import cm.siplus2018.bocom.model.Station;
import cm.siplus2018.bocom.utils.Util;
import cm.siplus2018.bocom.view.CustomAutoCompleteView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static cm.siplus2018.bocom.utils.Util.RequestPermissionCode;

public class SaveStation extends AppCompatActivity implements LocationListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private LocationManager locationManager;

    private Location location;
    private GoogleApiClient mGoogleApiClient;

    private CustomAutoCompleteView edit_autocomplete_station_name, edit_autocomplete_station_address, edit_autocomplete_station_phone;
    private SpinnerAdapterStationName spinnerAdapterStationName;
    private List<Station> listeSecondaireStation;
    private Station selectedStation;
    private TextView resultsavestation;
    private Button saveStationBtn;
    private ProgressBar savestation_progrees_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_station);

        edit_autocomplete_station_name = findViewById(R.id.edit_autocomplete_station_name);
        spinnerAdapterStationName = new SpinnerAdapterStationName(this, Util.stations);
        spinnerAdapterStationName.setDropDownViewResource(R.layout.custom_item_spinner_station_name);

        edit_autocomplete_station_name.setThreshold(1);
        edit_autocomplete_station_name.setTextColor(Color.parseColor("#00bfff"));

        edit_autocomplete_station_name.setAdapter(spinnerAdapterStationName);

        edit_autocomplete_station_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                spinnerAdapterStationName.notifyDataSetChanged();

                listeSecondaireStation = new ArrayList<Station>();
                String userInput = s.toString();
                for (Station station:Util.stations){

                    if(station.getName().toUpperCase().contains(s.toString().toUpperCase()) ||
                            station.getName().toLowerCase().contains(s.toString().toLowerCase())){
                        listeSecondaireStation.add(station);
                    }
                }
                if (listeSecondaireStation.size() > 0) selectedStation = listeSecondaireStation.get(0);
                else selectedStation = null;
                spinnerAdapterStationName =
                        new SpinnerAdapterStationName(SaveStation.this, listeSecondaireStation);
                edit_autocomplete_station_name.setAdapter(spinnerAdapterStationName);
            }
        });

        edit_autocomplete_station_address = findViewById(R.id.edit_autocomplete_station_address);
        edit_autocomplete_station_phone = findViewById(R.id.edit_autocomplete_station_phone);
        resultsavestation = findViewById(R.id.resultsavestation);
        savestation_progrees_bar = findViewById(R.id.savestation_progrees_bar);
        saveStationBtn = findViewById(R.id.saveStationBtn);
        saveStationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStation != null){
                    savestation_progrees_bar.setVisibility(View.VISIBLE);
                    resultsavestation.setText("");

                    if (edit_autocomplete_station_address.getText().toString().equals("") || edit_autocomplete_station_phone.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Tous les champs so nt obligatoires", Toast.LENGTH_LONG).show();
                        return;
                    }else{
                        //addPlaceToGoogleDbAndToTrip();
                    }

                }
            }
        });

        initializeMap();

    }

    private void addPlaceToGoogleDbAndToTrip()
    {
        final String placeName = selectedStation.getName();
        final String address = edit_autocomplete_station_address.getText().toString();
        final String website = "http://www.groupebocom.com/index.php";
        final String phoneNumber = edit_autocomplete_station_phone.getText().toString();
        LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());

        //GeoDataClient geodataClient = Places.getGeoDataClient(this);


        AddPlaceRequest place = new AddPlaceRequest(
                        placeName, // Name
                        latlng, // Latitude and longitude
                        address, // Address
                        Collections.singletonList(Place.TYPE_POINT_OF_INTEREST), // Place types
                        phoneNumber, // Phone number
                        Uri.parse(website) // Website
                );


        Places.GeoDataApi.addPlace(mGoogleApiClient, place).setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places)
            {

                if (!places.getStatus().isSuccess()) {

                    Log.e("PLACE PLACE", "Place query did not complete. Error: " + places.getStatus().toString());
                    places.release();
                    return;
                }

                final Place place = places.get(0);
                String newPlaceID = place.getId();
                Log.i("PLACE PLACE", "Place add result: " + place.getName() + "\n\n" + newPlaceID + "\n\n");
                places.release();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void initializeMap(){
        Log.e("INITIALIZE", "initializing initializing initializinginitializing");
        if (!checkPermission()) {
            requestPermission();
        } else {
            // Get the location manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestcode, String permissions[], int[] grantResults){
        switch (requestcode){
            case RequestPermissionCode :{
                Log.e("Nkalla0", "Nkalla1: " + (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED));
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&  grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Log.e("Nkalla1", "Nkalla1: " + (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED));
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        //this.onResume();
                        Log.e("Nkalla2", "Nkalla2: " + (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED));

                        // Get the location manager
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                        mGoogleApiClient = new GoogleApiClient
                                .Builder(this)
                                .addApi(Places.GEO_DATA_API)
                                .addApi(Places.PLACE_DETECTION_API)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .build();


                        //MainActivity.GetAllStation getAllStation = new MainActivity.GetAllStation(this, loading_content);
                        //getAllStation.execute(Util.BASE_URL + "representations");

                    }
                }else {
                    //Toast.makeText(getApplicationContext(), "Permission refusee", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), getString(R.string.you_mustgrant_permission), Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
            default:{

            }
        }
    }

    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, RequestPermissionCode);
    }

    @Override
    public void onLocationChanged(Location location) {

        this.location = location;

        Util.latitude = location.getLatitude();
        Util.longitude = location.getLongitude();

        Log.e("Location", "(" + location.getLatitude() + ", " + location.getLongitude() + ")");


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
