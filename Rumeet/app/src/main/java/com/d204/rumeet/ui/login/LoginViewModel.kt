package com.d204.rumeet.ui.login

import androidx.core.content.ContentProviderCompat.requireContext
import com.d204.rumeet.data.remote.dto.KakaoLoginErrorException
import com.d204.rumeet.data.remote.dto.NoUserFindErrorException
import com.d204.rumeet.domain.onError
import com.d204.rumeet.domain.onSuccess
import com.d204.rumeet.domain.usecase.auth.DoEmailLoginUseCase
import com.d204.rumeet.domain.usecase.auth.DoKakaoLoginUseCase
import com.d204.rumeet.domain.usecase.auth.RedirectKakaoLoginUseCase
import com.d204.rumeet.domain.usecase.auth.SetUserAutoLoginCheck
import com.d204.rumeet.domain.usecase.user.SetUserTokenUseCase
import com.d204.rumeet.ui.base.BaseViewModel
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


// Todo 네이버 로그인 승인되면 구현
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val doEmailLoginUseCase: DoEmailLoginUseCase,
    private val doKakaoLoginUseCase: DoKakaoLoginUseCase,
    private val setUserTokenUseCase: SetUserTokenUseCase,
    private val redirectKakaoLoginUseCase: RedirectKakaoLoginUseCase,
    private val setUserAutoLoginCheck: SetUserAutoLoginCheck
) : BaseViewModel() {

    private val _navigationEvent: MutableSharedFlow<LoginNavigationAction> = MutableSharedFlow()
    val navigationEvent: SharedFlow<LoginNavigationAction> = _navigationEvent.asSharedFlow()

    /**
     * 카카오 소셜 로그인
     * 자동로그인은 true로 고정
     * 예외에서 회원가입이 필요한 로그인 로직이라면 다시 요청을 보냄
     * @param accessToken - 카카오 OAuthToken에서 받은 AccessToken
     * */
    fun doKakaoLogin(accessToken : String) {
        baseViewModelScope.launch {
            doKakaoLoginUseCase(accessToken)
                .onSuccess { jwt ->
                    _navigationEvent.emit(LoginNavigationAction.LoginSuccess)
                    setUserTokenUseCase(jwt.accessToken, jwt.refreshToken)
                    setUserAutoLoginCheck(true)
                }
                .onError { e ->
                    if(e is KakaoLoginErrorException) redirectKakaoLogin(accessToken)
                }
        }
    }

    /**
     * 회원가입이 필요한 카카오 로그인
     * 자동 로그인은 true로 설정
     * @param accessToken - 카카오 OAuthToken에서 받은 AccessToken
     * */
    fun redirectKakaoLogin(accessToken: String){
        baseViewModelScope.launch {
            redirectKakaoLoginUseCase(accessToken)
                .onSuccess { oauthInfo ->
                    _navigationEvent.emit(LoginNavigationAction.NeedJoinFirst(oauthInfo.oauth, oauthInfo.profileImg))
                }
                .onError { e -> catchError(e) }
        }
    }


    /**
     * 이메일 계정 로그인
     * 자동 로그인 여부도 가져가야함
     * @param email - 사용자가 입력한 계정
     * @param password - 사용자가 입력한 비밀번호
     * @param autoLoginState - 자동로그인의 체크 상태
     * */
    fun doEmailLogin(email: String, password: String, autoLoginState: Boolean) {
        baseViewModelScope.launch {
            doEmailLoginUseCase.invoke(email, password, autoLoginState)
                .onSuccess { jwt ->
                    _navigationEvent.emit(LoginNavigationAction.LoginSuccess)
                    setUserAutoLoginCheck(autoLoginState)
                    setUserTokenUseCase(jwt.accessToken, jwt.refreshToken)
                }
                .onError { e ->
                    setUserAutoLoginCheck(false)
                    if (e is NoUserFindErrorException) _navigationEvent.emit(LoginNavigationAction.LoginFailed)
                    else catchError(e)
                }
        }
    }

    // 이메일 로그인
    fun emailLogin() {
        baseViewModelScope.launch {
            _navigationEvent.emit(LoginNavigationAction.EmailLogin)
        }
    }

    // 카카오 로그인
    fun kakaoLogin() {
        baseViewModelScope.launch {
            _navigationEvent.emit(LoginNavigationAction.KakaoLogin)
        }
    }

    // 계정찾기로 이동
    fun navigateFindAccount() {
        baseViewModelScope.launch {
            _navigationEvent.emit(LoginNavigationAction.NavigateFindAccount)
        }
    }

    // 회원가입으로 이동
    fun navigateJoin() {
        baseViewModelScope.launch {
            _navigationEvent.emit(LoginNavigationAction.NavigateJoin)
        }
    }
}