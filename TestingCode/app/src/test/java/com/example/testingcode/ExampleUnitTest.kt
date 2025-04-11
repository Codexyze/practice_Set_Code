package com.example.testingcode

import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private var user: User? = null
    @Before // initilization
    fun setUp() {
          user = User("John", 30)

    }
    @After // teardown removes all the data
    fun tearDown() {
        user = null
    }
    @Test
    fun addition_isCorrect() {
        //lhs == rhs
        assertEquals(4, 2 + 2)
    }


    @Test
    fun testUser() {
        assertEquals("John", user?.name)

    }

    @Test
    fun testUserAge() {
        assertEquals(30, user?.age)
    }
    @Test
    fun testUserNull() {
        //check for null if null pass else fail
        assertNull(null)
    }

    @Test
    fun testUserNotNull() {
        //check for null if not null pass else fail
        assertNotNull(user)
    }

    @Test
    fun testSame(){
        //if same pass else fail
        assertSame(user,user)
    }

    @Test
    fun testNotSame(){
        //if not same pass else fail
        assertNotSame(user,25)
    }

    @Test
    fun ArrayEquals(){
        assertArrayEquals(arrayOf(1,2,2), arrayOf(1,2,2))
    }
}

data class User(val name: String, val age: Int)