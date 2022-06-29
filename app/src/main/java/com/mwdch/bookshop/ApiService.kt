package com.mwdch.bookshop

import com.google.gson.JsonObject
import com.mwdch.bookshop.model.*
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(@Body jsonObject: JsonObject): Single<TokenResponse>

    @POST("register")
    fun signUp(@Body jsonObject: JsonObject): Single<Response>

    @GET("cats")
    fun getAllCategories(): Single<List<Category>>

    @GET("banners")
    fun getAllBanners(): Single<List<Banner>>

    @GET("books")
    fun getAllBooks(): Single<List<Book>>

    @GET("bookscat/{category}")
    fun getBooksByCategory(@Path("category") category: Int): Single<List<Book>>

    @GET("profile/{username}")
    fun getCustomer(@Path("username") username: String): Single<User>

    @POST("postcomment")
    fun sendComment(@Body jsonObject: JsonObject): Single<Comment>

    @GET("bookcomments/{bookId}")
    fun getComments(@Path("bookId") bookId: String): Single<List<Comment>>

    @PUT("person/{userId}")
    fun updateUserInfo(
        @Path("userId") userId: String,
        @Body jsonObject: JsonObject
    ): Completable

    @PUT("person/password")
    fun updateUserPassword(
        @Body jsonObject: JsonObject
    ): Completable

    @GET("booksearch/{bookName}")
    fun search(@Path("bookName") bookName: String): Single<List<Book>>

    @GET("orders/{customerId}")
    fun getCartBooks(@Path("customerId") customerId: String): Single<List<Cart>>

    @GET("orders/customer/{customerId}")
    fun getOrders(@Path("customerId") customerId: String): Single<List<Order>>

    @POST("orders")
    fun addToCart(
        @Body jsonObject: JsonObject
    ): Single<AddToCartResponse>

    @PUT("orders/purchase/{customerId}")
    fun purchase(@Path("customerId") customerId: String): Completable

}

fun createApiServiceInstance(): ApiService {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val oldRequest = it.request()
            val newRequestBuilder = oldRequest.newBuilder()
            if (TokenContainer.token != null)
                newRequestBuilder.addHeader("Authorization", "Bearer ${TokenContainer.token}")
            newRequestBuilder.method(oldRequest.method, oldRequest.body)
            return@addInterceptor it.proceed(newRequestBuilder.build())

        }.addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }).build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://bookshop.iran.liara.run/api/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    return retrofit.create(ApiService::class.java)
}