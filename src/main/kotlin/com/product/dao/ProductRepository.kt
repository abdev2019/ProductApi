package com.product.dao

import com.product.entities.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ProductRepository : JpaRepository<Product, Long> {
}