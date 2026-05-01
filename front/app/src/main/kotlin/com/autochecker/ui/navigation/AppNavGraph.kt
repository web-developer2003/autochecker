package com.autochecker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.autochecker.data.local.TokenManager
import com.autochecker.ui.auth.LoginScreen
import com.autochecker.ui.auth.SignUpScreen
import com.autochecker.ui.accident.ReportAccidentScreen
import com.autochecker.ui.home.HomeScreen
import com.autochecker.ui.history.HistoryScreen
import com.autochecker.ui.profile.ProfileScreen
import com.autochecker.ui.profile.EditProfileScreen
import com.autochecker.ui.report.VehicleSummaryScreen
import com.autochecker.ui.report.FullReportScreen
import com.autochecker.ui.report.AccidentsListScreen
import com.autochecker.ui.report.AccidentDetailScreen
import com.autochecker.ui.vehicles.SavedVehiclesScreen
import com.autochecker.ui.vehicles.AddVehicleScreen
import com.autochecker.ui.settings.NotificationsScreen
import com.autochecker.ui.settings.AppearanceScreen
import com.autochecker.ui.settings.LanguageScreen
import com.autochecker.ui.settings.PrivacyPolicyScreen
import com.autochecker.ui.settings.TermsOfServiceScreen
import com.autochecker.ui.theme.AutoCheckerTheme
import kotlinx.coroutines.flow.first

private val screensWithBottomBar = setOf(
    Screen.Home.route,
    Screen.History.route,
    Screen.Profile.route,
)

@Composable
fun AppNavGraph(tokenManager: TokenManager) {
    val startDestination by produceState<String?>(initialValue = null) {
        val token = tokenManager.token.first()
        value = if (token != null) Screen.Home.route else Screen.Login.route
    }

    val resolvedStart = startDestination ?: return

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = AutoCheckerTheme.colors.background,
        bottomBar = {
            if (currentRoute in screensWithBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = resolvedStart,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth
            composable(Screen.Login.route) {
                LoginScreen(
                    onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.SignUp.route) {
                SignUpScreen(
                    onNavigateToLogin = { navController.popBackStack() },
                    onSignUpSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // Main tabs
            composable(Screen.Home.route) {
                HomeScreen(
                    onSearchResult = { vin ->
                        navController.navigate(Screen.VehicleSummary.createRoute(vin))
                    },
                    onReportAccident = {
                        navController.navigate(Screen.ReportAccident.route)
                    }
                )
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    onVehicleClick = { vin ->
                        navController.navigate(Screen.VehicleSummary.createRoute(vin))
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onEditProfile = { navController.navigate(Screen.EditProfile.route) },
                    onSavedVehicles = { navController.navigate(Screen.SavedVehicles.route) },
                    onNotifications = { navController.navigate(Screen.Notifications.route) },
                    onAppearance = { navController.navigate(Screen.Appearance.route) },
                    onLanguage = { navController.navigate(Screen.Language.route) },
                    onPrivacyPolicy = { navController.navigate(Screen.PrivacyPolicy.route) },
                    onTermsOfService = { navController.navigate(Screen.TermsOfService.route) },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Report
            composable(
                route = Screen.VehicleSummary.route,
                arguments = listOf(navArgument("vin") { type = NavType.StringType })
            ) {
                VehicleSummaryScreen(
                    onBack = { navController.popBackStack() },
                    onViewFullReport = { vin ->
                        navController.navigate(Screen.FullReport.createRoute(vin))
                    }
                )
            }
            composable(
                route = Screen.FullReport.route,
                arguments = listOf(navArgument("vin") { type = NavType.StringType })
            ) {
                FullReportScreen(
                    onBack = { navController.popBackStack() },
                    onViewAccidents = { vin ->
                        navController.navigate(Screen.AccidentsList.createRoute(vin))
                    }
                )
            }
            composable(
                route = Screen.AccidentsList.route,
                arguments = listOf(navArgument("vin") { type = NavType.StringType })
            ) {
                AccidentsListScreen(
                    onBack = { navController.popBackStack() },
                    onAccidentClick = { accidentId ->
                        navController.navigate(Screen.AccidentDetail.createRoute(accidentId))
                    }
                )
            }
            composable(
                route = Screen.AccidentDetail.route,
                arguments = listOf(navArgument("accidentId") { type = NavType.StringType })
            ) {
                AccidentDetailScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // Report accident
            composable(Screen.ReportAccident.route) {
                ReportAccidentScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            // Profile sub-screens
            composable(Screen.EditProfile.route) {
                EditProfileScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.SavedVehicles.route) {
                SavedVehiclesScreen(
                    onBack = { navController.popBackStack() },
                    onVehicleClick = { vin ->
                        navController.navigate(Screen.VehicleSummary.createRoute(vin))
                    }
                )
            }
            composable(Screen.AddVehicle.route) {
                AddVehicleScreen(onBack = { navController.popBackStack() })
            }

            // Settings
            composable(Screen.Notifications.route) {
                NotificationsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Appearance.route) {
                AppearanceScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Language.route) {
                LanguageScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.PrivacyPolicy.route) {
                PrivacyPolicyScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.TermsOfService.route) {
                TermsOfServiceScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
