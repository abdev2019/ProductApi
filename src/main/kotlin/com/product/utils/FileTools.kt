package com.product.utils

import com.product.entities.Image
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


val ressourcesDirectory = "C:\\ressources"


fun saveImage(image: Image, file: MultipartFile)
{
    val dir: Path = Paths.get(ressourcesDirectory +"\\p"+(image.product?.id?:"default"))
    if(!Files.exists(dir)){
        Files.createDirectory(dir)
    }
    val path: Path = Paths.get(gnFilePath(image))
    if(!Files.exists(path)){
        Files.createFile(path)
    }
    Files.write(path, file.bytes)
}

fun deleteImage(image: Image)
{
    val path: Path = Paths.get(gnFilePath(image))
    Files.delete(path)
}

fun loadImage(image: Image) : ByteArray
{
    val path: Path = Paths.get(gnFilePath(image))
    return Files.readAllBytes(path)
}


fun gnFilePath(image: Image) : String{
    return ressourcesDirectory +"\\p"+(image.product?.id?:"default")+"\\"+image.name+"_"+image.id+".jpeg";
}