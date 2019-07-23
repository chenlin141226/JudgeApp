package com.judge.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

class LogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val endTime = System.currentTimeMillis()
        val source = response.body()?.source()
        source?.request(Long.MAX_VALUE)
        val buffer = source?.buffer

        val log = "\n**********************************"
            .plus("\nnetwork code ==== " + response.code())
            .plus("\n network url === " + request.url())
            .plus("\n duration ==== " + (endTime - startTime))
            .plus("\n request duration ==== " + (response.receivedResponseAtMillis() - response.sentRequestAtMillis()))
            .plus("\n request header === " + request.headers())
            .plus("\n request ===== " + request.body()?.let { bodyToString(requestBody = it) })
            .plus("\n body ===== " + buffer?.clone()?.readString(UTF8))
        Log.e("request info", log)
        return response
    }

    companion object {
        val UTF8: Charset = Charset.forName("UTF-8")
    }

    private fun bodyToString(requestBody: RequestBody): String? {
        return try {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            "request error"
        }
    }
}


