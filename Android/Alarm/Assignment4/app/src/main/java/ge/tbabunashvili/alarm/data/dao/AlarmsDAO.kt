package ge.tbabunashvili.alarm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ge.tbabunashvili.alarm.data.entity.Alarm

@Dao
interface AlarmsDAO {
    @Query("select * from Alarm")
    fun getAll(): MutableList<Alarm>

    @Insert
    fun addAlarm(note: Alarm): Long

    @Query("delete from Alarm where id = :id")
    fun removeAlarm(id: Long)

    @Query("Update Alarm set active = :active where id = :id")
    fun update(id: Long, active: Boolean)
}