package com.project.code.Controller;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Model.Store;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private ServiceClass serviceClass;


    @PutMapping
    public Map<String, String> updateInventory(@RequestBody CombinedRequest combinedRequest) {

        Product product = combinedRequest.getProduct();
        Inventory inventory = combinedRequest.getInventory();

        Map<String, String> map = new HashMap<>();

        if (!serviceClass.validateProductId(product.getId())) {

            map.put("Error" + product.getId(), "failed to update inventory" + inventory.getId());
            return map;
        }

        productRepository.save(product);
        map.put("message", "Successfully updated product with id: " + product.getId());

        if (inventory != null) {
            try {
                Inventory result = serviceClass.getInventoryId(inventory);

                if (result != null) {
                    inventory.setId(result.getId());
                    inventoryRepository.save(inventory);
                    map.put("message", "Successfully updated product" + inventory.getId());
                    return map;


            } else{

                map.put("message", "No data available for this product or store id");
                return map;
            }


        }catch(DataIntegrityViolationException e){
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        } catch(Exception e){
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        }
    }

        return map;
    }


    @PostMapping
    public Map<String, String>saveInventory(@RequestBody Inventory inventory) {

        Map<String, String> map = new HashMap<>();

        try {
        if (!serviceClass.validateInventory(inventory)) {


                map.put("message", "Data already exists");
                return map;
            }else{
                inventoryRepository.save(inventory);
                map.put("message", "Inventory data saved successfully");
                return map;
            }

        } catch(DataIntegrityViolationException e){
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        } catch(Exception e){
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        }
    }


    @GetMapping("/{storeid}")
    public Map<String,Object>getAllProducts(@PathVariable long storeid){
        Map<String, Object>map=new HashMap<>();

       List<Product>result= productRepository.findProductsByStoreId(storeid);
       map.put("products",result);
       return map;
    }


    @GetMapping("filter/{category}/{name}/{storeid}")
    public Map<String, Object> getProductName(@PathVariable String category, @PathVariable String name,
                                              @PathVariable long storeid) {
        Map<String, Object> map = new HashMap<>();
        if (category.equals("null") ) {
            map.put("product", productRepository.findByNameLike(storeid, name));
            return map;
        }
        else if(name.equals("null"))
        {
            System.out.println("name is null");
            map.put("product", productRepository.findByCategoryAndStoreId(storeid,category));
            return map;
        }
        map.put("product", productRepository.findByNameAndCategory(storeid, name, category));
        return map;
    }


    @GetMapping("search/{name}/{storeid}")
    public Map<String, Object>searchProduct(@PathVariable String name, @PathVariable Long storeid){

        Map<String, Object>map=new HashMap<>();
        map.put("product",productRepository.findByNameLike(storeid,name));
        return map;

    }


    @DeleteMapping("/{id}")
    public Map<String,String>removeProduct(@PathVariable Long id){

        Map<String, String>map=new HashMap<>();

        if (!serviceClass.validateProductId(id)) {
            map.put("message", "Id " + id + " not present in database ");
        }else{
            inventoryRepository.deleteByProductId(id);
            map.put("message", "Deleted product successfully with id: " + id);
        }
        return map;

    }


    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(@PathVariable int quantity, @PathVariable long storeId, @PathVariable long productId){

        Inventory result=inventoryRepository.findByProductIdandStoreId(productId,storeId);

        if (result.getStockLevel()>=quantity){
            return true;
        }
        return false;
    }

}
