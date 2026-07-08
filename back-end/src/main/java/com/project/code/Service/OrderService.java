package com.project.code.Service;


import com.project.code.Model.*;
import com.project.code.Repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;


    @Transactional
 public void saveOrder (PlaceOrderRequestDTO placeOrderRequest) {

     Customer existingCustomer = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail());

     Customer customer = new Customer();
     customer.setName(placeOrderRequest.getCustomerName());
     customer.setEmail(placeOrderRequest.getCustomerEmail());
     customer.setPhone(placeOrderRequest.getCustomerPhone());

     if (existingCustomer==null){
         customer = customerRepository.save(customer);
     }else{
         customer=existingCustomer;
     }

    Store store= storeRepository.findStoreById(placeOrderRequest.getStoreId());
     if (store==null){
         throw new RuntimeException("Store not found");
     }

     OrderDetails orderDetails=new OrderDetails();
     orderDetails.setCustomer(customer);
     orderDetails.setStore(store);
     orderDetails.setTotalPrice(placeOrderRequest.getTotalPrice());
     orderDetails.setDate(java.time.LocalDateTime.now());

     orderDetails= orderDetailsRepository.save(orderDetails);


     List<PurchaseProductDTO> purchaseProducts = placeOrderRequest.getPurchaseProduct();
     for (PurchaseProductDTO productDTO : purchaseProducts) {
         OrderItem orderItem = new OrderItem();
         Inventory inventory = inventoryRepository.findByProductIdandStoreId(productDTO.getId(), placeOrderRequest.getStoreId());
         inventory.setStockLevel(inventory.getStockLevel() - productDTO.getQuantity());
         inventoryRepository.save(inventory);
         orderItem.setOrder(orderDetails); // Link the order to the order item
         orderItem.setProduct(productRepository.findByid(productDTO.getId()));
         orderItem.setQuantity(productDTO.getQuantity());
         orderItem.setPrice(productDTO.getPrice() * productDTO.getQuantity());
         orderItemRepository.save(orderItem); // Save OrderItem

     }

 }
}
