package com.product.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import org.hibernate.validator.constraints.Range
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.Size
import kotlin.math.min


@Entity
data class Rating (
		@Id @GeneratedValue
		var id: Long?,
		@get:Range(min=1,max=5) var nStars: Int,
		var comment: String="",

		@JsonBackReference
		@ManyToOne var product: Product?
)