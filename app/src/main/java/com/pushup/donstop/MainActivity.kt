package com.pushup.donstop

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.pushup.donstop.ui.LoadingFragment
import com.pushup.donstop.ui.MainFragment
import com.pushup.donstop.ui.WorkoutFragment
import com.pushup.donstop.ui.theme.PushUpDontStopTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            openLoadingFragment()
        }

        hideSystemUI()
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
    }

    private fun openLoadingFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, LoadingFragment())
            .commit()
    }

    fun openMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, MainFragment()) // или твой главный фрагмент
            .commit()
    }

    fun openWorkoutFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, WorkoutFragment()) // замените на свой фрагмент, если имя другое
            .addToBackStack(null)
            .commit()
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