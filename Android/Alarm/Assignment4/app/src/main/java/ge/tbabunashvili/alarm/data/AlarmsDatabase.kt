package ge.tbabunashvili.alarm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ge.tbabunashvili.alarm.data.dao.AlarmsDAO
import ge.tbabunashvili.alarm.data.entity.Alarm

@Database(entities = arrayOf(Alarm::class), version = 1)
abstract class AlarmsDatabase(): RoomDatabase() {
    abstract fun alarmsDao(): AlarmsDAO
}