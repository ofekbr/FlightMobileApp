package control

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.SeekBar
import android.widget.Toast
import com.example.myapplication.R
import com.google.gson.GsonBuilder
import connect.Api
import connect.Command
import kotlinx.android.synthetic.main.activity_control.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.abs
import java.util.*
import kotlin.concurrent.schedule

class ControlActivity : AppCompatActivity() {
    private lateinit var url: String
    private lateinit var timer: Timer
    private var aileron : Double = 0.0
    private var elevator : Double = 0.0
    private var rudder : Double = 0.0
    private var throttle : Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        url = intent.getStringExtra("EXTRA_TEXT")
        setThrottleSlider()
        setRudderSlider()
        setJoystick()

    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            //resume sending
            timer = Timer("getImg", false)
            timer.schedule(0, 500) {
                loop()
            }
        } else {
            //stop sending
            timer.cancel()
        }
    }

    private fun postJson() {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build()
        val api = retrofit.create(Api::class.java)

        api.sendCom(Command(aileron,rudder,elevator,throttle)).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT)
                message.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,0,400)
                message.show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            }
        })
    }

    private fun setJoystick() {
        joystickView.setOnMoveListener { angle, strength ->
            val prevAileron = aileron
            val prevElevator = elevator
            aileron = kotlin.math.cos(Math.toRadians(angle.toDouble())) * strength / 100
            elevator = kotlin.math.sin(Math.toRadians(angle.toDouble())) * strength / 100
            val absAileron = abs(prevAileron - aileron)
            val absElevator = abs(prevElevator - elevator)
            if (absAileron >= 0.02 || absElevator >= 0.02) {
                postJson()
            }
        }
    }

    private fun loop(){
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(Api::class.java)

        api.getImg().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>
            ) {
                val I = response.body()?.byteStream()
                val B = BitmapFactory.decodeStream(I)
                runOnUiThread {
                    screen_shot.setImageBitmap(B)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val message = Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT)
                message.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,0,400)
                message.show()
                //TODO flag for error messages
                //TODO go back to reconnect
            }
        })
    }


    private fun setThrottleSlider() {
         throttleSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
             override fun onProgressChanged(
                 seekBar: SeekBar,
                 progress: Int,
                 fromUser: Boolean
             ) {
                 throttleVal.text = (progress.toFloat() / 100 ).toString()
                 throttle = progress.toDouble() / 100
                 postJson()
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
                rudder = (progress.toDouble() - 50) / 50
                postJson()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}