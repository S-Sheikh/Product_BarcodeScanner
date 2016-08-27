package za.ac.cut.productscanner.data;

import android.provider.BaseColumns;

/**
 * Created by hodielisrael on 2016/08/27.
 */
public class ProductsContract {

    private ProductsContract() {}

    /* Inner class that defines the table contents */
    public static final class ProductEntry implements BaseColumns {
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String TABLE_NAME = "products";
    }
}
