package com.project.spring.controllers;

import com.project.spring.dto.CartDTO;
import com.project.spring.dto.CartItemDTO;
import com.project.spring.dto.ResponseObject;
import com.project.spring.model.CartItem;
import com.project.spring.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;
    @SuppressWarnings("finally")
	@GetMapping("")
    public String showCart(Model model){
    	int totalProduct = 0;
    	int numberItems = 0;
    	double total = 0.0;
    	try {
    		  Long idUser = 1L;
    	        List<CartDTO> cartDTOS = this.cartService.getCartDTOByIdUser(idUser);
    	        List<CartItemDTO> cartItems = cartDTOS.get(0).getCartItems();
    	        model.addAttribute("cartItems",cartItems);
    	        numberItems = cartItems.size();
    	        total = cartDTOS.get(0).getTotal();
    	}
    	catch (Exception e) {
			// TODO: handle exception 
		}
    	finally {
    		model.addAttribute("numberItems",numberItems);
	        model.addAttribute("total",total);	
			return "cart";
		}
    }
}
