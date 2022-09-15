package ge.tbabunashvili.weatherapp

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(var firstFragment: Fragment, var secondFragment: Fragment, activity: FragmentActivity): FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            firstFragment
        }   else {
            secondFragment
        }
    }
}