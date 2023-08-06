package com.project.spring.repositories;

import com.project.spring.model.Category;
import com.project.spring.model.Manufacture;
import com.project.spring.model.Product;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByName(String name);

    List<Product> findProductByCategory_Id(@Param("id") Long id);

    @Query("SELECT p from Product p inner join ProductManufacture pm on p.id = pm.product_id.id where pm.manufacture_id.id = :idMan")
    List<Product> findAllProductByManufacture(@Param("idMan") Long id);


//    @Query(value = "select p from  Product p where  (:name is null or  LOWER(p.name) like %:name%)")
//    Page<Product> filterProduct(String name, Pageable pageable);

    //    List<Product> findAllByCategoryContaining(Category category);
//    List<Product> filterProducts(@Param("price") Double price,@Param("category") Category category);
    //    Search

//    Page<Product> findAllByPriceAndCategory(Double aDouble,Category category,Pageable pageable);

//    Page<Product> findAllByManufactureInAndCategoryAndColorAndPrice(Set<Manufacture> manufacture,Category category,String color,Double aDouble,Pageable pageable);

    Page<Product>  findAllByManufactureIn(Set<Manufacture>manufactures,Pageable pageable);
    Page<Product> findProductByCategory(Category category , Pageable pageable);
    Page<Product> findProductByPrice(Double aDouble,Pageable pageable);
    Page<Product> findProductByColor(String color,Pageable pageable);
    Page<Product> findProductByName(String name, Pageable pageable);

//    PaginationProductResponse filterProducts(Double price, String color, Category category, Set<Manufacture> manufactureSet, Pageable pageable);
    @Query(value = "select p from Product p " +
            "join ProductManufacture pm on  p.id = pm.product_id.id " +
            "join Manufacture m on m.id = pm.manufacture_id.id " +
            "where (p.name like %?1% ) " +
            "or  (concat (p.price,'' ) like %?1%) " +
            "or (p.category.name like %?1% )" +
            "or m.name like %?1% "
    )
//    @Query(value = "select  p from Product  p " +
//            "where (p.name like %?1% ) " +
//            "or  (concat (p.price,'' ) like %?1%) " +
//            "or (p.category.name like %?1% )" )
    List<Product> searchProducts(@Param("keyword") String keyword,@Nullable Pageable pageable);

    /* Numbers comment of Products */
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.product_comment.id = :id")
    Integer findCommentCountByProduct_comment(@Param("id") Long id);

    /*Number star by Product and star */
    @Query("SELECT c.rating,COUNT(c.rating) from Comment c WHERE c.product_comment.id = :id group by c.rating")
    List<Integer[]> findStarByProduct(@Param("id") Long id);

    /* get product by id*/
    Product findProductById(Long id);
}

