package com.example.testappforgit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testappforgit.ui.screens.LoginScreen
import com.example.testappforgit.ui.screens.RegisterScreen
import com.example.testappforgit.ui.screens.ForgotPasswordScreen

import com.example.testappforgit.ui.screens.WelcomeScreen
import com.example.testappforgit.ui.theme.TestAppForGitTheme
import com.example.testappforgit.ui.screens.RosterPlanScreen
import com.jakewharton.threetenabp.AndroidThreeTen
import com.example.testappforgit.ui.screens.AddStaffScreen
import com.example.testappforgit.ui.screens.RosterCalendarScreen




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidThreeTen.init(this)
        setContent {
            TestAppForGitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") { LoginScreen(navController) }
                        composable("register") { RegisterScreen(navController) }
                        composable("welcome") { WelcomeScreen(navController) }
                        composable("plan") { RosterPlanScreen(navController) }
                        composable("addStaff") { AddStaffScreen(navController) }
                        composable("calendar") { RosterCalendarScreen() }
                        composable("forgotPassword") { ForgotPasswordScreen(navController) }


                    }

                }
            }
        }
    }
}
