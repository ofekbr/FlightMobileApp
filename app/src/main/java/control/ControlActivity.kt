package control

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Gravity
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.google.gson.GsonBuilder
import connect.Api
import kotlinx.android.synthetic.main.activity_control.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ControlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        val URL = intent.getStringExtra("EXTRA_TEXT")
        //val textView = findViewById<TextView>(R.id.textView)
        //textView.text = URL

        setThrottleSlider()
        setRudderSlider()
        setJoystick()
        controlLoop(URL)
    }

    private fun controlLoop(url : String) {
        while (true) {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(url)
                .build()
            val api = retrofit.create(Api::class.java)

            val body = api.getImg().enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val I = response.body()?.byteStream()
                    val B = BitmapFactory.decodeStream(I)
                    runOnUiThread {
                        screen_shot.setImageBitmap(B)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    val message = Toast.makeText(applicationContext, "connection failed", Toast.LENGTH_SHORT)
                    message.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,0,400)
                    message.show()
                }
            })
        }
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