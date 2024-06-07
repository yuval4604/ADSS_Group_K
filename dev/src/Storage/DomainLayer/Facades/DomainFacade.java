package Storage.DomainLayer.Facades;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Product;

import java.util.HashMap;
import java.util.Map;

public class DomainFacade {

    private Map<Category, CategoryFacade> categories;

    public DomainFacade() {
        this.categories = new HashMap<>();
    }

    public void addProduct(Product product) throws Exception{
        try {
            if(!this.categories.containsKey(product.getCategory())){
                this.categories.put(product.getCategory(),new CategoryFacade());
            }
            this.getCategoryFacade(product.getCategory()).addProduct(product);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public void removeProduct(Product product) throws Exception{
        try {
            if (!this.categories.containsKey(product.getCategory()))
                throw new IllegalArgumentException("Category does not exist");
            this.getCategoryFacade(product.getCategory()).removeProduct(product);
        } catch (Exception e) {
            throw e;
        }
    }

    public CategoryFacade getCategoryFacade(Category category) throws Exception {
        if(!this.categories.containsKey(category)) {
            categories.put(category, new CategoryFacade());
        }
        return this.categories.get(category);
    }

    public Map<Category, CategoryFacade> getCategories() {
        return this.categories;
    }

    public void setCategories(Map<Category, CategoryFacade> categories) {
        this.categories = categories;
    }
}
