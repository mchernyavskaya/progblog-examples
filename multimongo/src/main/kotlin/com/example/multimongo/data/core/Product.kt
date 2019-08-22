package com.example.multimongo.data.core

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository

@Document
data class Product(
    @Id
    val id: String? = null,
    val sku: String,
    val name: String
)

interface ProductRepository : MongoRepository<Product, String>
