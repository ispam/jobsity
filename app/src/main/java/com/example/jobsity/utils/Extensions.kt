package com.example.jobsity.utils

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.jobsity.common.delegate.DelegateAdapter
import com.example.jobsity.common.delegate.ItemDelegate
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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