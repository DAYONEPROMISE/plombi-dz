package dz.plombidz.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String = "",
    val address: String = "",
    val notes: String = "",
)

@Entity(
    tableName = "interventions",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.SET_NULL,
        )
    ],
    indices = [Index("clientId")]
)
data class Intervention(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientId: Long? = null,
    val clientName: String = "",
    val dateEpoch: Long = System.currentTimeMillis(),
    val description: String = "",
    val materials: String = "",
    val priceDa: Long = 0,
    val done: Boolean = false,
)

@Dao
interface PlombiDao {
    // Clients
    @Query("SELECT * FROM clients ORDER BY name COLLATE NOCASE")
    fun clients(): Flow<List<Client>>

    @Query("SELECT * FROM clients ORDER BY name COLLATE NOCASE")
    suspend fun clientsOnce(): List<Client>

    @Insert
    suspend fun insertClient(c: Client): Long

    @Update
    suspend fun updateClient(c: Client)

    @Delete
    suspend fun deleteClient(c: Client)

    // Interventions
    @Query("SELECT * FROM interventions ORDER BY dateEpoch DESC")
    fun interventions(): Flow<List<Intervention>>

    @Insert
    suspend fun insertJob(j: Intervention): Long

    @Update
    suspend fun updateJob(j: Intervention)

    @Delete
    suspend fun deleteJob(j: Intervention)
}
