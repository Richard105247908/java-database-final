package com.project.code.Controller;


import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OrderService orderService;


    @PostMapping
    public Map<String,String> addStore(@RequestBody Store store){

        Map<String,String>map=new HashMap<>();
        Store savedStore=storeRepository.save(store);

        map.put("message","Store added successfully with id "+savedStore.getId());
        return map;
    }

    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId){

        Store store =storeRepository.findStoreById(storeId);

        if (store!=null){
            return true;
        }else{
            return false;
        }
    }


    @PostMapping("/placeOrder")
    public Map<String,String>placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO){

        Map<String,String>map=new HashMap<>();


        try {
            orderService.saveOrder(placeOrderRequestDTO);
            map.put("message", "Order placed successfully");
        }
        catch (Error e)
        {
            map.put("Error",""+e);
        }
         return map;

    }


   
}
