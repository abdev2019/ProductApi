package com.product.services

import com.product.dao.ProductRepository
import com.product.entities.Product
import com.product.utils.ProductNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
@Transactional
class ProductService : IProductService {

    @Autowired lateinit var productRep: ProductRepository;

    override fun getAllProducts(): List<Product> = productRep.findAll()


    // throwing exception when product related to [id] doesn't exist.
    override fun removeProduct(id:Long): Boolean {
        val product = productRep.findById(id).orElse(null) ?: throw ProductNotFoundException()
        productRep.delete(product)
        return true
    }

    override fun addProduct(product: Product) = productRep.save(product)

    // throwing exception when product related to [id] doesn't exist.
    override fun updateProduct(idProduct: Long, productData: Map<String, Any>) : Product {
        val product = getProduct(idProduct)
        product.title       = productData.get("title") as? String ?: product.title
        product.description = productData.get("description") as? String ?: product.description
        product.price       = productData.get("price") as? Double ?: product.price
        product.subtitle    = productData.get("subtitle") as? String ?: product.subtitle

        return productRep.save(product)
    }

    // throwing exception when product related to [id] doesn't exist.
    override fun getProduct(id: Long) = productRep.findById(id).orElse(null) ?: throw ProductNotFoundException()



    override fun getAllProducts(page: Int, size: Int, sort: String): Page<Product> {
        if(size==0) return productRep.findAll( Pageable.unpaged() )
        var typeSort = Sort.Direction.DESC
        if(sort == "asc") typeSort = Sort.Direction.ASC
        return productRep.findAll( PageRequest.of(page,size,typeSort) )
    }

    override fun getAllProductsByKeyWords(title: String, subtitle: String, description: String, page: Int, size: Int, sort: String): Page<Product> {
        if(size==0)
            return productRep.findAllByTitleLikeAndSubtitleLikeAndDescriptionLike("%$title%", "%$subtitle%", "%$description%", Pageable.unpaged())
        val typeSort = Sort.Direction.valueOf(sort)
        return productRep.findAllByTitleLikeAndSubtitleLikeAndDescriptionLike("%$title%", "%$subtitle%", "%$description%", PageRequest.of(page,size,typeSort))
    }


    // throwing exception when product related to [id] doesn't exist.
    override fun getProduct(id: Long, filterFields:String): Map<String, Any?> {
        val product = getProduct(id)
        val fields = filterFields.split(",")
        val productData: MutableMap<String, Any?> = HashMap()
        if(fields.contains("id")) productData["id"] = product.id
        if(fields.contains("title")) productData["title"] = product.title
        if(fields.contains("description")) productData["description"] = product.description
        if(fields.contains("price")) productData["price"] = product.price
        if(fields.contains("subtitle")) productData["subtitle"] = product.subtitle
        if(fields.contains("ratings")) productData["ratings"] = product.ratings
        if(fields.contains("images")) productData["images"] = product.images
        return productData
    }
}