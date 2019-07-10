package com.judge.models

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.app.JudgeApplication
import com.judge.app.MvRxViewModel
import com.judge.data.DogRepository
import com.judge.state.DogState
import io.reactivex.schedulers.Schedulers

class DogViewModel(
    state: DogState,
    dogRepository: DogRepository
) : MvRxViewModel<DogState>(state) {
    init {
        dogRepository.getDogs()
            .subscribeOn(Schedulers.io())
            .execute { copy(dogs = it) }
    }

    companion object : MvRxViewModelFactory<DogViewModel, DogState> {
        override fun create(viewModelContext: ViewModelContext, state: DogState): DogViewModel? {
            val dogRepository = viewModelContext.app<JudgeApplication>().dogsRepository
            return DogViewModel(state, dogRepository)
        }
    }
}