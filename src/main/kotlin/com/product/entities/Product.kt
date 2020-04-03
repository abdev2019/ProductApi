package com.product.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.FetchType


@Entity
data class Product(
		@Id @GeneratedValue var id: Long? = null,
		var title: String = "",
		var subtitle: String = "",
		var price: Double = 0.0,
		var description: String = "",
		var images: String = "",

		@JsonManagedReference
		@OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
		var ratings: List<Rating> = emptyList()
)