package com.consumption.imitator.ui

import javafx.geometry.Pos
import javafx.scene.control.TextField
import tornadofx.*

class TimeConsumptionView : View() {

    private lateinit var fromTimeField: TextField

    private lateinit var toTimeField: TextField

    private lateinit var consumptionField: TextField

    override val root = hbox(alignment = Pos.CENTER_LEFT) {
        label("from:") {
            paddingRight = 10
        }
        fromTimeField = textfield() {
            maxWidth = TIME_TEXT_FIELD_WIDTH
        }
        label("to:") {
            paddingLeft = 10
            paddingRight = 10
        }
        toTimeField = textfield() {
            maxWidth = TIME_TEXT_FIELD_WIDTH
        }
        label("consumption:") {
            paddingLeft = 10
            paddingRight = 10
        }
        consumptionField = textfield() {
            maxWidth = 50.0
        }
    }

    fun getFromTime(): Double {
        return fromTimeField.text.toDouble()
    }

    fun getToTime(): Double {
        return toTimeField.text.toDouble()
    }

    fun getConsumption(): Double {
        return consumptionField.text.toDouble()
    }

    companion object {
        private const val TIME_TEXT_FIELD_WIDTH = 40.0
    }
}
