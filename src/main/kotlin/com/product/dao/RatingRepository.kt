package com.product.dao

import org.springframework.stereotype.Repository
import com.product.entities.Rating
import org.springframework.data.jpa.repository.JpaRepository


@Repository
interface RatingRepository : JpaRepository<Rating, Long> {
    fun findAllByProductId(id: Long) : List<Rating>
}