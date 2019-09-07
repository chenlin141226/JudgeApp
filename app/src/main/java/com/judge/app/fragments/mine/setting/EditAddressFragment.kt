package com.judge.app.fragments.mine.setting

import android.graphics.Color
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.contrarywind.interfaces.IPickerViewData
import com.judge.R
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.blankView
import com.judge.data.bean.City
import com.judge.data.bean.ProvinceBean
import com.judge.data.bean.Region
import com.judge.data.repository.MineRepository
import com.judge.settingItem
import com.judge.views.BottomPopupViewList
import com.lxj.xpopup.interfaces.OnSelectListener
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.support.v4.toast

class EditAddressFragment : BaseEditFragment() {
    private lateinit var provincesAndCities: Pair<List<ProvinceBean>, List<List<City>>>
    private lateinit var regions: List<List<List<Region>>>
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        blankView {
            id("blank view")
        }
        state.items.forEachWithIndex { index, itemBean ->
            settingItem {
                id("Address privacy$index")
                item(itemBean)
                onClick { _ ->
                    when (index) {
                        0 -> {
                            val pickView: OptionsPickerView<IPickerViewData> =
                                OptionsPickerBuilder(context,
                                    OnOptionsSelectListener { position1, position2, position3, _ ->
                                        val address = provincesAndCities.first[position1].name
                                            .plus(" ")
                                            .plus(
                                                provincesAndCities.second[position1][position2].name
                                            ).plus(" ").plus(
                                                regions[position1][position2][position3].name
                                            )
                                        args.content = address
                                        viewModel.updateItem(index, itemBean, address)
                                    }).setTitleText("城市选择")
                                    .setDividerColor(Color.BLACK)
                                    .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                                    .setContentTextSize(20)
                                    .setCancelColor(context!!.resources.getColor(R.color.colorPrimary))
                                    .setSubmitColor(context!!.resources.getColor(R.color.colorPrimary))
                                    .build()
                            provincesAndCities = MineRepository.getProvincesAndCities(context!!)
                            regions = MineRepository.getCityRegions(context!!)
                            pickView.setPicker(
                                provincesAndCities.first,
                                provincesAndCities.second,
                                regions
                            )
                            pickView.show()
                        }
                        1 -> {
                            BottomPopupViewList(context!!, viewModel.privacyList)
                                .setOnSelectListener(OnSelectListener { position, text ->
                                    toast(text)
                                    if (position != viewModel.privacyList.size - 1) {
                                        viewModel.updateItem(index, itemBean, text)
                                    }
                                }).showPopup()
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }
}