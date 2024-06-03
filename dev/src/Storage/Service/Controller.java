package Storage.Service;

import Storage.DomainLayer.DomainManager;
import Storage.DomainLayer.Facades.DomainFacade;
import Storage.DomainLayer.Product;

import java.util.List;
import java.util.Map;

public class Controller {
    private DomainManager manager;

    public Controller(Map<Integer, Product> productMap, DomainFacade facade) {
        this.manager = new DomainManager(productMap, facade);
    }

    public void addProduct(Product product) {
        this.manager.addProduct(product);
    }

    public Product getProduct(int catalogNumber) {
        return this.manager.getProduct(catalogNumber);
    }

    public String produceProductReport(List<String> categories){
        List<Product> info = manager.getProductsByCategories(categories);
        String report = "";
        for(Product product : info){
            report += product.toString() + "\n";
        }
    }
}
