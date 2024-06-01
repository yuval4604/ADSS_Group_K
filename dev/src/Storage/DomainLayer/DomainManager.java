package Storage.DomainLayer;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;
import Storage.DomainLayer.Facades.DomainFacade;

import java.util.*;

public class DomainManager {

    private Map<Integer, Product> productMap;

    private DomainFacade domainFacade;

    public DomainManager(DomainFacade domainFacade) {
        this.domainFacade = domainFacade;
        this.productMap = new HashMap<Integer, Product>();
    }
    public DomainManager(Map<Integer, Product> productMap, DomainFacade domainFacade) {
        this.productMap = productMap;
        this.domainFacade = domainFacade;
    }

    public void addProduct(Product product){
        if(this.productMap.containsKey(product.getCatalogNumber())){
            throw new IllegalArgumentException("Product with this catalog number already exists");
        }
        this.domainFacade.addProduct(product);
        this.productMap.put(product.getCatalogNumber(), product);
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

    // in the presentaion we will force the user to choose all the higher level categories of a chosen category
    public List<Product> getProductsByCategories(List<String> categories){
        List<Product> products = new LinkedList<>();
        for(String category : categories){
            String[] divided = category.split(",");
            if(divided == null || divided.length == 0 || divided.length > 3 )
                throw new IllegalArgumentException("invalid entry");
            if(Category.contains(divided[0])){
                if(divided.length == 1)
                    domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getAllProducts();
                else if(SubCategory.contains(divided[1])){
                    if(divided.length == 2)
                        domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getSubCategoryFacade(SubCategory.valueOf(divided[1])).getAllProducts();
                    else if(SubSubCategory.contains(divided[2])){
                        domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getSubCategoryFacade(SubCategory.valueOf(divided[1])).
                                getSubSubCategoryFacade(SubSubCategory.valueOf(divided[2])).getAllProducts();
                    }
                    else
                        throw new NoSuchElementException("Size doesn't exist");
                }
                else
                    throw new NoSuchElementException("Sub Category doesn't exist");
            }
            else
                throw new NoSuchElementException("Category doesn't exist");
        }
        return products;
    }


}
