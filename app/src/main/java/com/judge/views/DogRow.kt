package com.judge.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.judge.R
import com.judge.data.Dog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dog_row.view.*

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DogRow @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.dog_row, this, true)
    }

    @ModelProp
    fun setDog(dog: Dog) {
        Picasso.with(context)
            .load(dog.imageUrl)
            .into(image)
        nameView.text = dog.name
        breedsView.text = dog.breeds
    }

    @ModelProp
    fun setTextColor(color: Int) {
        nameView.setTextColor(color)
    }

    @CallbackProp
    fun setClickListener(listener: OnClickListener?) {
        setOnClickListener(listener)
    }
}