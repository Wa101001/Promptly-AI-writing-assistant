package com.example.promptly

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

object GptHelper {

    private val TAG = "PromptlyGPT"
    private val client = OkHttpClient()

    fun callGpt(userInput: String, instruction: String, callback: (String?) -> Unit) {
        val apiKey = BuildConfig.OPENAI_API_KEY
        val url = "https://api.openai.com/v1/chat/completions"

        val messages = org.json.JSONArray()
            .put(JSONObject().put("role", "system").put("content", instruction))
            .put(JSONObject().put("role", "user").put("content", userInput))

        val bodyJson = JSONObject()
            .put("model", "gpt-3.5-turbo")
            .put("messages", messages)
            .put("temperature", 0.7)

        val requestBody = bodyJson.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "❌ GPT request failed: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body?.string()
                Log.d(TAG, "🪵 Raw GPT response: $responseText")

                try {
                    val json = JSONObject(responseText)
                    val content = json.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                    callback(content)
                } catch (e: Exception) {
                    Log.e(TAG, "❌ JSON parsing error: ${e.message}")
                    callback(null)
                }
            }
        })
    }
}
