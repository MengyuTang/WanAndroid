package com.example.fpm0322.wanandroid.bean

/**
 * 基础数据返回类
 */
data class BaseBean(val data: Any, val errorCode: Int, val errorMsg: String)

/**
 * Banner资源类
 */
data class BannerBean(var desc: String, var id: Int, var imagePath: String, var isVisible: Int, var order: Int, var title: String, var type: Int, var url: String)

/**
 * 首页文章统计信息
 */
data class ArticleSumBean(
        val curPage: Int,
        val datas: List<Any>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
)

/**
 * w
 */
data class ArticleInfoBean(
        val apkLink: String,
        val author: String,
        val chapterId: Int,
        val chapterName: String,
        val collect: Boolean,
        val courseId: Int,
        val desc: String,
        val envelopePic: String,
        val fresh: Boolean,
        val id: Int,
        val link: String,
        val niceDate: String,
        val origin: String,
        val projectLink: String,
        val publishTime: Long,
        val superChapterId: Int,
        val superChapterName: String,
        val tags: List<Tag>,
        val title: String,
        val type: Int,
        val userId: Int,
        val visible: Int,
        val zan: Int
)

data class Tag(
        val name: String,
        val url: String
)

data class ArticleTypeBean(
    val articles: List<Any>,
    val cid: Int,
    val name: String
)

data class KnowledgeBean(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val visible: Int
)

data class KnowledgeInfo(val name:String,val id: Int)

enum class ErrorType(code: Int) {
    UNKNOW_ERROR(1000),
    JSON_FORMAT_ERROR(1001),
    NET_WORK_ERROR(1002),
    HTTP_ERROR(1003),
}