package connect

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import control.ControlActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityConnectBinding
import urlDataBase.Url
import urlDataBase.UrlDataBase

class ConnectActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityConnectBinding
    private lateinit var urlList: MutableList<Url>
    private lateinit var db: UrlDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_connect)
        db = UrlDataBase.getInstance(applicationContext)
        val urlDao = db.urlDao
        urlList = urlDao.getAllUrl()
        urlList.sortByDescending { it.id }
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
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                connectButton.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub - check if valid url
            }
        })
    }

    fun connect(view: View) {
        val URL = binding.URLText.text.toString()

        //Updating DB
        db.clearAllTables()
        if (urlList.size < 5) {
            urlList.add(Url(0, URL))
        } else {
            urlList.last().id = 0
            urlList.last().url = URL
        }
        db.urlDao.insert(urlList)

        
        val intent = Intent(this, ControlActivity::class.java)
        intent.putExtra("EXTRA_TEXT", URL)
        // TODO - update the cache with the current url
        // TODO - GET picture from simulator. navigate only if the GET was successful.
        if (true) {
            startActivity(intent)
        } else {
            val message = Toast.makeText(this,"connection failed", Toast.LENGTH_SHORT)
            message.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,0,400)
            message.show()
        }

    }

    override fun onClick(b: View) {
        val button = b as Button
        val field = findViewById<EditText>(R.id.URLText) as EditText //TODO use binding
        if (button.text.toString().isNotEmpty()) {
            field.setText(button.text)
        }
    }
}