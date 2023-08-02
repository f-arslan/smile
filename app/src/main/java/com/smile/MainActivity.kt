package com.smile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.smile.ui.theme.SmileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // For proper ime padding
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SmileTheme {
                SmileApp()
            }
        }
    }
}
