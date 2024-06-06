package Storage.DomainLayer.Facades;

import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryFacade {

    private Map<SubCategory, SubCategoryFacade> subCategories;

    public CategoryFacade() {
        this.subCategories = new HashMap<>();
    }

    public void addProduct(Product product) throws Exception {
        this.getSubCategories().get(product.getSubCategory()).addProduct(product);
    }

    public void removeProduct(Product product) throws Exception{
        this.getSubCategories().get(product.getSubCategory()).removeProduct(product);
    }

    public SubCategoryFacade getSubCategoryFacade(SubCategory subCategory) throws Exception {
        return this.subCategories.get(subCategory);
    }

    public Map<SubCategory, SubCategoryFacade> getSubCategories() {
        return this.subCategories;
    }

    public void setSubCategories(Map<SubCategory, SubCategoryFacade> subCategories) {
        this.subCategories = subCategories;
    }

    public List<Product> getAllProducts() throws Exception{
        List<Product> products = new java.util.ArrayList<Product>();
        for(SubCategoryFacade subCategoryFacade : this.subCategories.values()){
            products.addAll(subCategoryFacade.getAllProducts());
        }
        return products;
    }
}
