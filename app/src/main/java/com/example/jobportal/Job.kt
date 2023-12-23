package com.example.jobportal

data class Job(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val budget: Double = 0.0,
    val contactNumber: String = "",
    val location: String = ""
)
