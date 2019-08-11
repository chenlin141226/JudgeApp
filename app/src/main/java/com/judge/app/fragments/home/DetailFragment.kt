package com.judge.app.fragments.home

import com.airbnb.mvrx.*
import com.judge.app.core.BaseFragment
import com.judge.app.core.MvRxEpoxyController
import com.judge.app.core.MvRxViewModel
import com.judge.app.core.simpleController
import com.judge.data.bean.Dog
import com.judge.views.dogDetailView


data class DogDetailState(val dog: Dog) :
    MvRxState {
    /**
     * This secondary constructor will automatically called if your InformationFragment has
     * a parcelable in its arguments at key [com.airbnb.mvrx.MvRx.KEY_ARG].
     */
    //constructor(args: Dog) : this(dog = args)
}

class DogDetailViewModel(
    state: DogDetailState
) : MvRxViewModel<DogDetailState>(state) {
    companion object : MvRxViewModelFactory<DogDetailViewModel, DogDetailState> {
        @JvmStatic
        override fun create(viewModelContext: ViewModelContext, state: DogDetailState): DogDetailViewModel? {
            return DogDetailViewModel(state)
        }
    }
}

class DetailFragment : BaseFragment() {
    private val dogDetailViewModel: DogDetailViewModel by fragmentViewModel()

    override fun epoxyController(): MvRxEpoxyController = simpleController(dogDetailViewModel) { state ->
        //val dog = state.dog(dogId) ?: throw IllegalStateException("Cannot find dog with id $dogId")
        dogDetailView {
            id(state.dog.id)
            dog(state.dog)
        }
    }

    override fun initData() {
        sharedViewModel.setVisible(false)
    }
}