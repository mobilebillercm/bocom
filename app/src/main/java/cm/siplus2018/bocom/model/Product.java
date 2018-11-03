package cm.siplus2018.bocom.model;

/**
 * Created by nkalla on 02/11/18.
 */

public class Product {
    private int id;
    private String productid, name, description;

    public Product(int id, String productid, String name, String description) {
        this.id = id;
        this.productid = productid;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return  name;
    }
}
