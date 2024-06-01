package Storage.DomainLayer.Facades;

import Storage.DomainLayer.Product;

import java.util.List;
import java.util.Map;

public class SubSubCategoryFacade {

    private Map<Integer, Product> products;

    public SubSubCategoryFacade() {
        this.products = null;
    }

    public void addProduct(Product product) {
        if (this.products == null) {
            this.products = new java.util.HashMap<Integer, Product>();
        }
        this.products.put(product.getCatalogNumber(), product);
    }

    public Product getProduct(int catalogNumber) {
        return this.products.get(catalogNumber);
    }

    public Map<Integer, Product> getProducts() {
        return this.products;
    }

    public void setProducts(Map<Integer, Product> products) {
        this.products = products;
    }

    public void removeProduct(int catalogNumber) {
        this.products.remove(catalogNumber);
    }

    public void deleteAllProducts() {
        this.products.clear();
    }

    public List<Product> getAllProducts(){
        return new java.util.ArrayList<Product>(this.products.values());
    }
}
