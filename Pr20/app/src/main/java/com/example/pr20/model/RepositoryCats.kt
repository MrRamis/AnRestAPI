package com.example.pr20.model

import android.provider.VoicemailContract
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RepositoryCats:IRepositoryCats {
    private val retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val catService =
        retrofit.create(CatService::class.java)

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getFacts(animalType: String,amount: Int): StateFlow<Pair<Status, List<ScheduleDTO>?>> {
        val result = catService.getFacts(animalType, amount);
        val resultOne = catService.getOneFacts(animalType, amount);
        val stateFlow = MutableStateFlow<Pair<Status, List<ScheduleDTO>?>>(Status.Woiting to null)
        scope.launch {
            try {
                val res = if (amount != 1) {result.execute()}
                else {resultOne.execute()}
                if (res.isSuccessful) {
                    val body = res.body()
                    if (body == null) {
                        stateFlow.emit(Status.Error to null)
                    } else {
                        if (amount != 1) {
                            stateFlow.emit(Status.OK to (body as List<Schedule>).map {
                                ScheduleDTO(
                                    it.text,
                                    it.type,
                                    LocalDate.parse(
                                        it.updatedAt,
                                        DateTimeFormatter.ISO_DATE_TIME
                                    )
                                )
                            })
                        } else {
                            stateFlow.emit(Status.OK to (body as Schedule).let {
                                listOf(
                                    ScheduleDTO(
                                        it.text,
                                        it.type,
                                        LocalDate.parse(
                                            it.updatedAt,
                                            DateTimeFormatter.ISO_DATE_TIME
                                        )
                                    )
                                )
                            })
                        }
                    }
                } else {
                    Log.d("mgkit", res.message())
                    stateFlow.emit(Status.Error to null)
                }
            } catch (ex: Exception) {
                stateFlow.emit(Status.Error to null)
            }
        }
        return stateFlow
    }

    companion object {
        const val BASE_URL = "https://cat-fact.herokuapp.com"
    }
}
