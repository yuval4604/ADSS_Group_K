package Storage.DomainLayer;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;
import Storage.DomainLayer.Facades.DomainFacade;

import java.time.LocalDate;
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

    public void addProduct(Product product) throws Exception{
        if(this.productMap.containsKey(product.getCatalogNumber())){
            throw new IllegalArgumentException("Product with this catalog number already exists");
        }
        this.domainFacade.addProduct(product);
        this.productMap.put(product.getCatalogNumber(), product);
    }

    public Product getProduct(int catalogNumber) throws Exception{
        if(!this.productMap.containsKey(catalogNumber)){
            throw new IllegalArgumentException("Product with this catalog number does not exist");
        }
        return this.productMap.get(catalogNumber);
    }

    public Map<Integer, Product> getProductMap() {
        return this.productMap;
    }

    public void removeProduct(Product product) throws Exception{
        if(!this.productMap.containsKey(product.getCatalogNumber())){
            throw new IllegalArgumentException("Product with this catalog number does not exist");
        }
        this.productMap.remove(product.getCatalogNumber());
        this.domainFacade.removeProduct(product);
    }

    // in the presentaion we will force the user to choose all the higher level categories of a chosen category
    public List<Product> getProductsByCategories(List<String> categories) throws Exception{
        List<Product> products = new LinkedList<>();
        for(String category : categories){
            String[] divided = category.split(",");
            if(divided.length == 0 || divided.length > 3 )
                throw new IllegalArgumentException("invalid entry");
            if(Category.contains(divided[0])){
                if(divided.length == 1)
                    products.addAll(domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getAllProducts());
                else if(SubCategory.contains(divided[1])){
                    if(domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getSubCategories().containsKey(SubCategory.valueOf(divided[1]))) {
                        if (divided.length == 2)
                            products.addAll(domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getSubCategoryFacade(SubCategory.valueOf(divided[1])).getAllProducts());
                        else if (SubSubCategory.contains(divided[2])) {
                            if (domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getSubCategoryFacade(SubCategory.valueOf(divided[1])).getSubSubCategories().containsKey(SubSubCategory.valueOf(divided[2])))
                                products.addAll(domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getSubCategoryFacade(SubCategory.valueOf(divided[1])).
                                        getSubSubCategoryFacade(SubSubCategory.valueOf(divided[2])).getAllProducts());
                            else throw new NoSuchElementException("sub category does not have this size");
                        }
                        else throw new NoSuchElementException("Size doesn't exist");
                    }
                    else throw new NoSuchElementException("category does not have this sub category");
                }
                else throw new NoSuchElementException("Sub Category doesn't exist");
            }
            else throw new NoSuchElementException("Category doesn't exist");
        }
        return products;
    }

    public void moveProductToStore(int catalogNumber, int quantity) throws Exception {
        Product product = this.productMap.get(catalogNumber);
        product.moveProductToStore(quantity);
    }

    public void subtractFromStore(int catalogNumber, Map<LocalDate,Integer> products) throws Exception{
        Product product = this.productMap.get(catalogNumber);
        for(Map.Entry<LocalDate,Integer> entry : products.entrySet()){
            if(product.getStoreQuantity() < entry.getValue())
                throw new IllegalArgumentException("Not enough products in store");
            for(int i = 0; i < entry.getValue(); i++){
                product.removeOne(false,entry.getKey());
            }
        }
    }

    public Map<Integer,Integer> expiredCount() throws Exception{
        Map<Integer, Integer> expiredProducts = new HashMap<>();
        for(Integer catalogNumber : productMap.keySet()){
            expiredProducts.put(catalogNumber,productMap.get(catalogNumber).expiredCount());
        }
        return expiredProducts;
    }
}
