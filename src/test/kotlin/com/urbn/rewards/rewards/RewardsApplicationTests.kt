package com.urbn.rewards.rewards

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.OrderRequest
import org.springframework.core.ParameterizedTypeReference

@RunWith(SpringRunner::class)
@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RewardsApplicationTests {

    @Autowired
	lateinit var testRestTemplate: TestRestTemplate
	
	@Before
	fun setupOrder() {
        createOrder("joeuser@someisp.com", 130)
	}

	@After
	fun cleanOrders() {
	    testRestTemplate.delete("/customer/joeuser@someisp.com")
		testRestTemplate.delete("/customer/ann@someisp.com")
	}

	@Test
	fun customerGetSucceeds() {
        val result = testRestTemplate.getForEntity("/customer/joeuser@someisp.com", Customer::class.java)
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertEquals("joeuser@someisp.com", result?.body?.email)
	}

	@Test
	fun customersListSucceeds() {		
        var result = testRestTemplate.exchange("/customers", HttpMethod.GET, null,
		    object: ParameterizedTypeReference<List<Customer>?>() {})
		
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertEquals(1, result?.body?.size)
        var firstCustomer = result?.body?.first()
        assertEquals("joeuser@someisp.com", firstCustomer?.email)
        assertEquals(130, firstCustomer?.rewardPoints)
        assertEquals("A", firstCustomer?.rewardsTier)
        assertEquals("5% off purchase", firstCustomer?.rewardsTierName)
        assertEquals("B", firstCustomer?.nextRewardsTier)
        assertEquals("10% off purchase", firstCustomer?.nextRewardsTierName)
        assertEquals(30.0.toFloat(), firstCustomer?.nextRewardsTierProgress)

        createOrder("ann@someisp.com", 679)
		
        result = testRestTemplate.exchange("/customers", HttpMethod.GET, null,
            object: ParameterizedTypeReference<List<Customer>?>() {})

        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertEquals(2, result?.body?.size)
        firstCustomer = result?.body?.first()
        assertEquals("ann@someisp.com", firstCustomer?.email)
        assertEquals(679, firstCustomer?.rewardPoints)
        assertEquals("F", firstCustomer?.rewardsTier)
        assertEquals("30% off purchase", firstCustomer?.rewardsTierName)
        assertEquals("G", firstCustomer?.nextRewardsTier)
        assertEquals("35% off purchase", firstCustomer?.nextRewardsTierName)
        assertEquals(79.0.toFloat(), firstCustomer?.nextRewardsTierProgress)
    }		

    @Test
    fun purchaseSucceeds() {
        val result = createOrder("ann@someisp.com", 679)
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals(1, result.body?.size)
    }

    @Test
    fun purchaseFailsWithoutValidEmail() {
        val anOrder = OrderRequest(email = "", purchaseTotal=679)
        val result = testRestTemplate.postForEntity("/purchase", anOrder, HashMap::class.java)		
        assertNotNull(result)
        assertEquals(HttpStatus.BAD_REQUEST, result.statusCode)
    }

    @Test
    fun deleteCustomerSucceeds() {
        testRestTemplate.delete("/customer/joeuser@someisp.com")
        val result = testRestTemplate.getForEntity("/customer/joeuser@someisp.com", Customer::class.java)
        assertNotNull(result)
        assertEquals(HttpStatus.OK, result?.statusCode)
        assertNull(result?.body?.email)
    }

    private fun createOrder(orderEmail: String, orderTotal: Int): ResponseEntity<HashMap<*,*>> {
        val anotherOrder = OrderRequest(email = orderEmail, purchaseTotal=orderTotal)
        return testRestTemplate.postForEntity("/purchase", anotherOrder, HashMap::class.java)
    }
}

