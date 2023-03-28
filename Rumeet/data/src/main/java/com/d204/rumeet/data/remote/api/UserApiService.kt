package com.d204.rumeet.data.remote.api

import com.d204.rumeet.data.remote.dto.response.BaseResponse
import com.d204.rumeet.data.remote.dto.response.user.FriendResponseDto
import com.d204.rumeet.data.remote.dto.response.user.UserResponseDto
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

internal interface UserApiService {
    @GET("/users/search")
    fun searchUsers(
        @Query("nickname") nickname : String
    ) : BaseResponse<List<UserResponseDto?>>
}