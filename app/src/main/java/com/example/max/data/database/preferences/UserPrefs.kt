package com.example.max.data.database.preferences


import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserPrefs @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences(
        "user_settings", Context.MODE_PRIVATE
    )

    companion object{
        private const val KEY_MY_ID = "my_personal_id"
    }
    fun saveMyId(id: String) {
       prefs.edit().putString(KEY_MY_ID, id).apply()
    }
    fun getMyID(): String {
        return prefs.getString(KEY_MY_ID, "") ?:""
    }
}