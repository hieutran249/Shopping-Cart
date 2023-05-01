package hanu.a2_2001040078;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hanu.a2_2001040078.adapters.CartItemAdapter;
import hanu.a2_2001040078.db.CartItemManager;
import hanu.a2_2001040078.models.CartItem;

public class CartActivity extends AppCompatActivity implements CartItemAdapter.OnChangeListener {
    public final static int CART_ITEM_ADDED = 1;
    CartItemManager cartItemManager;
    List<CartItem> cartItemList;
    CartItemAdapter cartItemAdapter;
    RecyclerView rvCartItem;
    ImageButton mainActivityBtn;
    TextView tvTotal;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        // dataset
        cartItemManager = cartItemManager.getInstance(this);
        cartItemList = cartItemManager.all();
        // adapter
        cartItemAdapter = new CartItemAdapter(cartItemList, getApplicationContext(), this);
        // recycler view
        rvCartItem = findViewById(R.id.rvCartItem);
        rvCartItem.setAdapter(cartItemAdapter);
        rvCartItem.setLayoutManager(new LinearLayoutManager(this));
        // cal total price
        tvTotal = findViewById(R.id.tv_total);
        tvTotal.setText(formatCurrency(calTotal(cartItemManager)));

        // navigate to MainActivity
        mainActivityBtn = findViewById(R.id.btn_main_activity);
        mainActivityBtn.setOnClickListener(view -> {
            Intent intent = new Intent(CartActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CART_ITEM_ADDED) {
            cartItemList.clear();
            cartItemList.addAll(cartItemManager.all());
            cartItemAdapter.notifyDataSetChanged();
        }
    }

    public int calTotal(CartItemManager cartItemManager) {
        int total = 0;
        for (CartItem cartItem : cartItemManager.all()) {
            total += cartItem.getQuantity() * cartItem.getUnitPrice();
        }
        return total;
    }

    @Override
    public void changeWhenClicked(CartItemManager cartItemManager) {
        this.tvTotal.setText(formatCurrency(calTotal(cartItemManager)));
    }
    private String formatCurrency(int price) {
        return "₫ " + price;
    }
}
