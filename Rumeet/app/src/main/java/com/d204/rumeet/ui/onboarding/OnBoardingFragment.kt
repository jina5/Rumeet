package com.d204.rumeet.ui.onboarding

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentOnboardingBinding
import com.d204.rumeet.ui.activities.LoginActivity
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.util.startActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OnBoardingFragment: BaseFragment<FragmentOnboardingBinding, OnBoardingViewModel>() {
    override val layoutResourceId: Int = R.layout.fragment_onboarding
    override val viewModel: OnBoardingViewModel by viewModels()

    // 뷰모델 실행
    override fun initStartView() {

    }

    // 뷰모델 초기 설정
    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.navigateToLogin.collectLatest {
                launch {
                    requireContext().startActivity(LoginActivity::class.java)
                }
            }
        }
    }

    // view 설정
    override fun initAfterBinding() {
        binding.btnOnboardingContinue.apply {
            setContent("계속하기")
            setState(true)
            setButtonClickEvent {
                viewModel.setVisitCheck()
            }
        }
    }
}