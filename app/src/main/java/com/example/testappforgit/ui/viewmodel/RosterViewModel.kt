package com.example.testappforgit.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.testappforgit.ui.model.StaffMember
import com.example.testappforgit.ui.model.StaffRank
import org.threeten.bp.LocalDate

class RosterViewModel : ViewModel() {

    // Staff list
    private val _staffList = mutableStateListOf<StaffMember>()
    val staffList: List<StaffMember> get() = _staffList

    // Weekly roster: Map<LocalDate, SnapshotStateList<StaffMember>>
    private val _weeklyRoster = mutableStateMapOf<LocalDate, SnapshotStateList<StaffMember>>()
    val weeklyRoster: Map<LocalDate, List<StaffMember>> get() = _weeklyRoster

    init {
        if (_staffList.isEmpty()) {
            _staffList.addAll(
                listOf(
                    StaffMember("Paul", StaffRank.MANAGER),
                    StaffMember("Richie", StaffRank.MANAGER),
                    StaffMember("Ash", StaffRank.STAFF),
                    StaffMember("Billy", StaffRank.STAFF),
                    StaffMember("Tom", StaffRank.STAFF),
                    StaffMember("Dick", StaffRank.STAFF),
                    StaffMember("Harry", StaffRank.STAFF),
                    StaffMember("Paddy", StaffRank.STAFF),
                    StaffMember("Patricia", StaffRank.STAFF),
                    StaffMember("Julie", StaffRank.STAFF),
                    StaffMember("Ashling", StaffRank.TRAINEE),
                    StaffMember("Mags", StaffRank.MANAGER),
                    StaffMember("Michele", StaffRank.SUPERVISOR)
                )
            )
        }
    }

    // Add a new staff member
    fun addStaff(name: String, rank: StaffRank = StaffRank.STAFF) {
        if (name.isNotBlank() && _staffList.none { it.name == name }) {
            _staffList.add(StaffMember(name, rank))
        }
    }

    // Remove a staff member and unassign from all days
    fun removeStaff(member: StaffMember) {
        _staffList.remove(member)
        _weeklyRoster.forEach { (_, dayList) -> dayList.remove(member) }
    }

    // Assign staff to a day
    fun assignStaff(date: LocalDate, staff: StaffMember) {
        val list = _weeklyRoster.getOrPut(date) { mutableStateListOf() }
        if (!list.contains(staff)) list.add(staff)
    }

    // Unassign staff from a day
    fun unassignStaff(date: LocalDate, staff: StaffMember) {
        _weeklyRoster[date]?.remove(staff)
    }
}
