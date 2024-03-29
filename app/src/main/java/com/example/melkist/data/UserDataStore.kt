package com.example.melkist.data

import android.annotation.SuppressLint
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

object Ds {
    @SuppressLint("StaticFieldLeak")
    var instance: UserDataStore? = null
    fun getDataStore(context: Context) : UserDataStore{
        if (instance == null)
            instance =  UserDataStore(context)
        return instance!!
    }
}

class UserDataStore(private val context: Context) {
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
    private val userRealEstatePk = stringPreferencesKey("real_estate")
    private val tokenPk = stringPreferencesKey("token")

    suspend fun emptyPreferences(): Preferences {
        return context.dataStore.edit {
            it.clear()
        }
    }

    suspend fun saveUserToPreferencesStore(
        user: LoginResponseModel
    ) {
        context.dataStore.edit { preferences ->
            user.data?.let { data ->
                preferences[userIdPk] = data.id!!
                preferences[userFirstNamePk] = data.firstName!!
                preferences[userLastNamePk] = data.lastName!!
                preferences[userMobileIdPk] = data.mobileId!!
                preferences[userprofilePicPk] = data.profilePic ?: "" // should be ""
                // I use "" to check if user need to navigate to login or mainAct
                preferences[userRoleIdPk] = data.roleId!!
                preferences[userParentIdPk] =
                    data.parentId ?: 0 // every user must have a parent
                preferences[userIsFirstTimePk] = data.isFirstTime!!
                preferences[userCityIdPk] = data.city!!.cityId!!
                preferences[userCityTitlePk] = data.city.cityTitle!!
                preferences[userProvinceIdPk] = data.city.province!!.provinceId!!
                preferences[userProvinceTitlePk] = data.city.province.provinceTitle!!
                preferences[userEmailPk] = data.email ?: ""
            }
            preferences[tokenPk] = "Bearer " + user.token.toString()
        }
    }

    suspend fun saveImage(uploadResponseModel: PublicResponseModel) {
        context.dataStore.edit { preferences ->
            uploadResponseModel.message?.let {
                preferences[userprofilePicPk] = it
                preferences[userIsFirstTimePk] = false
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
            nationalCode = null,
            profilePic = preferences[userprofilePicPk],
            isVerify = null,
            createAt = null,
            updateAt = null,
            roleId = preferences[userRoleIdPk],
            parentId = preferences[userParentIdPk],
            isFirstTime = preferences[userIsFirstTimePk],
            cityId = preferences[userCityIdPk],
            cityTitle = preferences[userCityTitlePk],
            mobile = null,
            role = null,
            provinceId = preferences[userProvinceIdPk],
            provinceTitle = preferences[userProvinceTitlePk],
            realEstate = preferences[userRealEstatePk],
            token = preferences[tokenPk]
        )
    }
}