package com.alexstephanov.brestnews

import io.reactivex.Observable
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL

fun createRequest(url: String): Observable<String> = Observable.create<String> {
    val urlConnection = URL(url).openConnection() as HttpURLConnection
    try {
        urlConnection.connect()

        if(urlConnection.responseCode != HttpURLConnection.HTTP_OK)
            it.onError(RuntimeException(urlConnection.responseMessage))
        else {
            val str = urlConnection.inputStream.bufferedReader().readText()
            it.onNext(str)
        }
    } finally {
        urlConnection.disconnect()
    }
}