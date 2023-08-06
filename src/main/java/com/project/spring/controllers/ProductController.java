package com.project.spring.controllers;

import com.project.spring.dto.CartItemDTO;
import com.project.spring.dto.CommentDTO;
import com.project.spring.dto.PaginationProductResponse;
import com.project.spring.exceptions.ProductNotFoundException;
import com.project.spring.model.Category;
import com.project.spring.model.Comment;
import com.project.spring.model.Manufacture;
import com.project.spring.model.Product;
import com.project.spring.repositories.CategoryRepository;
import com.project.spring.repositories.CommentRepository;
import com.project.spring.repositories.ManufactureRepository;
import com.project.spring.repositories.ProductRepository;
import com.project.spring.service.CommentService;
import com.project.spring.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;


@Controller
@RequestMapping("products")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ManufactureRepository manufactureRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    CommentService commentService;


    @RequestMapping(value = {"","/"},method = RequestMethod.GET)
    public String index(@RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                        @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize,
                        @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                        @RequestParam(name = "orderField", defaultValue = "desc") String orderField,
                        Model model) {

        Direction direction = orderField.equals("desc") ? Direction.DESC : Direction.ASC;
        Order order = new Order(direction, sortBy);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(order));
        PaginationProductResponse products = this.productService.getAllProduct(pageable);

        model.addAttribute("man", manufactureRepository.findAll());
        model.addAttribute("category", categoryRepository.findAll());
        model.addAttribute("data", products);

        /*pagnitation*/
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("orderField", orderField);
        model.addAttribute("totalPages", products.getNumberTotalPages());

        return "product";
    }

    //    Show list product
    @GetMapping("/filter")
    public String filter(@RequestParam(name = "price", required = false) String price,
                         @RequestParam(name = "color", required = false) String color,
                         @RequestParam(name = "category", required = false) String categoryName,
                         @RequestParam(name = "manufacture", required = false) String manufactureName,
                         @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                         @RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,
                         @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                         @RequestParam(name = "orderField", defaultValue = "desc") String orderField,
                         Model model

    ) {
        double priceProduct = 0.0;
        List<Double> doubles = new ArrayList<Double>();
        if (price != null && !price.isEmpty()) {
            switch (price) {
                case "0_10000000" -> model.addAttribute("price", 1);
                case "10000000_30000000" -> model.addAttribute("price", 2);
                case "30000000_70000000" -> model.addAttribute("price", 3);
                default -> model.addAttribute("price", 4);
            }
            double bottom_price = Double.parseDouble(price.split("_")[0]);
            double top_price = Double.parseDouble(price.split("_")[1]);
            doubles.add(bottom_price);
            doubles.add(top_price);
        }

        Set<Manufacture> manufacture = null;
        try {
            Category category = null;
            Optional<Category> categoryFind = categoryRepository.getCategoriesByName(categoryName);
            if (categoryFind.isPresent()) {
                category = categoryFind.get();
                model.addAttribute("category_filter", category.getName());
            }

            manufacture = new HashSet<Manufacture>();
            if (manufactureName != null) {
                if (manufactureName.contains(",")) {
                    String[] manufactures = manufactureName.split(",");
                    for (String s : manufactures) {
                        if (manufactureRepository.findManufactureByNameContainsIgnoreCase(s).isPresent()) {
                            manufacture.add(manufactureRepository.findManufactureByNameContainsIgnoreCase(s).get());
                        }
                    }
                    model.addAttribute("manufacture", manufacture);
                } else {
                    Optional<Manufacture> manufacture1 = manufactureRepository.findManufactureByNameContainsIgnoreCase(manufactureName);
                    Manufacture m1 = null;
                    if (manufacture1.isPresent()) {
                        m1 = manufacture1.get();
                        manufacture.add(m1);
                        model.addAttribute("manufacture", manufacture);
                    }
                }
                StringBuilder manString = new StringBuilder();
                if (manufacture.size() > 0) {
                    for (Manufacture manufacture1 : manufacture) {
                        manString.append(manufacture1.getName()).append(",");
                    }
                }
                model.addAttribute("valueMan", manString.deleteCharAt(manString.length() - 1));
            } else {
                manufacture = new HashSet<>();
                model.addAttribute("manufacture", null);
            }

            Direction direction = orderField.equals("desc") ? Direction.DESC : Direction.ASC;
            Order order = new Order(direction, sortBy);
            Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(order));
            PaginationProductResponse paginationProductResponse =
                    this.productService.filterProducts(doubles, color, category, manufacture, pageable);
            model.addAttribute("data", paginationProductResponse);
            model.addAttribute("totalPages", paginationProductResponse.getNumberTotalPages());

            if (paginationProductResponse.getNumberOfItems() == 0) {
                model.addAttribute("error", true);
            }
        } catch (Exception e) {
            model.addAttribute("error", true);
        }
        model.addAttribute("man", manufactureRepository.findAll());
        model.addAttribute("category", categoryRepository.findAll());

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("orderField", orderField);

        return "product";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", defaultValue = "") Optional<String> keyword,
                         @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                         @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize,
                         @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                         @RequestParam(name = "orderField", defaultValue = "desc") String orderField,
                         Model model) {
        Direction direction = orderField.equals("desc") ? Direction.DESC : Direction.ASC;
        Order order = new Order(direction, sortBy);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by(order));
        PaginationProductResponse paginationProductResponse = this.productService.searchProducts(keyword.orElse(""), pageable);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("orderField", orderField);
        model.addAttribute("data", paginationProductResponse);
        model.addAttribute("totalPages", paginationProductResponse.getNumberTotalPages());

        model.addAttribute("man", manufactureRepository.findAll());
        model.addAttribute("category", categoryRepository.findAll());

        model.addAttribute("keyword", keyword.get());

        return "product";
    }

    /* Product detail */
    @GetMapping("/details/{id}")
    public String showDetail(@PathVariable("id") String productId, Model model) {
        Long id = null;
        try {
            id = Long.parseLong(productId);
            Optional<Product> product = productService.getProductById(id);
            if (product.isEmpty()) {
                throw new ProductNotFoundException("Product not found with ID: \" + id");
            }
            product.ifPresent(value -> model.addAttribute("product", value));
            //* Numbers comment  *//*
            Integer numberOfComment = this.productService.countCommentProduct(id);
            //* Rating product*//*
            BigDecimal average = this.productService.rating(id);
            //*Comment of product*//*
            List<Comment> comments = this.commentRepository.findCommentByProductId(id);
            //*Bind CommentDto *//*
            CommentDTO commentDto = new CommentDTO();
            commentDto.setProductId(id);
            /*update view */
            productService.incrementViewCount(id);

            model.addAttribute("commentDto", commentDto);
            model.addAttribute("rating", average);
            model.addAttribute("numberOfComments", numberOfComment);
            model.addAttribute("comment", comments);
            model.addAttribute("cartItemDTO",new CartItemDTO());
            //* all product*//*
            model.addAttribute("products", this.productRepository.findAll());

            if (model.getAttribute("errors") != null) {
                BindingResult result = (BindingResult) model.getAttribute("errors");
                assert result != null;
                List<String> errorMessages = result.getAllErrors()
                        .stream()
                        .map(ObjectError::getDefaultMessage)
                        .toList();
                model.addAttribute("errorMessages", errorMessages);
            }
            return "details";
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid product ID: " + productId);
            return "404";
        } catch (ProductNotFoundException e) {
            model.addAttribute("errorMessage","Not found product ID:" + productId);
            return "404";
        }
    }

    /*Comment*/
    @PostMapping("/comments")
    public String saveComment(@Valid @ModelAttribute("commentDto") CommentDTO commentDto, BindingResult result, RedirectAttributes redirectAttributes ) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors",result);
            return "redirect:/products/details/" + commentDto.getProductId();
        }
        /*Convert dto -> model */
        Comment comment = new Comment();
        comment.setRating(commentDto.getRating());
        comment.setComment(commentDto.getComment());
        comment.setTime(commentDto.getTime());
        Optional<Product> product = productService.getProductById(commentDto.getProductId());
        comment.setProduct_comment(product.get());
        comment.setTime(new Date());
        /* save into database */
        commentService.addOrUpdate(comment);
        return "redirect:/products/details/" + commentDto.getProductId();
    }

}
