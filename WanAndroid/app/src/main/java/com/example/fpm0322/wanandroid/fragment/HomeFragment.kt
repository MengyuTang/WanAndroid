package com.example.fpm0322.wanandroid.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fpm0322.wanandroid.R
import com.example.fpm0322.wanandroid.adapter.HomeAdapter
import com.example.fpm0322.wanandroid.bean.ArticleInfoBean
import com.example.fpm0322.wanandroid.bean.ArticleSumBean
import com.example.fpm0322.wanandroid.bean.BannerBean
import com.example.fpm0322.wanandroid.network.BaseObserver
import com.example.fpm0322.wanandroid.network.RetrofitManager
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.lang.Exception

class HomeFragment: Fragment() {

    private var banners = ArrayList<String>()
    private var bannerDetailUrl = ArrayList<String>()
    private var bannerTitles = ArrayList<String>()
    private var articleInfos = ArrayList<ArticleInfoBean>()
    private val retrofitManager = RetrofitManager()
    private var adapter:HomeAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_home,container,false)
    }

    override fun onStart() {
        super.onStart()
        adapter = HomeAdapter(this.context!!)
        var layoutManager = LinearLayoutManager(activity)
        home_recyclerView.layoutManager = layoutManager
        home_recyclerView.adapter = adapter
        initView()
        backToTop()
    }

     private fun initView(){
         var flagBanner  =false
         var flagArticle  =false
         try {
             //获取Banner图片
             async {
                 retrofitManager.getBanner(object: BaseObserver<BannerBean>(){

                     override fun onFailed(code: Int) {
                         Log.e(javaClass.simpleName, "code:$code")
                     }

                     override fun onSuccess(datas:Any) {
                         var bannerBeans = datas as ArrayList<Any>
                         if (bannerBeans.size>0){
                             for (item in bannerBeans){
                                 var map = item as Map<String,String>
                                 banners.add(map["imagePath"]!!)
                                 bannerDetailUrl.add(map["url"]!!)
                                 bannerTitles.add(map["title"]!!)
                                 flagBanner =  true
                                 if (flagBanner && flagArticle){
                                     uiThread {
                                         adapter?.setDatas(banners,bannerDetailUrl,bannerTitles,articleInfos)
                                     }
                                 }
                             }
                         }
                     }
                 })
             }
             async {
                 retrofitManager.getHomePageArticles(0,object :BaseObserver<ArticleSumBean>(){
                     override fun onSuccess(datas: Any) {
                         var articleSumBean = datas as Map<*, *>
                         if (articleSumBean.isNotEmpty()){
                             articleInfos = articleSumBean["datas"] as ArrayList<ArticleInfoBean>
                             if (articleInfos.size>0){
                                 flagArticle = true
                                 if (flagBanner && flagArticle){
                                     uiThread {
                                         adapter?.setDatas(banners,bannerDetailUrl,bannerTitles,articleInfos)
                                     }
                                 }
                             }
                         }
                     }

                     override fun onFailed(code: Int) {
                         Log.e("HomeFragment", "code:$code")
                     }
                 }) }
         }catch (e:Exception){
            e.printStackTrace()
         }
    }

    private fun backToTop(){
        toTop.setOnClickListener {
            home_recyclerView.smoothScrollToPosition(0)
        }
    }
}