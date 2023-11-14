package com.example.pr20.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CatService {
    @GET("/facts/random/")
    fun getFacts(
        @Query("animal_type") animalType: String,
        @Query("amount") amount: Int,
    ): Call<List<Schedule>>

    @GET("/facts/random/")
    fun getOneFacts(
        @Query("animal_type") animalType: String,
        @Query("amount") amount: Int
    ): Call<Schedule>
}

