package com.egorshustov.vpoiske.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.egorshustov.vpoiske.util.getValue
import com.egorshustov.vpoiske.util.testUser
import com.egorshustov.vpoiske.util.testUsers
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsersDaoTest {
    private lateinit var database: VPoiskeDatabase
    private lateinit var usersDao: UsersDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, VPoiskeDatabase::class.java).build()
        usersDao = database.usersDao()

        runBlocking {
            usersDao.insertUsers(testUsers)
        }
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testInsertUser() = runBlocking {
        usersDao.insertUser(testUser)
        assertThat(
            getValue(usersDao.getLiveUsers()).find { it.id == testUser.id }?.id,
            equalTo(testUser.id)
        )
    }

    @Test
    fun testGetLiveUsers() = runBlocking {
        assertThat(getValue(usersDao.getLiveUsers()).size, equalTo(2))
    }

    @Test
    fun deleteUsersFromSearch() = runBlocking {
        usersDao.deleteUsersFromSearch(1)
        assertThat(getValue(usersDao.getLiveUsers()).size, equalTo(1))
    }
}