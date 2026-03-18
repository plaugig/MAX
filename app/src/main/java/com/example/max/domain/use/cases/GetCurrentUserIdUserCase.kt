package com.example.max.domain.use.cases

import com.example.max.data.repository.MaxRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserIdUserCase @Inject constructor(
    private val maxRepository: MaxRepository
){
    fun getCurrentUserId(): Flow<String>{
       return maxRepository.getCurrentUserIdFromPrefs()
    }
}