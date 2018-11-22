package com.example.fpm0322.wanandroid.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fpm0322.wanandroid.R
import com.example.fpm0322.wanandroid.adapter.KnowledgeDetailAdapter
import com.example.fpm0322.wanandroid.bean.ArticleInfoBean
import kotlinx.android.synthetic.main.fragment_tab.*

class TabFragment: Fragment() {

    private var adapter: KnowledgeDetailAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_tab,container,false)
    }

    fun setDatas(articleInfos:ArrayList<ArticleInfoBean>){
        adapter = KnowledgeDetailAdapter(activity!!)
        ry_knowledge.layoutManager = LinearLayoutManager(activity!!)
        ry_knowledge.adapter = adapter
        adapter!!.setDatas(articleInfos)
        knowledge_toTop.setOnClickListener {
            ry_knowledge.smoothScrollToPosition(0)
        }
    }

}