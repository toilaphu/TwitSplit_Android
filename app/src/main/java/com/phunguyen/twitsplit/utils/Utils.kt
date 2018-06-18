package com.phunguyen.twitsplit.utils

import android.content.Context
import com.phunguyen.twitsplit.R

class Utils {
    fun splitMessage(context: Context, _input: String, limitCharacters: Int): List<String> {

        //Get non-whitespace error message
        val errorLimitCharacter = String.format(context.getString(R.string.error_longer_than_limit_character), limitCharacters)

        //Remove whitespace on both of leading and trailing, double whitespace and format message with newlines text.
        val message = _input.trim().replace("\\s+".toRegex(), " ").replace("\\n", "", true)

        //Return message when the length is less than limit characters
        if (message.length < limitCharacters) return listOf(message)

        //Estimate total partial before split
        val estTotal = (message.length / limitCharacters) + (if (message.length % limitCharacters > 0) 1 else 0)

        //Add sentences into words by remove whitespaces
        val words = message.split("\\s".toRegex())

        //Get the word in case it's length greater than limit characters and contains non-whitespace
        val errorWords = words.filter { it.length > limitCharacters }

        //Return tweets message if words are less than limit characters
        return if (errorWords.isNotEmpty()) listOf(errorLimitCharacter) else joinedPartial(words, estTotal, limitCharacters)
    }

    private fun joinedPartial(messages: List<String>, estTotal: Int, limitCharacters: Int): List<String> {

        //Initial empty String array to store
        var results: MutableList<String> = mutableListOf()
        var interactAtIndex = -1

        for (i in 0 until estTotal) {
            // Add prefix to each partial
            var partial = "${i + 1}/$estTotal "
            var indicatorLength = partial.length

            //Work on individual word
            for ((index, item) in messages.withIndex()) {

                //Continue if word's not already added to partial
                if (index <= interactAtIndex) continue

                //Check partial length before added (+ 1 because of a suffix whitespace per word)
                indicatorLength += item.length + 1

                //Break loop when the length is over than limit (+ 1 because the last whitespace before trimming)
                if (indicatorLength > limitCharacters + 1) break

                //Add word into partial
                partial += "$item "

                //Update current index where the last word is appended
                interactAtIndex = index
            }
            results.add(partial.trim())
        }
        //Re-calculate total partials to make sure estimate total partial is correct.
        if (interactAtIndex < messages.count() - 1) {
            val total = estTotal + 1
            results.clear()
            results = joinedPartial(messages, total, limitCharacters).toMutableList()
        }
        return results
    }

}