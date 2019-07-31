package com.judge.extensions

fun <T> List<T>.update(value: T, finder: (T) -> Boolean) = indexOfFirst(finder).let { index ->
    if (index >= 0) copy(index, value) else this + value
}

fun <T> List<T>.copy(i: Int, value: T): List<T> = toMutableList().apply { set(i, value) }

fun <T> List<T>.clear(): List<T> = toMutableList().apply { clear() }

inline fun <T> List<T>.delete(filter: (T) -> Boolean): List<T> =
    toMutableList().apply { removeAt(indexOfFirst(filter)) }

fun <T> List<T>.update(newValue: (T) -> T, finder: (T) -> Boolean) = indexOfFirst(finder).let { index ->
    if (index >= 0) copy(index, newValue(get(index))) else this
}
