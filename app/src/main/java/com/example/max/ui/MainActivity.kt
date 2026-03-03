package com.example.max.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.max.R
import com.example.max.data.max.repository.MaxRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var repository: MaxRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d("FIREBASE_TEST", "1. onCreate запущен") // Проверка, что до сюда дошли

        lifecycleScope.launch {
            Log.d("FIREBASE_TEST", "2. Корутина стартовала")
            try {
                repository.sendTestMessage()
                Log.d("FIREBASE_TEST", "3. УСПЕХ!")
                Toast.makeText(this@MainActivity, "ОТПРАВЛЕНО!", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("FIREBASE_TEST", "3. ОШИБКА: ${e.message}")
                Toast.makeText(this@MainActivity, "ОШИБКА: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }


    }

}