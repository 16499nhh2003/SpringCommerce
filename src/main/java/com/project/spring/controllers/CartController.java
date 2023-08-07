package com.project.spring.controllers;

import com.project.spring.dto.CartDTO;
import com.project.spring.dto.CartItemDTO;
import com.project.spring.dto.ProductDTO;
import com.project.spring.dto.ResponseObject;
import com.project.spring.model.AppUser;
import com.project.spring.model.Cart;
import com.project.spring.model.CartItem;
import com.project.spring.model.Product;
import com.project.spring.repositories.CartRepository;
import com.project.spring.repositories.ProductRepository;
import com.project.spring.repositories.UserRepository;
import com.project.spring.service.CartService;
import com.project.spring.service.impl.UserDetailsServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
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

    @GetMapping("/cart")
    public ModelAndView showCart() {
        ModelAndView modelAndView = new ModelAndView("/cart");
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
                CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
                List<CartItemDTO> cartItemDTOs =  cartDTO.getCartItems();
                numberItems = cartItemDTOs.size();
                for(CartItemDTO cartItemDTO : cartItemDTOs){
                    total = total.add(BigDecimal.valueOf(cartItemDTO.getProductPrice()*cartItemDTO.getQuantity()));
                }

                modelAndView.addObject("cartItems", cartItemDTOs);

            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        modelAndView.addObject("numberItems", numberItems);
        modelAndView.addObject("total", total);
        return modelAndView;

    }

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = {"/cart/add"}, method = RequestMethod.POST)
    public String addToCart(@ModelAttribute("cartItem") CartItemDTO cartItemDTO, Model model) {
        /* check exist product*/
        Long idProduct = cartItemDTO.getProductId();
        Product product = productRepository.findById(idProduct).get();
        int quantity = cartItemDTO.getQuantity();
        /*Fake user*/
        AppUser user = this.userRepository.findById(1L).get();
        List<Cart> cart = this.cartRepository.findByUserId(user.getId());
        List<CartItem> cartItems = cart.get(0).getCartItems();
        boolean found = false;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getId().equals(product.getId())) {
                cartItem.setQuantity(quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            CartItem item = new CartItem();
            item.setCart(cart.get(0));
            item.setProduct(product);
            item.setQuantity(quantity);
            cartItems.add(item);
        }
        cart.get(0).setCartItems(cartItems);
        this.cartRepository.save(cart.get(0));
        return "redirect:/cart";
    }
}
