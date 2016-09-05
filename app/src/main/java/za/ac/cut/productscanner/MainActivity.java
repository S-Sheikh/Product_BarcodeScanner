package za.ac.cut.productscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {

    TextView tv_title, tv_description, tv_code;
    EditText et_TEMP;
    private DBHelper mydb;
    boolean updateFlag;
    private ArrayList<Product> productsLocalList;
    private ArrayList<Product> productsBackEnd;
    ActionBar actionBar;
    boolean[] update = {false};

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
        et_TEMP = (EditText) findViewById(R.id.et_TEMP);
        Button btn_TEMP = (Button) findViewById(R.id.btn_TEMP);
        updateFlag = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_buttons, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_products:
                startActivity(new Intent(MainActivity.this, Add_New_Product.class));
                return true;
            case R.id.sync_products:
                if (connectionAvailable()) {
//                    update[0] = false;
//                    String whereClause;
//                    BackendlessDataQuery dataQuery = new BackendlessDataQuery();
//                    productsLocalList = mydb.getEverything();
//                    if(productsLocalList.size() == 0){
//                        productsLocalList.add(new Product("TEMP","TEMP","TEMP"));
//                    }
//                    for(Product product : productsLocalList){
//                        whereClause = "productCode = '"  + product.getProductCode() + "'";
//                        dataQuery.setWhereClause(whereClause);
//                        sync(update,dataQuery);
//                    }
                   // getBackendData();
                    try {
                        fetchingFirstPageAsync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Access, Please Check you Connection", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                //tvScanned.setText("Scanned code: " + scanContent);
                Cursor rs = mydb.getData(Integer.parseInt(scanContent));
                rs.moveToFirst();
                while (rs.moveToNext()) {
                    if (rs.getString(rs.getColumnIndex(DBHelper.COLUMN_CODE)) == scanContent.toString()) {
                        tv_title.setText(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_TITLE)));
                        tv_description.setText(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_DESCRIPTION)));
                        tv_code.setText(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_CODE)));
                        break;
                    } else {
                        rs.moveToNext();
                    }
                }
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void btn_TEMP(View v) {
        boolean flag = true;
        Cursor rs = mydb.getData(Integer.parseInt(et_TEMP.getText().toString().trim()));
        while (rs.moveToNext()) {
            //SQL QUERIES ARE FOR PLEBS
            if (et_TEMP.getText().toString().trim().equals(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_CODE)))) {
                tv_title.setText(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_TITLE)));
                tv_description.setText(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_DESCRIPTION)));
                tv_code.setText(rs.getString(rs.getColumnIndex(DBHelper.COLUMN_CODE)));
                flag = false;
            }
        }
        if (flag) {
            Toast.makeText(MainActivity.this, "No Such Product Exists in Database!", Toast.LENGTH_SHORT).show();
        }
    }

    private static BackendlessCollection<Product> basicDataLoad() {
        BackendlessCollection<Product> products = Backendless.Data.of(Product.class).find();
        return products;
    }

    private boolean connectionAvailable() {
        boolean connected = false;

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { //Connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                connected = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                connected = true;
            }
        } else {
            connected = false;
        }
        return connected;
    }
    public boolean[] sync(final boolean[] booleen, BackendlessDataQuery dataQuery){

            Backendless.Persistence.of(Product.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Product>>() {
                @Override
                public void handleResponse(BackendlessCollection<Product> productBackendlessCollection) {
                    Toast.makeText(MainActivity.this, "FOUND Match", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleFault(BackendlessFault backendlessFault) { //When there is a difference detected , drop table
                    Toast.makeText(MainActivity.this, "DIFFERENCE !!!!!", Toast.LENGTH_SHORT).show();
                    booleen[0] = true;
                    if(update[0]){
                        SQLiteDatabase sqlitedatabase = mydb.getReadableDatabase();
                        mydb.onUpgrade(sqlitedatabase,1,2);
                        Toast.makeText(MainActivity.this, "Dropped", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        return booleen;
    }

    private void fetchingFirstPageAsync() throws InterruptedException
    {
        Backendless.Data.of( Product.class ).find( new AsyncCallback<BackendlessCollection<Product>>()
        {
            @Override
            public void handleResponse( BackendlessCollection<Product> users )
            {
                Iterator<Product> userIterator = users.getCurrentPage().iterator();
                while( userIterator.hasNext() )
                {
                    Product user = userIterator.next();
                    Product product = new Product(user.getProductCode());
                }
            }
            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                //System.out.println( "Server reported an error - " + backendlessFault.getMessage() );
            }
        } );
    }

    private void fetchingFirstPage() {
        long startTime = System.currentTimeMillis();
        BackendlessCollection<Product> restaurants = Backendless.Data.of(Product.class).find();
        Iterator<Product> iterator = restaurants.getCurrentPage().iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            productsBackEnd.add(new Product(product.getProductCode(), product.getProductTitle(), product.getProductDesc()));
        }
        System.out.println("Total time (ms) - " + (System.currentTimeMillis() - startTime));
    }
}