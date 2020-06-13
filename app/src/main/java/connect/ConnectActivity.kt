package connect

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import control.ControlActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityConnectBinding


class ConnectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConnectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_connect)
    }

    fun connect(view: View) {
        val URL = binding.URLText.text.toString()
        val intent = Intent(this, ControlActivity::class.java)
        intent.putExtra("EXTRA_TEXT", URL)
        startActivity(intent)
    }
}