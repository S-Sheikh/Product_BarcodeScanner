package za.ac.cut.productscanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahbaaz Sheikh on 24/08/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ProductsDB.db";
    public static final String DATABASE_TABLE = "Products";
    public static final int DATABASE_VERSION = 1;
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "create table products " +
                        "(code text primary key, title text,description text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS products");
        onCreate(sqLiteDatabase);
    }

    public boolean insertProduct(String code, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("title", title);
        contentValues.put("description", description);
        db.insert("Products", null, contentValues);
        return true;
    }

    public Cursor getData(int code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Products where code=" + code + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DATABASE_TABLE);
        return numRows;
    }

    public boolean updateContact(String code, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("title", title);
        contentValues.put("description", description);
        db.update("Products", contentValues, "code = ? ", new String[]{code});
        return true;
    }

    public Integer deleteContact(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Products", "code = ? ", new String[]{code});
    }

    public ArrayList<String> getAllProducts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Products", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(DATABASE_TABLE)));
            res.moveToNext();
        }
        return array_list;
    }
    public ArrayList<Product> getEverything(){
        ArrayList<Product> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Products", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(new Product(res.getString(res.getColumnIndex(COLUMN_CODE)),res.getString(res.getColumnIndex(COLUMN_TITLE)),res.getString(res.getColumnIndex(COLUMN_DESCRIPTION))));
            res.moveToNext();
        }
        return array_list;
    }
}
