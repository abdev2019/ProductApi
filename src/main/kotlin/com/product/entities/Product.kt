package com.product.entities

import javax.persistence.*


@Entity
data class Product(
		@Id @GeneratedValue var id: Long? = null,
		var title: String = "",
		var subtitle: String = "",
		var price: Double = 0.0,
		var description: String = "",
		var images: String = ""
)