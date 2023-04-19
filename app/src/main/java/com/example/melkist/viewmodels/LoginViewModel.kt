package com.example.melkist.viewmodels

import androidx.lifecycle.ViewModel
import com.example.melkist.models.Role
import com.example.melkist.models.Roles


class LoginViewModel:  ViewModel(){
    val STATE_REAL_ESTATE = 0
    val STATE_USER = 1
    val SUB_STATE_MANAGER = 2
    val SUB_STATE_SUPERVISER = 3
    val SUB_STATE_CONSOLTANT = 4
    val SUB_STATE_NORMAL_USER = 5
    val SUB_STATE_DEALER = 6
    private var condition: Int = STATE_REAL_ESTATE
    private var subCondition: Int = SUB_STATE_MANAGER

    fun getRoles (): Roles = Roles()
    fun getCondition (): Int = condition

    fun setCondition(state: Int){
        condition = state
    }

    fun setSubCondition(state: Int, subState: Int){
        subCondition = state
    }
}