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
        try {
            if(!this.subCategories.containsKey(product.getSubCategory())){
                this.subCategories.put(product.getSubCategory(),new SubCategoryFacade());
            }
            this.getSubCategoryFacade(product.getSubCategory()).addProduct(product);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public void removeProduct(Product product) throws Exception{
        try {
            if (!this.subCategories.containsKey(product.getSubCategory()))
                throw new IllegalArgumentException("SubCategory does not exist");
            this.getSubCategoryFacade(product.getSubCategory()).removeProduct(product);
        } catch (Exception e) {
            throw e;
        }
    }

    public SubCategoryFacade getSubCategoryFacade(SubCategory subCategory) throws Exception {
        if(!this.subCategories.containsKey(subCategory)) {
            subCategories.put(subCategory, new SubCategoryFacade());
        }
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
