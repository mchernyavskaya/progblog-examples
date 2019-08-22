package com.example.multimongo.data.internal

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

@Document(collection = "priceHistory")
data class PriceHistory(
    @Id
    val id: String? = null,
    val sku: String,
    val prices: MutableList<PriceEntry> = mutableListOf()
)

data class PriceEntry(
    val price: Double,
    val expired: Date? = null
)

interface PriceHistoryRepository : MongoRepository<PriceHistory, String>
