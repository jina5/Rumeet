package com.d204.rumeet.ui.running.matching

import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.d204.rumeet.R
import com.d204.rumeet.databinding.FragmentRunningMatchingBinding
import com.d204.rumeet.ui.base.AlertModel
import com.d204.rumeet.ui.base.BaseFragment
import com.d204.rumeet.ui.base.DefaultAlertDialog
import com.d204.rumeet.ui.chatting.chatting_list.model.ChattingRoomUiModel
import com.d204.rumeet.ui.find_account.FindAccountAction
import com.d204.rumeet.ui.running.matching.model.RunningMatchingRequestModel
import com.d204.rumeet.ui.running.matching.model.RunningRaceModel
import com.d204.rumeet.util.amqp.RunningAMQPManager
import com.google.gson.Gson
import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RunningMatchingFragment :
    BaseFragment<FragmentRunningMatchingBinding, RunningMatchingViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_running_matching

    override val viewModel: RunningMatchingViewModel by viewModels()
    private val args by navArgs<RunningMatchingFragmentArgs>()

    override fun initStartView() {
        viewModel.startMatching(args.gameType)
    }

    override fun initDataBinding() {
        lifecycleScope.launchWhenResumed {
            viewModel.runningMatchingSideEffect.collectLatest {
                when (it) {
                    is RunningMatchingSideEffect.FailMatching -> {
                        // 매칭 실패,  프래그먼트 이동 후 러닝옵션으로 pop
                        navigate(RunningMatchingFragmentDirections.actionRunningMatchingFragmentToRunningMatchingFailFragment())
                    }
                    is RunningMatchingSideEffect.SuccessMatching -> {
                        // 매칭 성공, 달리기 3초 후 시작
                        Log.d(TAG, "runningMatchingSideEffect: navigate ${args.gameType}")
                        navigate(
                            RunningMatchingFragmentDirections.actionRunningMatchingFragmentToRunningLoadingFragment(
                                myId = it.userId,
                                gameType = args.gameType,
                                roomId = it.roomId,
                                partnerId = it.partnerId
                            )
                        )
                    }
                }
            }
        }
    }


    override fun initAfterBinding() {

    }
}

private const val TAG = "RunningMatchingFragment"