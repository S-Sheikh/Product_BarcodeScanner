package za.ac.cut.productscanner;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;

public class Add_New_Product extends AppCompatActivity {

    EditText et_scan, et_title, et_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__product);
        getSupportActionBar().setTitle("Add Product");
        et_scan =(EditText)findViewById(R.id.et_scan);
        et_title = (EditText)findViewById(R.id.et_title);
        et_description = (EditText)findViewById(R.id.et_description);


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
}
