package com.product.services

import com.product.dao.ImageRepository
import com.product.dao.ProductRepository
import com.product.dao.RatingRepository
import com.product.entities.Image
import com.product.entities.Product
import com.product.entities.Rating
import com.product.utils.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.transaction.Transactional



@Service
@Transactional
class ProductService : IProductService {

    @Autowired lateinit var productRep: ProductRepository;
    @Autowired lateinit var ratingRep: RatingRepository;
    @Autowired lateinit var imageRep: ImageRepository;


    // return all products
    override fun getProducts(): Page<Product> {
        val products = productRep.findAll(Pageable.unpaged())
        if(products.isEmpty)
            throw NoItemFoundException()
        return products
    }

    // search products by key-words/pagination
    override fun searchProducts(args: Map<String, Any>?): Page<Product> {
        val page = args?.get("page") as? Int?:0
        val size = args?.get("size") as? Int?:0
        val pageable: Pageable = if(page>0 || size>1) PageRequest.of(page, size) else Pageable.unpaged()

        val products = productRep.findAllByTitleLikeAndSubtitleLikeAndDescriptionLike(
            "%"+ (args?.get("title") as? String?:"") +"%",
            "%"+ (args?.get("subtitle") as? String?:"") +"%",
            "%"+ (args?.get("description") as? String?:"") +"%",
            pageable
        )
        return if(products.isEmpty) throw NoItemFoundException() else products
    }

    // throwing exception when product related to [id] doesn't exist.
    override fun getProduct(id: Long) = productRep.findById(id).orElse(null) ?: throw ProductNotFoundException()

    override fun saveProduct(product:Product): Product {
        return productRep.save(product)
    }

    override fun setProductImage(id: Long, files: Array<MultipartFile>): List<Image>{
        val images = ArrayList<Image>()
        files.forEach { file->
            if(!(file.contentType?:"").startsWith("image/"))
                throw OnlyImagesAllowedException()
            val product = getProduct(id)
            val image = imageRep.save(Image(null, file.originalFilename+product.id+".jpeg",product))
            saveImage(image, file)
            images.add(image)
        }
        return images
    }

    // throwing exception when product related to [id] doesn't exist.
    override fun updateProduct(idProduct: Long, productData: Map<String, Any>) : Product {
        if(productData.isEmpty())
            throw NoValueChangedException()
        val product = getProduct(idProduct)
        product.title       = productData.get("title") as? String ?: product.title
        product.description = productData.get("description") as? String ?: product.description
        product.price       = productData.get("price") as? Double ?: product.price
        product.subtitle    = productData.get("subtitle") as? String ?: product.subtitle

        return productRep.save(product)
    }

    // throwing exception when product related to [id] doesn't exist.
    override fun removeProduct(id:Long) {
        val product = productRep.findById(id).orElse(null) ?: throw ProductNotFoundException()
        productRep.delete(product)
    }

    // remove image identified by id by requesting DELETE http://host/api/products/images?idImage=xxx
    // throwing exception when image related to [id] doesn't exist.
    override fun removeProductImage(idImage:Long) {
        val image = imageRep.findById(idImage).orElse(null) ?: throw ImageNotFoundException()
        imageRep.delete(image)
        deleteImage(image)
    }

    // load image identified id by requesting GET http://host/api/products/images?idImage=xxx
    // throwing exception when image related to [id] doesn't exist.
    override fun loadProductImage(idImage:Long) : ByteArray {
        val image = imageRep.findById(idImage).orElse(null) ?: throw ImageNotFoundException()
        return loadImage(image)
    }

    override fun addRatingToProduct(idProduct: Long, rating: Rating): Rating{
        rating.product = getProduct(idProduct)
        return ratingRep.save(rating)
    }

    override fun removeRatingFromProduct(idRating: Long){
        val rating = ratingRep.findById(idRating).orElse(null) ?: throw RatingNotFoundException()
        ratingRep.delete(rating)
    }
}

