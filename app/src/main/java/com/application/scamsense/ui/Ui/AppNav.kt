package com.application.scamsense.ui.Ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.application.scamsense.ui.Ui.screens.HistoryScreen
import com.application.scamsense.ui.Ui.screens.HomeScreen
import com.application.scamsense.ui.Ui.screens.ReportsScreen
import com.application.scamsense.ui.Ui.screens.ResultScreen
import com.application.scamsense.ui.Ui.screens.ScanScreen
import com.application.scamsense.ui.Ui.screens.SettingsScreen

private enum class Tab(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Home", Icons.Filled.Home),
    SCAN("scan", "Scan", Icons.Filled.Search),
    HISTORY("history", "History", Icons.Filled.History),
    REPORTS("reports", "Reports", Icons.Filled.Flag),
    SETTINGS("settings", "Settings", Icons.Filled.Settings)
}

private const val RESULT = "result"

@Composable
fun ScamSenseApp(vm: AppViewModel = viewModel()) {
    val nav = rememberNavController()
    val route = nav.currentBackStackEntryAsState().value?.destination?.route

    fun goTab(r: String) = nav.navigate(r) {
        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                Tab.entries.forEach { tab ->
                    val selected = route == tab.route || (route == RESULT && tab == Tab.SCAN)
                    val s by animateFloatAsState(
                        if (selected) 1.2f else 1f,
                        spring(Spring.DampingRatioMediumBouncy), label = "tabScale"
                    )
                    NavigationBarItem(
                        selected = selected,
                        onClick = { goTab(tab.route) },
                        icon = { Icon(tab.icon, tab.label, Modifier.scale(s)) },
                        label = { Text(tab.label, style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Tab.HOME.route,
            modifier = Modifier.padding(padding),
            enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 10 } },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(300)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) {
            composable(Tab.HOME.route) {
                HomeScreen(vm,
                    onScan = { goTab(Tab.SCAN.route) },
                    onHistory = { goTab(Tab.HISTORY.route) },
                    onReports = { goTab(Tab.REPORTS.route) },
                    onSettings = { goTab(Tab.SETTINGS.route) })
            }
            composable(Tab.SCAN.route) { ScanScreen(vm, onResult = { nav.navigate(RESULT) }) }
            composable(RESULT) { ResultScreen(vm, onBack = { nav.popBackStack() }) }
            composable(Tab.HISTORY.route) { HistoryScreen(vm) }
            composable(Tab.REPORTS.route) { ReportsScreen(vm) }
            composable(Tab.SETTINGS.route) { SettingsScreen(vm) }
        }
    }
}
