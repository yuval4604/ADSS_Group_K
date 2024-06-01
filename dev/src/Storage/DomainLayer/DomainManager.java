package Storage.DomainLayer;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Facades.DomainFacade;

import java.util.List;
import java.util.Map;

public class DomainManager {

    private Map<Integer, Product> productMap;
    private DomainFacade domainFacade;

    public DomainManager(DomainFacade domainFacade) {
        this.productMap = new java.util.HashMap<Integer, Product>();
        this.domainFacade = domainFacade;
    }
    public DomainManager(Map<Integer, Product> productMap, DomainFacade domainFacade) {
        this.productMap = productMap;
        this.domainFacade = domainFacade;
    }

    public void addProduct(Product product){
        if(this.productMap.containsKey(product.getCatalogNumber())){
            throw new IllegalArgumentException("Product with this catalog number already exists");
        }
        this.productMap.put(product.getCatalogNumber(), product);
        this.domainFacade.addProduct(product);
    }

    public Product getProduct(int catalogNumber){
        if(!this.productMap.containsKey(catalogNumber)){
            throw new IllegalArgumentException("Product with this catalog number does not exist");
        }
        return this.productMap.get(catalogNumber);
    }

    public Map<Integer, Product> getProductMap() {
        return this.productMap;
    }

    public void removeProduct(Product product){
        if(!this.productMap.containsKey(product.getCatalogNumber())){
            throw new IllegalArgumentException("Product with this catalog number does not exist");
        }
        this.productMap.remove(product.getCatalogNumber());
        this.domainFacade.removeProduct(product);
    }

    // we can make it better (we need to think about what happens if client wants products from specific subcategory)
    public List<Product> getWantedProducts(List<Category> categories){
        List<Product> products = new java.util.ArrayList<Product>();
        for(Category category : categories){
            products.addAll(this.domainFacade.getCategoryFacade(category).getAllProducts());
        }
        return products;
    }
}
