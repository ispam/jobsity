package com.example.jobsity.details.adapters

import com.example.jobsity.common.delegate.GenericViewAdapter
import com.example.jobsity.data.local.entities.EpisodeItem
import com.example.jobsity.details.delegates.SeasonDelegate
import com.example.jobsity.details.delegates.ShowDetailsEpisodeDelegate
import com.example.jobsity.main_screen.delegates.ShowDelegate
import com.example.jobsity.utils.SEASON_TYPE
import com.example.jobsity.utils.SHOW_DETAILS_TYPE
import com.example.jobsity.utils.SHOW_TYPE
import com.example.jobsity.utils.toDA

class ShowDetailsAdapter(
    onClick: (EpisodeItem) -> Unit
): GenericViewAdapter() {

    init {
        delegateAdapters.put(
            SHOW_TYPE,
            ShowDelegate(null).toDA()
        )
        delegateAdapters.put(
            SHOW_DETAILS_TYPE,
            ShowDetailsEpisodeDelegate(onClick).toDA()
        )
        delegateAdapters.put(
            SEASON_TYPE,
            SeasonDelegate().toDA()
        )
    }
}