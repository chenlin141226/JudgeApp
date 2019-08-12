package com.judge.views

import android.content.Context
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.judge.R
import com.judge.textViewItem
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.interfaces.OnSelectListener

class BottomPopupViewList(
    context: Context
) :
    BottomPopupView(context) {
    private lateinit var recyclerView: EpoxyRecyclerView
    private lateinit var list: Array<String>
    private lateinit var onSelectListener: OnSelectListener

    constructor(
        context: Context, list: Array<String>
    ) : this(context) {
        this.list = list
    }

    override fun getImplLayoutId(): Int = R.layout.bottom_popup_view

    override fun initPopupContent() {
        super.initPopupContent()
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
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
                        dismiss()
                    }
                }
            }
        }
    }

    fun setOnSelectListener(onSelectListener: OnSelectListener): BottomPopupViewList {
        this.onSelectListener = onSelectListener
        return this@BottomPopupViewList
    }

    fun showPopup() {
        XPopup.Builder(context).asCustom(this).show()
    }
}
