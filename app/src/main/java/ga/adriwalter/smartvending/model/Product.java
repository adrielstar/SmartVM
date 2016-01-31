package ga.adriwalter.smartvending.model;

import java.util.HashMap;

public class Product {
    private String mId;
    private String mTitle;
    private String mImage;
    private String mDescription;
    private double mPrice;
    public static HashMap<String, Product> productHashMap = new HashMap<>();

    public Product(String id, String title, String image, String description, double price) {
        mId = id;
        mTitle = title;
        mImage = image;
        mDescription = description;
        mPrice = price;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImage() {
        return mImage;
    }

    public String getDescription() {
        return mDescription;
    }

    public double getPrice() {
        return mPrice;
    }

    public static void addProduct(Product product) {
        if (!productHashMap.containsKey(product.getId())) {
            productHashMap.put(product.getId(), product);
        }
    }
}
