package com.product.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*


@Entity
data class Image(
    @Id @GeneratedValue var id: Long?,
    var name: String,

    @JsonBackReference
    @ManyToOne var product: Product?
)