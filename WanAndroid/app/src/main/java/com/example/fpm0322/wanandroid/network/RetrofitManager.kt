package com.example.fpm0322.wanandroid.network

import com.example.fpm0322.wanandroid.bean.BaseBean
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitManager() {

    private var okHttpClient = OkHttpClient.Builder()
            .readTimeout(8, TimeUnit.SECONDS)
            .connectTimeout(8,TimeUnit.SECONDS)
            .build()

        private var apiService = Retrofit.Builder()
                .baseUrl("http://www.wanandroid.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)

        fun getBanner(observer: Observer<BaseBean>){
            apiService.getBanner()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer)
        }

        fun getHomePageArticles(page:Int, observer: Observer<BaseBean>){
            apiService.getHomePageArticles(page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer)
        }

        fun getGuideData(observer: Observer<BaseBean>){
            apiService.getGuideData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer)
        }

        fun getKnowledgeData(observer: Observer<BaseBean>){
            apiService.getKnowledgeData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer)
        }

        fun getKnowledgeArticle(page:Int,cid:Int,observer: Observer<BaseBean>){
            apiService.getKnowledgeArticle(page,cid)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer)
        }

        fun getProjectType(observer: Observer<BaseBean>){
            apiService.getProjectType()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer)
        }

        fun getProjectArticle(page:Int,id:Int,observer: Observer<BaseBean>){
            apiService.getProjectArticle(page,id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer)
        }

        fun post(url:String,map: Map<String,String>,observer: Observer<BaseBean>){
            apiService.post(url,map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer)
        }

}