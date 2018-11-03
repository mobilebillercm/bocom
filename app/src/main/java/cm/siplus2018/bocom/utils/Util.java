package cm.siplus2018.bocom.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cm.siplus2018.bocom.model.Product;
import cm.siplus2018.bocom.model.Service;
import cm.siplus2018.bocom.model.Station;


/**
 * Created by Ange_Douki on 07/01/2017.
 */

public class Util {
    public static double latitude = 0.0, longitude = 0.0;
    public static List<Station> stations;
    public static final String BASE_URL = "http://188.213.171.38/elecam/public/api/";
    public static final int RequestPermissionCode = 1;

    public static final String APP_AUTHENTICATION                = "APP_AUTHENTICATION";
    public static final String APP_CONFIGURAION                  = "APP_CONFIGURAION";
    public static final String APP_OTHER_CONFIGURAION            = "APP_OTHER_CONFIGURAION";
    public static final String EMAIL                             = "email";
    public static  final String USERNAME                         = "username";
    public static  final String PASSWORD                         = "password";
    public static final String CONNECTED_SINCE                   = "connected_since";
    public static final String REGEX_EMAIL                       =  "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$" ;
    public static final String BOCOM_PRIVACY_POLICY              = "http://idea-cm.club/regles-pridesoft-mobile.html";


    public static List<Product> getProducts(){
        List<Product> products = new ArrayList<>();
        for (int i = 0; i<20; i++){
            Product product = new Product(i+1, UUID.randomUUID().toString(), "Product Name " + (i+1), "A product description " + (i+1));
            products.add(product);
        }
        return  products;
    }

    public static List<Service> getServices(){
        List<Service> services = new ArrayList<>();
        for (int i = 0; i<20; i++){
            Service service = new Service(i+1, UUID.randomUUID().toString(), "Service Name " + (i+1), "A Service description " + (i+1));
            services.add(service);
        }
        return  services;
    }

}
