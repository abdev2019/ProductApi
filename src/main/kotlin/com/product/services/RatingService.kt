package com.product.services

import com.product.dao.RatingRepository
import com.product.entities.Rating
import com.product.utils.RatingNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
@Transactional
class RatingService : IRatingService {

    @Autowired lateinit var ratingRep: RatingRepository;
    @Autowired lateinit var productService: IProductService

    // throwing exception when rating related to [id] doesn't exist.
    override fun removeRating(id:Long): Boolean {
        val rating = ratingRep.findById(id).orElse(null) ?: throw RatingNotFoundException()
        ratingRep.delete(rating)
        return true
    }

    // throwing exception when product related to [id] doesn't exist.
    override fun addRating(idProduct:Long, rating: Rating): Rating {
        rating.product = productService.getProduct(idProduct)
        return ratingRep.save(rating)
    }
}