package com.judge.app.fragments

import android.os.Bundle
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.simpleController
import com.judge.data.Dog
import com.judge.models.DogViewModel
import com.judge.views.dogDetailView


data class VideoDetailState(val dog: Dog) :
    MvRxState {
    /**
     * This secondary constructor will automatically called if your Fragment has
     * a parcelable in its arguments at key [com.airbnb.mvrx.MvRx.KEY_ARG].
     */
    //constructor(args: Dog) : this(dog = args)
}


class DetailFragment : BaseFragment() {
    private val dogViewModel: DogViewModel by fragmentViewModel()
    //private val dogId: Long by args()
    val dog :Dog by args()
    override fun epoxyController(): MvRxEpoxyController = simpleController(dogViewModel) { _ ->
        //val dog = state.dog(dogId) ?: throw IllegalStateException("Cannot find dog with id $dogId")
        dogDetailView {
            id(dog.id)
            dog(dog)
        }
    }

    companion object {
        fun arg(dogId: Long): Bundle {
            val args = Bundle()
            args.putLong(MvRx.KEY_ARG, dogId)
            return args
        }

        fun arg(dog: Dog): Bundle {
            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, dog)
            return args
        }
    }

    override fun onNetWorkChanged(state: Boolean) {
        if (state) initData()
    }
}