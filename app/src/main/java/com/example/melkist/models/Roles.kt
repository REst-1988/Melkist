package com.example.melkist.models

import com.example.melkist.R

data class Roles(
    val headManager: Role = Role(id = 1, title = "مدیر سایت", value = "headManager", null),
    val manager: Role = Role(id = 2, title = "مدیر املاک", value = "manager", R.drawable.manager),
    val supervisor: Role = Role(id = 3, title = "سرپرست", value = "supervisor", R.drawable.supervisor),
    val consultant: Role = Role(id = 4, title = "مشاور", value = "consultant", R.drawable.consultant),
    val normalUser: Role = Role(id = 5, title = "کاربر عادی", value = "normaluser", null),
    val dealer: Role = Role(id = 6, title = "معامله گر", value = "dealer", null)
)
