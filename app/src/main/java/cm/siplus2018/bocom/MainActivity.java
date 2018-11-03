package cm.siplus2018.bocom;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import cm.siplus2018.bocom.adapter.SpinnerAdapterStationName;
import cm.siplus2018.bocom.dialog.StationDetailDialog;
import cm.siplus2018.bocom.model.Station;
import cm.siplus2018.bocom.utils.Util;
import cm.siplus2018.bocom.view.CustomAutoCompleteView;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static cm.siplus2018.bocom.utils.Util.RequestPermissionCode;

//http://wptrafficanalyzer.in/blog/adding-google-places-autocomplete-api-as-custom-suggestions-in-android-search-dialog/


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
        ClusterManager.OnClusterClickListener<Station>,
        ClusterManager.OnClusterInfoWindowClickListener<Station>,
        ClusterManager.OnClusterItemClickListener<Station>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Station>, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "DEBUG Custom_auto";
    private SupportMapFragment mapFragment;
    private GoogleMap mGoogleMap;

    private ClusterManager<Station> mClusterManager;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    //private SupportPlaceAutocompleteFragment place_autocomplete_fragment;

    private LinearLayout loading_content;

    private SpinnerAdapterStationName spinnerAdapterStationName;
    private List<Station> listeSecondaireStation;
    private Station selectedStation;
    private CustomAutoCompleteView editsearch;

    private Location location;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;

    public static int mStackLevel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading_content = findViewById(R.id.loading_content);
        loading_content.setVisibility(View.VISIBLE);

        spinnerAdapterStationName = new SpinnerAdapterStationName(this, Util.stations);
        spinnerAdapterStationName.setDropDownViewResource(R.layout.custom_item_spinner_station_name);
        /*search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchRequested();
            }
        });*/

        editsearch = findViewById(R.id.editsearch);

        editsearch.addTextChangedListener(new TextWatcher() {
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
                for (Station station : Util.stations) {

                    if (station.getName().toUpperCase().contains(s.toString().toUpperCase()) ||
                            station.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        listeSecondaireStation.add(station);
                    }
                }
                if (listeSecondaireStation.size() > 0)
                    selectedStation = listeSecondaireStation.get(0);
                else selectedStation = null;
                spinnerAdapterStationName =
                        new SpinnerAdapterStationName(MainActivity.this, listeSecondaireStation);
                editsearch.setAdapter(spinnerAdapterStationName);

                if (listeSecondaireStation.size() == 1) {

                }

            }
        });

        editsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listeSecondaireStation.size() > 0) {
                    //position = 0;
                    Station station = (Station) spinnerAdapterStationName.getItem(position);//listeSecondaireCentreVote.get(position);
                    String sss = station.getName();
                    Log.e("result", "\n\n" + sss + "\n\n");
                    //spinnerCentreVote.setText(sss);
                    //Toast.makeText(getApplicationContext(), c.getCentre_vote_name()+ "__" +c.getId(),Toast.LENGTH_LONG).show();
                    View v = getCurrentFocus();
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    selectedStation = station;
                    //Toast.makeText(MainActivity.thisActivity, choosenStation.toString(), Toast.LENGTH_LONG).show();
                    LatLngBounds.Builder builder = LatLngBounds.builder();

                    builder.include(station.getPosition());

                    final LatLngBounds bounds = builder.build();

                    // Animate camera to the bounds
                    try {
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
                        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(station.getPosition().latitude, station.getPosition().longitude), 14.2f));
                        editsearch.setText("");

                        /*markerdedepart = mGoogleMap.addMarker(new MarkerOptions()
                                .title("I am Here")
                                .snippet(getString(R.string.myposition))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                                .position(new LatLng(Util.latitude, Util.longitude)));

                        (new PathGetter()).execute("" + station.getPosition().latitude, "" + station.getPosition().longitude);*/


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);


        /**
         * Setup Drawer Toggle of the Toolbar
         */

        //android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null, R.string.app_name, R.string.app_name);

        mDrawerToggle.syncState();

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        ImageView manage_drawer = (ImageView) findViewById(R.id.manage_drawer);
        manage_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.save_station) {
                    //FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                    //fragmentTransaction.replace(R.id.containerView,new SentFragment()).commit();
                    //Toast.makeText(getApplicationContext(), "Menu item se connecter", Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPreferences = getSharedPreferences(Util.APP_AUTHENTICATION, MODE_PRIVATE);
                    String username = sharedPreferences.getString(Util.USERNAME, null);
                    String password = sharedPreferences.getString(Util.PASSWORD, null);
                    long connected_since = sharedPreferences.getLong(Util.CONNECTED_SINCE, -1);

                    if (username == null || password == null) {
                        Toast.makeText(getApplicationContext(), "Vous devez etre connecte ...", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                    } else if ((connected_since + 5 * 60 * 1000) > System.currentTimeMillis()) {
                        startActivity(new Intent(getApplicationContext(), SaveStation.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Vous devez etre connecte ...", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                    }
                }

                if (menuItem.getItemId() == R.id.station_list) {
                    //FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    //xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
                    //Toast.makeText(getApplicationContext(), "Menu item enregistrer un site touristique", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), StationsList.class));
                }

                if (menuItem.getItemId() == R.id.product_list_menu) {
                    startActivity(new Intent(getApplicationContext(), ProductList.class));
                }

                if (menuItem.getItemId() == R.id.service_list_menu) {
                    startActivity(new Intent(getApplicationContext(), ServicesList.class));
                }

                if (menuItem.getItemId() == R.id.nous_contacter_menu) {
                    startActivity(new Intent(getApplicationContext(), ContactUs.class));
                }

                if (menuItem.getItemId() == R.id.terms_of_use) {
                    startActivity(new Intent(getApplicationContext(), TermsOfUse.class));
                }




                return false;
            }

        });

        //place_autocomplete_fragment =  (SupportPlaceAutocompleteFragment)getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
       /* place_autocomplete_fragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.e(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e(TAG, "An error occurred: " + status);
            }
        });*/

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        initializeMap();

    }

    @Override

    public void onResume() {
        super.onResume();

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

    public void initializeMap() {
        Log.e("INITIALIZE", "initializing initializing initializinginitializing");
        if (!checkPermission()) {
            requestPermission();
        } else {
            // Get the location manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        MainActivity.this.location = location;

                        Util.latitude = location.getLatitude();
                        Util.longitude = location.getLongitude();

                        Log.e("Location", "(" + location.getLatitude() + ", " + location.getLongitude() + ")");
                    }
                }
            });

            GetAllStation getAllStation = new GetAllStation(this, loading_content);
            getAllStation.execute(Util.BASE_URL + "representations");


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestcode, String permissions[], int[] grantResults) {
        switch (requestcode) {
            case RequestPermissionCode: {
                Log.e("Nkalla0", "Nkalla1: " + (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED));
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Nkalla1", "Nkalla1: " + (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED));
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //this.onResume();
                        Log.e("Nkalla2", "Nkalla2: " + (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED));

                        // Get the location manager
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    MainActivity.this.location = location;

                                    Util.latitude = location.getLatitude();
                                    Util.longitude = location.getLongitude();

                                    Log.e("Location", "(" + location.getLatitude() + ", " + location.getLongitude() + ")");
                                }
                            }
                        });

                        GetAllStation getAllStation = new GetAllStation(this, loading_content);
                        getAllStation.execute(Util.BASE_URL + "representations");

                    }
                } else {
                    //Toast.makeText(getApplicationContext(), "Permission refusee", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), getString(R.string.you_mustgrant_permission), Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
            default: {

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
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("PERMISSION PERMISSION " , "PERMISSION PERMISSION PERMISSION PERMISSION PERMISSION PERMISSION");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);


        /*boolean booleen = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.
                permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;

        Log.e("BOOOOLEENN", " " + booleen);
        if (booleen) {
            Log.e("ENTREEEEEEE", " " + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/
        /*mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13));
        mGoogleMap.addMarker(new MarkerOptions().title("Ma Position").snippet("Je me trouve ici").
                position(myPosition));*/


        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Util.latitude, Util.longitude), 12));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<>(this, mGoogleMap);

        // Point the map's listeners at the listeners implemented by the cluster manager.

        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.

        // Set some lat/lng coordinates to start with.
        /*double lat = 51.5145160;
        double lng = -0.1270060;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            Person offsetItem = new Person(new LatLng(lat, lng), "", R.drawable.ruth);
            mClusterManager.addItem(offsetItem);
        }*/

        //initCamera();

        mClusterManager.clearItems();
        for (Station station : Util.stations) {
            mClusterManager.addItem(station);
        }
        mClusterManager.cluster();

        setListener();


    }

    private void initCamera() {
        CameraPosition position = CameraPosition.builder()
                .target( new LatLng( 40.7506, -73.9936 ) )
                .zoom( 18f )
                .bearing( 0.0f )
                .tilt( 0.0f )
                .build();

        mGoogleMap.animateCamera(
                CameraUpdateFactory.newCameraPosition( position ), null );
        mGoogleMap.setMapType( GoogleMap.MAP_TYPE_HYBRID );
    }

    @Override
    public void onLocationChanged(Location location) {

        MainActivity.this.location  = location;

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


    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class StationRenderer extends DefaultClusterRenderer<Station> {
        //private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        //private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        //private final ImageView mImageView;
        //private final ImageView mClusterImageView;
        //private final int mDimension;

        public StationRenderer() {
            super(getApplicationContext(),mGoogleMap, mClusterManager);

            //View cluster_icon = getLayoutInflater().inflate(R.layout.cluster_icon, null);
            //mClusterIconGenerator.setContentView(cluster_icon);
            //mClusterImageView = (ImageView) cluster_icon.findViewById(R.id.clustericon);

            // View marker_icon = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_icon, null);



            //View marker_icon = getLayoutInflater().inflate(R.layout.marker_icon, null);
            // mImageView = (ImageView) marker_icon.findViewById(R.id.marker_icon);
            //mDimension = 50;
            //mImageView = new ImageView(getApplicationContext());
            // mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            //mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            //int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            // mImageView.setPadding(padding, padding, padding, padding);
            //mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Station station, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            //mImageView.setImageResource(station.getProfilePhoto());
            //Bitmap icon = mIconGenerator.makeIcon();
            View marker_icon = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.marker_icon, null);
            TextView station_name = (TextView) marker_icon.findViewById(R.id.station_name);
            station_name.setText(station.getName());
            markerOptions.icon(
                    BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MainActivity.this, marker_icon))
                    /*BitmapDescriptorFactory.fromBitmap(icon)).title(station.getName()*/
            ).title(station.getName());
        }

        // Convert a view to bitmap
        public Bitmap createDrawableFromView(Context context, View view) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((AppCompatActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
            view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
            view.buildLayer();
            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);

            return bitmap;
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Station> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            /*List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (Station s : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getDrawable(s.getProfilePhoto());
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));*/
            View cluster_icon = getLayoutInflater().inflate(R.layout.cluster_icon, null);
            TextView amu_text = (TextView) cluster_icon.findViewById(R.id.amu_text);
            amu_text.setText("" + cluster.getItems().size());

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MainActivity.this, cluster_icon))).
                    title("" + cluster.getItems().size());
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }



    @Override
    public boolean onClusterClick(Cluster<Station> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String name = cluster.getItems().iterator().next().getName();
        Toast.makeText(this, cluster.getSize() + " (including " + name + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Station> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(Station item) {
        // Does nothing, but you could go into the user's profile page, for example.
        int i;
        for (i = 0; i<Util.stations.size(); i++){
            //Marker m = mListMarker.get(i);
            Station station = Util.stations.get(i);
            LatLng pos = new LatLng(station.getLatitude(), station.getLongitude());
            if (item.getPosition().latitude == station.getLatitude() &&
                    item.getPosition().longitude == station.getLongitude()){

                showStationDetailDialog(station);
                //Toast.makeText(getApplicationContext(), "marker sixe: " + mListMarker.size()+ " i: " + i , Toast.LENGTH_LONG).show();
                break;
            }
        }
        return false;
    }

    public void showStationDetailDialog(Station station){
        mStackLevel++;

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        StationDetailDialog logoutDialog = StationDetailDialog.newInstance(station, mStackLevel);
        //QRCodeDialog qrCodeDialog = new QRCodeDialog();
        //errorDialog.setErrorMessage(message);
        logoutDialog.show(ft, "dialog");

    }



    @Override
    public void onClusterItemInfoWindowClick(Station item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    //@Override
    protected void setListener() {
        /*mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(4.1074877000,
                9.7629983000), 9.5f)
        );*/

        //mClusterManager = new ClusterManager<Station>(this, mGoogleMap);
        mClusterManager.setRenderer(new MainActivity.StationRenderer());
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
        mGoogleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        //addItems();
        mClusterManager.cluster();
    }

    private void addItems() {

        List<Station> stations = new ArrayList<>();

        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("index.json")));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        String fileContent = sb.toString();

        try {
            JSONArray jsonArray = new JSONArray(fileContent);
            for (int x = 0; x<jsonArray.length(); x++){
                JSONObject jsonObject = jsonArray.getJSONObject(x);
                Station station = new Station(
                        jsonObject.getInt("id"),
                        jsonObject.getString("name"),
                        // R.drawable.logo,
                        jsonObject.getDouble("latitude"),
                        jsonObject.getDouble("longitude")
                );
                stations.add(station);
            }
            Util.stations = stations;
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Failed: " + e.getMessage() + "   " + e.getCause(), Toast.LENGTH_LONG).show();
        }

        for (Station station:stations){
            mClusterManager.addItem(station);
        }
        /*
        // http://www.flickr.com/photos/sdasmarchives/5036248203/
        mClusterManager.addItem(new Person(position(), "Walter", R.drawable.walter));

        // http://www.flickr.com/photos/usnationalarchives/4726917149/
        mClusterManager.addItem(new Person(position(), "Gran", R.drawable.gran));

        // http://www.flickr.com/photos/nypl/3111525394/
        mClusterManager.addItem(new Person(position(), "Ruth", R.drawable.ruth));

        // http://www.flickr.com/photos/smithsonian/2887433330/
        mClusterManager.addItem(new Person(position(), "Stefan", R.drawable.stefan));

        // http://www.flickr.com/photos/library_of_congress/2179915182/
        mClusterManager.addItem(new Person(position(), "Mechanic", R.drawable.mechanic));

        // http://www.flickr.com/photos/nationalmediamuseum/7893552556/
        mClusterManager.addItem(new Person(position(), "Yeats", R.drawable.yeats));

        // http://www.flickr.com/photos/sdasmarchives/5036231225/
        mClusterManager.addItem(new Person(position(), "John", R.drawable.john));

        // http://www.flickr.com/photos/anmm_thecommons/7694202096/
        mClusterManager.addItem(new Person(position(), "Trevor the Turtle", R.drawable.turtle));

        // http://www.flickr.com/photos/usnationalarchives/4726892651/
        mClusterManager.addItem(new Person(position(), "Teach", R.drawable.teacher));*/
    }

    private class SpinnerAdapterStation extends ArrayAdapter {

        private Context context;
        private  List<Station> values;

        public SpinnerAdapterStation(Context context, List<Station> values) {
            super(context, R.layout.custom_item_spinner_station, values);
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
            return values.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater li=(LayoutInflater) context.
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=li.inflate(R.layout.custom_item_spinner_station, null);
            }


            TextView station_name = (TextView)convertView.findViewById(R.id.station_name);
            station_name.setText("" + values.get(position).getName());

            return convertView;
        }
    }


    private class GetAllStation extends AsyncTask<String, Integer, String> {
        private LinearLayout loadingContent;
        private Context context;

        public GetAllStation(Context context, LinearLayout loadingContent) {
            this.context = context;
            this.loadingContent = loadingContent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.loadingContent.setVisibility(View.VISIBLE);
            //mClusterManager.clearItems();
            //Log.e("geting agences", "getting agences");
        }

        @Override
        protected String doInBackground(String... strings) {
            String resultat = "";
            String url_string = strings[0];
            URL url = null;
            try {
                url = new URL(url_string);
                HttpURLConnection urlConnection;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);

                    InputStream in = urlConnection.getInputStream();
                    BufferedReader br = null;
                    StringBuilder sb = new StringBuilder();
                    String line;
                    try {
                        br = new BufferedReader(new InputStreamReader(in));
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                    } catch (IOException e) {
                        return e.getMessage();
                    } finally {
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e) {
                                return e.getMessage();
                            }
                        }
                    }
                    in.close();
                    //os.close();
                    resultat = sb.toString();

                } catch (IOException e) {
                    return e.getMessage();
                }
            } catch (MalformedURLException e) {
                return e.getMessage();
            }

            return resultat;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (loadingContent.getVisibility() == View.VISIBLE) {
                loadingContent.setVisibility(View.GONE);
            }

            try {
                Util.stations = new ArrayList<Station>();
                JSONArray jsonArray = new JSONArray(result);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    Station station = new Station(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getDouble("latitude"),
                            jsonObject.getDouble("longitude")
                    );
                    Util.stations.add(station);
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Failled to get data", Toast.LENGTH_LONG).show();
            }

            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.stations_map);
            mapFragment.getMapAsync(MainActivity.this);


            Log.e("RESULT RESULT", result);
        }
    }

}
