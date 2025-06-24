package co.edu.udea.compumovil.gr07_20251.lab2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.edu.udea.compumovil.gr07_20251.lab2.ui.screens.HomeScreen
import co.edu.udea.compumovil.gr07_20251.lab2.ui.screens.PodcastDetailScreen

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("detail/{feedUrl}") { backStackEntry ->
            val feedUrl = backStackEntry.arguments?.getString("feedUrl")
            if (feedUrl != null) {
                PodcastDetailScreen(feedUrl)
            }
        }

    }
}
