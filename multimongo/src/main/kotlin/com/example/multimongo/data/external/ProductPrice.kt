package com.example.multimongo.data.external

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

@Document(collection = "productPrice")
data class ProductPrice(
    @Id
    val id: String? = null,
    val sku: String,
    val price: Double
)

interface ProductPriceRepository : MongoRepository<ProductPrice, String>
