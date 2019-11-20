package com.urbn.rewards.models

data class Customer(
    val email: String,
    val rewardPoints: Int,
    val rewardsTier: String,
    val nextRewardsTier: String,
    val nextRewardsTierName: String,
    val nextRewardsTierProgress: Float
) {
    override fun toString(): String {
        return "Customer(email='$email', rewardPoints=$rewardPoints, rewardsTier='$rewardsTier', nextRewardsTier='$nextRewardsTier', nextRewardsTierName='$nextRewardsTierName', nextRewardsTierProgress=$nextRewardsTierProgress)"
    }
}