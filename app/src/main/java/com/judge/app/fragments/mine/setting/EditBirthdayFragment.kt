package com.judge.app.fragments.mine.setting

import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.judge.R
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.blankView
import com.judge.settingItem
import com.judge.views.BottomPopupViewList
import com.lxj.xpopup.interfaces.OnSelectListener
import com.vondear.rxtool.RxTimeTool
import org.jetbrains.anko.collections.forEachWithIndex
import java.text.SimpleDateFormat
import java.util.*

class EditBirthdayFragment : BaseEditFragment() {
    override fun epoxyController(): MvRxEpoxyController = simpleController(viewModel) { state ->
        blankView {
            id("blank view")
        }

        state.items.forEachWithIndex { index, itemBean ->
            settingItem {
                id("Birthday privacy$index")
                item(itemBean)
                onClick { _ ->
                    when (index) {
                        0 -> {
                            TimePickerBuilder(context, OnTimeSelectListener { date, _ ->
                                val dateStr = RxTimeTool.date2String(
                                    date,
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                )
                                args.content = dateStr
                                viewModel.updateItem(index, itemBean, dateStr)
                            }).setCancelColor(context!!.resources.getColor(R.color.colorPrimary))
                                .setSubmitColor(context!!.resources.getColor(R.color.colorPrimary))
                                .build().show()
                        }
                        1 -> {
                            BottomPopupViewList(context!!, viewModel.privacyList)
                                .setOnSelectListener(OnSelectListener { position, text ->
                                    if (position != viewModel.privacyList.size - 1) {
                                        args.privacy = text
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