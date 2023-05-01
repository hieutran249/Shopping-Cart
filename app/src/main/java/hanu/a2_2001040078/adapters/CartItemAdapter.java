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

import hanu.a2_2001040078.R;
import hanu.a2_2001040078.db.CartItemManager;
import hanu.a2_2001040078.models.CartItem;
import hanu.a2_2001040078.models.Constants;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemHolder> {
    List<CartItem> cartItemList;
    Context context;
    OnChangeListener onChangeListener;

    public CartItemAdapter(List<CartItem> cartItemList, Context context, OnChangeListener onChangeListener) {
        this.cartItemList = cartItemList;
        this.context = context;
        this.onChangeListener = onChangeListener;
    }

    @NonNull
    @Override
    public CartItemAdapter.CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemHolder(LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false), onChangeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.CartItemHolder holder, int position) {
        CartItem cartItem = this.cartItemList.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public interface OnChangeListener {
        void changeWhenClicked(CartItemManager cartItemManager);
    }

    public class CartItemHolder extends RecyclerView.ViewHolder {
        CartItem cartItem;
        OnChangeListener onChangeListener;
        ImageView ivThumbnail;
        TextView tvName;
        TextView tvUnitPrice;
        TextView tvQuantity;
        TextView tvSumPrice;
        TextView tvTotal;
        ImageButton increaseBtn;
        ImageButton decreaseBtn;
        CartItemManager cartItemManager;

        public void bind(CartItem cartItem) {
            this.cartItem = cartItem;

            increaseBtn.setOnClickListener(view -> {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItemManager.update(cartItem.getId(), cartItem.getQuantity());
                tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
                tvUnitPrice.setText(formatCurrency(cartItem.getUnitPrice()));
                notifyDataSetChanged();
                onChangeListener.changeWhenClicked(cartItemManager);
            });

            decreaseBtn.setOnClickListener(view -> {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemManager.update(cartItem.getId(), cartItem.getQuantity());
                tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
                tvUnitPrice.setText(formatCurrency(cartItem.getUnitPrice()));
                if (cartItem.getQuantity() == 0) {
                    cartItemManager.delete(cartItem.getId());
                    cartItemList.remove(cartItem);
                }
                notifyDataSetChanged();
                onChangeListener.changeWhenClicked(cartItemManager);
            });

            this.tvName.setText(cartItem.getName());
            this.tvUnitPrice.setText(formatCurrency(cartItem.getUnitPrice()));
            this.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
            this.tvSumPrice.setText(formatCurrency(cartItem.getUnitPrice() * cartItem.getQuantity()));

            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            Constants.executor.execute(() -> {
                Bitmap bitmap = downloadImage(cartItem.getThumbnail());
                if (bitmap != null) {
                    handler.post(() -> ivThumbnail.setImageBitmap(bitmap));
                }
            });
        }

        public CartItemHolder(@NonNull View itemView , OnChangeListener onChangeListener) {
            super(itemView);
            this.onChangeListener = onChangeListener;
            this.ivThumbnail = itemView.findViewById(R.id.iv_cartThumbnail);
            this.tvName = itemView.findViewById(R.id.tv_cartName);
            this.tvUnitPrice = itemView.findViewById(R.id.tv_cartUnitPrice);
            this.tvQuantity = itemView.findViewById(R.id.tv_quantity);
            this.tvSumPrice = itemView.findViewById(R.id.tv_sumPrice);
            this.tvTotal = itemView.findViewById(R.id.tv_total);
            this.increaseBtn = itemView.findViewById(R.id.btn_increaseItem);
            this.decreaseBtn = itemView.findViewById(R.id.btn_decreaseItem);
            cartItemManager = cartItemManager.getInstance(itemView.getContext());
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
