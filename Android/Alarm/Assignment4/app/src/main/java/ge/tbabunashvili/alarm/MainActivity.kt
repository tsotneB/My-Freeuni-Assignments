package ge.tbabunashvili.alarm

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import ge.tbabunashvili.alarm.data.AlarmsDatabase
import ge.tbabunashvili.alarm.data.entity.Alarm
import java.util.*
import kotlin.math.min


class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener, AlarmListener {
    private val TIME_CHOOSE_ERROR = "You can only choose value greater than current time"
    private var alarms = mutableListOf<Alarm>()
    private var alarmAdapter = AlarmAdapter(alarms, this)//,viewModel)
    private lateinit var pimap: HashMap<Long, PendingIntent>
    private val PREFERENCES = "ALARM SHARED PREFERENCES"
    private val THEME = "CHOSEN THEME"
    val viewModel: AlarmViewModel by lazy {
        ViewModelProvider(this, MainViewModelsFactory(application)).get(AlarmViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_main)
        initTheme()
        pimap = HashMap()

        findViewById<RecyclerView>(R.id.rv).adapter = alarmAdapter
        Thread(Runnable {
            alarms = viewModel.getAll()
            alarmAdapter.items = alarms
            alarmAdapter.notifyDataSetChanged()
        }).start()
    }

    fun themeChanger(view: View) {
        val sharedPreferences =
            getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        if (findViewById<TextView>(R.id.themeChange).text == resources.getString(R.string.fordark)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            findViewById<TextView>(R.id.themeChange).setText(R.string.forlight)
            sharedPreferences.edit().putInt(THEME, 1).apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            findViewById<TextView>(R.id.themeChange).setText(R.string.fordark)
            sharedPreferences.edit().putInt(THEME, 0).apply()
        }
    }

    fun getMode(): Int {
        val sharedPreferences =
            getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val mode = sharedPreferences.getInt(THEME, -1)
        return mode
    }

    fun initTheme() {
        when (getMode()) {
            -1 -> {
                var nightModeFlags = resources
                    .getConfiguration().uiMode and Configuration.UI_MODE_NIGHT_MASK

                when (nightModeFlags) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        findViewById<TextView>(R.id.themeChange).setText(R.string.forlight)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        findViewById<TextView>(R.id.themeChange).setText(R.string.fordark)
                    }
                    Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    }
                }
            }
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                findViewById<TextView>(R.id.themeChange).setText(R.string.fordark)
            }
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                findViewById<TextView>(R.id.themeChange).setText(R.string.forlight)
            }
        }
    }

    fun newAlarm(view: View) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        val calendar = Calendar.getInstance()
        if (p1 < calendar.get(Calendar.HOUR_OF_DAY) ||
            (p1 == calendar.get(Calendar.HOUR_OF_DAY) && p2 <= calendar.get(Calendar.MINUTE))) {
            Toast.makeText(this, this.TIME_CHOOSE_ERROR, Toast.LENGTH_LONG).show()
            newAlarm(findViewById<ImageView>(R.id.addAlarm))
            return
        }
        addNewAlarm(p1,p2)
        alarmAdapter.notifyDataSetChanged()
    }

    fun addNewAlarm(hour: Int, minute: Int) {
        var item = Alarm(0, hour, minute, true)
        Thread(Runnable {
            var id = viewModel.saveAlarm(item)
            item = Alarm(id, hour, minute, true)
            alarms.add(item)
            alarmAdapter.items = alarms
            scheduleAlarm(hour, minute, id)
        }).start()
    }

    fun scheduleAlarm(hour:Int, minute: Int, id: Long) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        var pi = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE + id.toInt(),
            Intent(AlarmReceiver.ALARM_ACTION_NAME).apply {
                `package` = packageName
                putExtra(ID, id)
                putExtra(HOUR, hour)
                putExtra(MINUTE, minute)
            },
            PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi);
        pimap[id] = pi
    }

    fun deleteAlarm(id: Long) {
        var alarmManager = getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pimap[id])
    }

    override fun onCheckboxChange(item: Alarm, position: Int) {
        Thread(Runnable {
            val id = item.id
            val newValue = !item.active
            viewModel.update(id, newValue)
            item.active = newValue
            alarms[position] = item
            alarmAdapter.items = alarms
            if (newValue) {
                scheduleAlarm(item.hour, item.minute, item.id)
            }   else {
                var cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, item.hour)
                cal.set(Calendar.MINUTE, item.minute)
                cal.set(Calendar.SECOND, 0)
                if (cal.timeInMillis > System.currentTimeMillis()) {
                    deleteAlarm(id)
                }
            }
        }).start()
        alarmAdapter.notifyDataSetChanged()
    }

    override fun onRemoveClick(item: Alarm, position: Int) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.question);
        builder.setPositiveButton(R.string.y, DialogInterface.OnClickListener { dialogInterface, i ->
            alarms.removeAt(position)
            alarmAdapter.items = alarms
            Thread(Runnable {
                val id = item.id
                viewModel.remove(id)
                var cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, item.hour)
                cal.set(Calendar.MINUTE, item.minute)
                cal.set(Calendar.SECOND, 0)
                if (cal.timeInMillis > System.currentTimeMillis()) {
                    deleteAlarm(id)
                }
            // if (item.active && System.currentTimeMillis().hours) deleteAlarm(id)
            }).start()
            alarmAdapter.notifyDataSetChanged()
        })
        builder.setNegativeButton(R.string.n, DialogInterface.OnClickListener{ dialogInterface, i ->
        })
        builder.create().show()
    }

    companion object {
        const val JOB_ID = 100
        const val ALARM_REQUEST_CODE = 200

        const val PENDINGS = "PENDING HASH MAP"
        const val ID = "ALARM ID"
        const val HOUR = "HOUR OF ALARM"
        const val MINUTE = "MINUTE OF ALARM"
    }


}



class MainViewModelsFactory(var application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            return AlarmViewModel(
                Repository(
                    Room.databaseBuilder(application, AlarmsDatabase::class.java, "Alarms").build()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}



