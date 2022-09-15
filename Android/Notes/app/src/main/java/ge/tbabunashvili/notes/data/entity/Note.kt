package ge.tbabunashvili.notes.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo var text: String,
    @ColumnInfo var parentId: Long,
    @ColumnInfo var done: Boolean,
    @ColumnInfo var pinned: Boolean
) {
}
