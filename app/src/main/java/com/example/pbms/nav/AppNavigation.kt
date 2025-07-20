package com.example.pbms.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pbms.view.AddScreen
import com.example.pbms.view.HomeScreen
import com.example.pbms.view.EditScreen

// handles navigation
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route// Default screen
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(Screen.AddScreen.route) {
            AddScreen(navController)
        }
        composable(
            route = Screen.EditScreen.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: return@composable
            EditScreen(bookId = bookId, navController = navController)
            }
        }
    }