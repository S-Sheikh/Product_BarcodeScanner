package za.ac.cut.productscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.backendless.Backendless;
import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String appVersion = "v1";
        Backendless.initApp(MainActivity.this, "224DEF34-4407-B1F5-FF66-D2D273486A00",
                "DEFF6443-CF52-2597-FF61-5301E7745F00", appVersion);
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
        switch (item.getItemId()){
            case R.id.add_products:
                startActivity(new Intent(MainActivity.this,Add_New_Product.class));
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

}
