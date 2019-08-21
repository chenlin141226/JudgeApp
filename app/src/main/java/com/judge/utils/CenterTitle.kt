package com.judge.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter
import com.judge.R
import kotlin.math.max
import kotlin.math.min

object CenterTitle {

    lateinit var titleWatcher: TextWatcher
    lateinit var subTitleWatcher: TextWatcher
    lateinit var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener
    @JvmStatic
    var isCenter = true

    @BindingAdapter("centerTitle")
    @JvmStatic
    fun centerTitle(toolbar: Toolbar, flagCenter: Boolean) {

        isCenter = flagCenter

        // title
        val fieldTitle = Toolbar::class.java.getDeclaredField("mTitleTextView")
        fieldTitle.isAccessible = true
        val mTitleTextView: TextView? = fieldTitle.get(toolbar) as TextView?

        // sub title
        val fieldSubTitle = Toolbar::class.java.getDeclaredField("mSubtitleTextView")
        fieldSubTitle.isAccessible = true
        val mSubtitleTextView: TextView? = fieldSubTitle.get(toolbar) as TextView?

        // nav drawer
        val fieldNav = Toolbar::class.java.getDeclaredField("mNavButtonView")
        fieldNav.isAccessible = true
        val mNavButtonView: ImageButton? = fieldNav.get(toolbar) as ImageButton?

        mNavButtonView?.setImageResource(R.drawable.left)
        mTitleTextView?.let {

            //removing observer for avoid recursive call
            if (::globalLayoutListener.isInitialized)
                removeAllListners(
                    mTitleTextView,
                    mSubtitleTextView,
                    globalLayoutListener,
                    titleWatcher,
                    subTitleWatcher
                )

            // view tree observer for listen any layout changes
            globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
                removeAllListners(
                    mTitleTextView,
                    mSubtitleTextView,
                    globalLayoutListener,
                    titleWatcher,
                    subTitleWatcher
                )
                centerTitle(toolbar, isCenter)
            }

            titleWatcher = object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    mTitleTextView.requestLayout()
                    removeAllListners(
                        mTitleTextView,
                        mSubtitleTextView,
                        globalLayoutListener,
                        titleWatcher,
                        subTitleWatcher
                    )
                    centerTitle(toolbar, isCenter)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
            }

            subTitleWatcher = object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    mSubtitleTextView?.requestLayout()
                    removeAllListners(
                        mTitleTextView,
                        mSubtitleTextView,
                        globalLayoutListener,
                        titleWatcher,
                        subTitleWatcher
                    )
                    centerTitle(toolbar, isCenter)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }
            }

            //mesure all views
            mTitleTextView.measure(0, 0)
            mNavButtonView?.measure(0, 0)
            mSubtitleTextView?.measure(0, 0)

            // TODO: 12/18/2017 RTL layout support
            // get the right side margin from the menu items
            var menuIconsMargin = Int.MAX_VALUE
            for (i in 0 until toolbar.menu?.size().orZero()) {
                if (toolbar.menu?.getItem(i)?.isVisible.orFalse()) {
                    val menuView = toolbar.menu?.getItem(i)?.itemId?.let { toolbar.findViewById<View>(it) }
                    val size = IntArray(2)
                    menuView?.getLocationOnScreen(size)

                    menuIconsMargin = min(
                        when (size[0]) {
                            0 -> menuIconsMargin
                            else -> size[0]
                        }, menuIconsMargin
                    )
                }

            }

            val leftSideMarginTitle = max(
                (toolbar.measuredWidth - mTitleTextView.measuredWidth) / 2,
                mNavButtonView?.measuredWidth.orZero()
            ) // also consider nav button margin

            val leftSideMarginSubTitle = max(
                (toolbar.measuredWidth - mSubtitleTextView?.measuredWidth.orZero()) / 2,
                mNavButtonView?.measuredWidth.orZero()
            ) // also consider nav button margin

            // position title
            mTitleTextView.run {
                left = if (isCenter) leftSideMarginTitle
                else mNavButtonView?.measuredWidth.orZero()
                right = min(toolbar.measuredWidth - leftSideMarginTitle, menuIconsMargin)
                layoutParams.width =
                    min(toolbar.measuredWidth - leftSideMarginTitle, menuIconsMargin) - leftSideMarginTitle
                //text = text
            }

            // position sub title
            mSubtitleTextView?.run {
                left = if (isCenter) leftSideMarginSubTitle
                else mNavButtonView?.measuredWidth.orZero()
                right = min(toolbar.measuredWidth - leftSideMarginSubTitle, menuIconsMargin)
                layoutParams.width =
                    min(toolbar.measuredWidth - leftSideMarginSubTitle, menuIconsMargin) - leftSideMarginSubTitle
                text = text
            }


            // add a view tree observer so that we can center the title every time view tree is updated
            val vto = mTitleTextView.viewTreeObserver
            vto.addOnGlobalLayoutListener(globalLayoutListener)

            // add text watcher for listen text change
            mTitleTextView.addTextChangedListener(titleWatcher)

            // add text watcher for listen text change
            mSubtitleTextView?.addTextChangedListener(subTitleWatcher)

        }
    }


    private fun removeAllListners(
        titleView: TextView,
        subTitleView: TextView?,
        titleObserver: ViewTreeObserver.OnGlobalLayoutListener,
        titleTextWatcher: TextWatcher,
        subTitleTextWatcher: TextWatcher
    ) {
        titleView.viewTreeObserver.removeOnGlobalLayoutListener(titleObserver)
        titleView.removeTextChangedListener(titleTextWatcher)
        subTitleView?.removeTextChangedListener(subTitleTextWatcher)
    }
}

fun Int?.orZero(): Int = this ?: 0
fun Boolean?.orFalse(): Boolean = this ?: false