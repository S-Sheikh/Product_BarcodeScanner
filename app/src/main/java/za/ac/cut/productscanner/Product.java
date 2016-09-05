package za.ac.cut.productscanner;

/**
 * Created by Shahbaaz Sheikh on 24/08/2016.
 */
public class Product {
    private String productCode,productTitle,productDesc, objectId;

    public Product() {}

    public Product(String productCode, String productTitle, String productDesc) {
        this.productCode = productCode;
        this.productTitle = productTitle;
        this.productDesc = productDesc;
    }


    public Product(String productCode){
        this.productCode = productCode;
    }
    public String getProductCode() {
        return productCode;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getObjectId() {
        return objectId;
    }

}
