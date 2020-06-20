package urlDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Url::class], version = 1, exportSchema = false)
abstract class UrlDataBase : RoomDatabase() {
    abstract val urlDao: UrlDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: UrlDataBase? = null

        fun getInstance(context: Context): UrlDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UrlDataBase::class.java,
                        "server_url_database")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
            }
                return instance
            }
        }
    }
}