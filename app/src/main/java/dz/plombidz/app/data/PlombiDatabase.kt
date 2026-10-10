package dz.plombidz.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Client::class, Intervention::class], version = 1, exportSchema = false)
abstract class PlombiDatabase : RoomDatabase() {
    abstract fun dao(): PlombiDao

    companion object {
        @Volatile
        private var INSTANCE: PlombiDatabase? = null

        fun get(context: Context): PlombiDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PlombiDatabase::class.java,
                    "plombidz.db",
                ).build().also { INSTANCE = it }
            }
    }
}
