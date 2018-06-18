package com.phunguyen.twitsplit.presenter

import android.content.Context
import com.phunguyen.twitsplit.utils.Utils
import com.phunguyen.twitsplit.view.MainView

class MainViewPresenterImpl : MainViewPresenter {

    private var mainView: MainView? = null

    override fun attachView(mainView: MainView) {
        this.mainView = mainView
    }

    override fun detachView() {
        mainView = null
    }

    override fun displayMessage(messages: List<String>) {
        val builder = StringBuilder()
        for (message in messages) {
            builder.append("$message \n")
        }
        mainView?.showValidationMessage(builder)
    }

    override fun verifyMessage(context: Context, messages: String, limitCharacters: Int) {
        if (messages.isEmpty()) {
            mainView?.showErrorMessage()
            return
        }
        val results = Utils().splitMessage(context, messages, limitCharacters)
        displayMessage(results)
    }

}