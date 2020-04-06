# RESTfull Product Api
 #
 # 
 
 ### Architecture
![alt text](https://raw.githubusercontent.com/abdev2019/ProductApi/master/architecture.PNG)

The architecture compose from five layers:

-   Entities layer contains all JPA entities
-	DAO layer contains all JPA repositories related to each JPA entity
-	Service layer contains all services classes related to each entity
-	Controller(WS) layer contains Product controller which provide all endpoints of the API
-	In addition we have a tools package contains all functions needed in service layer
    >As file functions (write Image file, read Image file, generate file’s path, create directory of some product to store images files)

    >It contains also all needed custom exceptions to rewrite the http response: 
    -    `ProductNotFoundException` : handled when the product related to requested ID not exist 
         Sent a status code 404 [NOT_FOUND]
    -	`RatingNotFoundException` : handled when the rating related to requested ID not exist 
Sent a status code 404 [NOT_FOUND]
    -	`ImageNotFoundException` : handled when the image related to requested ID not exist
Sent a status code 404 [NOT_FOUND]
    -	`OnlyImagesAllowedException` : handled when the file uploaded is not an image, 
Sent a status code 406 [NOT_ACCEPTABLE]
    - `NoItemFoundException` : handled when returning empty result, return status code ``204 [NO_CONTENT]``
    - `NoValueChangedException` : handled when no value updated with PUT request, return status code ``304 [NOT_MODIFIED]``
    - `ValidationExceptionHandler` : handled when activating the validation of entities with @Valid anotation and sent  status code 400 [BAD_REQUEST]
    <br/>
### Classes diagram
![alt text](https://raw.githubusercontent.com/abdev2019/ProductApi/master/diagramClasses.PNG)
  
<br/><br/><br/>
 
 ### This API provide many endpoints to consume data:
 
##### Adding new Product
#
```sh
POST /products 
```
POST Body: there are two fields which are should respect validator: title should be at least two characters. In addition, price should be positive decimal <br/>
-> Return status code 201[CREATED] on success<br/>
-> Return status code 400[BAD_REQUEST] on failed<br/>
Example:
```json
{
   "title" : "example title", 
   "subtitle" : "example subtitle", 
   "description" : "example description",
   "price" : 5000.00
} 
```
<br/><br/>

##### Updating product 
#
```sh
PUT /products/{idProduct}
```  

POST Body can contains only the fields needed to update<br/>
-> Return status code 200[OK] on success<br/>
-> Return status code 400[BAD_REQUEST] on failed<br/>
-> Return status code 304[NOT_MODIFIED] when no changing happened<br/>
-> Return status code 404[BAD_REQUEST] when no product exist<br/> 
Example:
```json
{ 
    "description" : "description updated",
    "price" : 6000.00
} 
```
  
<br/><br/>
  
##### Deleting a product 
#
```sh
DELETE  /products/{idProduct}
``` 

If the product doesn’t exist the response will contains a notification message <br/>
-> Return status code 204[NO_CONTENT] on success<br/> 
-> Return status code 404[BAD_REQUEST] when no product exist<br/>
   
<br/><br/>
##### search products 
#
```sh
GET  /products
``` 
to fetch all products
```sh
POST  /products/search
``` 
to search products by keywords with specifying keywords in body
<br/>
-> Return status code 200[OK] on success with pageable result<br/> 
-> Return status code 204[NO_CONTENT] when empty result<br/>
 


<br/><br/>
 
##### Fetch special product 
#
```sh
GET  /products/{id}
```  

<br/>
-> Return status code 200[OK] on success with returning JSON product object<br/> 
-> Return status code 404[NO_FOUND] when no product exist<br/>
The response will be a <br/>
We can fetch only specific fields of product by adding filterField param with values separated by comma: <br/>
•	Fetch only ratings : `GET /products/{id}?filterFields=ratings`<br/>
•	Fetch only images : `GET /products/{id}?filterFields=images`<br/>
•	Fetch title and images : `GET /products/{id}?filterFields=title,ratings`


<br/><br/>
 
##### Adding rating to product  
#
```sh
POST  /products/{id}/ratings
```   

<br/>
-> Return status code 201[CREATED] on success with returning JSON rating object<br/> 
-> Return status code 404[NO_FOUND] when no product exist<br/>
The response will be a JSON rating data or can be a message to notify that the related product does not exist


<br/><br/>
 
##### Removing rating 
#
```sh
DELETE  /products/ratings/{id}
```     
<br/>
-> Return status code 204[NO_CONTENT] on success with empty result<br/> 
-> Return status code 404[NO_FOUND] when no rating exist<br/>
The response will be true on success or a message to notify that the rating does not exist


<br/><br/>
 
##### Uploading and affect image to product 
#
```sh
POST  /products/{id}/images 
```      

<br/>
-> Return status code 201[CREATED] on success with returning list of images<br/> 
-> Return status code 404[NO_FOUND] when no product exist<br/>
-> Return status code 406[NOT_ACCEPTABLE] when file type is not an image<br/>
POST Body should has content-type as multipart/form-data and image as field name. 
The response, on success, will be a JSON containing information about the image or a message to notify that the product does not exist, or that the type of file should be Image


<br/><br/>
 
##### Removing image 
#
```sh
DELETE  /products/images/{id}
```    
 
<br/>
-> Return status code 204[NO_CONTENT] on success with empty result<br/> 
-> Return status code 404[NO_FOUND] when no image exist<br/> 
The response will be true on success or a message to notify that the image does not exist


<br/><br/>
 
##### Loading image resource 
#
```sh
GET /products/images/{id}.jpeg
```     
<br/>
-> Return status code 200[OK] on success with image ressource(array bytes)<br/> 
-> Return status code 404[NO_FOUND] when no image exist<br/>
The response's content is image/jpeg  or a message to notify that the image not exist

 
#
#
#
#
#
# 
 
 
  
