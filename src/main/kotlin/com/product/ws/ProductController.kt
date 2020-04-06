package com.product.ws

import com.product.entities.Product
import com.product.entities.Rating
import com.product.services.IProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
@RequestMapping("/api/products")
class ProductController
{
    @Autowired lateinit var productService: IProductService;


    // return all products by requesting GET http://host/api/products
    @GetMapping fun getProducts() = productService.getProducts()

    // search products by keywords by requesting POST http://host/api/products with body post containing keywords(title,page,size)
    @PostMapping("/search") fun searchProducts(@RequestBody args: Map<String, Any>?)
            = productService.searchProducts(args)


    // return product identified by id by requesting GET http://host/api/products/[id of product]
    @GetMapping("/{id}")
    fun getProduct(@PathVariable("id") id:Long) = productService.getProduct(id)


    // saving new product by requesting POST http://host/api/products
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun saveProduct(@Valid @RequestBody product: Product) = productService.saveProduct(product)


    // update any field of product identified by id by requesting PUT http://host/api/products/[id of product]
    @PutMapping("/{id}")
    fun updateProduct(@PathVariable("id") id:Long, @RequestBody productData : Map<String, Any>)
            = productService.updateProduct(id, productData)


    // delete product identified by id by requesting DELETE http://host/api/products/[id of product]
    // throwing exception when product related to [id] doesn't exist.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}") fun deleteProduct(@PathVariable("id") id:Long) = productService.removeProduct(id)


    // add new rating of existing product identified by id by requesting POST http://host/api/products/ratings?idProduct=x
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping ("/{id}/ratings")
    fun setRating(@PathVariable("id") idProduct:Long, @RequestBody rating: Rating)
            = productService.addRatingToProduct(idProduct, rating)


    // remove rating identified by id by requesting POST http://host/api/products/ratings/[id of rating]
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/ratings/{id}")
    fun removeRating(@PathVariable("id") id:Long) = productService.removeRatingFromProduct(id)


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/images")
    fun addProductImage(@PathVariable("id") id:Long, @RequestParam("files") files: Array<MultipartFile>)
            = productService.setProductImage(id, files)

    // remove image identified by id by requesting DELETE http://host/api/images/[id of image]
    // throwing exception when image related to [id] doesn't exist.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/images/{id}")
    fun removeImage(@PathVariable("id") id:Long) = productService.removeProductImage(id)


    // load image identified id by requesting GET http://host/api/images/[id of image].jpeg
    // throwing exception when image related to [id] doesn't exist.
    @GetMapping("/images/{id}.jpeg", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getImage(@PathVariable("id") id:Long) = productService.loadProductImage(id)
}