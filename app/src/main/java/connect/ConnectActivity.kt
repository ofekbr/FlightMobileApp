package connect

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityConnectBinding
import com.google.gson.GsonBuilder
import control.ControlActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import urlDataBase.Url
import urlDataBase.UrlDataBase
import java.lang.Exception


class ConnectActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityConnectBinding
    private lateinit var urlList: MutableList<Url>
    private lateinit var db: UrlDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_connect)
        db = UrlDataBase.getInstance(applicationContext)
        val urlDao = db.urlDao
        runBlocking {
            val job : Job = GlobalScope.launch {
                urlList = urlDao.getAllUrl()
                urlList.sortByDescending { it.id }
            }
            job.join()
        }
        setButtons()
        setConnectButton()
    }

    private fun setButtons(){
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val buttonList = arrayListOf<Button>(button1, button2, button3, button4, button5)
        var i = 0
        for (url in urlList) {
            if (url.url != "") {
                buttonList[i].text = url.url
                i++
            }
        }
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button3.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button1.isVisible = button1.text.isNotEmpty()
        button2.isVisible = button2.text.isNotEmpty()
        button3.isVisible = button3.text.isNotEmpty()
        button4.isVisible = button4.text.isNotEmpty()
        button5.isVisible = button5.text.isNotEmpty()

    }

    private fun setConnectButton() {
        val editText = findViewById<EditText>(R.id.URLText)
        val connectButton = findViewById<Button>(R.id.connectButton)
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                connectButton.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private suspend fun updateDB(url : String) {
        var foundFlag = false
        for (dbUrl in urlList) {
            if (dbUrl.url == url) {
                dbUrl.id = 0
                foundFlag = true
            }
        }
        if (!foundFlag) {
            if (urlList.size < 5) {
                urlList.add(Url(0, url))
            } else {
                urlList.last().id = 0
                urlList.last().url = url
            }
        }
        db.clearAllTables()
        db.urlDao.insert(urlList)
    }


    fun connect(view: View) {
        val url = binding.URLText.text.toString()
        //Updating DB
        GlobalScope.launch { updateDB(url) }

        if (!url.startsWith("http") && !url.startsWith("HTTP")) {
            val message = Toast.makeText(applicationContext, "Bad URL", Toast.LENGTH_SHORT)
            message.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,0,400)
            message.show()
            return
        }

        val intent = Intent(this, ControlActivity::class.java)
        intent.putExtra("EXTRA_TEXT", url)

        //Sending http request to test the server url
        try {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create(gson)).build()
            val api = retrofit.create(Api::class.java)
            api.sendCom(Command(0.0,0.0,0.0,0.0)).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    val message = Toast.makeText(applicationContext, "Failed to connect, check the server and try again", Toast.LENGTH_SHORT)
                    message.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,0,400)
                    message.show()
                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 503) {
                        val message = Toast.makeText(applicationContext, "Failed to connect, check the server and try again", Toast.LENGTH_SHORT)
                        message.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,0,400)
                        message.show()
                        return
                    }
                    startActivity(intent)
                    finish()
                }
            })
        } catch (e : Exception) {
            val message = Toast.makeText(applicationContext, "Error during connection, check the url and try again", Toast.LENGTH_SHORT)
            message.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,0,400)
            message.show()
        }
    }

    override fun onClick(b: View) {
        val button = b as Button
        val field = findViewById<EditText>(R.id.URLText) as EditText
        if (button.text.toString().isNotEmpty()) {
            field.setText(button.text)
        }
    }
}