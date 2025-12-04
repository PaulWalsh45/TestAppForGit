package com.example.testappforgit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Welcome to the Staff Roster App",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "This app is for creating and viewing weekly staff rosters quickly and easily.\n\n" +
                    "Managers will have full admin permissions to enable management of staff & rosters.\n\n" +
                    "Staff will have read only permissions for viewing rosters.\n\n" +
            "You can:\n\n" +
                    "• Add staff members\n" +
                    "• Build weekly schedules\n" +
                    "• Save and update rosters anytime\n"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("plan") }
        ) {
            Text("Roster Planner")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("addStaff") }
        ) {
            Text("Edit Staff")
        }
    }
}
