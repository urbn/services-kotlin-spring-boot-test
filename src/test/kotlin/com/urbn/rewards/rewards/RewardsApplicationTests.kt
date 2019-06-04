package com.urbn.rewards.rewards

import com.urbn.rewards.controller.RewardsController
import com.urbn.rewards.models.Customer
import com.urbn.rewards.models.OrderRequest
import com.urbn.rewards.service.OrderService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.annotation.Order
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class RewardsApplicationTests {

	@Test
	fun contextLoads() {
	}

	@Test
	fun testOrderService() {
		val orderService = OrderService()

		// check rewards populated
		assert(orderService.rewards.isNotEmpty())

		// check that we are starting with no customers
		var res = orderService.getCustomers()
		assert(res?.isEmpty() == true)

		// create and order request for testing
		val testOrderRequest = OrderRequest(email = "ftorning@gmail.com", purchaseTotal = 60)

		val testPurchase = orderService.purchase(testOrderRequest)

		assert(testPurchase?.email == testOrderRequest.email)
		assert(testPurchase?.rewardPoints == testOrderRequest.purchaseTotal)
		assert(testPurchase?.nextRewardsTierProgress == 0.6F)
		assert(testPurchase?.rewardsTier == "")
		assert(testPurchase?.nextRewardsTier == "A")

		// submit the order again
		val testPurchaseAgain = orderService.purchase(testOrderRequest)

		assert(testPurchaseAgain?.email == testOrderRequest.email)
		assert(testPurchaseAgain?.rewardPoints == testOrderRequest.purchaseTotal * 2)
		assert(testPurchaseAgain?.nextRewardsTierProgress == 0.2F)
		assert(testPurchaseAgain?.rewardsTier == "A")
		assert(testPurchaseAgain?.nextRewardsTier == "B")

		// create additional order request and submit purchase
		val testDifferentOrderRequest = OrderRequest(email = "sk@gmail.com", purchaseTotal = 40)
		val testAnotherPurchase = orderService.purchase(testDifferentOrderRequest)

		// test getCustomers returning correct number of records
		res = orderService.getCustomers()
		assert(res?.size == 2)

		// test getCustomer filtering correctly
		val customer = orderService.getCustomer(email="ftorning@gmail.com")
		assert(customer is Customer)
		assert(customer?.email == "ftorning@gmail.com")



	}


//		val testOrderRequest = OrderRequest(email = "ftorning@gmail.com", purchaseTotal = 45)
//		assert(testOrderRequest.email == "ftorning@gmail.com")

//		OrderService.purchase()
	}


