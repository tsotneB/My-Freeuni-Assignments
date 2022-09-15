package ge.tbabunashvili.alarm.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Alarm (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo
    var hour: Int,
    @ColumnInfo
    var minute: Int,
    @ColumnInfo
    var active: Boolean,
    ) {
}