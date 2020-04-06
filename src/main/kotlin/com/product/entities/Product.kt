package com.product.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import org.hibernate.validator.constraints.Length
import javax.persistence.*
import javax.validation.constraints.Min



@Entity
data class Product(
		@Id @GeneratedValue var id: Long?,
		@get:Length(min=2) var title: String,
		var subtitle: String,
		var description: String,
		@get:Min(0) var price: Double,

		@JsonManagedReference
		@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
		var ratings: List<Rating> = emptyList(),

		@JsonManagedReference
		@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
		var images: List<Image> = emptyList()
)