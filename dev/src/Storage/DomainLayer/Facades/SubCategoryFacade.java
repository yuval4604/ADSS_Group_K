package Storage.DomainLayer.Facades;

import Storage.DomainLayer.Enums.SubSubCategory;
import Storage.DomainLayer.Product;

import java.util.List;
import java.util.Map;

public class SubCategoryFacade {

    private Map<SubSubCategory, SubSubCategoryFacade> subSubCategories;

    public SubCategoryFacade() {
        this.subSubCategories = null;
    }

    public void addProduct(Product product) {
        this.getSubSubCategoryFacade(product.getSize()).addProduct(product);
    }

    public void removeProduct(Product product) {
        this.getSubSubCategoryFacade(product.getSize()).removeProduct(product.getCatalogNumber());
    }

    public SubSubCategoryFacade getSubSubCategoryFacade(SubSubCategory subSubCategory) {
        return this.subSubCategories.get(subSubCategory);
    }

    public Map<SubSubCategory, SubSubCategoryFacade> getSubSubCategories() {
        return this.subSubCategories;
    }

    public void setSubSubCategories(Map<SubSubCategory, SubSubCategoryFacade> subSubCategories) {
        this.subSubCategories = subSubCategories;
    }

    public List<Product> getAllProducts(){
        List<Product> products = new java.util.ArrayList<Product>();
        for(SubSubCategoryFacade subSubCategoryFacade : this.subSubCategories.values()){
            products.addAll(subSubCategoryFacade.getAllProducts());
        }
        return products;
    }
}
