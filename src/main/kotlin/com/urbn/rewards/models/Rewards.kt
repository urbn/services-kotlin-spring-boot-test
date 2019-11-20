package com.urbn.rewards.models

data class Rewards(
    val rewardName: String = "",
    val tier: String = "",
    val points: Int = 0
) {
    override fun toString(): String {
        return "Rewards(rewardName='$rewardName', tier='$tier', points=$points)"
    }
}


