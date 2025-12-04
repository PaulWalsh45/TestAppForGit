package com.example.testappforgit.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.testappforgit.ui.model.StaffRank
import com.example.testappforgit.ui.viewmodel.RosterViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


@Composable
fun AddStaffScreen(navController: NavHostController, viewModel: RosterViewModel = viewModel()) {

    var newStaffName by remember { mutableStateOf("") }
    var selectedRank by remember { mutableStateOf(StaffRank.STAFF) } // default rank
    var expanded by remember { mutableStateOf(false) } // for rank dropdown

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // ===== Add Staff Row =====
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = newStaffName,
                onValueChange = { newStaffName = it },
                label = { Text("Staff Name") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Rank dropdown
            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Text(selectedRank.name)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    StaffRank.values().forEach { rank ->
                        DropdownMenuItem(
                            text = { Text(rank.name) },
                            onClick = {
                                selectedRank = rank
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (newStaffName.isNotBlank()) {
                        viewModel.addStaff(newStaffName.trim(), selectedRank)
                        newStaffName = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Staff"
                )
            }
        }

//            Button(onClick = {
//                if (newStaffName.isNotBlank()) {
//                    viewModel.addStaff(newStaffName.trim(), selectedRank)
//                    newStaffName = ""
//                }
//            }) {
//                Text("Add")
//            }
//        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { navController.navigate("welcome") }
        ) {
            Text("Home")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ===== Scrollable Staff List =====
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 4.dp)
        ) {
            // Group staff by rank and sort descending (MANAGER first)
            val groupedStaff = viewModel.staffList
                .groupBy { it.rank }
                .toSortedMap(compareBy { it.ordinal })

            groupedStaff.forEach { (rank, staffMembers) ->
                item {
                    Text(
                        text = rank.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(staffMembers) { staffMember ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = staffMember.name,
                            modifier = Modifier.weight(1f)
                        )

                        TextButton(onClick = { viewModel.removeStaff(staffMember) }) {
                            Text("Remove")
                        }
                    }
                }
            }
        }
    }
}
