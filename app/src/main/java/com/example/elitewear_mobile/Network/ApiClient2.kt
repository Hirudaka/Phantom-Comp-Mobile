package com.example.elitewear_mobile.Network

import android.util.Log
import com.example.elitewear_mobile.models.Review
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object ApiClient2 {

    private val client = OkHttpClient()
    fun fetchReviews(vendorID: Int, callback: (List<Review>)-> Unit){

        val url = "http://10.0.2.2:5133/api/review/vendor/$vendorID"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }


            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val rawResponse = responseBody.string() // St2ore the string once
                    println("Response from API: $rawResponse")
                    val jsonResponse = JSONArray(rawResponse)
                    val reviews = mutableListOf<Review>()

                    for (i in 0 until jsonResponse.length()) {
                        val jsonReview = jsonResponse.getJSONObject(i)
                        val id = jsonReview.getInt("id")
                        val name = jsonReview.getString("name")
                        val vendorID = jsonReview.getInt("vendorID")
                        val description = jsonReview.getString("description")
                        val rate = jsonReview.getInt("rate")


                        reviews.add(Review(id,vendorID,name,description,rate))
                    }


                    callback(reviews)
                }
            }
        })
    }
    fun addReview(review: Review, callback: () -> Unit) {
        val url = "http://10.0.2.2:5133/api/review"

        val jsonReview = JSONObject().apply {
            put("vendorID", review.vendorID)
            put("name", review.name)
            put("description", review.description)
            put("rate", review.rate)
        }

        val requestBody = jsonReview.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder().url(url).post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback()
                }
            }
        })
    }
    fun getReviewsForUser(name: String, callback: (List<Review>) -> Unit) {
        val url = "http://10.0.2.2:5133/api/review/user/$name"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(emptyList())
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val rawResponse = responseBody.string()
                    println("Response from API: $rawResponse")
                    val jsonResponse = JSONArray(rawResponse)
                    val reviews = mutableListOf<Review>()

                    for (i in 0 until jsonResponse.length()) {
                        val jsonReview = jsonResponse.getJSONObject(i)
                        val id = jsonReview.getInt("id")
                        val name = jsonReview.getString("name")
                        val vendorID = jsonReview.getInt("vendorID")
                        val description = jsonReview.getString("description")
                        val rate = jsonReview.getInt("rate")

                        reviews.add(Review(id, vendorID, name, description, rate))
                    }


                    callback(reviews)
                }
            }
        })
    }


    fun fetchSingleReview(Id: Int, callback: (Review?) -> Unit) {
        val url = "http://10.0.2.2:5133/api/review/$Id"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { responseBody ->
                    val rawResponse = responseBody.string()
                    println("Response from API: $rawResponse")
                    val jsonReview = JSONObject(rawResponse)


                    val id = jsonReview.getInt("id")
                    val name = jsonReview.getString("name")
                    val vendorID = jsonReview.getInt("vendorID")
                    val description = jsonReview.getString("description")
                    val rate = jsonReview.getInt("rate")

                    val review = Review(id, vendorID, name, description, rate)
                    callback(review)
                } ?: callback(null)
            }
        })
    }


    fun updateReview(review: Review, callback: (Boolean) -> Unit) {
        val url = "http://10.0.2.2:5133/api/review/${review.id}"

        Log.d("ApiClient2", "Updating review at URL: $url")

        val jsonReview = JSONObject().apply {
            put("vendorID", review.vendorID)
            put("name", review.name)
            put("description", review.description)
            put("rate", review.rate)
        }

        val requestBody = jsonReview.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .put(requestBody)
            .addHeader("Content-Type", "application/json")  // Ensure correct content type header
            .build()

        Log.d("ApiClient2", "Review update payload: $jsonReview")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.e("ApiClient2", "Failed to update review: ${e.message}")
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("ApiClient2", "Response code: ${response.code}")
                Log.d("ApiClient2", "Response body: $responseBody")

                if (response.isSuccessful) {
                    Log.d("ApiClient2", "Review updated successfully")
                    callback(true)
                } else {
                    Log.e("ApiClient2", "Failed to update review, response code: ${response.code}")
                    Log.e("ApiClient2", "Server response: $responseBody")
                    callback(false)
                }
            }

        })
    }



    fun deleteReview(reviewId: Int, callback: (Boolean) -> Unit) {
        val url = "http://10.0.2.2:5133/api/review/$reviewId"

        val request = Request.Builder()
            .url(url)
            .delete() // Specify that this is a DELETE request
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.e("ApiClient2", "Failed to delete review: ${e.message}")
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("ApiClient2", "Response code: ${response.code}")
                Log.d("ApiClient2", "Response body: $responseBody")

                if (response.isSuccessful) {
                    Log.d("ApiClient2", "Review deleted successfully")
                    callback(true)
                } else {
                    Log.e("ApiClient2", "Failed to delete review, response code: ${response.code}")
                    Log.e("ApiClient2", "Server response: $responseBody")  // Log full response for debugging
                    callback(false)
                }
            }
        })
    }








}