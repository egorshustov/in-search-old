package com.egorshustov.vpoiske.main

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.egorshustov.vpoiske.analytics.FakeVPoiskeAnalyticsImpl
import com.egorshustov.vpoiske.data.source.FakeSearchesRepository
import com.egorshustov.vpoiske.data.source.FakeUsersRepository
import com.egorshustov.vpoiske.domain.searches.GetLastSearchIdUseCase
import com.egorshustov.vpoiske.domain.users.GetUsersUseCase
import com.egorshustov.vpoiske.util.V_POISKE_TEST_PREFERENCES_FILENAME
import com.egorshustov.vpoiske.util.getValue
import com.egorshustov.vpoiske.util.testSearch
import com.egorshustov.vpoiske.util.testUsers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var usersRepository: FakeUsersRepository

    private lateinit var searchesRepository: FakeSearchesRepository

    @Before
    fun setupStatisticsViewModel() {
        usersRepository = FakeUsersRepository()
        searchesRepository = FakeSearchesRepository()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        mainViewModel = MainViewModel(
            GetUsersUseCase(usersRepository),
            GetLastSearchIdUseCase(searchesRepository),
            context.getSharedPreferences(V_POISKE_TEST_PREFERENCES_FILENAME, Context.MODE_PRIVATE),
            FakeVPoiskeAnalyticsImpl()
        )
    }

    @Test
    fun testLastSearchIdIsNotNull() {
        runBlocking(Dispatchers.Main) {
            searchesRepository.saveSearch(testSearch)
            val lastSearchId = getValue(mainViewModel.lastSearchId)
            Assert.assertNotNull(lastSearchId)
        }
    }

    @Test
    fun testCurrentSearchUsersIsNotEmpty() {
        runBlocking(Dispatchers.Main) {
            usersRepository.saveUsers(testUsers)
            searchesRepository.saveSearch(testSearch)
            val currentSearchUsers = getValue(mainViewModel.currentSearchUsers)
            Assert.assertTrue(currentSearchUsers.isNotEmpty())
        }
    }
}