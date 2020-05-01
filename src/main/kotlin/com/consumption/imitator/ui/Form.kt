package com.consumption.imitator.ui

import javafx.fxml.FXML
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import tornadofx.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sin

class Form : View() {

    @FXML
    private lateinit var timeConsumptionsContainer: VBox

    @FXML
    private lateinit var amplitudeField: TextField

    @FXML
    private lateinit var frequencyField: TextField

    @FXML
    private lateinit var deviationField: TextField

    @FXML
    private lateinit var lineChart: LineChart<Double, Double>

    private val createdTimeConsumptionViews = mutableListOf<TimeConsumptionView>()

    override val root: AnchorPane by fxml(
            File(FORM_LAYOUT_PATH).inputStream()
    )

    fun onAddTimeConsumption() {
        timeConsumptionsContainer.add(TimeConsumptionView().apply {
            root.paddingBottom = 5
            createdTimeConsumptionViews.add(this)
        })
    }

    fun onRemove() {
        if (createdTimeConsumptionViews.isNotEmpty()) {
            removeLast(createdTimeConsumptionViews)
            removeLast(timeConsumptionsContainer.children)
        }
    }

    private fun removeLast(list: MutableList<*>) = list.removeAt(list.size - 1)

    fun onClearChart() {
        lineChart.data.clear()
    }

    fun onCalculate() {
        val amplitude = amplitudeField.text.toDouble()
        val frequency = Math.PI / frequencyField.text.toDouble()
        val deviation = deviationField.text.toDouble()
        val phaseShift = Math.PI
        var shiftedAmplitude = amplitude

        val x = mutableListOf<Double>()
        val y = mutableListOf<Double>()

        createdTimeConsumptionViews.forEach { timeConsumptionView ->
            val linearVector = generateLinearlySpaceVector(
                    timeConsumptionView.getFromTime(),
                    timeConsumptionView.getToTime(),
                    timeConsumptionView.getConsumption()
            )
            x.addAll(linearVector)

            y.addAll(approximateConsumptions(
                    linearVector, ++shiftedAmplitude, amplitude, frequency, phaseShift, deviation
            ))
        }

        lineChart.series("Consumptions ${lineChart.data.size}", createChartDataList(x, y).toObservable())
    }

    private fun createChartDataList(x: List<Double>, y: List<Double>): List<XYChart.Data<Double, Double>> {
        return x.mapIndexed { index, xValue ->
            XYChart.Data<Double, Double>(xValue, y[index])
        }
    }

    private fun approximateConsumptions(
            consumptionsVector: List<Double>,
            shiftedAmplitude: Double,
            amplitude: Double,
            frequency: Double,
            phaseShift: Double,
            deviation: Double
    ): List<Double> {
        return (sinVector(consumptionsVector * frequency + phaseShift) *
                amplitude + shiftedAmplitude) sum createRandomVector(deviation, consumptionsVector.size)
    }

    private operator fun List<Double>.plus(number: Double): List<Double> {
        return map { it + number }
    }

    private infix fun List<Double>.sum(vector: List<Double>): List<Double> {
        return mapIndexed { index, value ->
            value + vector[index]
        }
    }

    private operator fun List<Double>.times(number: Double): List<Double> {
        return map { it * number }
    }

    private fun sinVector(vector: List<Double>): List<Double> {
        return vector.map { sin(it) }
    }

    private fun createRandomVector(deviation: Double, size: Int): List<Double> {
        val random = Random()
        return ArrayList<Double>(size).apply {
            repeat(size) {
                add(random.nextGaussian() * deviation)
            }
        }
    }

    private fun generateLinearlySpaceVector(
            start: Double,
            stop: Double,
            num: Double
    ) = (start..stop step (stop - start) / (num - 1)).toList()

    private infix fun ClosedRange<Double>.step(step: Double): Iterable<Double> {
        require(start.isFinite())
        require(endInclusive.isFinite())
        require(step > 0.0) { "Step must be positive, was: $step." }
        val sequence = generateSequence(start) { previous ->
            if (previous == Double.POSITIVE_INFINITY) return@generateSequence null
            val next = previous + step
            if (next > endInclusive) null else next
        }
        return sequence.asIterable()
    }

    companion object {
        private const val FORM_LAYOUT_PATH = "C:\\Users\\tberc\\IdeaProjects\\Consumption imitator\\src\\main\\kotlin\\com\\consumption\\imitator\\ui\\Form.fxml"
    }
}
