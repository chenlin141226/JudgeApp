package com.judge.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.judge.R
import com.judge.db.bean.HistoryTopicBean

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class TopicItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val topicTitle by lazy { findViewById<TextView>(R.id.topic_title) }
    private val topicUserName by lazy { findViewById<TextView>(R.id.topic_userName) }
    private val topicDate by lazy { findViewById<TextView>(R.id.topic_date) }
    private val deleteButton by lazy { findViewById<ImageView>(R.id.delete_button) }
    private val topicView by lazy { findViewById<ConstraintLayout>(R.id.topic_item_view) }

    init {
        View.inflate(context, R.layout.topic_item_view, this)
    }

    @ModelProp
    fun setTopic(topic: HistoryTopicBean) {
        topicTitle.text = topic.topicTitle
        topicUserName.text = topic.topicAuthor
        topicDate.text = topic.surfedTime
    }

    @CallbackProp
    fun onDeleteListener(listener: OnClickListener?) {
        deleteButton.setOnClickListener(listener)
    }

    @CallbackProp
    fun onItemClickListener(listener: OnClickListener?) {
        topicView.setOnClickListener(listener)
    }
}