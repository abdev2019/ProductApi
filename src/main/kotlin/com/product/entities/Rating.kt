package com.product.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne


@Entity
data class Rating (
	@Id @GeneratedValue
	var id: Long? = null,
	var nStars: Int=0,
	var comment: String="",

	@JsonBackReference
	@ManyToOne var product: Product = Product()
)