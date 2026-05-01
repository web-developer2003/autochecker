package com.autochecker

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.autochecker.data.local.DataStoreManager
import com.autochecker.data.local.TokenManager
import com.autochecker.ui.navigation.AppNavGraph
import com.autochecker.ui.navigation.Screen
import com.autochecker.ui.theme.AutoCheckerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    @Inject
    lateinit var tokenManager: TokenManager

    private val startDestination = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Keep splash screen visible until the token check finishes
        splashScreen.setKeepOnScreenCondition { startDestination.value == null }

        // Read stored token on a background coroutine
        lifecycleScope.launch {
            val token = tokenManager.token.first()
            startDestination.value = if (token != null) Screen.Home.route else Screen.Login.route
        }

        setContent {
            val theme by dataStoreManager.theme.collectAsState(initial = "dark")
            val isDarkTheme = theme != "light"

            AutoCheckerTheme(isDarkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AutoCheckerTheme.colors.background
                ) {
                    val dest = startDestination.value
                    if (dest != null) {
                        AppNavGraph(startDestination = dest)
                    }
                }
            }
        }
    }
}
