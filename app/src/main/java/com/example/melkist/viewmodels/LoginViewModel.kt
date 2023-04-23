package com.example.melkist.viewmodels

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.melkist.models.Role
import com.example.melkist.models.Roles
import com.example.melkist.models.User


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

    /*private */var realEstateName: String? = null
    /*private*/ /*lateinit*/ var firstName: String = ""
    /*private*/ /*lateinit*/ var lastName: String = ""
    /*private*/ /*lateinit*/ var mobileNo: String = ""
    /*private*/ var nationalCode: Long = 0
    /*private*/ var email: String? = null
    /*private*/ /*lateinit*/ var password: String = ""
    /*private*/ var roleId: Int = 0
    /*private*/ var parentId: Int? = null
    /*private*/ var isVerify: Boolean = false
    /*private*/ /*lateinit*/ /*var user: User */

    fun getRoles (): Roles = Roles()
    fun getCondition (): Int = condition

    fun setCondition(state: Int){
        condition = state
    }

    fun setSubCondition(subCondition: Int){
        this.subCondition = subCondition
    }
    fun getSubCondition(): Int = subCondition

    fun createUser(
        realEstateName: String?,
        firstName: String,
        lastName: String,
        mobileNo: String,
        nationalCode: Long,
        email: String?,
        password: String
    ){
        this.realEstateName = realEstateName
        this.firstName = firstName
        this.lastName = lastName
        this.mobileNo = mobileNo
        this.nationalCode = nationalCode
        this.email = email
        this.password = password

    }
}