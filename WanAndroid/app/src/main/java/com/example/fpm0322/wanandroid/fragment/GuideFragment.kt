package com.example.fpm0322.wanandroid.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fpm0322.wanandroid.R
import com.example.fpm0322.wanandroid.activity.BannerDetailActivity
import com.example.fpm0322.wanandroid.adapter.GuideTitleAdapter
import com.example.fpm0322.wanandroid.bean.ArticleTypeBean
import com.example.fpm0322.wanandroid.network.BaseObserver
import com.example.fpm0322.wanandroid.network.RetrofitManager
import kotlinx.android.synthetic.main.fragment_guide.*
import kotlinx.android.synthetic.main.item_guide_content.view.*
import kotlinx.android.synthetic.main.item_guide_title.view.*
import org.jetbrains.anko.async

class GuideFragment: Fragment() {

    private var guideAdapter: GuideTitleAdapter? = null

    private var guideContentAdapter: GuideContentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(activity).inflate(R.layout.fragment_guide,container,false)
    }

    override fun onStart() {
        super.onStart()
        initView()
        backToTop()
    }

    private fun initView(){
        if (articles.size>0){
            var layoutManager = GridLayoutManager(activity,3)
            layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    var title = articles[position]
                    if (title.startsWith("title")){
                        return 3
                    }else{
                        with(articles[position].length){
                            return when {
                                this>=16 -> 3
                                this in 8..15 -> 2
                                else -> 1
                            }
                        }
                    }
                }
            }
            guideContentAdapter = GuideContentAdapter(context!!)
            ry_content_list.layoutManager = layoutManager
            ry_content_list.adapter = guideContentAdapter
            guideContentAdapter!!.setDatas(leftTitles,articles,articleUrl)
            ry_content_list.addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                    if (mShouldScrollContent&& RecyclerView.SCROLL_STATE_IDLE == newState) {
                        mShouldScrollContent = false
                        smoothMoveToPosition(ry_content_list, mToPosition,1)
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val firstPosition = ry_content_list.getChildLayoutPosition(ry_content_list.getChildAt(0))
                    with(articles[firstPosition]){
                        if (this.startsWith("title")){
                            with(leftTitles.indexOf(this.substring(5,this.length))){
                                if (this != -1){
                                    smoothMoveToPosition(ry_guide_list,this,2)
                                    for (i in 0 until selectedMap.size){
                                        selectedMap[leftTitles[i]] = i == this
                                    }
                                    guideAdapter!!.setDatas(selectedMap,leftTitles)
                                }else{
                                    smoothMoveToPosition(ry_guide_list,this+1,2)
                                    for (i in 0 until selectedMap.size){
                                        selectedMap[leftTitles[i]] = i == this+1
                                    }
                                    guideAdapter!!.setDatas(selectedMap,leftTitles)
                                }
                            }
                        }
                    }
                }
            })
        }

        guideAdapter = GuideTitleAdapter(context!!)
        ry_guide_list.layoutManager = LinearLayoutManager(activity)
        ry_guide_list.adapter = guideAdapter
        ry_guide_list.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (mShouldScrollTitle&& RecyclerView.SCROLL_STATE_IDLE == newState) {
                    mShouldScrollTitle = false
                    smoothMoveToPosition(ry_guide_list, mToPosition,2)
                }
            }
        })
        guideAdapter!!.setDatas(selectedMap,leftTitles)
        guideAdapter!!.setMyClickListener(listener = object :GuideTitleAdapter.MyClickListener{
            override fun onClick(position: Int) {
                for (i in 0 until selectedMap.size){
                    selectedMap[leftTitles[i]] = i == position
                }
                guideAdapter!!.setDatas(selectedMap,leftTitles)

                with(articles.indexOf("title"+leftTitles[position])){
                    if (this != -1) {
                        smoothMoveToPosition(ry_content_list,this,1)
                    }else {
                        smoothMoveToPosition(ry_content_list,this+1,1)
                    }
                }
            }
        })
    }

    private var leftTitles = ArrayList<String>()

    private var articles = ArrayList<String>()

    private var selectedMap = HashMap<String,Boolean>()
    private var articleTypeMap = HashMap<String,List<*>>()
    var articleUrl = HashMap<String,String>()

    private fun initData(){
        async {
            RetrofitManager().getGuideData(object : BaseObserver<ArticleTypeBean>(){
                override fun onSuccess(datas: Any) {
                    var articleTypes = datas as ArrayList<*>
                    if (articleTypes.size>0){
                        for (articleType in articleTypes){
                            var map = articleType as Map<*, *>
                            leftTitles.add(map["name"].toString())
                            articleTypeMap[map["name"].toString()] = map["articles"] as List<*>
                            for (i in 0 until leftTitles.size){
                                selectedMap[leftTitles[i]] = false
                            }
                        }
                        for (title in leftTitles){
                            with(articleTypeMap[title]!!){
                                articles.add("title$title")
                                for (article in this){
                                    var map = article as Map<*, *>
                                    articles.add(map["title"].toString())
                                    articleUrl[map["title"].toString()] = map["link"].toString()
                                }
                            }
                        }
                    }
                }

                override fun onFailed(code: Int) {
                    Log.e("GuideFragment", "code:$code")
                }

            })
        }
    }

    private fun backToTop(){
        guide_toTop.setOnClickListener {
            ry_content_list.smoothScrollToPosition(0)
            ry_guide_list.smoothScrollToPosition(0)
        }
    }

    /**
     * 内容recyclerView是否滑动
     */
    private var mShouldScrollContent:Boolean = false

    /**
     * 标题recyclerView是否滑动
     */
    private var mShouldScrollTitle:Boolean = false

    /**
     * 要滑动到的位置
     */
    private var mToPosition:Int = 0

    /**
     * @param recyclerView 要滑动的recyclerView
     * @param position   滑动目标位置
     * @param type  用来区分是标题recyclerView还是内容recyclerView
     */
    fun smoothMoveToPosition(recyclerView:RecyclerView,position:Int,type:Int){
        var firstVisibleItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0))
        var lastVisibleItem = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(recyclerView.childCount -1))
        if (position< firstVisibleItem){
            recyclerView.smoothScrollToPosition(position)
        }else if (position <= lastVisibleItem){
            val movePosition = position - firstVisibleItem
            if (movePosition>0 && movePosition< recyclerView.childCount){
                val top = recyclerView.getChildAt(movePosition).top
                recyclerView.smoothScrollBy(0,top)
            }
        }else{
            recyclerView.smoothScrollToPosition(position)
            mToPosition = position
            if (type == 1){
                mShouldScrollContent = true
            }else{
                mShouldScrollTitle = true
            }
        }
    }

    class GuideContentAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var titles = ArrayList<String>()
        private var articles = ArrayList<String>()
        private var articleUrl = HashMap<String,String>()
        private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

        fun setDatas(titles: ArrayList<String>, articles: ArrayList<String>,articleUrl:HashMap<String,String>) {
            this.titles = titles
            this.articles = articles
            this.articleUrl = articleUrl
            notifyDataSetChanged()
        }

        enum class ITEMTYPE{
            ITEM_TITLE,
            ITEM_CONTENT
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
            if (i == ITEMTYPE.ITEM_TITLE.ordinal){
                return GuideTitleHolder(mLayoutInflater.inflate(R.layout.item_guide_content_title, viewGroup, false))
            }else{
                return GuideContentHolder(mLayoutInflater.inflate(R.layout.item_guide_content, viewGroup, false))
            }
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            if (articles.size>0){
                if (viewHolder is GuideTitleHolder){
                    viewHolder.itemView.tv_guide.text = articles[position].substring(5,articles[position].length)
                    viewHolder.itemView.tv_guide.textSize = 25f
                    viewHolder.itemView.tv_guide.setTextColor(Color.parseColor("#333333"))
                }else if (viewHolder is GuideContentHolder){
                    val colors:List<String>  = listOf("#FF4081","#3F51B5","#9a87a7"
                            ,"#59f8fa","#333333")
                    val i:Int = (Math.random()*5).toInt()
                    with(viewHolder.itemView.tv_guide_content){
                        this.text = articles[position]
                        this.setTextColor(Color.parseColor(colors[i]))
                        this.setOnClickListener {
                            //跳转Activity
                            var intent = Intent()
                            intent.setClass(mContext, BannerDetailActivity::class.java)
                            intent.putExtra("title",articles[position])
                            intent.putExtra("url",articleUrl[articles[position]] )
                            mContext.startActivity(intent)
                        }
                    }

                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            if (articles[position].startsWith("title")){
                return ITEMTYPE.ITEM_TITLE.ordinal
            }else{
                return ITEMTYPE.ITEM_CONTENT.ordinal
            }
        }

        override fun getItemCount(): Int {
            return articles.size
        }

        inner class GuideTitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        inner class GuideContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

}