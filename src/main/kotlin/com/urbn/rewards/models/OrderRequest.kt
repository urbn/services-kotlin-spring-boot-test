package com.urbn.rewards.models

data class OrderRequest(
    val email: String,
    val purchaseTotal: Float
)