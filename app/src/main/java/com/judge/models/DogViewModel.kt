package com.judge.models

import android.graphics.Color
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.judge.app.core.JudgeApplication
import com.judge.app.core.MvRxViewModel
import com.judge.data.Dog
import com.judge.data.repository.DogRepository
import com.judge.data.state.DogState
import com.judge.extensions.copy
import io.reactivex.schedulers.Schedulers

class DogViewModel(
    state: DogState,
    dogRepository: DogRepository
) : MvRxViewModel<DogState>(state) {
    init {
        fetchDogs(dogRepository)
    }

    private fun fetchDogs(dogRepository: DogRepository) = withState {
        if (it.isLoading) return@withState
        dogRepository.getDogs()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { setState { copy(isLoading = true) } }
            .doOnComplete { setState { copy(isLoading = false) } }
            .execute { copy(dogs = it()) }
    }

    fun setItemState(index: Int, dog: Dog) {
        setState {
            copy(dogs = dogs?.copy(index, dog.copy(color = Color.RED)))
        }
    }

    companion object : MvRxViewModelFactory<DogViewModel, DogState> {
        override fun create(viewModelContext: ViewModelContext, state: DogState): DogViewModel? {
            val dogRepository = viewModelContext.app<JudgeApplication>().dogsRepository
            return DogViewModel(state, dogRepository)
        }
    }
}