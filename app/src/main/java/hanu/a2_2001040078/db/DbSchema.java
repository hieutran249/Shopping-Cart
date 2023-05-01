package hanu.a2_2001040078.db;

public class DbSchema {
    public final class CartItemTalbe {
        public final static String NAME = "cart_items";

        public final class Cols {
            public static final String ID = "id";
            public static final String PRODUCT_ID = "productId";
            public static final String NAME = "name";
            public static final String THUMBNAIL = "thumbnail";
            public static final String CATEGORY = "category";
            public static final String UNIT_PRICE = "unitPrice";
            public static final String QUANTITY = "quantity";
        }
    }
}
