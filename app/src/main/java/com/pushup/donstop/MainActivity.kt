package com.pushup.donstop

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pushup.donstop.ui.LoadingFragment
import com.pushup.donstop.ui.theme.PushUpDontStopTheme
import com.pushup.donstop.ui.MainFragment
import com.pushup.donstop.ui.WorkoutFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideSystemUI()

        if (savedInstanceState == null) {
            // Загружаем начальный фрагмент при старте
            openLoadingFragment()
        }
    }

    // Функция для скрытия системного UI (включая статус-бар и навигационные кнопки)
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN // Скрыть статус-бар
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Скрыть навигационные кнопки
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // Сохраняем этот режим даже при взаимодействии с экраном
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    private fun openLoadingFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, LoadingFragment())
            .commit()
    }

    // Метод для открытия MainFragment
    fun openMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, MainFragment())
            .commit()
    }

    // Метод для открытия WorkoutFragment
    fun openWorkoutFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, WorkoutFragment())
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