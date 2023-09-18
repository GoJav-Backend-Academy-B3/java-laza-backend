package com.phincon.laza.service.impl;

import com.phincon.laza.exception.custom.ConflictException;
import com.phincon.laza.exception.custom.NotFoundException;
import com.phincon.laza.exception.custom.NotProcessException;
import com.phincon.laza.model.dto.rajaongkir.CourierResponse;
import com.phincon.laza.model.dto.request.CheckoutRequest;
import com.phincon.laza.model.dto.request.ROCostRequest;
import com.phincon.laza.model.entity.*;
import com.phincon.laza.repository.OrderRepository;
import com.phincon.laza.service.*;
import com.phincon.laza.utils.GenerateRandom;
import com.phincon.laza.validator.OrderValidator;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private XenditService xenditService;

    @Autowired
    private MidtransService midtransService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductOrderDetailService productOrderDetailService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressOrderDetailService addressOrderDetailService;

    @Autowired
    private RajaongkirService rajaongkirService;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Page<Order> getAllOrdersByUserIdPage(int page, int size, String userId) {
        User user = userService.getById(userId);
        return orderRepository.findAllByUserIdOrderByCreatedAtDesc(user, PageRequest.of(page, size));
    }

    @Override
    public Order getOrderById(String id) {
        Optional<Order> order = orderRepository.findById(id);

        orderValidator.validateOrderNotFound(order, id);

        return order.get();
    }

    @Override
    public Order createOrder(Order order) {
        try {
            order.setId(generateOrderId());
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(order.getCreatedAt());
            return orderRepository.save(order);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ConflictException(e.getMessage());
        }
    }

    @Override
    public Order requestCreateOrder(String userId, CheckoutRequest checkoutRequest) throws Exception {

        User user = userService.getById(userId);

        PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodById(checkoutRequest.getPaymentMethodId());

        if (!paymentMethod.getIsActive()) {
            throw new NotProcessException("payment method inactive");
        }

        Order order = new Order();

        order.setOrderStatus("requested");

        int amount = 0;
        int itemQuantity = 0;

        // count total amount from product
        List<Cart> carts = cartService.findCartByUser(userId);
        List<ProductOrderDetail> productOrderDetailList = new ArrayList<>();

        if (carts.isEmpty()) {
            throw new NotFoundException("The user cart is currently empty; there are no products in it.");
        }

        for (Cart cart : carts){
            ProductOrderDetail tmp = getProductOrderDetail(cart, order);
          
            amount += tmp.getTotalPrice();
            itemQuantity += tmp.getQuantity();

            productOrderDetailList.add(tmp);
        }

        // implement add address order detail
        Address address = addressService.findByIdAndByUserId(userId, checkoutRequest.getAddressId());
        AddressOrderDetail addressOrderDetail = getAddressOrderDetail(order, address);

        // get shipping fee
        ROCostRequest roCostRequest =  new ROCostRequest();
        roCostRequest.setOrigin("153");
        roCostRequest.setDestination(address.getCity().getCityId());
        roCostRequest.setCourier(checkoutRequest.getCourier());
        roCostRequest.setWeight(500 * itemQuantity);

        List<CourierResponse> courierResponseList = rajaongkirService.findCostCourierService(roCostRequest);

        int shippingFee = courierResponseList.get(0).getCosts().get(0).getCost().get(0).getValue();
        order.setShippingFee(shippingFee);
        amount += shippingFee;

        // add admin fee
        int adminFee = paymentMethod.getAdminFee();
        amount += adminFee;
        order.setAdminFee(adminFee);

        // set amount from user cart
        order.setAmount(amount);

        order.setUser(user);
        order.setExpiryDate(LocalDateTime.now().plusDays(1));
        order = createOrder(order);

        // Add product order detail to database
        order.setProductOrderDetails(productOrderDetailService.createProductOrderDetails(productOrderDetailList));

        // Add address order detail to database
        order.setAddressOrderDetail(addressOrderDetailService.createAddressOrderDetail(addressOrderDetail));

        PaymentDetail paymentDetail = new PaymentDetail();

        // check if using CC
        if (checkoutRequest.getPaymentMethod().equalsIgnoreCase("credit_card")) {

        } else {
            // xendit payment gateway
            if (paymentMethod.getProvider().equalsIgnoreCase("xendit")) {
                if (paymentMethod.getType().equalsIgnoreCase("e-wallet")) {
                    paymentDetail = xenditService.chargeEwallet(paymentMethod, order, checkoutRequest.getCallbackUrl());
                } else if (paymentMethod.getType().equalsIgnoreCase("virtual_account")) {
                    paymentDetail = xenditService.chargeVirtualAccount(paymentMethod, order);
                }
            } else if (paymentMethod.getProvider().equalsIgnoreCase("midtrans")) {
                if (paymentMethod.getType().equalsIgnoreCase("e-wallet")) {
                    paymentDetail = midtransService.chargeGopay(paymentMethod, order, checkoutRequest.getCallbackUrl());
                }
            }
        }

        order.setPaymentDetail(paymentDetail);

        // update order
        order = updateOrder(order.getId(), order);

        // Delete all product in user cart
        cartService.deleteCartByUser(userId);

        return getOrderById(order.getId());
    }

    private AddressOrderDetail getAddressOrderDetail(Order order, Address address) {
        AddressOrderDetail addressOrderDetail = new AddressOrderDetail();
        addressOrderDetail.setOrder(order);
        addressOrderDetail.setFullAddress(address.getFullAddress());
        addressOrderDetail.setPhoneNumber(address.getPhoneNumber());
        addressOrderDetail.setReceiverName(address.getReceiverName());
        addressOrderDetail.setProvince(address.getCity().getProvinces().getProvince());
        addressOrderDetail.setCityName(address.getCity().getCityName());
        addressOrderDetail.setCityType(address.getCity().getType());
        addressOrderDetail.setPostalCode(address.getCity().getPostalCode());
        return addressOrderDetail;
    }

    private ProductOrderDetail getProductOrderDetail(Cart cart, Order order) {
        Product product =  cart.getProduct();
        ProductOrderDetail result = new ProductOrderDetail();

        result.setOrder(order);
        result.setName(product.getName());
        result.setProductId(product.getId().toString());
        result.setPrice(product.getPrice());
        result.setDescription(product.getDescription());
        result.setBrandName(product.getBrand().getName());
        result.setSize(cart.getSize().getSize());
        result.setCategoryName(product.getCategory().getCategory());
        result.setQuantity(cart.getQuantity());
        result.setImageUrl(product.getImageUrl());

        result.setTotalPrice(result.getPrice() * result.getQuantity());
        return result;
    }

    @Override
    public Order updateOrder(String id, Order updatedOrder) {
        Optional<Order> order = orderRepository.findById(id);

        orderValidator.validateOrderNotFound(order, id);

        updatedOrder.setId(id);
        return orderRepository.save(updatedOrder);
    }

    @Override
    public void deleteOrder(String id) {
        Optional<Order> order = orderRepository.findById(id);

        orderValidator.validateOrderNotFound(order, id);

        orderRepository.deleteById(id);
    }

    private String generateOrderId() {
        LocalDateTime date = LocalDateTime.now();
        String orderId = String.format("ORD-%02d%02d%s-", date.getDayOfMonth(), date.getMonthValue(), date.getYear());

        String result = orderId + GenerateRandom.generateRandomNumber(10);

        while (orderRepository.existsById(result)) {
            result = orderId + GenerateRandom.generateRandomNumber(10);
        }

        return result;
    }
}
