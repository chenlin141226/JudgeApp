package com.judge.data.bean

import com.contrarywind.interfaces.IPickerViewData

data class ProvinceBean(
    val children: List<City>,
    val code: String,
    val name: String
) : IPickerViewData {
    override fun getPickerViewText(): String {
        return this.name
    }
}

data class City(
    val code: String,
    val name: String
): IPickerViewData {
    override fun getPickerViewText(): String {
        return this.name
    }
}