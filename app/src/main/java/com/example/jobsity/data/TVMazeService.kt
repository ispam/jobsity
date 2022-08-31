package com.example.jobsity.data

import com.example.jobsity.data.local.ShowItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TVMazeService {

    @GET("shows")
    suspend fun getShows(
        @Query("page")
        page: Int
    ): Response<List<ShowItem>>
}