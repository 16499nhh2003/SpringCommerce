package com.project.spring.controllers;

import com.project.spring.dto.*;
import com.project.spring.model.*;
import com.project.spring.repositories.CartRepository;
import com.project.spring.repositories.ProductRepository;
import com.project.spring.repositories.UserRepository;
import com.project.spring.service.CartService;
import com.project.spring.service.impl.UserDetailsServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager entityManager;

    @GetMapping("/cart")
    public ModelAndView showCart() {
        ModelAndView modelAndView = new ModelAndView("/cart");
        int totalProduct = 0;
        int numberItems = 0;
        BigDecimal total = BigDecimal.ZERO;
        System.out.println(userDetailsServiceImpl.getCurrentUserId());
        AppUser user = this.userRepository.getUserByUsername(userDetailsServiceImpl.getCurrentUserId());
        if (user == null) {
            System.out.println("user null ne");
        }
        if (user != null) {
            try {
                Long idUser = user.getId();
                List<Cart> carts = this.cartRepository.findByUserId(idUser);
                /*get cart [0]*/
                Cart cart = carts.get(0);
                CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                List<CartItemDTO> cartItemDTOs = cartDTO.getCartItems();
                numberItems = cartItemDTOs.size();
                for (CartItemDTO cartItemDTO : cartItemDTOs) {
                    total = total.add(BigDecimal.valueOf(cartItemDTO.getProductPrice() * cartItemDTO.getQuantity()));
                }

                modelAndView.addObject("cartItems", cartItemDTOs);
                modelAndView.addObject("isLogin", user.getName());
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        modelAndView.addObject("numberItems", numberItems);
        modelAndView.addObject("total", total);
        return modelAndView;

    }


    @RequestMapping(value = {"/cart/add"}, method = RequestMethod.POST)
    public String addToCart(@ModelAttribute("cartItemDTO") CartItemDTO cartItemDTO, Model model) {

        /* send request CartItemDTO*/
        Long idProduct = cartItemDTO.getProductId();
        Product product = productRepository.findById(idProduct).get();
        int quantity = cartItemDTO.getQuantity();

        /* get user*/
        AppUser user = this.userRepository.getUserByUsername(userDetailsServiceImpl.getCurrentUserId());
        if (user != null) {
            /*Get list cart of user*/
            List<Cart> carts = this.cartRepository.findByUserId(user.getId());
            if (carts == null) {
                Cart cart = new Cart();
                CartItem item = new CartItem();
                item.setCart(cart);
                item.setProduct(product);
                item.setQuantity(quantity);
                cart.setTotal(product.getPrice() * quantity);
                cart.setUser(user);
                this.cartRepository.save(cart);
            } else {
                Cart cart = carts.get(0);
                List<CartItem> cartItems = cart.getCartItems();
                boolean found = false;
                for (CartItem cartItem : cartItems) {
                    if (cartItem.getProduct().getId().equals(product.getId())) {
                        cartItem.setQuantity(cartItem.getQuantity() + quantity);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    CartItem item = new CartItem();
                    item.setCart(cart);
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    cartItems.add(item);
                }
                cart.setCartItems(cartItems);
                this.cartRepository.save(cart);
            }
        }
        return "redirect:/cart";
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public ModelAndView checkOut() {
        ModelAndView modelAndView = new ModelAndView("/checkout");
        int totalProduct = 0;
        int numberItems = 0;
        BigDecimal total = BigDecimal.ZERO;
        AppUser user = this.userRepository.getUserByUsername(userDetailsServiceImpl.getCurrentUserId());
        if (user != null) {
            try {
                Long idUser = user.getId();
                List<Cart> carts = this.cartRepository.findByUserId(idUser);
                /*get cart [0]*/
                Cart cart = carts.get(0);
                CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                List<CartItemDTO> cartItemDTOs = cartDTO.getCartItems();
                numberItems = cartItemDTOs.size();
                for (CartItemDTO cartItemDTO : cartItemDTOs) {
                    total = total.add(BigDecimal.valueOf(cartItemDTO.getProductPrice() * cartItemDTO.getQuantity()));
                }
                modelAndView.addObject("cartItems", cartItemDTOs);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        modelAndView.addObject("numberItems", numberItems);
        modelAndView.addObject("total", total);
        modelAndView.addObject("isLogin", user.getName());
        return modelAndView;
    }

    @Transactional
    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public String payment(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = this.userRepository.getUserByUsername(userDetailsServiceImpl.getCurrentUserId());
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("isLogin",user.getName());
        Long idUser = user.getId();
        List<Cart> carts = this.cartRepository.findByUserId(idUser);
        Cart cart = carts.get(0);
        List<CartItem> cartItems = cart.getCartItems();
        List<CartItemDTO> cartItemDTOS = cartItems.stream().map(cartItem -> modelMapper.map(cartItem, CartItemDTO.class)).toList();

        List<OrderDetailDTO> orderDetailDTOSGetFromCartItem = cartItemDTOS.stream().map(cartItemDTO -> {
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
            orderDetailDTO.setIdProduct(cartItemDTO.getProductId());
            orderDetailDTO.setQuantity(cartItemDTO.getQuantity());
            return orderDetailDTO;
        }).toList();
        List<OrderDetailDTO> orderDetailDTOS = orderDetailDTOSGetFromCartItem;

        Order order = new Order();
        order.setDate(new Date());
        order.setUser(user);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderDetailDTO orderDetailDTO : orderDetailDTOS) {
            Long idP = orderDetailDTO.getIdProduct();
            Product product = this.productRepository.findById(idP).orElse(null);

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetail.setQuantity(orderDetailDTO.getQuantity());

            orderDetails.add(orderDetail);

            double priceProduct = product.getPrice();
            int quantity = orderDetailDTO.getQuantity();
            total = total.add(BigDecimal.valueOf(priceProduct * quantity));
        }
        order.setTotal(total);
        order.setOrderDetails(orderDetails);
        entityManager.persist(order);
        /*clear cart */
        this.cartRepository.delete(cart);
        return "checkoutsuccess";
    }
}
