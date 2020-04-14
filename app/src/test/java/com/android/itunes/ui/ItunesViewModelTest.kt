package com.android.itunes.ui

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.itunes.R
import com.android.itunes.api.ApiRepository
import com.android.itunes.api.NetworkResult
import com.android.itunes.model.Album
import com.android.itunes.model.Albums
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class ItunesViewModelTest {

    private val application : Application = mockk(relaxed = true)
    private val apiRepository: ApiRepository = mockk(relaxed = true)
    private val viewModel = ItunesViewModel(application, apiRepository)

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // The dispatcher and the before and after are needed if we want to test ViewModelScope.Launch{}
    private val testingDispatcher = Dispatchers.Unconfined

    @ExperimentalCoroutinesApi
    @Before
    fun doBeforeEach() {
        Dispatchers.setMain(testingDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun doAfterEach() {
        Dispatchers.resetMain()
    }

    @Test
    fun testOnSearchSelectedSuccess() {

        val response = Albums(25, listOf(
            Album(1234, 1234, 1234, "testArtist1", "testCollection1", null, null,null, null,null, null),
            Album(2345, 2345, 2345, "testArtist1", "testCollection1", null, null,null, null,null, null),
            Album(3456, 3456, 3456, "testArtist1", "testCollection1", null, null,null, null,null, null)))
        coEvery { apiRepository.fetchAlbums(any()) } returns NetworkResult.Success(response)
        runBlocking {
            viewModel.onSearchSelected("test")
        }
        Assert.assertEquals(viewModel.getAlbumList()?.size, 3)
        Assert.assertNotNull(viewModel.hideText.value)
        Assert.assertNotNull(viewModel.showLoading.value)
        Assert.assertNotNull(viewModel.showAlbumList.value)
        Assert.assertNotNull(viewModel.hideLoading.value)
    }

    @Test
    fun testOnSearchSelectedEmptyListSuccess() {

        val response = Albums(25, listOf())
        every { application.resources.getString(R.string.unknown_artist) } returns "testing"

        coEvery { apiRepository.fetchAlbums(any()) } returns NetworkResult.Success(response)
        runBlocking {
            viewModel.onSearchSelected("test")
        }
        Assert.assertEquals(viewModel.getAlbumList(), null)
        Assert.assertNotNull(viewModel.hideText.value)
        Assert.assertNotNull(viewModel.showLoading.value)
        Assert.assertNull(viewModel.showAlbumList.value)
        Assert.assertNotNull(viewModel.hideLoading.value)
        Assert.assertNotNull(viewModel.showText.value)
    }

    @Test
    fun testOnSearchSelectedFailure() {

        coEvery { apiRepository.fetchAlbums(any()) } returns NetworkResult.Failure(mockk(relaxed = true))
        runBlocking {
            viewModel.onSearchSelected("test")
        }
        Assert.assertEquals(viewModel.getAlbumList(), null)
        Assert.assertNotNull(viewModel.hideText.value)
        Assert.assertNotNull(viewModel.showLoading.value)
        Assert.assertNull(viewModel.showAlbumList.value)
        Assert.assertNotNull(viewModel.hideLoading.value)
        Assert.assertNotNull(viewModel.showErrorText.value)
        Assert.assertEquals(viewModel.showErrorText.value?.peekContent(), "test")
    }

    @Test
    fun testOnRetryButtonClickedSuccess() {

        val response = Albums(25, listOf(
            Album(1234, 1234, 1234, "testArtist1", "testCollection1", null, null,null, null,null, null),
            Album(2345, 2345, 2345, "testArtist1", "testCollection1", null, null,null, null,null, null),
            Album(3456, 3456, 3456, "testArtist1", "testCollection1", null, null,null, null,null, null)))
        coEvery { apiRepository.fetchAlbums(any()) } returns NetworkResult.Success(response)
        runBlocking {
            viewModel.onRetryButtonClicked("test")
        }
        Assert.assertEquals(viewModel.getAlbumList()?.size, 3)
        Assert.assertNotNull(viewModel.hideText.value)
        Assert.assertNotNull(viewModel.showLoading.value)
        Assert.assertNotNull(viewModel.showAlbumList.value)
        Assert.assertNotNull(viewModel.hideLoading.value)
    }

    @Test
    fun testOnRetryButtonClickedFailure() {

        coEvery { apiRepository.fetchAlbums(any()) } returns NetworkResult.Failure(mockk(relaxed = true))
        runBlocking {
            viewModel.onRetryButtonClicked("test")
        }
        Assert.assertEquals(viewModel.getAlbumList(), null)
        Assert.assertNotNull(viewModel.hideText.value)
        Assert.assertNotNull(viewModel.showLoading.value)
        Assert.assertNull(viewModel.showAlbumList.value)
        Assert.assertNotNull(viewModel.hideLoading.value)
        Assert.assertNotNull(viewModel.showErrorText.value)
        Assert.assertEquals(viewModel.showErrorText.value?.peekContent(), "test")
    }

    @Test
    fun testOnAlbumClicked() {
        val album = Album(1234, 1234, 1234, "testArtist1", "testCollection1", null, null,null, null,null, null)
        viewModel.onAlbumClicked(album)
        Assert.assertNotNull(viewModel.showAlbumDetails.value)
        val resultAlbum : Album? = viewModel.showAlbumDetails.value?.peekContent()
        resultAlbum?.let { result ->
            Assert.assertEquals(result.artistId, album.artistId)
            Assert.assertEquals(result.collectionId, album.collectionId)
            Assert.assertEquals(result.trackId, album.trackId)
        }

    }

    @Test
    fun testGetAlbumList() {
        val response = Albums(25, listOf(
            Album(1234, 1234, 1234, "testArtist1", "testCollection1", null, null,null, null,null, null),
            Album(2345, 2345, 2345, "testArtist1", "testCollection1", null, null,null, null,null, null),
            Album(3456, 3456, 3456, "testArtist1", "testCollection1", null, null,null, null,null, null)))
        coEvery { apiRepository.fetchAlbums(any()) } returns NetworkResult.Success(response)
        runBlocking {
            viewModel.onSearchSelected("test")
        }
        Assert.assertEquals(viewModel.getAlbumList()?.size, 3)
    }
}