package com.autochecker

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.autochecker.data.local.DataStoreManager
import com.autochecker.data.local.TokenManager
import com.autochecker.ui.navigation.AppNavGraph
import com.autochecker.ui.theme.AutoCheckerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val theme by dataStoreManager.theme.collectAsState(initial = "dark")
            val isDarkTheme = theme != "light"

            AutoCheckerTheme(isDarkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AutoCheckerTheme.colors.background
                ) {
                    AppNavGraph(tokenManager = tokenManager)
                }
            }
        }
    }
}
