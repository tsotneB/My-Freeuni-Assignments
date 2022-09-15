package ge.tbabunashvili.alarm

import androidx.lifecycle.ViewModel
import ge.tbabunashvili.alarm.data.entity.Alarm

class AlarmViewModel(val repository: Repository): ViewModel() {
    fun saveAlarm(alarm: Alarm): Long {
        return repository.addItem(alarm)
    }


    fun getAll(): MutableList<Alarm> {
        return repository.getAll()
    }

    fun remove(id: Long) {
        return repository.remove(id)
    }

    fun update(id: Long, active: Boolean) {
        repository.update(id, active)
    }
}