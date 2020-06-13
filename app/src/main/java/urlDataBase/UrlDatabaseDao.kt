package urlDataBase

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface UrlDatabaseDao {
    @Insert
    fun insert(url: Url)

}