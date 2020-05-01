package com.consumption.imitator

import com.consumption.imitator.ui.Form
import javafx.application.Application
import tornadofx.App

class CustomerApp : App(Form::class)

fun main(args: Array<String>) {
    Application.launch(CustomerApp::class.java, *args)
}