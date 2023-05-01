package hanu.a2_2001040078.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import hanu.a2_2001040078.R;
import hanu.a2_2001040078.db.CartItemManager;
import hanu.a2_2001040078.models.CartItem;
import hanu.a2_2001040078.models.Constants;
import hanu.a2_2001040078.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<Product> products;
    Context context;

    public ProductAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductHolder holder, int position) {
        Product product = this.products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        Product product;
        ImageView ivThumbnail;
        TextView tvName;
        TextView tvUnitPrice;
        ImageButton addToCartBtn;

        public void bind(Product product) {
            this.product = product;
            this.tvName.setText(product.getName());
            this.tvUnitPrice.setText(formatCurrency(product.getUnitPrice()));
            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            Constants.executor.execute(() -> {
                Bitmap bitmap = downloadImage(product.getThumbnail());
                if (bitmap != null) {
                    handler.post(() -> ivThumbnail.setImageBitmap(bitmap));
                }
            });
            this.addToCartBtn.setOnClickListener(view -> {
                CartItemManager cartItemManager = CartItemManager.getInstance(view.getContext());
                boolean exist = false;
                for (CartItem cartItem : cartItemManager.all()) {
                    if (Objects.equals(cartItem.getProductId(), product.getId())) {
                        cartItem.setQuantity(cartItem.getQuantity() + 1);
                        exist = true;
                        cartItemManager.update(cartItem.getId(), cartItem.getQuantity());
                        break;
                    }
                }
                if (!exist) {
                    cartItemManager.add(new CartItem(product.getId(), product.getName(), product.getThumbnail(), product.getCategory(), product.getUnitPrice(), 1));
                }
            });
        }

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            this.ivThumbnail = itemView.findViewById(R.id.iv_productThumbnail);
            this.tvName = itemView.findViewById(R.id.tv_productName);
            this.tvUnitPrice = itemView.findViewById(R.id.tv_productUnitPrice);
            this.addToCartBtn = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }

    private Bitmap downloadImage(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatCurrency(int price) {
        return "₫ " + price;
    }
}
