package com.autochecker.ui.navigation

sealed class Screen(val route: String) {
    // Auth
    data object Login : Screen("login")
    data object SignUp : Screen("signup")

    // Main tabs
    data object Home : Screen("home")
    data object History : Screen("history")
    data object Profile : Screen("profile")

    // Report
    data object VehicleSummary : Screen("vehicle_summary/{vin}") {
        fun createRoute(vin: String) = "vehicle_summary/$vin"
    }
    data object FullReport : Screen("full_report/{vin}") {
        fun createRoute(vin: String) = "full_report/$vin"
    }
    data object AccidentsList : Screen("accidents_list/{vin}") {
        fun createRoute(vin: String) = "accidents_list/$vin"
    }
    data object AccidentDetail : Screen("accident_detail/{accidentId}") {
        fun createRoute(accidentId: String) = "accident_detail/$accidentId"
    }

    // Internet search result
    data object InternetResult : Screen("internet_result/{query}") {
        fun createRoute(query: String) = "internet_result/$query"
    }

    // Accident reporting
    data object ReportAccident : Screen("report_accident")

    // Profile sub-screens
    data object EditProfile : Screen("edit_profile")
    data object SavedVehicles : Screen("saved_vehicles")
    data object AddVehicle : Screen("add_vehicle")

    // Settings
    data object Notifications : Screen("notifications")
    data object Appearance : Screen("appearance")
    data object Language : Screen("language")
    data object PrivacyPolicy : Screen("privacy_policy")
    data object TermsOfService : Screen("terms_of_service")
}
