package hanu.a2_2001040078.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_2001040078.models.CartItem;

public class CartItemCursorWrapper extends CursorWrapper {
    public CartItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CartItem getCartItem() {
        Long id = getLong(getColumnIndex(DbSchema.CartItemTalbe.Cols.ID));
        Long productId = getLong(getColumnIndex(DbSchema.CartItemTalbe.Cols.PRODUCT_ID));
        String name = getString(getColumnIndex(DbSchema.CartItemTalbe.Cols.NAME));
        String thumbnail = getString(getColumnIndex(DbSchema.CartItemTalbe.Cols.THUMBNAIL));
        String category = getString(getColumnIndex(DbSchema.CartItemTalbe.Cols.CATEGORY));
        int unitPrice = getInt(getColumnIndex(DbSchema.CartItemTalbe.Cols.UNIT_PRICE));
        int quantity = getInt(getColumnIndex(DbSchema.CartItemTalbe.Cols.QUANTITY));

        return new CartItem(id, productId, name, thumbnail, category, unitPrice, quantity);
    }

    public List<CartItem> getCartItemList() {
        List<CartItem> cartItemList = new ArrayList<>();

        moveToFirst();
        while (!isAfterLast()) {
            CartItem friend = getCartItem();
            cartItemList.add(friend);
            moveToNext();
        }
        return cartItemList;
    }
}
