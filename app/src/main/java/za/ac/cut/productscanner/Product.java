package za.ac.cut.productscanner;

/**
 * Created by Shahbaaz Sheikh on 24/08/2016.
 */
public class Product {
    private String productCode;
    private String productTitle;
    private String productDesc;

    private String objectId;

    public Product() {
        this.productCode = null;
        this.productTitle = null;
        this.productDesc = null;
    }

    public Product(String code, String title, String desc){
        this.productCode = code;
        this.productTitle = title;
        this.productDesc = desc;
    }
    public Product(String productCode){
        this.productCode = productCode;
    }
    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
