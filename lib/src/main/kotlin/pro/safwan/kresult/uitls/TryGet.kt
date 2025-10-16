package pro.safwan.kresult.uitls

fun interface TryGet<T> {
    operator fun invoke(): Pair<Boolean, T>
}

fun interface TryGetValue<K, V> {
    operator fun invoke(key: K): Pair<Boolean, V>
}
