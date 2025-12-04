package com.example.testappforgit.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testappforgit.ui.components.StaffDropdown
import com.example.testappforgit.ui.model.StaffMember
import com.example.testappforgit.ui.viewmodel.RosterViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

@Composable
fun RosterCalendarScreen(viewModel: RosterViewModel = viewModel()) {

    Log.d("RosterUI", "Staff list size: ${viewModel.staffList.size}")

    var newStaffName by remember { mutableStateOf("") }
    var selectedStaff by remember { mutableStateOf<StaffMember?>(null) }

    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    var currentWeek by remember { mutableStateOf(LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())) }
    var currentYear by remember { mutableStateOf(LocalDate.now().year) }

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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Roster Planner", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        // ===== Add Staff =====
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = newStaffName,
                onValueChange = { newStaffName = it },
                label = { Text("Staff Name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (newStaffName.isNotBlank()) {
                    viewModel.addStaff(newStaffName.trim())
                    newStaffName = ""
                }
            }) {
                Text("Add Staff")
            }
        }

        Spacer(Modifier.height(16.dp))

        // ===== Staff Dropdown =====
        StaffDropdown(
            staffList = viewModel.staffList,
            selectedStaff = selectedStaff,
            onStaffSelected = { staff ->
                selectedStaff = staff
                Log.d("RosterUI", "Selected staff: ${staff.name}")
            }
        )

        Spacer(Modifier.height(16.dp))

        // ===== Week Selector =====
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                currentWeek--
                if (currentWeek < 1) {
                    currentYear--
                    currentWeek = 52
                }
            }) { Text("◀") }

            // Compute first and last day of week for month display
            val weekFields = WeekFields.ISO
            val firstDayOfWeek = LocalDate.of(currentYear, 1, 4)
                .with(weekFields.weekOfWeekBasedYear(), currentWeek.toLong())
                .with(weekFields.dayOfWeek(), 1)
            val lastDayOfWeek = firstDayOfWeek.plusDays(6)

            val monthLabel = if (firstDayOfWeek.month == lastDayOfWeek.month) {
                firstDayOfWeek.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            } else {
                "${firstDayOfWeek.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}/" +
                        "${lastDayOfWeek.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}"
            }

            Text("Week $currentWeek – $monthLabel $currentYear", style = MaterialTheme.typography.titleMedium)

            Button(onClick = {
                currentWeek++
                if (currentWeek > 52) {
                    currentYear++
                    currentWeek = 1
                }
            }) { Text("▶") }
        }

        Spacer(Modifier.height(16.dp))

        // ===== Weekly Grid =====
        Row(modifier = Modifier.fillMaxWidth()) {
            daysOfWeek.forEach { day ->
                val date = getDateForDay(day)
                val assigned = viewModel.weeklyRoster[date]?.toMutableList() ?: mutableListOf()

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(140.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(4.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Day label
                    Text("${day} ${date.dayOfMonth}", style = MaterialTheme.typography.titleSmall)

                    Spacer(Modifier.height(4.dp))

                    // Assigned staff names
                    assigned.forEach { staff ->
                        Text(staff.name)
                    }

                    Spacer(Modifier.height(4.dp))

                    // Assign selected staff
                    selectedStaff?.let { staff ->
                        TextButton(onClick = { viewModel.assignStaff(date, staff) }) {
                            Text("Assign")
                        }
                    }

                    // Remove selected staff if assigned
                    selectedStaff?.takeIf { assigned.contains(it) }?.let { staff ->
                        TextButton(onClick = { viewModel.unassignStaff(date, staff) }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ===== Staff List with Remove =====
        Text("Staff List", style = MaterialTheme.typography.titleMedium)
        Column {
            viewModel.staffList.forEach { staffMember ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(staffMember.name, modifier = Modifier.weight(1f))
                    TextButton(onClick = { viewModel.removeStaff(staffMember) }) {
                        Text("Remove")
                    }
                }
            }
        }
    }
}
