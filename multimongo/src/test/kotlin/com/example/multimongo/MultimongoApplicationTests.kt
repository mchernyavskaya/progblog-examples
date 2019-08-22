package com.example.multimongo

import com.example.multimongo.data.core.ProductRepository
import com.example.multimongo.data.external.ProductPriceRepository
import com.example.multimongo.data.internal.PriceHistoryRepository
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasItems
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.event.ContextStartedEvent
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.Validator
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get as get

@RunWith(SpringRunner::class)
@SpringBootTest
class MultimongoApplicationTests {
    @Autowired
    private val productRepo: ProductRepository? = null
    @Autowired
    private val priceRepo: ProductPriceRepository? = null
    @Autowired
    private val priceHistoryRepo: PriceHistoryRepository? = null

    @Autowired
    private val initializer: DataInitializer? = null
    @Autowired
    private val context: ApplicationContext? = null

    private var mvc: MockMvc? = null

    @Before
    fun setUp() {
        val resource = ProductResource(
            productRepo!!,
            priceRepo!!,
            priceHistoryRepo!!
        )
        this.mvc = MockMvcBuilders
            .standaloneSetup(resource)
            .build()
        initializer!!.onApplicationEvent(ContextStartedEvent(context!!))
    }

    @Test
    fun productsCreated() {
        mvc!!.perform(get("/api/product"))
            .andExpect(status().isOk)
            .andDo {
                println(it.response.contentAsString)
            }
            .andExpect(jsonPath("$.[*].sku").isArray)
            .andExpect(jsonPath("$.[*].sku")
                .value(hasItems("123", "456")))
    }

    @Test
    fun pricesCreated() {
        mvc!!.perform(get("/api/price"))
            .andExpect(status().isOk)
            .andDo {
                println(it.response.contentAsString)
            }
            .andExpect(jsonPath("$.[*].sku").isArray)
            .andExpect(jsonPath("$.[*].sku")
                .value(hasItems("123", "456")))
            .andExpect(jsonPath("$.[0].price")
                .value(5.0))
            .andExpect(jsonPath("$.[1].price")
                .value(10.0))
    }

    @Test
    fun pricesHistoryCreated() {
        mvc!!.perform(get("/api/priceHistory"))
            .andExpect(status().isOk)
            .andDo {
                println(it.response.contentAsString)
            }
            .andExpect(jsonPath("$.[*].sku").isArray)
            .andExpect(jsonPath("$.[*].sku")
                .value(hasItems("123", "456")))
            .andExpect(jsonPath("$.[0].prices.[*].price")
                .value(hasItems(5.0, 4.0, 3.0, 2.0, 1.0)))
            .andExpect(jsonPath("$.[1].prices.[*].price")
                .value(hasItems(10.0, 8.0, 6.0, 4.0, 2.0)))
    }
}
