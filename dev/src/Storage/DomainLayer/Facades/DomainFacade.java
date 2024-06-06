package Storage.DomainLayer.Facades;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Product;

import java.util.Map;

public class DomainFacade {

    private Map<Category, CategoryFacade> categories;

    public DomainFacade() {
        this.categories = null;
    }

    public void addProduct(Product product) throws Exception{
        this.getCategoryFacade(product.getCategory()).addProduct(product);
    }

    public void removeProduct(Product product) throws Exception{
        this.getCategoryFacade(product.getCategory()).removeProduct(product);
    }

    public CategoryFacade getCategoryFacade(Category category) throws Exception {
        return this.categories.get(category);
    }

    public Map<Category, CategoryFacade> getCategories() {
        return this.categories;
    }

    public void setCategories(Map<Category, CategoryFacade> categories) {
        this.categories = categories;
    }
}
