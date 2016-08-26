package za.ac.cut.productscanner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AddNewProductActivity extends AppCompatActivity {

    EditText et_scan, et_title, et_description;
    private DBHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydb = new DBHelper(this);
        setContentView(R.layout.activity_add_new_product);
        getSupportActionBar().setTitle("Add Product");
        et_scan =(EditText)findViewById(R.id.et_scan);
        et_title = (EditText)findViewById(R.id.et_title);
        et_description = (EditText)findViewById(R.id.et_description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_option, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void onClick_et_scan(View v){
        scanNow(v);

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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName(); // if you need the format

            if (scanContent != null && !scanContent.equals("")) {
                et_scan.setText(scanContent);
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_new_product:
                if (connectionAvailable()) {
                    if (et_scan.getText().toString().equals("")
                            || et_title.getText().toString().equals("")
                            || et_description.toString().equals("")) {
                        Toast.makeText(AddNewProductActivity.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                    } else {
                        Product product = new Product();
                        product.setProductCode(et_scan.getText().toString().trim());
                        product.setProductTitle(et_title.getText().toString().trim());
                        product.setProductDesc(et_description.getText().toString().trim());

                        Backendless.Persistence.save(product, new AsyncCallback<Product>() {
                            @Override
                            public void handleResponse(Product product) {
                                Toast.makeText(AddNewProductActivity.this, "Product Successfully Saved!", Toast.LENGTH_SHORT).show();
                                if (mydb.insertProduct(et_scan.getText().toString(), et_title.getText().toString(), et_description.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "not done", Toast.LENGTH_LONG).show();
                                }
                                AddNewProductActivity.this.finish();

                            }

                            @Override
                            public void handleFault(BackendlessFault backendlessFault) {
                                Toast.makeText(AddNewProductActivity.this, "Error: " + backendlessFault.getMessage()
                                        , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(AddNewProductActivity.this, "No Internet Connection, Please Connect First"
                            , Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
