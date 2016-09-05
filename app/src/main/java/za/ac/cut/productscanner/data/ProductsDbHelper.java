package za.ac.cut.productscanner.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static za.ac.cut.productscanner.data.ProductsContract.*;

/**
 * Created by Shahbaaz Sheikh on 24/08/2016.
 */
public class ProductsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "productscanner.db";
    public static final int DATABASE_VERSION = 1;


    public ProductsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_PRODUCTS_TABLE =
                "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                        ProductEntry.COLUMN_CODE + " TEXT PRIMARY KEY, " +
                        ProductEntry.COLUMN_TITLE + " TEXT," +
                        ProductEntry.COLUMN_DESCRIPTION + " TEXT)";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + ProductEntry.TABLE_NAME);
        onCreate(db);
    }

    public boolean insertProduct(String code, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_CODE, code);
        contentValues.put(ProductEntry.COLUMN_TITLE, title);
        contentValues.put(ProductEntry.COLUMN_DESCRIPTION, description);
        db.insert(ProductEntry.TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + ProductEntry.TABLE_NAME + " WHERE code=" + id, null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ProductEntry.TABLE_NAME);
        return numRows;
    }

    public boolean updateProduct(String code, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_CODE, code);
        contentValues.put(ProductEntry.COLUMN_TITLE, title);
        contentValues.put(ProductEntry.COLUMN_DESCRIPTION, description);
        db.update(ProductEntry.TABLE_NAME, contentValues, ProductEntry.COLUMN_CODE + " = ? ", new String[]{code});
        return true;
    }

    public Integer deleteProduct(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ProductEntry.TABLE_NAME, ProductEntry.COLUMN_CODE + " = ? ", new String[]{code});
    }

    public ArrayList<String> getAllProducts() {
        ArrayList<String> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + ProductEntry.TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(ProductEntry.TABLE_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
