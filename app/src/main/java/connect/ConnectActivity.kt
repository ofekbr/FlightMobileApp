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
import kotlinx.android.synthetic.main.activity_connect.*
import kotlinx.android.synthetic.main.activity_connect.view.*
import kotlinx.android.synthetic.main.activity_connect.view.URLText
import kotlinx.android.synthetic.main.activity_control.view.*

class ConnectActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityConnectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_connect)
        setButtons()
        setConnectButton()
    }

    private fun setButtons(){
        val button1 = findViewById<Button>(R.id.localHost1)
        val button2 = findViewById<Button>(R.id.localHost2)
        val button3 = findViewById<Button>(R.id.localHost3)
        val button4 = findViewById<Button>(R.id.localHost4)
        val button5 = findViewById<Button>(R.id.localHost5)
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
        val intent = Intent(this, ControlActivity::class.java)
        intent.putExtra("EXTRA_TEXT", URL)
        // TODO - update the cache with the current url
        // TODO - GET picture from simulator. navigate only if the GET was successful.
        if (true) {
            startActivity(intent)
        } else {
            val message = Toast.makeText(this,"connection failed", Toast.LENGTH_SHORT)
            message.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM,0,400)
            message.show();
        }

    }

    override fun onClick(b: View) {
        val button = b as Button
        val field = findViewById<EditText>(R.id.URLText) as EditText;
        if (button.text.toString().isNotEmpty()) {
            field.setText(button.text)
        }
    }
}