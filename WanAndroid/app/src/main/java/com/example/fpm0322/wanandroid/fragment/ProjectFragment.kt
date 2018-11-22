package com.example.fpm0322.wanandroid.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fpm0322.wanandroid.R
import com.example.fpm0322.wanandroid.adapter.ProjectsAdapter
import com.example.fpm0322.wanandroid.bean.ArticleInfoBean
import com.example.fpm0322.wanandroid.bean.ArticleSumBean
import com.example.fpm0322.wanandroid.bean.KnowledgeBean
import com.example.fpm0322.wanandroid.bean.KnowledgeInfo
import com.example.fpm0322.wanandroid.network.BaseObserver
import com.example.fpm0322.wanandroid.network.RetrofitManager
import kotlinx.android.synthetic.main.fragment_project.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread
import java.math.BigDecimal

class ProjectFragment: Fragment() {

    private var TAG = "ProjectFragment"

    private var adapter:ProjectsAdapter? = null

    private var articleInfos = ArrayList<ArticleInfoBean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_project,container,false)
    }

    override fun onStart() {
        super.onStart()
        initView()
        backToTop()
    }

    private fun initView(){
        project_tab.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position:Int = project_tab.selectedTabPosition
                val item = projectTypes[position]
                async {
                    try {
                        RetrofitManager().getProjectArticle(0,item.id,object :BaseObserver<ArticleSumBean>(){
                            override fun onSuccess(datas: Any) {
                                val articleSumBean = datas as Map<*, *>
                                if (articleSumBean.isNotEmpty()){
                                    articleInfos = articleSumBean["datas"] as ArrayList<ArticleInfoBean>
                                    if (articleInfos.size>0){
                                        uiThread {
                                            adapter = ProjectsAdapter(context!!)
                                            ry_projects.layoutManager = LinearLayoutManager(activity)
                                            ry_projects.adapter = adapter
                                            adapter!!.setDatas(articleInfos)
                                        }
                                    }
                                }
                            }

                            override fun onFailed(code: Int) {
                                Log.e(TAG,"code:$code")
                            }
                        })
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }
            }
        })

        for (item in projectTypes){
            val tab = project_tab.newTab()
            tab.text = item.name
            project_tab.addTab(tab)
        }
        project_tab.setTabTextColors(ContextCompat.getColor(activity!!,R.color.color_light_gray),ContextCompat.getColor(activity!!,R.color.color_white))
        project_tab.setSelectedTabIndicatorColor(ContextCompat.getColor(activity!!, R.color.color_white))
    }

    private var projectTypes = ArrayList<KnowledgeInfo>()

    private fun initData(){
        RetrofitManager().getProjectType(object :BaseObserver<KnowledgeBean>(){
            override fun onSuccess(datas: Any) {
                val list = datas as List<*>
                projectTypes.clear()
                for (item in list){
                    val itemMap = item as Map<*, *>
                    projectTypes.add(KnowledgeInfo(itemMap["name"].toString(), BigDecimal(itemMap["id"].toString()).toInt()))
                }
            }

            override fun onFailed(code: Int) {
            }

        })
    }

    private fun backToTop(){
        projects_toTop.setOnClickListener {
            ry_projects.smoothScrollToPosition(0)
        }
    }
}