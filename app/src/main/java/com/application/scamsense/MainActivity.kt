package com.application.scamsense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.application.scamsense.ui.Ui.ScamSenseApp
import com.application.scamsense.ui.theme.ScamSenseTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScamSenseTheme {
                ScamSenseApp()
            }
        }
    }
}
