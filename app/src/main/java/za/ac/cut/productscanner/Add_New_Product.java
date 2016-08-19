package za.ac.cut.productscanner;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Add_New_Product extends AppCompatActivity {

    ActionBar actionaBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__product);
        actionaBar.setTitle("Add Product");
    }
}
