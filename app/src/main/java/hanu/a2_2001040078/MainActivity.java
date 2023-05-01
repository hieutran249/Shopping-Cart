package hanu.a2_2001040078;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hanu.a2_2001040078.adapters.ProductAdapter;
import hanu.a2_2001040078.models.Constants;
import hanu.a2_2001040078.models.Product;

public class MainActivity extends AppCompatActivity {
    List<Product> products = new ArrayList<>();
    ProductAdapter productAdapter;
    RecyclerView rvProduct;
    ImageButton shoppingCartBtn;
    EditText searchText;
    ImageButton searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvProduct = findViewById(R.id.rvProduct);
        String url = "https://hanu-congnv.github.io/mpr-cart-api/products.json";

        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        Constants.executor.execute(() -> {
            String json = loadJSON(url);

            handler.post(() -> {
                if (json == null) {
                    Toast.makeText(MainActivity.this, "ERROR getting data", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(json);
                        for (int i=0; i< jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Long id = jsonObject.getLong("id");
                            String name = jsonObject.getString("name");
                            String thumbnail = jsonObject.getString("thumbnail");
                            int unitPrice = jsonObject.getInt("unitPrice");
                            String category = jsonObject.getString("category");

                            products.add(new Product(id, name, thumbnail, category, unitPrice));
                        }
                        productAdapter = new ProductAdapter(products, getApplicationContext());
                        rvProduct.setAdapter(productAdapter);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });

        // handle search
        searchText = findViewById(R.id.search_text);
        searchBtn = findViewById(R.id.btn_search);
        searchProduct(searchText, searchBtn);

        // navigate to CartActivity
        shoppingCartBtn = findViewById(R.id.btn_shopping_cart);
        shoppingCartBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    public String loadJSON(String link) {
        URL url;
        HttpURLConnection urlConnection;
        try {
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            StringBuilder result = new StringBuilder();
            String line;
            while(sc.hasNextLine()) {
                line = sc.nextLine();
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void searchProduct(EditText editText, ImageButton imageButton) {
        imageButton.setOnClickListener(view -> {
            String query = String.valueOf(editText.getText()).toLowerCase();
            List<Product> results = new ArrayList<>();
            for (Product product: products) {
                if (product.getName().toLowerCase().contains(query)) {
                    results.add(product);
                }
            }
            productAdapter = new ProductAdapter(results, getApplicationContext());
            rvProduct.setAdapter(productAdapter);
        });
    }
}