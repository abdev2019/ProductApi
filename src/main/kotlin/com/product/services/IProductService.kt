package com.product.services

import com.product.entities.Image
import com.product.entities.Product
import com.product.entities.Rating
import org.springframework.data.domain.Page
import org.springframework.web.multipart.MultipartFile



interface IProductService {
    fun getProducts(): Page<Product>
    fun searchProducts(args: Map<String, Any>?): Page<Product>
    fun getProduct(id: Long): Product
    fun saveProduct(product:Product): Product
    fun updateProduct(idProduct: Long, productData: Map<String, Any>) : Product
    fun removeProduct(id:Long)
    fun setProductImage(id:Long, files:Array<MultipartFile>): List<Image>
    fun loadProductImage(idImage:Long): ByteArray
    fun removeProductImage(idImage:Long)
    fun addRatingToProduct(idProduct: Long, rating: Rating): Rating
    fun removeRatingFromProduct(idRating: Long)
}
