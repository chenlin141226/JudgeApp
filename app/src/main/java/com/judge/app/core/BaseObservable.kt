package com.judge.app.core

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class BaseObservable<T>(actual: Observer<Any>) : Observer<T> {
    override fun onNext(t: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSubscribe(d: Disposable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onError(e: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}