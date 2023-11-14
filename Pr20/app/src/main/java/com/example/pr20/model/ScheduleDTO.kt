package com.example.pr20.model

import java.time.LocalDate

data class ScheduleDTO(
    val text:String,
    val type: String,
    val updatedAt:LocalDate
)
