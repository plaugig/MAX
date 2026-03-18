package com.example.max.domain.use.cases

import com.example.max.data.repository.MaxRepository
import javax.inject.Inject

class SingUpUseCase @Inject constructor(
    private val repository: MaxRepository
) {
    suspend fun singUp(email: String, password: String, name: String){
        repository.singUp(email,password,name)
    }
}