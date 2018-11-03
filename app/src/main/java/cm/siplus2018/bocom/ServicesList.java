package cm.siplus2018.bocom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.List;

import cm.siplus2018.bocom.adapter.ProductAdapter;
import cm.siplus2018.bocom.adapter.ServiceAdapter;
import cm.siplus2018.bocom.model.Product;
import cm.siplus2018.bocom.model.Service;
import cm.siplus2018.bocom.utils.Util;

public class ServicesList extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView service_list;
    private ServiceAdapter serviceAdapter;
    private List<Service> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.product_list));

        service_list = findViewById(R.id.service_list);

        services = Util.getServices();
        serviceAdapter = new ServiceAdapter(this, services);
        service_list.setAdapter(serviceAdapter);
    }
}
