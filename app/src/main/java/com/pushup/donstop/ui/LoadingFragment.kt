package com.pushup.donstop.ui

import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.pushup.donstop.MainActivity
import com.pushup.donstop.R

class LoadingFragment : Fragment() {

    private var loadingCircleView: LoadingCircleView? = null
    private var percentageText: TextView? = null
    private var progressAnimator: ValueAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.loading_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingCircleView = view.findViewById(R.id.loadingCircle)
        percentageText = view.findViewById(R.id.percentageText)

        startLoadingAnimation()
    }

    private fun startLoadingAnimation() {
        progressAnimator = ValueAnimator.ofInt(0, 100).apply {
            duration = 2000
            addUpdateListener { animator ->
                val progress = animator.animatedValue as Int
                percentageText?.text = "$progress%"
                if (progress == 100) {
                    view?.postDelayed({
                        navigateToMainScreen()
                    }, 500)
                }
            }
            start()
        }
    }

    private fun navigateToMainScreen() {
        (activity as? MainActivity)?.openMainFragment()
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onDestroyView() {
        progressAnimator?.cancel()
        progressAnimator = null
        super.onDestroyView()
    }
}
