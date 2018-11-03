package cm.siplus2018.bocom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cm.siplus2018.bocom.adapter.ProductAdapter;
import cm.siplus2018.bocom.adapter.SpinnerAdapterProductName;
import cm.siplus2018.bocom.adapter.SpinnerAdapterStationName;
import cm.siplus2018.bocom.model.Product;
import cm.siplus2018.bocom.utils.Util;
import cm.siplus2018.bocom.view.CustomAutoCompleteView;

public class ProductList extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView product_list;
    private ProductAdapter productAdapter;
    private List<Product> products;

    private SpinnerAdapterProductName spinnerAdapterProductName;
    private List<Product> listeSecondaireProduct;
    private Product selectedProduct;
    private CustomAutoCompleteView editsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.product_list));

        product_list = findViewById(R.id.product_list);

        products = Util.getProducts();
        productAdapter = new ProductAdapter(this, products);
        product_list.setAdapter(productAdapter);

        product_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedProduct = products.get(position);
                Intent intent = new Intent(getApplicationContext(), StationListByProduct.class);
                intent.putExtra("productid", selectedProduct.getProductid());
                intent.putExtra("name", selectedProduct.getName());
                intent.putExtra("description", selectedProduct.getDescription());
                Log.e("result-1", "\n\n" + selectedProduct.getName() + "\n\n");
                startActivity(intent);
            }
        });

        spinnerAdapterProductName = new SpinnerAdapterProductName(this, products);
        spinnerAdapterProductName.setDropDownViewResource(R.layout.custom_item_spinner_product_name);

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
                spinnerAdapterProductName.notifyDataSetChanged();

                listeSecondaireProduct = new ArrayList<Product>();
                String userInput = s.toString();
                for (Product product : products) {

                    if (product.getName().toUpperCase().contains(s.toString().toUpperCase()) ||
                            product.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        listeSecondaireProduct.add(product);
                    }
                }
                if (listeSecondaireProduct.size() > 0)
                    selectedProduct = listeSecondaireProduct.get(0);
                else selectedProduct = null;
                spinnerAdapterProductName =
                        new SpinnerAdapterProductName(ProductList.this, listeSecondaireProduct);
                editsearch.setAdapter(spinnerAdapterProductName);

                if (listeSecondaireProduct.size() == 1) {
                    Intent intent = new Intent(getApplicationContext(), StationListByProduct.class);
                    intent.putExtra("productid", selectedProduct.getProductid());
                    intent.putExtra("name", selectedProduct.getName());
                    intent.putExtra("description", selectedProduct.getDescription());
                    Log.e("result0", "\n\n" + selectedProduct.getName() + "\n\n");
                    startActivity(intent);
                }

            }
        });

        editsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listeSecondaireProduct.size() > 0) {
                    //position = 0;
                    Product product = (Product) spinnerAdapterProductName.getItem(position);//listeSecondaireCentreVote.get(position);
                    String sss = product.getName();
                    Log.e("result", "\n\n" + sss + "\n\n");
                    //spinnerCentreVote.setText(sss);
                    //Toast.makeText(getApplicationContext(), c.getCentre_vote_name()+ "__" +c.getId(),Toast.LENGTH_LONG).show();
                    View v = getCurrentFocus();
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    selectedProduct = product;
                    Intent intent = new Intent(getApplicationContext(), StationListByProduct.class);
                    intent.putExtra("productid", selectedProduct.getProductid());
                    intent.putExtra("name", selectedProduct.getName());
                    intent.putExtra("description", selectedProduct.getDescription());
                    startActivity(intent);

                }
            }
        });

        editsearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("SELECTED", products.get(position).getName());
                Product product = (Product) spinnerAdapterProductName.getItem(position);//listeSecondaireCentreVote.get(position);
                selectedProduct = product;
                Intent intent = new Intent(getApplicationContext(), StationListByProduct.class);
                intent.putExtra("productid", selectedProduct.getProductid());
                intent.putExtra("name", selectedProduct.getName());
                intent.putExtra("description", selectedProduct.getDescription());
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}
