package com.example.fpm0322.wanandroid.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.fpm0322.wanandroid.R
import com.example.fpm0322.wanandroid.activity.BannerDetailActivity
import com.example.fpm0322.wanandroid.bean.ArticleInfoBean
import com.example.fpm0322.wanandroid.utils.GlideImageLoader
import kotlinx.android.synthetic.main.item_article_info.view.*
import kotlinx.android.synthetic.main.item_banners.view.*
import java.math.BigDecimal

class HomeAdapter(private val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var bannerImagePaths: List<String>? = null
    private var bannerInfoUrls: List<String>? = null
    private var bannerTitles: List<String>? = null
    private var articles: List<ArticleInfoBean>? = null
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    enum class ITEM_TYPE {
        ITEM_BANNER,
        ITEM_ARTICLE
    }

    fun setDatas(bannerImagePaths: List<String>, bannerInfoUrls: List<String>,bannerTitles: List<String>, articles: List<ArticleInfoBean>) {
        this.bannerImagePaths = bannerImagePaths
        this.bannerInfoUrls = bannerInfoUrls
        this.bannerTitles = bannerTitles
        this.articles = articles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return if (i == 0) {
            BannerHolder(mLayoutInflater.inflate(R.layout.item_banners, viewGroup, false))
        } else {
            ArticleHolder(mLayoutInflater.inflate(R.layout.item_article_info, viewGroup, false))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is BannerHolder) {
            with(viewHolder.itemView) {
                banner.setImageLoader(GlideImageLoader())
                banner.setImages(bannerImagePaths)
                banner.start()
                banner.setOnBannerListener {
                    val intent = Intent()
                    intent.setClass(mContext, BannerDetailActivity::class.java)
                    intent.putExtra("url", bannerInfoUrls!![it])
                    intent.putExtra("title",bannerTitles!![it])
                    mContext.startActivity(intent)
                }
            }
        } else if (viewHolder is ArticleHolder) {
            if (articles!!.isNotEmpty()) {
                with(viewHolder.itemView) {
                    val article = articles!![position] as Map<String, Any>
                    tv_label.text = article["chapterName"].toString()
                    tv_author.text = article["author"].toString()
                    tv_title.text = article["title"].toString()
                    val time = BigDecimal(article["publishTime"].toString()).toLong()
                    val dateTime = System.currentTimeMillis().minus(time.toString().toLong())
                    with(dateTime) {
                        if (this < 2 * 24 * 60 * 60 * 1000) {
                            when {
                                this < 60 * 60 * 1000 -> {
                                    tv_new.visibility = View.VISIBLE
                                    tv_time.text = this.div(60 * 1000).toString() + "分钟前"
                                }
                                this < 24 * 60 * 60 * 1000 -> {
                                    tv_new.visibility = View.VISIBLE
                                    tv_time.text = this.div(60 * 60 * 1000).toString() + "小时前"
                                }
                                else -> {
                                    tv_new.visibility = View.GONE
                                    tv_time.text = this.div(24 * 60 * 60 * 1000).toString() + "天前"
                                }
                            }
                        } else {
                            tv_new.visibility = View.GONE
                            tv_time.text = article["niceDate"].toString()
                        }
                    }
                    if (article["envelopePic"].toString().isEmpty()){
                        picture.visibility = View.GONE
                    }else{
                        picture.visibility = View.VISIBLE
                        Glide.with(mContext)
                                .load(article["envelopePic"].toString())
                                .into(picture)
                    }
                    iv_collect.setOnClickListener {
                        iv_collect.isSelected = !iv_collect.isSelected
                    }

                    this.setOnClickListener {
                        val intent = Intent()
                        intent.setClass(mContext, BannerDetailActivity::class.java)
                        intent.putExtra("url", article["link"].toString())
                        intent.putExtra("title",article["title"].toString())
                        mContext.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ITEM_TYPE.ITEM_BANNER.ordinal else ITEM_TYPE.ITEM_ARTICLE.ordinal
    }

    override fun getItemCount(): Int {
        return if (null == articles) {
            0
        } else articles!!.size
    }

    inner class BannerHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
