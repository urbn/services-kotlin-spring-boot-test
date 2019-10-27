package com.urbn.rewards.models

import javax.validation.constraints.Email

data class OrderRequest(
        @field:Email val email: String,
        val purchaseTotal: Int
)