package ge.tbabunashvili.alarm

import ge.tbabunashvili.alarm.data.AlarmsDatabase
import ge.tbabunashvili.alarm.data.entity.Alarm

class Repository(private val db: AlarmsDatabase) {
    fun getAll(): MutableList<Alarm> {
        return db.alarmsDao().getAll()
    }

    fun addItem(alarm: Alarm): Long {
        return db.alarmsDao().addAlarm(alarm)
    }

    fun remove(id: Long) {
        return db.alarmsDao().removeAlarm(id)
    }

    fun update(id: Long, active: Boolean) {
        db.alarmsDao().update(id, active)
    }
}

