package com.judge.views

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.judge.R
import com.judge.shareItem
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView
import com.lxj.xpopup.interfaces.OnSelectListener
import com.vondear.rxtool.RxTool

class ShareBottomPopupView(
    context: Context
) :
    BottomPopupView(context) {
    private lateinit var recyclerView: EpoxyRecyclerView
    private val shareIcons =
        RxTool.getContext().resources.obtainTypedArray(R.array.share_item_icons)
    private lateinit var onSelectListener: OnSelectListener
    override fun getImplLayoutId(): Int = R.layout.bottom_popup_view

    override fun initPopupContent() {
        super.initPopupContent()
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        recyclerView.withModels {
            for (i in 0 until shareIcons.length()) {
                shareItem {
                    id(i)
                    imageSrcId(shareIcons.getResourceId(i, 0))
                }
            }
            shareIcons.recycle()
        }
    }

    fun setOnSelectListener(onSelectListener: OnSelectListener): ShareBottomPopupView {
        this.onSelectListener = onSelectListener
        return this@ShareBottomPopupView
    }

    fun showPopup() {
        XPopup.Builder(context).asCustom(this).show()
    }
}
