package com.product.dao

import com.product.entities.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByTitleLikeAndSubtitleLikeAndDescriptionLike(title: String, subtitle: String, description: String, pageable: Pageable)
            : Page<Product>
}