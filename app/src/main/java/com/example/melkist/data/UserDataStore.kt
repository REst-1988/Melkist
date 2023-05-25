package com.example.melkist.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.melkist.models.LoginResponseModel
import com.example.melkist.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val USER_PREFERENCES = "user_preferences"
private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES)

class UserDataStore(context: Context) {
    private val userIdPk = intPreferencesKey("id") // userIdPreferencesKey
    private val userFirstNamePk = stringPreferencesKey("name")
    private val userLastNamePk = stringPreferencesKey("family")
    private val userMobileIdPk = intPreferencesKey("mobileId")
    private val userMobileNoPk = stringPreferencesKey("mobileNo")
    private val userPasswordPk = stringPreferencesKey("password")
    private val userprofilePicPk = stringPreferencesKey("profilePic")
    private val userRoleIdPk = intPreferencesKey("roleId")
    private val userParentIdPk = intPreferencesKey("parentId")
    private val userIsVerifyPk = booleanPreferencesKey("isverify")
    private val userCityIdPk = intPreferencesKey("city_id")
    private val userCityTitlePk = stringPreferencesKey("city_title")
    private val userProvinceIdPk = intPreferencesKey("province_id")
    private val userProvinceTitlePk = stringPreferencesKey("province_title")
    private val userEmailPk = stringPreferencesKey("email")
    private val tokenPk = stringPreferencesKey("token")

    suspend fun saveUserToPreferencesStore(
        mobileNo: String,
        password: String,
        user: LoginResponseModel,
        context: Context
    ) {
        context.dataStore.edit { preferences ->
            user.data?.let {
                preferences[userIdPk] = user.data!!.id!!
                preferences[userFirstNamePk] = user.data!!.firstName!!
                preferences[userLastNamePk] = user.data!!.lastName!!
                preferences[userMobileIdPk] = user.data!!.mobileId!!
                preferences[userMobileNoPk] = mobileNo
                preferences[userPasswordPk] = password
                preferences[userprofilePicPk] = user.data!!.profilePic!!
                preferences[userRoleIdPk] = user.data!!.roleId!!
                preferences[userParentIdPk] = user.data!!.parentId!!
                preferences[userIsVerifyPk] = user.data!!.isVerify!!
                preferences[userCityIdPk] = user.data!!.city!!.cityId!!
                preferences[userCityTitlePk] = user.data!!.city!!.cityTitle!!
                preferences[userProvinceIdPk] = user.data!!.city!!.province!!.provinceId!!
                preferences[userProvinceTitlePk] = user.data!!.city!!.province!!.provinceTitle!!
                preferences[userEmailPk] = user!!.data!!.email!!
                preferences[tokenPk] = user.token.toString()
            }
        }
    }

    val preferenceFlow: Flow<User> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            User(
                preferences[userIdPk],
                preferences[userFirstNamePk],
                preferences[userLastNamePk],
                preferences[userMobileIdPk],
                preferences[userMobileNoPk],
                preferences[userEmailPk],
                preferences[userPasswordPk],
                preferences[userprofilePicPk],
                preferences[userRoleIdPk],
                preferences[userParentIdPk],
                preferences[userIsVerifyPk],
                preferences[userCityIdPk],
                preferences[userCityTitlePk],
                preferences[userProvinceIdPk],
                preferences[userProvinceTitlePk],
                preferences[tokenPk]
            )
        }
}

