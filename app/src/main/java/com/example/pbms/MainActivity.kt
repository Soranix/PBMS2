package com.example.pbms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.pbms.model.data.DatabaseInstance
import com.example.pbms.nav.AppNavigation
import com.example.pbms.ui.theme.PBMSTheme
import com.example.pbms.viewmodel.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PBMSTheme{
                // loads nav graph
                // Database is Instantiated in HomeViewModel
                    AppNavigation()
            }
        }
    }
}
