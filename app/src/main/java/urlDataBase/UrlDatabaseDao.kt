package urlDataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UrlDatabaseDao {
    @Insert
    suspend fun insert(urlList: List<Url>)
    @Query("SELECT * FROM server_url_table")
    suspend fun getAllUrl(): MutableList<Url>
}