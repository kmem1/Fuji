package com.clownteam.profile_domain

data class ProfileActivityDay(
    val dateString: String,
    val completedTasksCount: Int,
    val type: ActivityDayType
)

enum class ActivityDayType(val fromCount: Int, val toCount: Int) {
    Level0(0, 0),
    Level1(1, 5),
    Level2(6, 10),
    Level3(11, 20),
    Level4(21, Int.MAX_VALUE);

    companion object {

        fun getTypeByCount(completedTasksCount: Int): ActivityDayType {
            val levels = listOf(Level0, Level1, Level2, Level3, Level4)

            for (level in levels) {
                if (completedTasksCount in level.fromCount..level.toCount) {
                    return level
                }
            }

            return Level0
        }
    }
}