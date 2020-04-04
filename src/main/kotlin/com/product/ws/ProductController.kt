package com.product.ws

import com.product.entities.Product
import com.product.entities.Rating
import com.product.services.IImageService
import com.product.services.IProductService
import com.product.services.IRatingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class ProductController
{
    @Autowired lateinit var productService: IProductService;
    @Autowired lateinit var ratingService: IRatingService;
    @Autowired lateinit var imageService: IImageService;


    // return all products by requesting GET http://host/api/products?page&size&sort&title&subtitle&description
    @GetMapping("/products") fun getAllProducts(
            @RequestParam("size",defaultValue="0") size:Int,
            @RequestParam("page",defaultValue="0") page:Int,
            @RequestParam("sort",defaultValue="desc") sort:String,
            @RequestParam("title",defaultValue="") title:String,
            @RequestParam("subtitle",defaultValue="") subtitle:String,
            @RequestParam("description",defaultValue="") description:String
    )
            = productService.getAllProductsByKeyWords(title, subtitle, description, page, size, sort)


    // return product identified by id by requesting GET http://host/api/products/[id of product]
    @GetMapping("/products/{id}")
    fun getProduct(@PathVariable("id") id:Long, @RequestParam("filterFields",defaultValue="") fields:String) : Any? {
        if(fields.isNotEmpty())
            return productService.getProduct(id,fields)
        return productService.getProduct(id)
    }


    // saving new product by requesting POST http://host/api/products
    @PostMapping("/products") fun saveProduct(@Valid @RequestBody product: Product) = productService.addProduct(product)


    // update any field of product identified by id by requesting PUT|PATCH http://host/api/products/[id of product]
    @RequestMapping("/products/{id}", method = [RequestMethod.PUT, RequestMethod.PATCH])
    fun updateProduct(@PathVariable("id") id:Long, @RequestBody productData : Map<String, Any>)
            = productService.updateProduct(id, productData)


    // delete product identified by id by requesting DELETE http://host/api/products/[id of product]
    // throwing exception when product related to [id] doesn't exist.
    @DeleteMapping("/products/{id}") fun deleteProduct(@PathVariable("id") id:Long) = productService.removeProduct(id)


    // add new rating of existing product identified by id by requesting POST http://host/api/products/ratings?idProduct=x
    @PostMapping ("/ratings")
    fun setRating(@RequestParam("idProduct") idProduct:Long, @RequestBody rating: Rating)
            = ratingService.addRating(idProduct, rating)


    // remove rating identified by id by requesting POST http://host/api/products/ratings/[id of rating]
    @DeleteMapping("/ratings/{id}")
    fun removeRating(@PathVariable("id") id:Long) = ratingService.removeRating(id)


    // Add new image of product
    // throwing exception when product related to [id] doesn't exist.
    // throwing exception when type of the file uploaded is not an image
    @PostMapping("/images")
    fun addImage(@RequestParam("idProduct") idProduct:Long, @RequestParam("image") file: MultipartFile) =
            imageService.addImage(idProduct,file)


    // remove image identified by id by requesting DELETE http://host/api/images/[id of image]
    // throwing exception when image related to [id] doesn't exist.
    @DeleteMapping("/images/{id}")
    fun removeImage(@PathVariable("id") id:Long) = imageService.removeImage(id)


    // load image identified id by requesting GET http://host/api/images/[id of image].jpeg
    // throwing exception when image related to [id] doesn't exist.
    @GetMapping("/images/{id}.jpeg", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getImage(@PathVariable("id") id:Long) = imageService.getImage(id)
}
