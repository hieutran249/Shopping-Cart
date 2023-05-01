package hanu.a2_2001040078.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_2001040078.models.CartItem;

public class CartItemManager {
    // singleton
    private static CartItemManager instance;
    private static final String INSERT_STMT =
            "INSERT INTO " + DbSchema.CartItemTalbe.NAME + "(productId, name, thumbnail, category, unitPrice, quantity) VALUES (?, ?, ?, ?, ?, ?)";
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    public static CartItemManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartItemManager(context);
        }
        return instance;
    }
    private CartItemManager(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    public List<CartItem> all() {
        String sql = "SELECT * FROM cart_items";
        Cursor cursor = db.rawQuery(sql, null);
        CartItemCursorWrapper cursorWrapper = new CartItemCursorWrapper(cursor);
        return cursorWrapper.getCartItemList();
    }

    /**
     * @modifies cart_item
     */
    public boolean add(CartItem cartItem) {
        SQLiteStatement statement = db.compileStatement(INSERT_STMT);
        statement.bindLong(1, cartItem.getProductId());
        statement.bindString(2, cartItem.getName());
        statement.bindString(3, cartItem.getThumbnail());
        statement.bindString(4, cartItem.getCategory());
        statement.bindLong(5, cartItem.getUnitPrice());
        statement.bindLong(6, cartItem.getQuantity());

        long id = statement.executeInsert();
        if (id > 0) {
            cartItem.setId(id);
            return true;
        }
        return false;
    }

    public boolean delete(Long id) {
        int result = db.delete(DbSchema.CartItemTalbe.NAME, "id = ?", new String[]{ id+"" });
        return result > 0;
    }

    public void update(Long id, int quantity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("quantity", quantity);
        db.update(DbSchema.CartItemTalbe.NAME, contentValues, "id = ?", new String[]{ id+"" });
    }
}
