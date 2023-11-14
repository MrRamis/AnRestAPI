package com.example.pr20.model

import kotlinx.coroutines.flow.StateFlow

interface IRepositoryCats {
    fun getFacts(animalType:String="cat",amount: Int = 1):
            StateFlow<Pair<Status, List<ScheduleDTO>?>>
}