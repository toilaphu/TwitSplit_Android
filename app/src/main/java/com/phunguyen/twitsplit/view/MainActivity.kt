package com.phunguyen.twitsplit.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.phunguyen.twitsplit.R
import com.phunguyen.twitsplit.presenter.MainViewPresenter
import com.phunguyen.twitsplit.presenter.MainViewPresenterImpl
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView {

    var mainViewPresenter: MainViewPresenter = MainViewPresenterImpl()
    private val limitCharacters = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewPresenter.attachView(this)
        sendButton.setOnClickListener {
            mainViewPresenter.verifyMessage(this, messageField.text.toString(), limitCharacters)
            messageField.text.clear()
        }
    }

    override fun showValidationMessage(stringBuilder: StringBuilder) {
        displayMessage.text = stringBuilder.toString()
    }

    override fun showErrorMessage() {
        messageField.error = getString(R.string.empty_message_notice)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewPresenter.detachView()
    }

}
