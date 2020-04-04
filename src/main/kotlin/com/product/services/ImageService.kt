package com.product.services

import com.product.dao.ImageRepository
import com.product.entities.Image
import com.product.utils.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.transaction.Transactional


@Service
@Transactional
class ImageService : IImageService
{
    @Autowired lateinit var imageRep: ImageRepository;
    @Autowired lateinit var productService: IProductService;


    // Add new image of product
    // throwing exception when product related to [id] doesn't exist.
    // throwing exception when type of the file uploaded is not an image
    override fun addImage(idProduct: Long, file: MultipartFile) : Image {
        if(!(file.contentType?:"").startsWith("image/"))
            throw OnlyImagesAllowedException()
        val product = productService.getProduct(idProduct)
        val img = imageRep.save(Image(null,file.originalFilename?.toLowerCase()?:"img",product))
        // save in server file or special folder
        saveImage(img, file)
        return img
    }

    // remove image identified by id by requesting DELETE http://host/api/products/images/[id of image]
    // throwing exception when image related to [id] doesn't exist.
    override fun removeImage(id:Long) : Boolean {
        val image = imageRep.findById(id).orElse(null) ?: throw ImageNotFoundException()
        imageRep.delete(image)
        deleteImage(image)
        return true
    }

    // load image identified id by requesting GET http://host/api/products/images/[id of image]
    // throwing exception when image related to [id] doesn't exist.
    override fun getImage(id:Long) : ByteArray {
        val image = imageRep.findById(id).orElse(null) ?: throw ImageNotFoundException()
        return loadImage(image)
    }
}