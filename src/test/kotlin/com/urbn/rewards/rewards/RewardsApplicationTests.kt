package com.urbn.rewards.rewards

import com.urbn.rewards.RewardsApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import com.urbn.rewards.models.OrderRequest
import org.junit.Assert
import org.junit.Assert.*
import org.springframework.http.ResponseEntity

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(RewardsApplication::class),
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RewardsApplicationTests {

	@Autowired
	lateinit var testRestTemplate: TestRestTemplate

	@Test
	fun purchase() {
		val result = createPurchase("customer1@example.com", 100.80F)
		assertNotNull(result)
		assertEquals(HttpStatus.OK, result.statusCode)
		assertEquals(7, result.body?.size)
		val actual: String = result.body.toString()
		val expected: String = "{email=customer1@example.com, " +
				"rewardPoints=100, " +
				"rewardsTier=A, " +
				"rewardsTierName=5% off purchase, " +
				"nextRewardsTier=B, " +
				"nextRewardsTierName=10% off purchase, " +
				"nextRewardsTierProgress=0.0}"
		assertEquals(expected, actual)

	}

	private fun createPurchase(orderEmail: String, orderTotal: Float): ResponseEntity<LinkedHashMap<*, *>> {
		val purchase = OrderRequest(email = orderEmail, purchaseTotal=orderTotal)
		return testRestTemplate.postForEntity("/purchase", purchase, LinkedHashMap::class.java)
	}
}
