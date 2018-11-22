package com.example.fpm0322.wanandroid.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fpm0322.wanandroid.R
import kotlinx.android.synthetic.main.item_guide_title.view.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor
import java.util.*

class GuideTitleAdapter(private val mContext: Context): RecyclerView.Adapter<GuideTitleAdapter.TitleViewHolder>() {

    private var guideTitles =  ArrayList<String>()

    private var mLayoutInflater:LayoutInflater

    private var selectedMap = HashMap<String,Boolean>()

    interface MyClickListener{
        fun onClick(position: Int)
    }

    private var listener:MyClickListener? = null

    fun setMyClickListener(listener:MyClickListener){
        this.listener = listener
    }

     init {
        mLayoutInflater = LayoutInflater.from(mContext)
    }

    fun setDatas(selectedMap:HashMap<String,Boolean>,guideTitles:ArrayList<String>){
        this.guideTitles = guideTitles
        this.selectedMap = selectedMap
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        var view = mLayoutInflater.inflate(R.layout.item_guide_title,parent,false)
        return TitleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return guideTitles.size
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        if (guideTitles.size>0){
            with(holder.itemView){
                this.tv_guide.text = guideTitles[position]
                this.tv_guide.setOnClickListener {
                    listener!!.onClick(position)
                }
                if (selectedMap[guideTitles[position]]!!){
                    this.backgroundResource = R.color.color_white
                    this.tv_guide.textColor = resources.getColor(R.color.color_sky_blue)
                }else{
                    this.backgroundResource = R.color.color_light_gray
                    this.tv_guide.textColor = resources.getColor(R.color.color_deep_gray)
                }
            }
        }
    }

    class TitleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
}