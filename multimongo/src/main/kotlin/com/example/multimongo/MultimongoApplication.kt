package com.example.multimongo

import com.example.multimongo.data.core.Product
import com.example.multimongo.data.core.ProductRepository
import com.example.multimongo.data.external.ProductPrice
import com.example.multimongo.data.external.ProductPriceRepository
import com.example.multimongo.data.internal.PriceEntry
import com.example.multimongo.data.internal.PriceHistory
import com.example.multimongo.data.internal.PriceHistoryRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextStartedEvent
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.util.*

@SpringBootApplication
class MultimongoApplication

fun main(args: Array<String>) {
    val context = runApplication<MultimongoApplication>(*args)
    // need to do explicitly to catch the event in listener
    context.start()
}

@RestController
@RequestMapping("/api")
class ProductResource(
    val productRepo: ProductRepository,
    val priceRepo: ProductPriceRepository,
    val priceHistoryRepo: PriceHistoryRepository
) {
    @GetMapping("/product")
    fun getProducts(): List<Product> = productRepo.findAll()

    @GetMapping("/price")
    fun getPrices(): List<ProductPrice> = priceRepo.findAll()

    @GetMapping("/priceHistory")
    fun getPricesHistory(): List<PriceHistory> = priceHistoryRepo.findAll()
}

@Component
class DataInitializer(
    val productRepo: ProductRepository,
    val priceRepo: ProductPriceRepository,
    val priceHistoryRepo: PriceHistoryRepository
) : ApplicationListener<ContextStartedEvent> {

    override fun onApplicationEvent(event: ContextStartedEvent) {
        // clean up
        productRepo.deleteAll()
        priceRepo.deleteAll()
        priceHistoryRepo.deleteAll()

        val p1 = productRepo.save(Product(sku = "123", name = "Toy Horse"))
        val p2 = productRepo.save(Product(sku = "456", name = "Real Horse"))

        val h1 = PriceHistory(sku = p1.sku)
        val h2 = PriceHistory(sku = p2.sku)

        for (i in 5 downTo 1) {
            if (i == 5) {
                // current price
                priceRepo.save(ProductPrice(sku = p1.sku, price = i.toDouble()))
                priceRepo.save(ProductPrice(sku = p2.sku, price = (i * 2).toDouble()))

                // current price history
                h1.prices.add(PriceEntry(price = i.toDouble()))
                h2.prices.add(PriceEntry(price = (i * 2).toDouble()))
            } else {
                // previous price
                val expiredDate = Date(ZonedDateTime.now()
                    .minusMonths(i.toLong())
                    .toInstant()
                    .toEpochMilli())
                h1.prices.add(PriceEntry(price = i.toDouble(), expired = expiredDate))
                h2.prices.add(PriceEntry(price = (i * 2).toDouble(), expired = expiredDate))
            }
        }
        priceHistoryRepo.saveAll(listOf(h1, h2))
    }
}
