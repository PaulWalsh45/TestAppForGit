package com.example.testappforgit.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.testappforgit.ui.model.StaffMember
import com.example.testappforgit.ui.viewmodel.RosterViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.WeekFields
import java.util.Locale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


@Composable
fun RosterPlanScreen(navController: NavHostController, viewModel: RosterViewModel = viewModel()) {

    var selectedStaff by remember { mutableStateOf<StaffMember?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Track current week and year
    var currentWeek by remember { mutableStateOf(LocalDate.now().get(WeekFields.ISO.weekOfWeekBasedYear())) }
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }

    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    // Helper to get LocalDate for each day of ISO week
    fun getDateForDay(dayName: String): LocalDate {
        val weekFields = WeekFields.ISO
        val firstDayOfWeek = LocalDate.of(currentYear, 1, 4)
            .with(weekFields.weekOfWeekBasedYear(), currentWeek.toLong())
            .with(weekFields.dayOfWeek(), 1) // Monday

        val dayOfWeek = when (dayName.take(3).lowercase()) {
            "mon" -> 1
            "tue" -> 2
            "wed" -> 3
            "thu" -> 4
            "fri" -> 5
            "sat" -> 6
            "sun" -> 7
            else -> 1
        }

        return firstDayOfWeek.with(weekFields.dayOfWeek(), dayOfWeek.toLong())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Weekly Roster Planner", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        // Week selector
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                currentWeek--
                if (currentWeek < 1) {
                    currentYear--
                    currentWeek = 52
                }
            }) { Text("◀") }

            val weekFields = WeekFields.ISO
            val firstDayOfWeek = LocalDate.of(currentYear, 1, 4)
                .with(weekFields.weekOfWeekBasedYear(), currentWeek.toLong())
                .with(weekFields.dayOfWeek(), 1)
            val thursdayOfWeek = firstDayOfWeek.with(weekFields.dayOfWeek(), 4)
            val monthLabel = thursdayOfWeek.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

            Text("Week $currentWeek – $monthLabel $currentYear", style = MaterialTheme.typography.titleMedium)

            Button(onClick = {
                currentWeek++
                if (currentWeek > 52) {
                    currentYear++
                    currentWeek = 1
                }
            }) { Text("▶") }
        }

        Spacer(Modifier.height(24.dp))

        // Staff selector
        Text("Select Staff:", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Box {
            OutlinedButton(onClick = { dropdownExpanded = true }) {
                Text(selectedStaff?.name ?: "Choose Staff")
            }

            DropdownMenu(expanded = dropdownExpanded, onDismissRequest = { dropdownExpanded = false }) {
                viewModel.staffList.forEach { staff ->
                    DropdownMenuItem(
                        text = { Text(staff.name) },
                        onClick = {
                            selectedStaff = staff
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Dynamic day grid
        daysOfWeek.forEach { day ->
            val date = getDateForDay(day) // exact LocalDate key
            val assignedStaff = viewModel.weeklyRoster[date]?.joinToString { it.name } ?: ""

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${day} ${date.dayOfMonth}")
                Text(assignedStaff)

                Spacer(modifier = Modifier.weight(1f))

                if (selectedStaff != null) {
                    IconButton(
                        onClick = { viewModel.assignStaff(date, selectedStaff!!) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add"
                        )
                    }

                    IconButton(
                        onClick = { viewModel.unassignStaff(date, selectedStaff!!) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Remove"
                        )
                    }
                }

            }
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(24.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("welcome") }
        ) {
            Text("Home")
        }
    }
}
