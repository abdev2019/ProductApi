package com.product.dao

import org.springframework.stereotype.Repository
import com.product.entities.Image
import org.springframework.data.jpa.repository.JpaRepository


@Repository interface ImageRepository : JpaRepository<Image, Long> {
    abstract fun findAllByProductId(id: Long): List<Image>
}