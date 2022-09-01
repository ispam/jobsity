package com.example.jobsity.utils

import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.jobsity.common.delegate.DelegateAdapter
import com.example.jobsity.common.delegate.ItemDelegate
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.json.JSONObject

fun DelegateAdapter<*, *>.toDA(): DelegateAdapter<RecyclerView.ViewHolder, ItemDelegate> {
    return this as DelegateAdapter<RecyclerView.ViewHolder, ItemDelegate>
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

inline fun <T: Any, L: StateFlow<T>> LifecycleOwner.observeFlow(stateFlow: L, crossinline body: (T) -> Unit) {
    lifecycleScope.launch {
        stateFlow.collect{ body(it) }
    }
}

fun Any.convertToJsonString(): String {
    return Gson().toJson(this).orEmpty()
}

inline fun <reified T> JSONObject.toModel(): T? = this.run {
    try {
        Gson().fromJson<T>(this.toString(), T::class.java)
    }
    catch (e: Exception){
        e.printStackTrace()
        Log.e("JSONObject to model", e.message.toString())
        null
    }
}

inline fun <reified T> String.toModel(): T? = this.run {
    try {
        JSONObject(this).toModel<T>()
    }
    catch (e: Exception){
        e.printStackTrace()
        Log.e("String to model", e.message.toString())
        null
    }
}

fun <T> Flow<T>.handleErrors(block: (Throwable) -> Unit): Flow<T> =
    catch { e -> block.invoke(e) }