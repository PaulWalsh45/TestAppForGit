package com.example.testappforgit.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.testappforgit.ui.model.StaffMember
import android.util.Log

@Composable
fun StaffDropdown(
    staffList: List<StaffMember>,
    selectedStaff: StaffMember?,
    onStaffSelected: (StaffMember) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {

        // Button to toggle dropdown
        OutlinedButton(
            onClick = { expanded = !expanded }, // toggle instead of always true
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = selectedStaff?.name ?: "Choose Staff")
        }

        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth() // match the button width
        ) {
            staffList.forEach { staff ->
                DropdownMenuItem(
                    text = { Text(staff.name) },
                    onClick = {
                        onStaffSelected(staff)
                        expanded = false
                        Log.d("RosterUI", "Selected staff: ${staff.name}") // put breakpoint here
                    }
                )
            }
        }
    }
}
