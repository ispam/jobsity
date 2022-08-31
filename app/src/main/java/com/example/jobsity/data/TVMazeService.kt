package com.example.jobsity.data

import retrofit2.http.GET
import retrofit2.http.Query

interface TVMazeService {

    @GET("shows")
    suspend fun getSeries(
        @Query("page")
        page: Int
    )
}