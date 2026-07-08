package com.project.code.Service;


import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    @Autowired
    private final InventoryRepository inventoryRepository;
    @Autowired
    private final ProductRepository  productRepository;

    public ServiceClass(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

   public Boolean validateInventory(Inventory inventory){
      Inventory result=inventoryRepository.findByProductIdandStoreId(inventory.getProduct().getId(),inventory.getStore().getId());

      if (result!=null){
          return false;
      }else{
          return true;
      }

    }

    public Boolean validateProduct(Product product){
        Product result=productRepository.findByName(product.getName());

        if (result!=null){
           return false;
        }else{
            return true;
        }
    }

    public boolean validateProductId(Long id){

            if (id == null) return false; // Prevents potential errors if id is null
            return productRepository.findByid(id) != null;
        //return productRepository.findByid(id) !=null;
    }


    public Inventory getInventoryId(Inventory inventory){
        Inventory result = inventoryRepository.findByProductIdandStoreId(inventory.getProduct().getId(),inventory.getStore().getId());

        return result;
    }

}
