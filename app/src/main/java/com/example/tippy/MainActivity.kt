package com.example.tippy

// This is the main activity, which is the main screen.

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.w3c.dom.Text
import java.time.temporal.TemporalAmount

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
class MainActivity : AppCompatActivity() {

    // late coz your are not doing it on create, and conventionall
    // name should be same as the actual edit text thing.
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip : SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvTipDescription : TextView


    // This function is called when you create the app.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // R stands for Resources, the content for our screen must be taken from resources/layout/
        // activity_main.xml file.
        setContentView(R.layout.activity_main)

        // Referencing the variables.
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        // getting notified when you interact with the seekbar
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                tvTipPercentLabel.text = "$p1%"
                computeTipAndTotal()
                updateTipDescription(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        etBaseAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "After text changed: $p0")
                computeTipAndTotal()
            }
        })
    }

    private fun updateTipDescription(tipPercent: Int) {
        var tipDescription = when(tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Good"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription

        // update the color
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this, R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {

        if(etBaseAmount.text.isEmpty())
        {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            return
        }

        // 1. Get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        // 2. Compute the tip and total.
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        // 4. Update the UI.
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }
}