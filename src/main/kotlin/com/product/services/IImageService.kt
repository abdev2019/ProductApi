package com.product.services

import com.product.entities.Image
import org.springframework.web.multipart.MultipartFile


interface IImageService{
    fun getImage(id: Long): ByteArray
    fun removeImage(id:Long): Boolean
    fun addImage(idProduct: Long, file: MultipartFile): Image
}