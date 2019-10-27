package com.urbn.rewards.rewards

import com.urbn.rewards.RewardsApplication
import com.urbn.rewards.models.Customer
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime


@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(RewardsApplication::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RewardsApplicationTest {

	@Autowired
	lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun contextLoads() {
		println("${LocalDateTime.now()}  INFO -------- " + "Context loaded..")
    }

	@Test
	fun test1() {
		println("${LocalDateTime.now()}  INFO -------- " + "test1 - check Endpoint 3 for empty map")
		val result = testRestTemplate.getForEntity("/rewards", String::class.java)
		assertNotNull(result)
		println("${LocalDateTime.now()}  INFO -------- " + "test1 - not null PASSSED")
		assertEquals(result.statusCode, HttpStatus.OK)
		println("${LocalDateTime.now()}  INFO -------- " + "test1 - HttpStatus OK")
	}

	@Test
	fun test2() {
		println("${LocalDateTime.now()}  INFO -------- " + "test2 - add order requests through Endpoint 1 and check Endpoint 3")

		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON

		// Send order requests with POST method.
		val order1 = "{\"email\": \"jrchuck@gmail.com\", \"purchaseTotal\": 40 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order1, headers), Void::class.java)
		val order2 = "{\"email\": \"jrchuck@gmail.com\", \"purchaseTotal\": 30 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order2, headers), Void::class.java)
		val order3 = "{\"email\": \"bigspend@aol.com\", \"purchaseTotal\": 140 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order3, headers), Void::class.java)
		val order4 = "{\"email\": \"bigspend@aol.com\", \"purchaseTotal\": 380 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order4, headers), Void::class.java)
		val order5 = "{\"email\": \"bigspend@aol.com\", \"purchaseTotal\": 630 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order5, headers), Void::class.java)
		val order6 = "{\"email\": \"bigspend@aol.com\", \"purchaseTotal\": 510 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order6, headers), Void::class.java)
		val order7 = "{\"email\": \"bigspend@aol.com\", \"purchaseTotal\": 290 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order7, headers), Void::class.java)
		val order8 = "{\"email\": \"bigspend@aol.com\", \"purchaseTotal\": 140 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order8, headers), Void::class.java)
		val order9 = "{\"email\": \"johndoe@comcast.net\", \"purchaseTotal\": 50 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order9, headers), Void::class.java)
		val order10 = "{\"email\": \"johndoe@comcast.net\", \"purchaseTotal\": 70 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order10, headers), Void::class.java)
		val order11 = "{\"email\": \"frosty345@mycompany.com\", \"purchaseTotal\": 20 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order11, headers), Void::class.java)
		val order12 = "{\"email\": \"frosty345@mycompany.com\", \"purchaseTotal\": 80 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order12, headers), Void::class.java)
		val order13 = "{\"email\": \"mrsmith@umich.edu\", \"purchaseTotal\": 180 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order13, headers), Void::class.java)
		val order14 = "{\"email\": \"foobar@umich.edu\", \"purchaseTotal\": 40 }"
		testRestTemplate.postForEntity("/purchase", HttpEntity(order14, headers), Void::class.java)

		// check rewards
		val rewardsResults = testRestTemplate.exchange("/rewards", HttpMethod.GET,null,
				object : ParameterizedTypeReference<List<Customer>>() {})
		assertNotNull(rewardsResults)
		val customers: List<Customer>? = rewardsResults.body

		// check total number of customers
		if (customers != null) {
			println("${LocalDateTime.now()}  INFO -------- " + "test2 - Customers size ${customers.size}")
			assertEquals(customers.size, 6)
		}
		println("${LocalDateTime.now()}  INFO -------- " + "test2 - 6 Customers made order requests -> OK")

		// check info for each customer
		val rewards1 = testRestTemplate.getForEntity("/rewards/jrchuck@gmail.com", Customer::class.java).body
		val rewards2 = testRestTemplate.getForEntity("/rewards/bigspend@aol.com", Customer::class.java).body
		val rewards3 = testRestTemplate.getForEntity("/rewards/johndoe@comcast.net", Customer::class.java).body
		val rewards4 = testRestTemplate.getForEntity("/rewards/frosty345@mycompany.com", Customer::class.java).body
		val rewards5 = testRestTemplate.getForEntity("/rewards/mrsmith@umich.edu", Customer::class.java).body
		val rewards6 = testRestTemplate.getForEntity("/rewards/foobar@umich.edu", Customer::class.java).body

		if (rewards1 != null) {
			assertEquals(rewards1.rewardPoints, 70)
			assertEquals(rewards1.rewardsTier, "None")
			assertEquals(rewards1.nextRewardsTier, "A")
			assertEquals(rewards1.nextRewardsTierProgress, 0.7F)
		}

		if (rewards2 != null) {
			assertEquals(rewards2.rewardPoints, 2090)
			assertEquals(rewards2.rewardsTier, "J")
			assertEquals(rewards2.nextRewardsTier, "None")
			assertEquals(rewards2.nextRewardsTierProgress, 0.0F)
		}

		if (rewards3 != null) {
			assertEquals(rewards3.rewardPoints, 120)
			assertEquals(rewards3.rewardsTier, "A")
			assertEquals(rewards3.nextRewardsTier, "B")
			assertEquals(rewards3.nextRewardsTierProgress, 0.2F)
		}

		if (rewards4 != null) {
			assertEquals(rewards4.rewardPoints, 100)
			assertEquals(rewards4.rewardsTier, "A")
			assertEquals(rewards4.nextRewardsTier, "B")
			assertEquals(rewards4.nextRewardsTierProgress, 0.0F)
		}

		if (rewards5 != null) {
			assertEquals(rewards5.rewardPoints, 180)
			assertEquals(rewards5.rewardsTier, "A")
			assertEquals(rewards5.nextRewardsTier, "B")
			assertEquals(rewards5.nextRewardsTierProgress, 0.8F)
		}

		if (rewards6 != null) {
			assertEquals(rewards6.rewardPoints, 40)
			assertEquals(rewards6.rewardsTier, "None")
			assertEquals(rewards6.nextRewardsTier, "A")
			assertEquals(rewards6.nextRewardsTierProgress, 0.4F)
		}

		println("${LocalDateTime.now()}  INFO -------- " + "test2 - Customer order requests verified with GET for each customer -> OK")
	}

	@Test
	fun test3() {
		println("${LocalDateTime.now()}  INFO -------- " + "test3 - check Endpoint 1 for invalid email and purchase amount")
		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON

		// Send order requests with POST using invalid email.
		val order1 = "{\"email\": \"notAnEmail\", \"purchaseTotal\": 40 }"
		val result1 = testRestTemplate.postForEntity("/purchase", HttpEntity(order1, headers), Void::class.java)
		assertEquals(result1.statusCode, HttpStatus.BAD_REQUEST)
		println("${LocalDateTime.now()}  INFO -------- " + "test3 - HttpStatus ${result1.statusCode} from invalid email...")

		// Send order requests with POST using invalid purchase amount
		val order2 = "{\"email\": \"jrchuck@gmail.com\", \"purchaseTotal\": \"XYZ\" }"
		val result2 = testRestTemplate.postForEntity("/purchase", HttpEntity(order2, headers), Void::class.java)
		assertEquals(result2.statusCode, HttpStatus.BAD_REQUEST)
		println("${LocalDateTime.now()}  INFO -------- " + "test3 - HttpStatus ${result2.statusCode} from invalid purchase total...")
	}

}
