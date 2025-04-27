package pushup.donstop.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import com.pushup.donstop.R
import com.pushup.donstop.ui.LoadingCircleView
import com.pushup.donstop.ui.MainFragment

class LoadingFragment : androidx.fragment.app.Fragment() {

    private var loadingCircleView: LoadingCircleView? = null
    private var percentageText: TextView? = null
    private var progressAnimator: ValueAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loading_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Установка полноэкранного режима
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        loadingCircleView = view.findViewById(R.id.loadingCircle)
        percentageText = view.findViewById(R.id.percentageText)

        startLoadingAnimation()
    }

    private fun startLoadingAnimation() {
        progressAnimator = ValueAnimator.ofInt(0, 100).apply {
            duration = 3000 // 3 секунды на загрузку
            addUpdateListener { animator ->
                val progress = animator.animatedValue as Int
                percentageText?.text = "$progress%"

                if (progress == 100) {
                    // Небольшая задержка перед переходом
                    view?.postDelayed({
                        navigateToMainScreen()
                    }, 500)
                }
            }
            start()
        }
    }

    private fun navigateToMainScreen() {
        parentFragmentManager.beginTransaction()
            .replace((view?.parent as ViewGroup).id, MainFragment())
            .commit()
    }

    override fun onDestroyView() {
        progressAnimator?.cancel()
        progressAnimator = null
        super.onDestroyView()
    }
}
