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
import kotlinx.android.synthetic.main.dog_detail.view.*
import kotlinx.android.synthetic.main.dog_row.view.breedsView
import kotlinx.android.synthetic.main.dog_row.view.image
import kotlinx.android.synthetic.main.dog_row.view.nameView

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class DogDetailView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.dog_detail, this, true)
    }

    @ModelProp
    fun setDog(dog: Dog) {
        Picasso.with(context)
            .load(dog.imageUrl)
            .into(image)
        nameView.text = dog.name
        breedsView.text = dog.breeds
        descriptionView.text = dog.description
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