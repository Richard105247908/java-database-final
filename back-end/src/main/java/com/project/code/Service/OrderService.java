package com.project.code.Service;


import com.project.code.Model.Customer;
import com.project.code.Model.OrderDetails;
import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

// 1. **saveOrder Method**:
//    - Processes a customer's order, including saving the order details and associated items.
//    - Parameters: `PlaceOrderRequestDTO placeOrderRequest` (Request data for placing an order)
//    - Return Type: `void` (This method doesn't return anything, it just processes the order)

 void saveOrder (PlaceOrderRequestDTO placeOrderRequest) {


// 2. **Retrieve or Create the Customer**:
//    - Check if the customer exists by their email using `findByEmail`.
//    - If the customer exists, use the existing customer; otherwise, create and save a new customer using `customerRepository.save()`.

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


// 3. **Retrieve the Store**:
//    - Fetch the store by ID from `storeRepository`.
//    - If the store doesn't exist, throw an exception. Use `storeRepository.findById()`.

    Store store= storeRepository.findStoreById(placeOrderRequest.getStoreId());
     if (store==null){
         throw new RuntimeException("Store not found");
     }


// 4. **Create OrderDetails**:
//    - Create a new `OrderDetails` object and set customer, store, total price, and the current timestamp.
//    - Set the order date using `java.time.LocalDateTime.now()` and save the order with `orderDetailsRepository.save()`.

     OrderDetails orderDetails=new OrderDetails();
     orderDetails.setCustomer(customer);
     orderDetails.setStore(store);
     orderDetails.setTotalPrice(placeOrderRequest.getTotalPrice());
     orderDetails.setLocalDateTime(LocalDate.from(java.time.LocalDateTime.now()));

     orderDetails= orderDetailsRepository.save(orderDetails);

// 5. **Create and Save OrderItems**:
//    - For each product purchased, find the corresponding inventory, update stock levels, and save the changes using `inventoryRepository.save()`.
//    - Create and save `OrderItem` for each product and associate it with the `OrderDetails` using `orderItemRepository.save()`.

     



 }
}
