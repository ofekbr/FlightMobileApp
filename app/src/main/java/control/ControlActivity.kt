package control

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_control.*

class ControlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        val URL = intent.getStringExtra("EXTRA_TEXT")
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = URL

        setThrottleSlider()
        setRudderSlider()
        setJoystick()
    }

    private fun setJoystick() {
        joystickView.setOnMoveListener { angle, strength ->
            val aileron = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevator = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100
            // TODO send to server - check if the change was 1%
        }
    }

    private fun setThrottleSlider() {
         throttleSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
             override fun onProgressChanged(
                 seekBar: SeekBar,
                 progress: Int,
                 fromUser: Boolean
             ) {
                 throttleVal.text = (progress.toFloat() / 100 ).toString()
                 // TODO send value to server
             }
             override fun onStartTrackingTouch(seekBar: SeekBar) {}
             override fun onStopTrackingTouch(seekBar: SeekBar) {}
         })
     }

    private fun setRudderSlider() {
        rudderSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                rudderVal.text = ((progress.toFloat() - 50) / 50).toString()
                // TODO send value to server
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}