package com.example.mentorai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mentorai.ui.screens.CategoryScreen
import com.example.mentorai.ui.screens.ChatScreen
import com.example.mentorai.ui.screens.LoginScreen
import com.example.mentorai.ui.screens.SignUpScreen
import com.example.mentorai.ui.theme.MentorAITheme
import com.example.mentorai.ui.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MentorAITheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(authViewModel: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsState()
    var currentRoute by remember { mutableStateOf(if (isUserLoggedIn) "home" else "login") }

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            currentRoute = backStackEntry.destination.route ?: ""
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute == "home") {
                BottomBar(navController = navController, authViewModel = authViewModel)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (isUserLoggedIn) "home" else "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") {
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                    navController = navController
                )
            }
            composable("signup") {
                SignUpScreen(navController = navController)
            }
            composable("home") {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Home Screen", style = MaterialTheme.typography.headlineMedium)
                }
            }
            composable("categories") {
                CategoryScreen(navController = navController)
            }
            composable("chat/{categoryName}") { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                ChatScreen(category = categoryName)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController, authViewModel: AuthViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("home") }) {
                Icon(Icons.Default.Home, contentDescription = "Ana Sayfa", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = {navController.navigate("categories")
            }) {
                Icon(Icons.Default.Category, contentDescription = "Kategoriler", tint = MaterialTheme.colorScheme.primary)
            }
            IconButton(onClick = {
                authViewModel.logoutUser()
                navController.navigate("login") { popUpTo(0) { inclusive = true } }
            }) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Çıkış", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
