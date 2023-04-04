package com.d204.rumeet.domain.usecase.user

import com.d204.rumeet.domain.repository.UserRepository
import javax.inject.Inject

class GetAcquiredBadgeListUseCase @Inject constructor(private val userRepository: UserRepository){
    suspend operator fun invoke(userId: Int) =  userRepository.getAcquiredBadgeList(userId)
}