package hanu.a2_2001040078.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "cart.db";
    private static final int DB_VERSION = 1;
    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table: cart_items
        db.execSQL("CREATE TABLE cart_items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "productId INTEGER NOT NULL, " +
                "name TEXT NOT NULL, " +
                "thumbnail TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "unitPrice INTEGER NOT NULL, " +
                "quantity INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("My CART", "My Cart Items: upgrading DB; dropping/recreating tables.");
        // drop old tables
        db.execSQL("DROP TABLE cart_items");
        // other tables here
        // create new tables
        onCreate(db);
    }
}
