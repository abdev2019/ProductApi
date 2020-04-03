package com.product.ws

import com.product.dao.ProductRepository
import com.product.dao.RatingRepository
import com.product.entities.Product
import com.product.entities.Rating
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import javax.websocket.server.PathParam


@RestController
@RequestMapping("/api/products")
class ProductController
{
    @Autowired lateinit var productRep: ProductRepository;
    @Autowired lateinit var ratingRep: RatingRepository;


    // return all products by requesting get http://host/api/products
    @GetMapping fun getAllProducts() : List<Product> = productRep.findAll()


    // return product by id by requesting get http://host/api/products/[id of product]
    // throwing exception when product related to [id] does not exist
    @GetMapping("/{id}")
    fun getProduct(@PathVariable("id") id:Long) =
            productRep.findById(id).orElse(null) ?: throw RuntimeException("Product not found !")



    // saving new product by requesting post http://host/api/products
    @PostMapping fun saveProduct(@RequestBody product: Product) = productRep.save(product)


    // update any field of product by id by requesting PUT http://host/api/products/[id of product]
    // throwing exception when product related to [id] does not exist
    @PutMapping("/{id}")
    fun updateProduct(@PathVariable("id") id:Long, @RequestBody productMap : Map<String, Any>) : Product?{
        val product = productRep.findById(id).orElse(null) ?: throw RuntimeException("Product not found !")

        product.title       = productMap.get("title") as? String ?: product.title
        product.description = productMap.get("description") as? String ?: product.description
        product.price       = productMap.get("price") as? Double ?: product.price
        product.subtitle    = productMap.get("subtitle") as? String ?: product.subtitle
        product.images      = productMap.get("images") as? String ?: product.images

        return productRep.save(product)
    }


    // delete product by id by requesting DELETE http://host/api/products/[id of product]
    // throwing exception when product related to [id] does not exist
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathParam("id") id:Long) : Boolean {
        val product = productRep.findById(id).orElse(null) ?: throw RuntimeException("Product not found !")
        productRep.delete(product)
        return true
    }


    // get all ratings of existing product identified by id by requesting GET http://host/api/products/[id of product]/ratings
    // throwing exception when product related to [id] does not exist
    @GetMapping("/{id}/ratings")
    fun getRatingsOfProduct(@PathVariable("id") id:Long) : List<Rating> {
        productRep.findById(id).orElse(null) ?: throw RuntimeException("Product not found !")
        return ratingRep.findAllByProductId(id)
    }


    // add new rating of existing product identified by id by requesting POST http://host/api/products/[id of product]/rating
    // throwing exception when product related to [id] does not exist
    @PostMapping ("/{id}/rating")
    fun setRating(@PathVariable("id") id:Long,@RequestBody rating: Rating) : Rating{
        rating.product = productRep.findById(id).orElse(null) ?: throw RuntimeException("Product not found !")
        return ratingRep.save(rating)
    }

    // remove rating identified by id by requesting POST http://host/api/products/ratings/[id of rating]
    // throwing exception when rating related to [id] does not exist
    @DeleteMapping("/ratings/{id}")
    fun removeRating(@PathVariable("id") id:Long) : Boolean{
        val rating = ratingRep.findById(id).orElse(null) ?: throw RuntimeException("Rating not found !")
        ratingRep.delete(rating)
        return true
    }
}