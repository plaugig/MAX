package com.example.max.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.max.R
import com.example.max.domain.MainInteractor
import com.example.max.ui.item.ItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val interactor: MainInteractor
) : ViewModel(){

    private val currentUserId = "my_test_uid"

    val message: LiveData<List<ItemData>> = interactor.getMessage(currentUserId)
        .map { list ->
            list.map { domainModel ->

                val isMineCheck = domainModel.senderId == currentUserId

                ItemData(
                    id = domainModel.id,
                    text = domainModel.text,
                    time = formatTime(domainModel.time.toLongOrNull() ?: 0L),
                    isMine = isMineCheck,
                    color = if (isMineCheck) R.color.purple else R.color.blue,
                    isSent = domainModel.isSent

                )
            }
        }.asLiveData()

   private fun formatTime(millis: Long): String {
        val sdf = java.text.SimpleDateFormat("HH.mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(millis))
    }
}