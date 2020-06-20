package connect

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import urlDataBase.UrlDatabaseDao

class ConnectViewModel(
    val dataBase: UrlDatabaseDao,
    application: Application) : AndroidViewModel(application) {

}