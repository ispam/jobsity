package com.example.jobsity.main_screen.adapters

import com.example.jobsity.common.delegate.GenericViewAdapter
import com.example.jobsity.data.local.entities.ShowItem
import com.example.jobsity.main_screen.delegates.ShowDelegate
import com.example.jobsity.utils.SHOW_TYPE
import com.example.jobsity.utils.toDA

class ShowAdapter(
    onClick: (ShowItem) -> Unit
): GenericViewAdapter() {

    init {
        delegateAdapters.put(
            SHOW_TYPE,
            ShowDelegate(onClick).toDA()
        )
    }
}