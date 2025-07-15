package com.example.pbms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pbms.model.data.DatabaseInstance
import com.example.pbms.nav.AppNavigation
import com.example.pbms.ui.theme.PBMSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseInstance.getDatabase(applicationContext)
        val bookDao = db.bookDao()
        enableEdgeToEdge()
        setContent {
            PBMSTheme{
                    AppNavigation()
            }
        }
    }
}
