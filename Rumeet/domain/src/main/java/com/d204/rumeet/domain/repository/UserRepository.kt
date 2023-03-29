package com.d204.rumeet.domain.repository

import com.d204.rumeet.domain.NetworkResult
import com.d204.rumeet.domain.model.user.UserInfoDomainModel
import java.io.File
import com.d204.rumeet.domain.model.user.UserModel

interface UserRepository {
    suspend fun setUserFirstRunCheck(): Boolean
    suspend fun getUserFirstRunCheck(): Boolean
    suspend fun getUserId(): Int
    suspend fun getUserInfo(userId: Int): NetworkResult<UserInfoDomainModel>
    suspend fun searchUsers(nickname : String) : NetworkResult<List<UserModel>>
}