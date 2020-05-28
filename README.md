# Mini-Shop
#This project is based on paging codelabs. 

* The project consume Woocommerce Restful Api of any website develop with wordpress using woocommerce plugin.

* The concept of the project is to explain how to use paging library for both remote & local datasource, and you will be able to perform searchview action 

* click [here](https://docs.woocommerce.com/document/woocommerce-rest-api/) to generate your Api Key (customer_key & customer_secret) for your ecommerce website.

# The Restful Api Used:
-------------------------
* click [here](https://www.akwe.com.ng/wp-json/wc/v3/products?per_page=50&consumer_key=ck_7ae18a838d4eef3321447c5fdfd88c96020d0f48&consumer_secret=cs_bed4f32d44a97c8bb6e7d23fb3a00b1c762e1969) for Restful API response used, you can test this on your postman.

### If you're trying to consume woocommerce Restful Api of your Site(Ecoomerce develop with wordpress), follow the below guidelines:
* Woocommerce Restful Api base url for product
##### https://example.com/wp-json/wc/v3/products
###### Replace example.com with your domain name
------------------------------------------------
* Complete Restful Api path
###### https://example/wp-json/wc/v3/products?per_page=50&consumer_key=ck_7ae18a838d4eef3321447c5fdfd88c96020d0f48&consumer_secret=cs_bed4f32d44a97c8bb6e7d23fb3a00b1c762e1969
####### Replace the customer key and customer secret with the one you generated from the above link.
--------------------------------------------------

## for this project the customer keys & customer secret we will be using details:

 customer_key = ck_7ae18a838d4eef3321447c5fdfd88c96020d0f48

### customer_secret = cs_bed4f32d44a97c8bb6e7d23fb3a00b1c762e1969



* for code structure video demo click [here](https://youtu.be/2ZdmpKbACF8)

# App Demo
------------
![](https://github.com/ayetolusamuel/Mini-Shop/blob/master/images/app-demooo.gif)


# Features
* Both remote & local data source
* display product list
* perform search on product list

# Source
-------------------------------
[Google paging Codelabs](https://codelabs.developers.google.com/codelabs/android-paging/)
