package com.judge.views

import android.content.Context
import com.airbnb.epoxy.EpoxyRecyclerView
import com.judge.R
import com.judge.textViewItem
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.interfaces.OnSelectListener

class BottomPopupViewList(
    context: Context,
    private val list: Array<String>,
    private val onSelectListener: OnSelectListener
) :
    BottomPopupView(context) {
    private lateinit var recyclerView: EpoxyRecyclerView
    override fun getImplLayoutId(): Int = R.layout.bottom_popup_view

    override fun onCreate() {
        super.onCreate()
        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.withModels {
            list.asIterable().forEachIndexed { index, content ->
                textViewItem {
                    id(content + index)
                    content(content)
                    colorType(
                        when (index) {
                            0 -> 1
                            list.size - 1 -> 3
                            else -> 2
                        }
                    )
                    onClick { _ ->
                        onSelectListener.onSelect(index, content)
                    }
                }
            }
        }
    }


}