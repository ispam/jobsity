package com.example.jobsity

import com.example.jobsity.common.delegate.ItemDelegate
import com.example.jobsity.data.local.ShowRepository
import com.example.jobsity.data.local.entities.Embedded
import com.example.jobsity.data.local.entities.EpisodeItem
import com.example.jobsity.data.local.entities.Image
import com.example.jobsity.data.local.entities.Rating
import com.example.jobsity.data.local.entities.Schedule
import com.example.jobsity.data.local.entities.ShowItem
import com.example.jobsity.details.delegates.SeasonDelegate
import com.example.jobsity.main_screen.view_model.DetailsState
import com.example.jobsity.main_screen.view_model.ShowState
import com.example.jobsity.main_screen.view_model.ShowViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShowViewModelTest {

    val dispatcher = TestCoroutineDispatcher()

    @JvmField
    @RegisterExtension
    val coroutineTestExtension = CoroutineTestExtension(dispatcher)

    private lateinit var repositoryMock: ShowRepository
    private lateinit var viewModel: ShowViewModel

    @BeforeEach
    fun setup() {
        val episodes = mutableListOf<EpisodeItem>()
        var epidoseCounter = 0
        do {
            episodes.add(
                EpisodeItem(
                    epidoseCounter + 1,
                    "episode",
                    epidoseCounter + 1,
                    epidoseCounter + 1,
                    30,
                    Rating(3.4),
                    Image("mediumUrl", "originalUrl"),
                    "some summary"
                )
            )
            epidoseCounter++
        } while (epidoseCounter < 10)

        val embedded = Embedded(episodes)
        val list = mutableListOf<ShowItem>(
            ShowItem(
                1,
                "show",
                emptyList(),
                Image("mediumUrl", "originalUrl"),
                "big summary",
                "01/09/2022",
                Schedule("time", emptyList()),
                embedded
            )
        )
        repositoryMock = mockk<ShowRepository>().apply {
            coEvery { getShows() } returns flowOf(ShowState.ShowsLoaded(list))
            coEvery { getShowWithEpisodes(1) } returns flowOf(DetailsState.ShowWithEpisodes(list))
            coEvery { getAllFavoriteShows() } returns flowOf(list)
        }
        viewModel = ShowViewModel(repositoryMock)
    }

    @Test
    fun `App is initiated, load shows`() {
        val repositorySpy = spyk<ShowRepository>()
        ShowViewModel(repositorySpy)
        coVerify(exactly = 1) { repositorySpy.getShows() }
    }

    @Test
    fun `App is initiated, cannot load shows`() {
        repositoryMock = mockk<ShowRepository>().apply {
            coEvery { getShows() } returns flowOf(ShowState.Error(Throwable("IllegalState")))
        }
        viewModel = ShowViewModel(repositoryMock)
        coVerify(exactly = 1) { repositoryMock.getShows() }
        assert(viewModel.showState.value is ShowState.Error)
    }

    @Test
    fun `User clicks on a show, load it`() {
        viewModel.getShowWithEpisodes(1)
        coVerify(exactly = 1) { repositoryMock.getShowWithEpisodes(1) }
        assert(viewModel.detailsState.value is DetailsState.ShowWithEpisodes)
    }

    @Test
    fun `User clicks on a show, cannot load it`() {
        repositoryMock = mockk<ShowRepository>().apply {
            coEvery { getShowWithEpisodes(1) } returns flowOf(DetailsState.Error(Throwable("Illegal State")))
        }
        viewModel = ShowViewModel(repositoryMock)
        viewModel.getShowWithEpisodes(1)
        coVerify(exactly = 1) { repositoryMock.getShowWithEpisodes(1) }
        assert(viewModel.detailsState.value is DetailsState.Error)
    }

    @Test
    fun `User sets a favorite show`() {
        val list = mutableListOf<ItemDelegate>(
            SeasonDelegate.Model("Season 1"),
            EpisodeItem(
                1,
                "episode",
                1,
                1,
                30,
                Rating(3.4),
                Image("mediumUrl", "originalUrl"),
                "some summary"
            )
        )
        repositoryMock = mockk<ShowRepository>().apply {
            coEvery { favoriteShow(1) } returns flowOf(DetailsState.UpdateShow(list))
        }
        viewModel = ShowViewModel(repositoryMock)
        viewModel.setFavoriteShow(1)
        coVerify(exactly = 1) { repositoryMock.favoriteShow(1) }
        assert(viewModel.detailsState.value is DetailsState.UpdateShow)
    }

    @Test
    fun `User navigates to favorite screen, show all favorites`() {
        viewModel.getAllFavorites()
        coVerify(exactly = 1) { repositoryMock.getAllFavoriteShows() }
        assert(viewModel.favoriteList.value?.isNotEmpty()!!)
    }

    @Test
    fun `User is searching for a show`() {
        val list = mutableListOf(
            ShowItem(
                1,
                "qweq",
                emptyList(),
                Image("mediumUrl", "originalUrl"),
                "big summary",
                "01/09/2022",
                Schedule("time", emptyList())
            )
        )
        repositoryMock = mockk<ShowRepository>().apply {
            coEvery { searchShowName("qweq") } returns flowOf(list)
        }
        viewModel = ShowViewModel(repositoryMock)
        viewModel.searchShowName("qweq")
        coVerify(exactly = 1) { repositoryMock.searchShowName("qweq") }
        assert(viewModel.searchList.value?.isNotEmpty()!!)
    }

}