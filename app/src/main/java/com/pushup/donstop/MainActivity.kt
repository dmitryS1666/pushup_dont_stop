package com.pushup.donstop

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.pushup.donstop.ui.LoadingFragment
import com.pushup.donstop.ui.MainFragment
import com.pushup.donstop.ui.ParamFragment
import com.pushup.donstop.ui.PlanFragment
import com.pushup.donstop.ui.SettingsFragment
import com.pushup.donstop.ui.StatsFragment
import com.pushup.donstop.ui.WorkoutFragment
import com.pushup.donstop.ui.theme.PushUpDontStopTheme
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: View
    private lateinit var navParams: LinearLayout
    private lateinit var navPlan: LinearLayout
    private lateinit var navRun: LinearLayout
    private lateinit var navStat: LinearLayout
    private lateinit var navSet: LinearLayout

    private lateinit var planIcon: ImageView
    private lateinit var runIcon: ImageView
    private lateinit var statIcon: ImageView
    private lateinit var setIcon: ImageView

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        // Принудительно установить английскую локаль
        val locale = Locale("en")
        Locale.setDefault(locale)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            openLoadingFragment()
        }

        hideSystemUI()

        // Инициализация элементов навигации
        navParams = findViewById(R.id.navParams)
        navPlan = findViewById(R.id.navPlan)
        navRun = findViewById(R.id.navRun)
        navStat = findViewById(R.id.navStat)
        navSet = findViewById(R.id.navSet)

        // Инициализация иконок
        planIcon = navPlan.findViewById(R.id.iconPlan)
        runIcon = navRun.findViewById(R.id.iconRun)
        statIcon = navStat.findViewById(R.id.iconStat)
        setIcon = navSet.findViewById(R.id.iconSet)

        // Обработчики кликов для каждого элемента нижней панели
        navParams.setOnClickListener {
            hideBottomNav()
            openFragment(ParamFragment())
        }

        navPlan.setOnClickListener {
            showBottomNav()
            openFragment(PlanFragment())
            updateNavIcons("plan")
        }

        navRun.setOnClickListener {
            showBottomNav()
            openFragment(WorkoutFragment())
            updateNavIcons("run")
        }

        navStat.setOnClickListener {
            showBottomNav()
            openFragment(StatsFragment())
            updateNavIcons("stat")
        }

        navSet.setOnClickListener {
            showBottomNav()
            openFragment(SettingsFragment())
            updateNavIcons("set")
        }
    }

    private fun updateNavIcons(activeFragment: String) {
        // Сбросить все иконки
        resetNavIcons()

        when (activeFragment) {
            "plan" -> {
                planIcon.setImageResource(R.drawable.plan_active)
            }
            "run" -> {
                runIcon.setImageResource(R.drawable.start_active)
            }
            "stat" -> {
                statIcon.setImageResource(R.drawable.statistic_active)
            }
            "set" -> {
                setIcon.setImageResource(R.drawable.settings_acitve)
            }
        }
    }

    private fun resetNavIcons() {
        // Сбросить все иконки
        planIcon.setImageResource(R.drawable.plan)
        runIcon.setImageResource(R.drawable.start)
        statIcon.setImageResource(R.drawable.statistic)
        setIcon.setImageResource(R.drawable.settings)
    }

    // Метод для замены фрагмента
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .addToBackStack(null) // Добавить фрагмент в back stack
            .commit()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )

        hideBottomNav()
    }

    // Метод для отображения панели навигации
    fun showBottomNav() {
        bottomNav = findViewById(R.id.bottomNavInclude)
        bottomNav.visibility = View.VISIBLE
    }

    // Метод для скрытия панели навигации
    fun hideBottomNav() {
        bottomNav = findViewById(R.id.bottomNavInclude)
        bottomNav.visibility = View.GONE
    }

    private fun openLoadingFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, LoadingFragment())
            .commit()
    }

    fun openMainFragment() {
        hideBottomNav()

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, MainFragment())
            .commit()
    }

    fun openWorkoutFragment() {
        showBottomNav()

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, WorkoutFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Подождём, пока фрагмент реально заменится
        supportFragmentManager.executePendingTransactions()

        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer)

        if (currentFragment is MainFragment || currentFragment is ParamFragment) {
            hideBottomNav()
        } else {
            showBottomNav()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PushUpDontStopTheme {
        Greeting("Android")
    }
}