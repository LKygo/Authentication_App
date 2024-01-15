package compose.material.theme

import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ApiClient {

    private val client = OkHttpClient()

    fun sendLoginRequest(email: String, password: String, onResponse: (response: String) -> Unit) {
        val request = Request.Builder()
            .url("https://dummyjson.com/auth/login ") // Replace with your actual API endpoint
            .post(
                FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                onResponse(response.body?.string() ?: "")
            }

            override fun onFailure(call: Call, e: IOException) {
                // Error handling
            }
        })
    }
}
