package com.phunguyen.twitsplit

import android.content.Context
import com.phunguyen.twitsplit.utils.Utils
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.MockitoAnnotations

class UtilsMessageSplitTests {

    private val limitCharacter = 50
    private val errorMessage = "The message contains a span of non-whitespace characters longer than 50 characters"

    @Mock
    private lateinit var mMockContext: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        doReturn("The message contains a span of non-whitespace characters longer than 50 characters").`when`(mMockContext).getString(R.string.error_longer_than_limit_character)
    }

    @Test
    fun input_Tweet_With_Message_Length_Less_Than_Limit_Character() {
        val inputMessage = "Hi Tweeter Split"
        val expected = arrayListOf("Hi Tweeter Split")
        val results = Utils().splitMessage(this.mMockContext, inputMessage, limitCharacter)
        assertEquals(expected, results)
    }


    @Test
    fun input_Tweet_With_Message_Length_Over_Limit_Character() {
        val inputMessage = "Hi Tweeter Split with long messageeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee"
        val expected = errorMessage
        val results = Utils().splitMessage(this.mMockContext, inputMessage, limitCharacter)
        assertEquals(expected, results[0])
    }

    @Test
    fun input_Tweet_With_Message_Need_To_Partial() {
        val inputMessage = "I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself."
        val expected = arrayListOf("1/2 I can't believe Tweeter now supports chunking", "2/2 my messages, so I don't have to do it myself.")
        val results = Utils().splitMessage(this.mMockContext, inputMessage, limitCharacter)
        assertEquals(expected, results)
    }

    @Test
    fun input_Tweet_With_Long_Message_Need_To_Partial() {
        val inputMessage = "Twitter figured out it was a message broadcast system. Better served only by distributed systems such as Erlang/OTP (which was harder to learn back then, but the architecture was proven by stuff such as ejabberd). They chose Scala, whose actor framework is a semi OTP clone attempt (I say \"attempt\" because JVM limitations disallow them to match Erlang, anyway I digress)."
        val expected = arrayListOf("1/9 Twitter figured out it was a message broadcast",
                "2/9 system. Better served only by distributed",
                "3/9 systems such as Erlang/OTP (which was harder",
                "4/9 to learn back then, but the architecture was",
                "5/9 proven by stuff such as ejabberd). They chose",
                "6/9 Scala, whose actor framework is a semi OTP",
                "7/9 clone attempt (I say \"attempt\" because JVM",
                "8/9 limitations disallow them to match Erlang,",
                "9/9 anyway I digress).")
        val results = Utils().splitMessage(this.mMockContext, inputMessage, limitCharacter)
        assertEquals(expected, results)
    }

    @Test
    fun input_Tweet_With_Index_At_LimitCharacter() {
        val inputMessage = "I can't believe Tweeter now supports chunking my messages, so I don't have to do it myself.1.2.3.4.5"
        val expected = arrayListOf("1/3 I can't believe Tweeter now supports chunking",
                "2/3 my messages, so I don't have to do it",
                "3/3 myself.1.2.3.4.5")
        val results = Utils().splitMessage(this.mMockContext, inputMessage, limitCharacter)
        assertEquals(expected, results)
    }


    @Test
    fun input_Tweet_With_Non_WhiteSpace() {
        val inputMessage = "Ican'tbelieveTweeternowsupportschunkingmymessages,soIdon'thavetodoitmyself.1.2.3.4.5"
        val expected = errorMessage
        val results = Utils().splitMessage(this.mMockContext, inputMessage, limitCharacter)
        assertEquals(expected, results[0])
    }

    @Test
    fun input_Tweet_With_Extra_WhiteSpace() {
        val inputMessage = "I   can't   believe   Tweeter   now   supports chunking my   messages, so   I     don't     have to do  it   myself."
        val expected = arrayListOf("1/2 I can't believe Tweeter now supports chunking", "2/2 my messages, so I don't have to do it myself.")
        val results = Utils().splitMessage(this.mMockContext, inputMessage, limitCharacter)
        assertEquals(expected, results)
    }

    @Test
    fun input_Tweet_With_Extra_WhiteSpace_And_Newlines() {
        val inputMessage = "I   can't \n  believe   Tweeter \n \n  now   supports chunking my   messages, so   I     don't     have to do  it   myself."
        val expected = arrayListOf("1/2 I can't believe Tweeter now supports chunking", "2/2 my messages, so I don't have to do it myself.")
        val results = Utils().splitMessage(this.mMockContext, inputMessage, limitCharacter)
        assertEquals(expected, results)
    }

}
