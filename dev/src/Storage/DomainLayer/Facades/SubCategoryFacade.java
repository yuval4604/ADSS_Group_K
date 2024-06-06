package Storage.DomainLayer.Facades;

import Storage.DomainLayer.Enums.SubSubCategory;
import Storage.DomainLayer.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCategoryFacade {

    private Map<SubSubCategory, SubSubCategoryFacade> subSubCategories;

    public SubCategoryFacade() {
        this.subSubCategories = new HashMap<>();
    }

    public void addProduct(Product product) throws Exception {
        this.getSubSubCategoryFacade(product.getSize()).addProduct(product);
    }

    public void removeProduct(Product product) throws Exception{
        if(!this.subSubCategories.containsKey(product.getSize())) throw new IllegalArgumentException("SubSubCategory does not exist");
        this.getSubSubCategoryFacade(product.getSize()).removeProduct(product.getCatalogNumber());
    }

    public SubSubCategoryFacade getSubSubCategoryFacade(SubSubCategory subSubCategory) throws Exception{
        if(!this.subSubCategories.containsKey(subSubCategory)) {
            subSubCategories.put(subSubCategory, new SubSubCategoryFacade());
        }
        return this.subSubCategories.get(subSubCategory);
    }

    public Map<SubSubCategory, SubSubCategoryFacade> getSubSubCategories() {
        return this.subSubCategories;
    }

    public void setSubSubCategories(Map<SubSubCategory, SubSubCategoryFacade> subSubCategories) throws Exception {
        this.subSubCategories = subSubCategories;
    }

    public List<Product> getAllProducts() throws Exception{
        List<Product> products = new java.util.ArrayList<Product>();
        for(SubSubCategoryFacade subSubCategoryFacade : this.subSubCategories.values()){
            products.addAll(subSubCategoryFacade.getAllProducts());
        }
        return products;
    }
}
