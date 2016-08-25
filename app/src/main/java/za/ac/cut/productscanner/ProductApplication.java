package za.ac.cut.productscanner;

import android.app.Application;

import com.backendless.Backendless;

/**
 * Created by Shahbaaz Sheikh on 24/08/2016.
 */
public class ProductApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String appVersion = "v1";
        Backendless.initApp(ProductApplication.this, "224DEF34-4407-B1F5-FF66-D2D273486A00",
                "DEFF6443-CF52-2597-FF61-5301E7745F00", appVersion);
    }
}
