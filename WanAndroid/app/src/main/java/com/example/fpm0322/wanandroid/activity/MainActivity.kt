package com.example.fpm0322.wanandroid.activity

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import com.example.fpm0322.wanandroid.R
import com.example.fpm0322.wanandroid.fragment.GuideFragment
import com.example.fpm0322.wanandroid.fragment.HomeFragment
import com.example.fpm0322.wanandroid.fragment.KnowledgeFragment
import com.example.fpm0322.wanandroid.fragment.ProjectFragment
import com.example.fpm0322.wanandroid.utils.BottomNavigationViewHelper
import com.example.fpm0322.wanandroid.utils.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var currentActivity: MainActivity? = null

    private var homeFragment: HomeFragment? = null
    private var guideFragment: GuideFragment? = null
    private var knowledgeFragment: KnowledgeFragment? = null
    private var projectFragment: ProjectFragment? = null

    private var showingTag:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
        initFragment(savedInstanceState)
        initView()
    }

    private fun initToolbar(){
        toolBar.title = resources.getString(R.string.home_page)
        toolBar.setTitleTextColor(Color.WHITE)
        toolBar.navigationIcon = resources.getDrawable(R.mipmap.more)
        setSupportActionBar(toolBar)
    }

    private fun initView(){
        currentActivity = this
        val mDrawerToggle =
                ActionBarDrawerToggle(currentActivity,drawerLayout,toolBar, R.string.drawer_open, R.string.drawer_close)
        mDrawerToggle.syncState()
        drawerLayout.setDrawerListener(mDrawerToggle)
        //侧边栏
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.collection -> consume {
                    toolBar.setTitle(R.string.collection)}
                R.id.todo_tools -> consume{
                    toolBar.setTitle(R.string.todo_tools)}
                R.id.dark_mode -> consume {
                    toolBar.setTitle(R.string.dark_mode)}
                R.id.settings -> consume {
                    toolBar.setTitle(R.string.settings)}
                R.id.about_us -> consume {
                    toolBar.setTitle(R.string.about_us)}
                R.id.logout -> consume {
                    toolBar.setTitle(R.string.logout) }
                else -> consume {
                    toolBar.setTitle(R.string.home_page)}
            }
            drawerLayout.closeDrawers()
            true
        }

        //底部导航栏
        BottomNavigationViewHelper.disableShiftMode(design_bottom_sheet)
        design_bottom_sheet.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_page -> consume {
                    if (!showingTag.equals("homeFragment")){
                        toolBar.setTitle(R.string.home_page)
                        addFragment(homeFragment!!,"homeFragment")
                        showFragment(homeFragment!!)
                    }
                }
                R.id.knowledge_system -> consume{
                    if (!showingTag.equals("knowledgeFragment")){
                        toolBar.setTitle(R.string.knowledge_system)
                        addFragment(knowledgeFragment!!,"knowledgeFragment")
                        showFragment(knowledgeFragment!!)
                    }
                   }
                R.id.guide -> consume {
                    if (!showingTag.equals("guideFragment")){
                        toolBar.setTitle(R.string.guide)
                        addFragment(guideFragment!!,"guideFragment")
                        showFragment(guideFragment!!)
                    }
              }
                R.id.projects -> consume {
                    if (!showingTag.equals("projectFragment")) {
                        toolBar.setTitle(R.string.project)
                        addFragment(projectFragment!!, "projectFragment")
                        showFragment(projectFragment!!)
                    }
                }
                else -> consume {
                    if (!showingTag.equals("projectFragment")){
                        toolBar.setTitle(R.string.home_page)
                        addFragment(homeFragment!!,"homeFragment")
                        showFragment(homeFragment!!)
                    }
                   }
            }
        }
        btnSearch.setOnClickListener { toast("搜索") }
    }

     private inline fun consume(f: () -> Unit): Boolean {
         f()
         return true
     }

    /**
     * 第一次打开，新建Fragment,后面再打开，从保存的状态中恢复数据
     * @param savedInstanceState 恢复数据来源
     */
    private fun initFragment(savedInstanceState: Bundle?){
            homeFragment = HomeFragment()
            knowledgeFragment = KnowledgeFragment()
            guideFragment = GuideFragment()
            projectFragment = ProjectFragment()
        addFragment(homeFragment!!,"homeFragment")
        showFragment(homeFragment!!)
    }

    private fun addFragment(fragment:Fragment,tag: String){
        var ft:FragmentTransaction? = supportFragmentManager.beginTransaction()
        ft?.replace(R.id.frameLayout,fragment,tag)
        showingTag =tag
        ft?.commitAllowingStateLoss()
    }

    private fun showFragment(fragment:Fragment?){
        var ft:FragmentTransaction? = supportFragmentManager.beginTransaction()
        ft?.hide(homeFragment!!)
        ft?.hide(knowledgeFragment!!)
        ft?.hide(guideFragment!!)
        ft?.hide(projectFragment!!)
        ft?.show(fragment)
        ft?.commitAllowingStateLoss()
    }
}
