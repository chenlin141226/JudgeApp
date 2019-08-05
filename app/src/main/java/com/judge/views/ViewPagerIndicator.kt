package com.judge.views

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.judge.R

import java.util.ArrayList
import kotlin.math.min


/**
 * ViewPager指示器
 */

class ViewPagerIndicator(private val mContext: Context, attrs: AttributeSet) : LinearLayout(mContext, attrs),
    ViewPager.OnPageChangeListener {
    //所有的标题
    private var mTitles: Array<String>? = null
    //可见的item个数
    private var mVisibleItemCount: Int = 0
    //宽高
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    //字体大小
    private val mTextSize: Float
    //正常字体颜色
    private val mNormalColor: Int
    //高亮字体颜色
    private val mLightColor: Int
    private val mIsBold: Boolean
    private val mIndicatorColor: Int
    private val mNormalColorArgb: IntArray
    private val mLightColorArgb: IntArray
    //下面指示条的宽度
    private val mIndicatorWidth: Int
    //下面指示条的高度
    private val mIndicatorHeight: Int
    //指示条到底部的距离
    private val mIndicatorBottomMargin: Int
    //当前的第几个选项高亮
    private val mCurrentItem: Int
    //每次滚动偏移量
    private var mScrollX: Int = 0
    //总偏移量
    private var mTotalScrollX: Int = 0
    private var mPaint: Paint? = null
    private var mViewPager: ViewPager? = null

    private var mChangeColor: Boolean = false
    private var mChangeSize: Boolean = false
    private var isAdd: Boolean = false
    private val mHandler: Handler
    private var mTrianglePositons: IntArray? = null
    private var mDrawTriangle: Boolean = false
    private val mScale: Float
    private var mTrianglePaint: Paint? = null
    private var mTitleList: MutableList<TextView>? = null

    private val listener = ValueAnimator.AnimatorUpdateListener { animation ->
        val value = animation.animatedValue as Float
        scrollTo(value.toInt(), 0)
    }

    private var mListener: OnPageChangeListener? = null

    init {
        val a = mContext.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator)
        mTextSize = a.getDimension(R.styleable.ViewPagerIndicator_title_textSize, 0f)
        mIndicatorWidth = a.getDimension(R.styleable.ViewPagerIndicator_indicatorWidth, 0f).toInt()
        mIndicatorHeight = a.getDimension(R.styleable.ViewPagerIndicator_indicatorHeight, 0f).toInt()
        mIndicatorBottomMargin =
            a.getDimension(R.styleable.ViewPagerIndicator_indicatorBottomMargin, dp2px(2).toFloat()).toInt()
        mNormalColor = a.getColor(R.styleable.ViewPagerIndicator_normalColor, Color.BLACK)
        mLightColor = a.getColor(R.styleable.ViewPagerIndicator_lightColor, Color.BLACK)
        mIndicatorColor = a.getColor(R.styleable.ViewPagerIndicator_indicatorColor, Color.BLACK)
        mNormalColorArgb = getColorArgb(mNormalColor)
        mLightColorArgb = getColorArgb(mLightColor)
        mCurrentItem = a.getInteger(R.styleable.ViewPagerIndicator_currentItem, 0)
        mChangeColor = a.getBoolean(R.styleable.ViewPagerIndicator_change_color, true)
        mChangeSize = a.getBoolean(R.styleable.ViewPagerIndicator_change_size, true)
        mIsBold = a.getBoolean(R.styleable.ViewPagerIndicator_bold, false)
        mVisibleItemCount = a.getInteger(R.styleable.ViewPagerIndicator_visibleItemCount, 1)
        a.recycle()
        orientation = HORIZONTAL
        mHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                val position = msg.what
                startAnim(position)
            }
        }
        initPaint()
        mScale = mContext.resources.displayMetrics.density
    }

    private fun initPaint() {
        mPaint = Paint()
        mPaint!!.color = mIndicatorColor
        mPaint!!.isAntiAlias = true
        mPaint!!.isDither = true
        mPaint!!.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth
        mHeight = measuredHeight
        if (!isAdd) {
            isAdd = true
            addChild(widthMeasureSpec, heightMeasureSpec)
        }
    }


    private fun addChild(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mTitles == null || mVisibleItemCount == 0) {
            return
        }
        mScrollX = mWidth / mVisibleItemCount
        mTotalScrollX = mCurrentItem * mScrollX
        mTitleList = ArrayList()
        for (i in mTitles!!.indices) {
            val textView = TextView(mContext)
            mTitleList!!.add(textView)
            val params = LinearLayout.LayoutParams(mScrollX, ViewGroup.LayoutParams.MATCH_PARENT)
            textView.layoutParams = params
            textView.text = mTitles!![i]
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
            if (mIsBold) {
                textView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }
            if (mCurrentItem == i) {
                textView.setTextColor(mLightColor)
            } else {
                textView.setTextColor(mNormalColor)
            }
            textView.gravity = Gravity.START
            textView.setOnClickListener(OnClickListener {
                if (mViewPager != null) {
                    if (i == mViewPager!!.currentItem) {
                        return@OnClickListener
                    }
                    if (mListener != null) {
                        mListener!!.onTabClick(i)
                    }
                    mViewPager!!.setCurrentItem(i, true)
                }
            })
            addView(textView)
            textView.measure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    /**
     * 画出下面的横线
     *
     * @param canvas
     */
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.save()
        canvas.translate(0f, mHeight.toFloat())
        //实际绘制的指示条的宽度
        val w = min(mIndicatorWidth, mScrollX)
        //求出右上角和左下角的坐标
        val x1 = (mScrollX - w) / 8 + mTotalScrollX
        val y1 = -mIndicatorHeight - mIndicatorBottomMargin
        val x2 = x1 + w
        val y2 = -mIndicatorBottomMargin
        if (mDrawTriangle) {//画三角形
            canvas.drawLine(
                (x1 + dp2px(8)).toFloat(),
                (-dp2px(10)).toFloat(),
                (x1 + w / 2).toFloat(),
                y2.toFloat(),
                mTrianglePaint!!
            )
            canvas.drawLine(
                (x1 + w / 2).toFloat(),
                y2.toFloat(),
                (x2 - dp2px(8)).toFloat(),
                (-dp2px(10)).toFloat(),
                mTrianglePaint!!
            )
        } else {
            //画出矩形
            val r = mIndicatorHeight / 2
            canvas.drawRoundRect(
                RectF(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat()),
                r.toFloat(),
                r.toFloat(),
                mPaint!!
            )
        }
        canvas.restore()
    }

    fun setTitles(titles: Array<String>) {
        mTitles = titles
    }

    fun setVisibleChildCount(visibleChildCount: Int) {
        mVisibleItemCount = visibleChildCount
    }

    fun setTitleText(position: Int, text: String) {
        if (mTitleList != null && position >= 0 && position < mTitleList!!.size) {
            val textView = mTitleList!![position]
            textView.text = text
        }
    }

    fun setViewPager(viewPager: ViewPager) {
        mViewPager = viewPager
        mViewPager!!.addOnPageChangeListener(this)
    }

    private fun getColorArgb(color: Int): IntArray {
        return intArrayOf(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color))
    }


    fun setChangeColor(changeColor: Boolean) {
        mChangeColor = changeColor
    }

    fun setChangeSize(changeSize: Boolean) {
        mChangeSize = changeSize
    }

    /**
     * 文字变颜色
     *
     * @param position
     * @param rate
     */
    private fun textChangeColor(position: Int, rate: Float) {
        val a = (mNormalColorArgb[0] * (1 - rate) + mLightColorArgb[0] * rate).toInt()
        val r = (mNormalColorArgb[1] * (1 - rate) + mLightColorArgb[1] * rate).toInt()
        val g = (mNormalColorArgb[2] * (1 - rate) + mLightColorArgb[2] * rate).toInt()
        val b = (mNormalColorArgb[3] * (1 - rate) + mLightColorArgb[3] * rate).toInt()
        val textView = getChildAt(position) as TextView
        textView.setTextColor(Color.argb(a, r, g, b))
    }


    /**
     * 文字缩放
     *
     * @param position
     * @param rate
     */
    private fun textChangeScale(position: Int, rate: Float) {
        val textView = getChildAt(position) as TextView
        textView.scaleX = rate
        textView.scaleY = rate
    }

    /**
     * 滚动的方法
     *
     * @param position
     * @param positionOffset
     */
    private fun scroll(position: Int, positionOffset: Float) {
        mTotalScrollX = ((position + positionOffset) * mScrollX).toInt()
        invalidate()
        if (mChangeColor) {
            changeColor(position, positionOffset)
        }
        if (mChangeSize) {
            changeScale(position, positionOffset)
        }
    }

    /**
     * 文字颜色变化
     */
    private fun changeColor(position: Int, positionOffset: Float) {
        textChangeColor(position, 1 - positionOffset)
        textChangeColor(position + 1, positionOffset)
    }

    /**
     * 文字大小变化
     */
    private fun changeScale(position: Int, positionOffset: Float) {
        textChangeScale(position, 1 + (1 - positionOffset) * 0.1f)
        textChangeScale(position + 1, 1 + positionOffset * 0.1f)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        scroll(position, positionOffset)
        if (mListener != null) {
            mListener!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    private fun startAnim(position: Int) {
        val target = (position - mVisibleItemCount + 2) * mScrollX
        if (scrollX != target) {
            val a = ObjectAnimator.ofFloat(this, "translationX", scrollX.toFloat(), target.toFloat())
            a.addUpdateListener(listener)
            a.duration = 250
            a.start()
        }
    }

    override fun onPageSelected(position: Int) {
        if (mVisibleItemCount != mTitles!!.size && position >= mVisibleItemCount - 2 && position != mTitles!!.size - 1) {
            mHandler.sendEmptyMessageDelayed(position, 100)
        }
        if (mTrianglePositons != null) {
            mDrawTriangle = false
            for (p in mTrianglePositons!!) {
                if (p == position) {
                    mDrawTriangle = true
                    break
                }
            }
        }

        if (mListener != null) {
            mListener!!.onPageSelected(position)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (state == 0) {
            fixTextColorChange()
        }
        if (mListener != null) {
            mListener!!.onPageScrollStateChanged(state)
        }
    }

    /**
     * 修复由于滚动造成的文字颜色不正常的问题
     */
    private fun fixTextColorChange() {
        for (i in 0 until childCount) {
            if (i != mViewPager!!.currentItem) {
                val textView = getChildAt(i) as TextView
                if (textView.currentTextColor != mNormalColor) {
                    textView.setTextColor(mNormalColor)
                }
            }
        }
    }

    fun setListener(listener: OnPageChangeListener) {
        mListener = listener
    }

    interface OnPageChangeListener {

        fun onTabClick(position: Int)

        fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)

        fun onPageSelected(position: Int)

        fun onPageScrollStateChanged(state: Int)
    }

    fun setTrianglePositions(positions: IntArray) {
        mTrianglePositons = positions
        if (mTrianglePositons != null) {
            mTrianglePaint = Paint()
            mTrianglePaint!!.isAntiAlias = true
            mTrianglePaint!!.isDither = true
            mTrianglePaint!!.color = mLightColor
            mTrianglePaint!!.strokeWidth = dp2px(2).toFloat()
            mTrianglePaint!!.style = Paint.Style.STROKE
        }
    }

    //dp转成px
    private fun dp2px(dpVal: Int): Int {
        return (mScale * dpVal + 0.5f).toInt()
    }

}
