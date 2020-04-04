package com.product.services

import com.product.entities.Rating


interface IRatingService{
    fun removeRating(id:Long): Boolean
    fun addRating(idProduct: Long, rating: Rating): Rating
}