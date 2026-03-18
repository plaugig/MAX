package com.example.max.domain.use.cases

import com.example.max.data.repository.MaxRepository
import javax.inject.Inject

class SingInUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    suspend fun singIn(email: String, password: String){
        repository.singIn(email,password)
    }

}