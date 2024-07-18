package com.example.fundacion.user.adapter

import com.example.fundacion.user.ActivitySequence

object ActivityManager {
    var activitySequence: ActivitySequence? = null

    fun initialize(activities: List<Class<*>>) {
        activitySequence = ActivitySequence(activities)
    }

    fun getNextActivity(currentIndex: Int): Class<*>? {
        val activities = activitySequence?.activities
        return if (activities != null && currentIndex < activities.size) {
            activities[currentIndex]
        } else {
            null
        }
    }
}
