package com.example.fpm0322.wanandroid.utils

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import java.lang.reflect.Field

class BottomNavigationViewHelper {
    companion object {
        /**
         * BottomNavigationView取消点击动画效果
         */
        @SuppressLint("RestrictedApi")
        fun disableShiftMode(view: BottomNavigationView){
            var menuItem: BottomNavigationMenuView = view.getChildAt(0) as BottomNavigationMenuView
            var shiftingMode: Field = menuItem.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuItem,false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuItem.getChildCount()){
                var itemView: BottomNavigationItemView = menuItem.getChildAt(i) as BottomNavigationItemView
                itemView.setShiftingMode(false)
                itemView.setChecked(itemView.itemData.isChecked)
            }
        }
    }
}