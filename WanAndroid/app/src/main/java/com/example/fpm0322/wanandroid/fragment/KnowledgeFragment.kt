package com.example.fpm0322.wanandroid.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fpm0322.wanandroid.R
import com.example.fpm0322.wanandroid.adapter.KnowledgeAdapter
import com.example.fpm0322.wanandroid.bean.KnowledgeBean
import com.example.fpm0322.wanandroid.bean.KnowledgeInfo
import com.example.fpm0322.wanandroid.network.BaseObserver
import com.example.fpm0322.wanandroid.network.RetrofitManager
import kotlinx.android.synthetic.main.fragment_guide.*
import kotlinx.android.synthetic.main.fragment_knowledge.*
import org.jetbrains.anko.async
import java.math.BigDecimal

class KnowledgeFragment: Fragment() {

    private val TAG = "KnowledgeFragment"

    private var adapter:KnowledgeAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_knowledge,container,false)
    }

    override fun onStart() {
        super.onStart()
        initView()
        backToTop()
    }

    private fun initView(){
        adapter = KnowledgeAdapter(context!!)
        ry_knowledge.layoutManager = LinearLayoutManager(activity)
        ry_knowledge.adapter = adapter
        adapter!!.setDatas(titles,titlesMap)
    }

    private var titlesMap = HashMap<String,List<KnowledgeInfo>>()

    private var titles = ArrayList<String>()
    private fun initData(){
        async {
            RetrofitManager().getKnowledgeData(object:BaseObserver<KnowledgeBean>(){
                override fun onSuccess(datas: Any) {
                    val list = datas as List<*>
                    for (item in list){
                        val itemMap = item as Map<*, *>
                        val listMap = itemMap["children"]!! as List<*>
                        val list = ArrayList<KnowledgeInfo>()
                        for (map in listMap){
                            val mapInList = map as Map<*,*>
                            list.add(KnowledgeInfo(mapInList["name"].toString(), BigDecimal(mapInList["id"].toString()).toInt()))
                        }
                        titlesMap[itemMap["name"].toString()] = list
                        titles.add(itemMap["name"].toString())
                    }
                }

                override fun onFailed(code: Int) {
                    Log.e(TAG,"code:$code")
                }

            })
        }
    }

    private fun backToTop(){
        know_toTop.setOnClickListener {
            ry_knowledge.smoothScrollToPosition(0)
        }
    }
}