package com.pushup.donstop

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.pushup.donstop.ui.SettingsFragment
import com.pushup.donstop.ui.WorkoutFragment
import com.pushup.donstop.ui.theme.PushUpDontStopTheme

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: View

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            openLoadingFragment()
        }

        hideSystemUI()

        // Инициализация элементов навигации
        val navParams: LinearLayout = findViewById(R.id.navParams)
        val navPlan: LinearLayout = findViewById(R.id.navPlan)
        val navRun: LinearLayout = findViewById(R.id.navRun)
        val navStat: LinearLayout = findViewById(R.id.navStat)
        val navSet: LinearLayout = findViewById(R.id.navSet)

        // Обработчики кликов для каждого элемента нижней панели
//        navParams.setOnClickListener {
//            System.out.println("navParams")
//            // Дополнительная логика для navParams
//            openFragment(ParamsFragment()) // Пример, откройте фрагмент для navParams
//        }

//        navPlan.setOnClickListener {
//            System.out.println("navPlan")
//            openFragment(PlanFragment()) // Пример, откройте фрагмент для navPlan
//        }

        navRun.setOnClickListener {
            System.out.println("navRun")
            openFragment(WorkoutFragment())
        }

//        navStat.setOnClickListener {
//            System.out.println("navStat")
//            openFragment(StatFragment()) // Пример, откройте фрагмент для navStat
//        }

        navSet.setOnClickListener {
            Log.d("MainActivity", "navSet clicked")
            openFragment(SettingsFragment())
        }
    }

    // Метод для замены фрагмента
    private fun openFragment(fragment: Fragment) {
        showBottomNav()

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
    private fun showBottomNav() {
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
            .replace(R.id.mainFragmentContainer, MainFragment()) // или твой главный фрагмент
            .commit()
    }

    fun openWorkoutFragment() {
        showBottomNav()

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, WorkoutFragment()) // замените на свой фрагмент, если имя другое
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        // Подождём, пока фрагмент реально заменится
        supportFragmentManager.executePendingTransactions()

        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainFragmentContainer)

        if (currentFragment is MainFragment) {
            hideBottomNav() // Скрываем навигацию на главном экране
        } else {
            showBottomNav() // Показываем навигацию, если на других экранах
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