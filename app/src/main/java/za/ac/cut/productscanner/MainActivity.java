package za.ac.cut.productscanner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;



public class MainActivity extends AppCompatActivity {

    TextView tv_title, tv_description, tv_code;

    DBHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String appVersion = "v1";
        Backendless.initApp(MainActivity.this, "224DEF34-4407-B1F5-FF66-D2D273486A00",
                "DEFF6443-CF52-2597-FF61-5301E7745F00", appVersion);
        mydb = new DBHelper(this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_code = (TextView) findViewById(R.id.tv_code);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_products:
                startActivity(new Intent(MainActivity.this,AddNewProductActivity.class));
                return true;
            case R.id.sync_products:

                return true;
            default:return super.onOptionsItemSelected(item);
        }
    }

    public void scanNow(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan the barcode or back to exit"); // text at bottom when scanner runs
        integrator.setResultDisplayDuration(0);
        integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();

    }

    public void btnScan(View v) {
        scanNow(v);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        Boolean flag = true;


        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName(); // if you need the format

            if (scanContent != null && !scanContent.equals("")) {
                DBHelper mydb = new DBHelper(getApplicationContext());
                SQLiteDatabase db=mydb.getReadableDatabase();
                Cursor rs = db.rawQuery("SELECT * From PRODUCTS WHERE CODE = '" + scanContent.trim() + "'",null);
                rs.moveToPosition(0);

                    do
                    {
                        if (rs.getString(rs.getColumnIndex(DBHelper.COLUMN_CODE)).equals(scanContent.toString())) {
                        tv_title.setText(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_TITLE)));
                        tv_description.setText(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_DESCRIPTION)));
                        tv_code.setText(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_CODE)));
                        flag = false;
                    }
                    }while (rs.moveToNext());

//
                rs.close();
                if(flag){
                    Toast.makeText(MainActivity.this, "No Such Product Exists in Database!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
