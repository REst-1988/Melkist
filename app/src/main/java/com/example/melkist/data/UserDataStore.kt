package com.example.melkist.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.melkist.models.LoginResponseModel
import com.example.melkist.models.PublicResponseModel
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
    private val userprofilePicPk = stringPreferencesKey("profilePic")
    private val userRoleIdPk = intPreferencesKey("roleId")
    private val userParentIdPk = intPreferencesKey("parentId")
    private val userIsFirstTimePk = booleanPreferencesKey("isfirsttime")
    private val userCityIdPk = intPreferencesKey("city_id")
    private val userCityTitlePk = stringPreferencesKey("city_title")
    private val userProvinceIdPk = intPreferencesKey("province_id")
    private val userProvinceTitlePk = stringPreferencesKey("province_title")
    private val userEmailPk = stringPreferencesKey("email")
    private val tokenPk = stringPreferencesKey("token")


    suspend fun emptyPreferences(context: Context) {
        context.dataStore.edit {
            it.clear()
        }
    }

    suspend fun saveUserToPreferencesStore(
        user: LoginResponseModel, context: Context
    ) {
        context.dataStore.edit { preferences ->
            user.data?.let {
                preferences[userIdPk] = user.data!!.id!!
                preferences[userFirstNamePk] = user.data!!.firstName!!
                preferences[userLastNamePk] = user.data!!.lastName!!
                preferences[userMobileIdPk] = user.data!!.mobileId!!
                preferences[userprofilePicPk] = user.data!!.profilePic ?: "" // should be ""
                // I use "" to check if user need to navigate to login or mainAct
                preferences[userRoleIdPk] = user.data!!.roleId!!
                preferences[userParentIdPk] =
                    user.data!!.parentId ?: 0 // TODO: must be change every user must have a parent
                preferences[userIsFirstTimePk] = user.data!!.isFirstTime!!
                preferences[userCityIdPk] = user.data!!.city!!.cityId!!
                preferences[userCityTitlePk] = user.data!!.city!!.cityTitle!!
                preferences[userProvinceIdPk] = user.data!!.city!!.province!!.provinceId!!
                preferences[userProvinceTitlePk] = user.data!!.city!!.province!!.provinceTitle!!
                preferences[userEmailPk] = user.data!!.email ?: ""
                preferences[tokenPk] = "Bearer " + user.token.toString()
            }
        }
    }

    suspend fun saveImage(context: Context, uploadResponseModel: PublicResponseModel) {
        context.dataStore.edit { preferences ->
            uploadResponseModel.message?.let {
                preferences[userprofilePicPk] = uploadResponseModel.message!!
                preferences[userIsFirstTimePk] = true
            }
        }
    }

    val preferenceFlow: Flow<User> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        User(
            id = preferences[userIdPk],
            firstName = preferences[userFirstNamePk],
            lastName = preferences[userLastNamePk],
            mobileId = preferences[userMobileIdPk],
            email = preferences[userEmailPk],
            profilePic = preferences[userprofilePicPk],
            roleId = preferences[userRoleIdPk],
            parentId = preferences[userParentIdPk],
            isFirstTime = preferences[userIsFirstTimePk],
            cityId = preferences[userCityIdPk],
            cityTitle = preferences[userCityTitlePk],
            provinceId = preferences[userProvinceIdPk],
            provinceTitle = preferences[userProvinceTitlePk],
            token = preferences[tokenPk]
        )
    }

    val preferencesFlow: Flow<String?> = context.dataStore.data.catch {
        if (it is IOException) {
            it.printStackTrace()
            emit(emptyPreferences())
        } else {
            throw it
        }
    }.map { preferences ->
        preferences[tokenPk]
    }
}

