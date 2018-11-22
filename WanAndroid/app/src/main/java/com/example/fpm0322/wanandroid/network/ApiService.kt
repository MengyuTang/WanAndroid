package com.example.fpm0322.wanandroid.network

import com.example.fpm0322.wanandroid.bean.BaseBean
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**
 * 网络请求接口
 */
interface ApiService {

    @GET
    fun get(@Url url:String): Call<BaseBean>

    @GET("banner/json")
    fun getBanner(): Observable<BaseBean>

    @HTTP(method = "GET",path = "article/list/{page}/json",hasBody = false)
    fun getHomePageArticles(@Path("page") page:Int): Observable<BaseBean>

    @GET("article/list/1/json")
    fun getHomePageArticles(): Observable<BaseBean>

    @GET("navi/json")
    fun getGuideData(): Observable<BaseBean>

    @GET("tree/json")
    fun getKnowledgeData(): Observable<BaseBean>

    @GET("project/tree/json")
    fun getProjectType(): Observable<BaseBean>

    @HTTP(method = "GET",path = "/article/list/{page}/json",hasBody = false)
    fun getKnowledgeArticle(@Path("page") page:Int,@Query("cid") cid:Int): Observable<BaseBean>

    @HTTP(method = "GET",path = "/project/list/{page}/json",hasBody = false)
    fun getProjectArticle(@Path("page") page:Int,@Query("cid") cid:Int): Observable<BaseBean>

    @POST
    @FormUrlEncoded
    fun post(@Url url:String,@FieldMap map:Map<String,String>): Observable<BaseBean>
}