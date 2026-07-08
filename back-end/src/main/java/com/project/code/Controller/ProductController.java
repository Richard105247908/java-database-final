package com.project.code.Controller;

import com.project.code.Model.Product;
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
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ServiceClass serviceClass;
    @Autowired
    InventoryRepository inventoryRepository;

    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {


        Map<String, String> map = new HashMap<>();
        if (!serviceClass.validateProduct(product)) {
            map.put("message", "Product with this name exists" + product.getId());
            return map;

        }
        try {

            productRepository.save(product);
            map.put("message", "Product has been added successfully" + product.getId());
            return map;

        } catch (DataIntegrityViolationException e) {
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        } catch (Exception e) {
            map.put("message", "Error: " + e);
            System.out.println(e);
            return map;
        }
    }


    @GetMapping("/prodcut/{id}")
    public Map<String,Object>getProductbyId(@PathVariable Long id){

        Map<String,Object>map=new HashMap<>();
        Product result =productRepository.findByid(id);

        map.put("Message",result);
        return map;
    }


    @PutMapping
    public Map<String,String>updateProduct(@RequestBody Product product){

        Map<String,String>map=new HashMap<>();

        try {
            productRepository.save(product);
            map.put("message","Data updated successfully");
        }catch (Error e) {
            map.put("message","Error occured");
        }

        return map;
    }


    @GetMapping("/category/{name}/{category}")
    public Map<String, Object>filterbyCategoryProduct(@PathVariable String name,
                                                      @PathVariable String category){

        Map<String,Object>map=new HashMap<>();

        if (name.equals("null")){
            map.put("products",productRepository.findByCategory(category));
        } else if (category.equals("null")){
            map.put("products", productRepository.findByName(name));
        }else{
            map.put("products",productRepository.findProductBySubNameAndCategory(name,category));
        }
        return map;

    }


    @GetMapping
    public Map<String,Object>listProduct(){
        Map<String,Object>map=new HashMap<>();

        map.put("products",productRepository.findAll());
        return map;
    }

    @GetMapping("filter/{category}/{storeid}")
    public Map<String, Object>getProductbyCategoryAndStoreId(@PathVariable String category,
                                                             @PathVariable long storeid) {
        Map<String, Object> map = new HashMap<>();

        List<Product>results= productRepository.findByCategoryAndStoreId(storeid, category);
        map.put("products", results);
        return map;
    }

    @DeleteMapping("/{id}")
    public Map<String, String>deleteProduct(@PathVariable Long id){

        Map<String,String>map=new HashMap<>();

        if (!serviceClass.validateProductId(id)){
            map.put("message", "The product" +id +"was not found");
            return map;
        }else{
            inventoryRepository.deleteByProductId(id);
            productRepository.deleteById(id);


            map.put("message","The product" +id +"was deleted");
            return map;
        }


    }

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable String name) {

        Map<String, Object> map = new HashMap<>();
        map.put("products", productRepository.findProductBySubName(name));
        return map;

    }

    
}
