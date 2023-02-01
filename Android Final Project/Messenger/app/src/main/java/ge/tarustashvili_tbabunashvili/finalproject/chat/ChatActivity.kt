package ge.tarustashvili_tbabunashvili.finalproject.chat

//import android.R
import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.FOCUS_DOWN
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import ge.tarustashvili_tbabunashvili.finalproject.R
import ge.tarustashvili_tbabunashvili.finalproject.user.UserActivity
import ge.tarustashvili_tbabunashvili.finalproject.data.model.Message
import java.util.*


class ChatActivity : AppCompatActivity() {
    private val NO_DATA = "Not found"
    private lateinit var messageField : EditText
    private lateinit var toNicknamestr: String
    private lateinit var toJobstr: String
    private lateinit var toPfpstr: String
    private lateinit var toPfp: ImageView
    private lateinit var toNickname: TextView
    private lateinit var toJob: TextView
    private lateinit var toPfpcoll: ImageView
    private lateinit var toNameNPoscoll: TextView
    private lateinit var collapsingbar: CollapsingToolbarLayout
    private lateinit var appbar: AppBarLayout
    private lateinit var backToolbarButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var currentUsername: String
    private lateinit var currentNickname: String
    private lateinit var currentAvatar: String
    private lateinit var currentJob: String
    private lateinit var toUserName: String
    private lateinit var chat: RecyclerView
    private lateinit var scroll: ScrollView
    val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this, ChatViewModelsFactory(application)).get(ChatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        toPfp = findViewById<ImageView>(R.id.chat_profile_pic)
        toNickname = findViewById<TextView>(R.id.toolbarname)
        toJob = findViewById<TextView>(R.id.toolbarposition)
        toPfpcoll = findViewById(R.id.chat_toolbar_profile_pic)
        toNameNPoscoll = findViewById(R.id.namepos)
        collapsingbar = findViewById(R.id.collapsing_toolbar_layout)
        appbar = findViewById(R.id.app_bar_layout)
        backToolbarButton = findViewById(R.id.back)
        backButton = findViewById(R.id.back_back)
        chat = findViewById(R.id.chat)
        scroll = findViewById(R.id.chat_scroll)
        toNicknamestr = intent.getStringExtra(tonick)?: NO_DATA
        toJobstr = intent.getStringExtra(tojob)?: NO_DATA
        currentUsername = intent.getStringExtra(frommail)?: NO_DATA
        currentJob = intent.getStringExtra(fromjob)?: NO_DATA
        toUserName = intent.getStringExtra(tomail)?: NO_DATA
        currentNickname = intent.getStringExtra(fromnick)?: NO_DATA
        currentAvatar = intent.getStringExtra(frompfp)?:""
        messageField = findViewById<EditText>(R.id.message_edit_text)
        chatViewModel.registerListener(currentUsername,toUserName)

        chatViewModel.getConvos().observe(this, Observer {
            if (it != null) {
                updateData(it)
            }
        })
        val span1 = SpannableString(toNicknamestr)
        span1.setSpan(AbsoluteSizeSpan(54), 0, toNicknamestr.length, SPAN_INCLUSIVE_INCLUSIVE)

        val span2 = SpannableString(toJobstr)
        span2.setSpan(AbsoluteSizeSpan(32
        ), 0, toJobstr.length, SPAN_INCLUSIVE_INCLUSIVE)

        span2.setSpan(ForegroundColorSpan(Color.LTGRAY), 0, span2.length, SPAN_INCLUSIVE_INCLUSIVE)

        val finalText = TextUtils.concat(span1, "\n", span2)

        toNameNPoscoll.text = finalText

        toPfpstr = intent.getStringExtra(topfp)?:""
        toNickname.text = toNicknamestr
        toJob.text = toJobstr
        if (toPfpstr != "") {
            Glide.with(this)
                .load(toPfpstr)
                .circleCrop()
                .into(toPfp)
            Glide.with(this)
                .load(toPfpstr)
                .circleCrop()
                .into(toPfpcoll)
        }
        chat.adapter = MessagesAdapter(currentUsername, emptyList())
        var mListener =
            OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (collapsingbar.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(
                        collapsingbar
                    )
                ) {
                    toPfpcoll.isVisible = true
                    backToolbarButton.isVisible = true
                    toNameNPoscoll.isVisible = true
                } else {
                    toPfpcoll.isVisible = false
                    backToolbarButton.isVisible = false
                    toNameNPoscoll.isVisible = false
                }
            }
        appbar.addOnOffsetChangedListener(mListener)
        scroll.fullScroll(FOCUS_DOWN)
        scroll.smoothScrollTo(0, scroll.height)
    }

    companion object {
        const val tonick = "to user nickname"
        const val tojob = "to user job"
        const val topfp = "to user profile picture"
        const val tomail = "to user username"
        const val fromnick = "from user nickname"
        const val frommail = "from user username"
        const val frompfp = "from user profile picture"
        const val fromjob = "from user job"
    }

    fun updateData(messages: MutableList<Message>)   {
        (chat.adapter as MessagesAdapter).items = messages
        (chat.adapter as MessagesAdapter).notifyDataSetChanged()
        scroll.smoothScrollTo(0, scroll.height)
    }

    fun onSend(view: View) {
        val message = this.messageField.text.toString()
        val to = this.toUserName
        val date = Calendar.getInstance().time
        if (message != "") {
            chatViewModel.sendMessage(currentUsername, to, message, date)
            chatViewModel.updateConversation(currentUsername,to, date, message,  currentAvatar,toPfpstr, currentNickname, toNicknamestr, currentJob, toJobstr)
        }
        messageField.setText("")
        scroll.smoothScrollTo(0, scroll.height + 30)

    }

    fun onBack(view: View) {
        val i = Intent(this, UserActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(i)
    }

}

class ChatViewModelsFactory(var application: Application) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(ChatRepository(this.application.applicationContext)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}