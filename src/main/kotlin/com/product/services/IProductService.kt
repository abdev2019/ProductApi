package com.product.services

import com.product.entities.Product
import org.springframework.data.domain.Page


interface IProductService{
    fun getAllProducts(): List<Product>
    fun getAllProducts(page: Int, size: Int, sort: String): Page<Product>
    fun getAllProductsByKeyWords(title: String, subtitle: String, description: String, page: Int, size: Int, sort: String): Page<Product>
    fun removeProduct(id:Long): Boolean
    fun addProduct(product: Product): Product
    fun updateProduct(idProduct: Long, productDTO: Map<String, Any>) : Product
    fun getProduct(id: Long): Product
    fun getProduct(id: Long, filterFields:String): Map<String, Any?>
}