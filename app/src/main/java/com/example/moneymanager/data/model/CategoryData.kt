package com.example.moneymanager.data.model

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryData {
    data class CategoryList(val categorys: List<Category>)
    data class Category(val id: Int, val name: String, val icon: String, val type: String)

    fun readJsonCategory(content: Context, fileName: String): List<Category> {
        var listCategory = mutableListOf<Category>()
        try {
            val inputStream = content.assets.open(fileName).bufferedReader().use { it.readText() }
            val categoryListType = object : TypeToken<List<Category>>() {}.type
            listCategory = Gson().fromJson(inputStream, categoryListType)
        } catch (e: Exception) {
            Log.e("readJsonData", e.toString())
        }
        return listCategory
    }
}