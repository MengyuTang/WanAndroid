package com.example.fpm0322.wanandroid.network

import android.util.Log
import com.example.fpm0322.wanandroid.bean.BaseBean
import com.example.fpm0322.wanandroid.bean.ErrorType
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketException

abstract class BaseObserver<T> : Observer<BaseBean> {

    override fun onNext(bean: BaseBean) {
        Log.e("BaseObserver", "onNext:"+bean.toString())
        onSuccess(bean.data)
    }

    override fun onComplete() {
        Log.e("BaseObserver", "onComplete")
    }

    override fun onError(e: Throwable) {
        Log.e("BaseObserver", "onError:" + e.printStackTrace())
        when (e.javaClass) {
            HttpException::class -> onFailed(ErrorType.HTTP_ERROR.ordinal)
            SocketException::class -> onFailed(ErrorType.NET_WORK_ERROR.ordinal)
            JSONException::class -> onFailed(ErrorType.JSON_FORMAT_ERROR.ordinal)
            else -> onFailed(ErrorType.UNKNOW_ERROR.ordinal)
        }
    }

    override fun onSubscribe(d: Disposable) {
        Log.e("BaseObserver", "onSubscribe:" + d.isDisposed)
    }

    abstract fun onSuccess(datas: Any)

    abstract fun onFailed(code: Int)

}

