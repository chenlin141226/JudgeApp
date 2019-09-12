package com.judge.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.judge.data.bean.*
import com.judge.network.JsonResponse
import com.judge.network.ServiceCreator
import com.judge.network.services.MineApIService
import io.reactivex.Observable
import okhttp3.MultipartBody
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object MineRepository {
    var userProfile: ProfileBean? = null
    private val mineService: MineApIService by lazy {
        ServiceCreator.create(MineApIService::class.java)
    }

    fun getUserData(map: HashMap<String, String>): Observable<JsonResponse<ProfileBean>> {
        return mineService.getUerData(map)
    }

    fun getMedals(map: HashMap<String, String>): Observable<JsonResponse<MedalBean>> {
        return mineService.getMedals(map)
    }

    fun getPublishedTopics(map: HashMap<String, String>): Observable<JsonResponse<TopicBean>> {
        return mineService.getTopics(map)
    }

    fun upLoadPhoto(
        file: MultipartBody.Part,
        map: HashMap<String, String>
    ): Observable<JsonResponse<UpLoadPhotoResultBean>> {
        return mineService.upLoadPhoto(file, map)
    }

    fun getPublicMessages(map: HashMap<String, String>): Observable<JsonResponse<PublicMessageBean>> {
        return mineService.getPublicMessage(map)
    }

    fun getPersonalMessages(map: HashMap<String, String>): Observable<JsonResponse<PersonalMessageBean>> {
        return mineService.getPersonalMessage(map)
    }

    private fun getProvinces(context: Context): List<ProvinceBean> {
        val stringBuilder = StringBuilder()
        try {
            val assetManager = context.assets
            val buff = BufferedReader(
                InputStreamReader(
                    assetManager.open("provinces.json")
                )
            )
            buff.forEachLine {
                stringBuilder.append(it)
            }
            buff.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Gson().fromJson(stringBuilder.toString(), object : TypeToken<List<ProvinceBean>>() {

        }.type)
    }

    fun getProvincesAndCities(context: Context): Pair<List<ProvinceBean>, List<List<City>>> {
        val provinces = getProvinces(context)
        val cities = ArrayList<List<City>>()
        provinces.forEach {
            cities.add(it.children)
        }
        return Pair(provinces, cities)
    }

    fun getCityRegions(context: Context): List<List<List<Region>>> {
        val regions = LinkedList<List<List<Region>>>()
        val provinces = getProvinces(context)
        provinces.forEach { province ->
            val cityRegions = LinkedList<List<Region>>()
            province.children.forEach {
                cityRegions.add(it.children)
            }
            regions.add(cityRegions)
        }
        return regions
    }

    fun getFriends(map: HashMap<String, String>): Observable<JsonResponse<FriendBean>> {
        return mineService.getFriends(map)
    }

    fun getFriendsMessage(map: HashMap<String, String>): Observable<JsonResponse<FriendsMessageBean>> {
        return mineService.getFriendsMessage(map)
    }

    fun sendMessage(map: HashMap<String, String>): Observable<JsonResponse<CommonResultBean>> {
        return mineService.sendMessage(map)
    }

    fun updateProfile(map: HashMap<String, String>): Observable<JsonResponse<ProfileUpdateResultBean>> {
        return mineService.updateProfile(map)
    }

    fun addFriend(
        queryMap: HashMap<String, String>,
        fieldMap: HashMap<String, String>
    ): Observable<JsonResponse<CommonResultBean>> {
        return mineService.addFriend(queryMap, fieldMap)
    }

    fun deleteTopics(
        queryMap: HashMap<String, String>,
        fieldMap: HashMap<String, String>,
        ids: List<String>
    ): Observable<JsonResponse<CommonResultBean>> {
        return mineService.deleteTopics(queryMap, fieldMap, ids)
    }
}