package urlDataBase

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "server_url_table")
data class Url(
    //@PrimaryKey
    @ColumnInfo(name = "server_url")
    var url: String = ""
)