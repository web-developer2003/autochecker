package com.autochecker.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.autochecker.R
import com.autochecker.ui.theme.AccentRed
import com.autochecker.ui.theme.AutoCheckerTheme

data class BottomNavItem(
    val route: String,
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        labelResId = R.string.nav_home,
        selectedIcon = Icons.Filled.DirectionsCar,
        unselectedIcon = Icons.Outlined.DirectionsCar,
    ),
    BottomNavItem(
        route = Screen.History.route,
        labelResId = R.string.nav_history,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
    ),
    BottomNavItem(
        route = Screen.Profile.route,
        labelResId = R.string.nav_profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    NavigationBar(
        containerColor = AutoCheckerTheme.colors.bottomNavBackground,
        contentColor = AutoCheckerTheme.colors.textPrimary,
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = stringResource(item.labelResId),
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(stringResource(item.labelResId)) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentRed,
                    selectedTextColor = AccentRed,
                    unselectedIconColor = AutoCheckerTheme.colors.bottomNavInactive,
                    unselectedTextColor = AutoCheckerTheme.colors.bottomNavInactive,
                    indicatorColor = Color.Transparent,
                ),
            )
        }
    }
}
