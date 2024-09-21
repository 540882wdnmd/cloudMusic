package com.example.cloudmusic.utils.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.cloudmusic.utils.base.BaseApplication
import androidx.datastore.preferences.core.edit

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

//PreferenceDataStore 文件名
private const val PREFERENCE_NAME : String = "data_preferences"

//获取 DataStore 实例
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

/**
 * 全局都可获取的 DataStore 实例
 */
val dataStoreInstance = BaseApplication.appContext.dataStore

/**
 * 用户名的键，用于获取用户名的值(用于记住登录信息)
 */
val preferenceNickname = stringPreferencesKey("nickname")

/**
 * 电话号码的键，用于获取电话号码的值(用于记住登录信息)
 */
val preferencePhone = stringPreferencesKey("phone")

/**
 * 密码的键，用于获取密码的值(用于记住登录信息)
 */
val preferencePassword = stringPreferencesKey("password")

/**
 * 头像的键，用于获取头像url的值(用于记住登录信息)
 */
val preferenceAvatar = stringPreferencesKey("avatar")

/**
 * 登陆状态的键，用于获取登录状态的键（判断是否登录）
 */
val preferenceStatus = booleanPreferencesKey("status")

/**
 * cookie 的键，用于 cookie 持久化
 */
val preferenceCookie = stringPreferencesKey("cookie")

/**
 * 上一次 cookie 获取/刷新的时间
 */
val preferenceLastCookieTime = longPreferencesKey("lastCookieTime")

/**
 * 插入字符串型值元素到 DataStore 中
 */
suspend fun putStringData(
    preferencesKey: Preferences.Key<String>,
    value: String
) = dataStoreInstance.edit {
    it[preferencesKey] = value
}

/**
 * 插入整型元素到 DataStore 中
 */
suspend fun putIntData(
    preferencesKey: Preferences.Key<Int>,
    value: Int
) = dataStoreInstance.edit {
    it[preferencesKey] = value
}

/**
 * 插入长整型元素到 DataStore 中
 */
suspend fun putLongData(
    preferencesKey: Preferences.Key<Long>,
    value: Long
) = dataStoreInstance.edit {
    it[preferencesKey] = value
}

/**
 * 插入布尔型元素到 DataStore 中
 */
suspend fun putBooleanData(
    preferencesKey: Preferences.Key<Boolean>,
    value: Boolean
) = dataStoreInstance.edit {
    it[preferencesKey] = value
}

/**
 * 获取 DataStore 对应的字符串值
 */
suspend fun getStringData(
    preferencesKey: Preferences.Key<String>,
    default: String = ""
): String = dataStoreInstance.data.map {
    it[preferencesKey] ?: default
}.first()

/**
 * 获取 DataStore 对应的整形值
 */
suspend fun getIntData(
    preferencesKey: Preferences.Key<Int>,
    default: Int = 0
): Int = dataStoreInstance.data.map {
    it[preferencesKey] ?: default
}.first()

/**
 * 获取 DataStore 对应的长整形值
 */
suspend fun getLongData(
    preferencesKey: Preferences.Key<Long>,
    default: Long = 0L
): Long = dataStoreInstance.data.map {
    it[preferencesKey] ?: default
}.first()

/**
 * 获取 DataStore 对应的布尔值
 */
suspend fun getBooleanData(
    preferencesKey: Preferences.Key<Boolean>,
    default: Boolean = false
): Boolean = dataStoreInstance.data.map {
    it[preferencesKey] ?: default
}.first()
