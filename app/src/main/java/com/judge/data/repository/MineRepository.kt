package com.judge.data.repository

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.judge.data.bean.City
import com.judge.data.bean.MedalBean
import com.judge.data.bean.MineDataBean
import com.judge.data.bean.ProvinceBean
import com.judge.network.JsonResponse
import com.judge.network.ServiceCreator
import com.judge.network.services.MineApIService
import io.reactivex.Observable
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object MineRepository {
    private val mineService: MineApIService by lazy {
        ServiceCreator.create(MineApIService::class.java)
    }

    fun getUserData(map: HashMap<String, String>): Observable<JsonResponse<MineDataBean>> {
        return mineService.getUerData(map)
    }

    fun getMedals(map: HashMap<String, String>): Observable<JsonResponse<MedalBean>> {
        return mineService.getMedals(map)
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
}