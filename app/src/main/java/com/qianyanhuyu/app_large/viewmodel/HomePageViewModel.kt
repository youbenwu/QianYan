package com.qianyanhuyu.app_large.viewmodel

import android.app.MediaRouteActionProvider
import android.app.Presentation
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.mediarouter.app.MediaRouteDialogFactory
import androidx.mediarouter.media.MediaControlIntent
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastState
import com.google.android.gms.cast.framework.CastStateListener
import com.qianyanhuyu.app_large.data.model.MediaData
import com.qianyanhuyu.app_large.data.user.UserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

/***
 * @Author : Cheng
 * @CreateDate : 2023/9/14 11:27
 * @Description : description
 */
@HiltViewModel
class HomePageViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val userApi: UserApi
) : ViewModel(), CastStateListener {

    var viewStates by mutableStateOf(HomePageViewState())
        private set

    private val _viewEvents = Channel<HomePageViewEvent>(Channel.BUFFERED)
    val viewEvents = _viewEvents.receiveAsFlow()


    var isCastingAvailable = false
        private set

    val castingStateLiveData = MutableLiveData(CastingState.UNKNOWN)

    private lateinit var mediaRouter: MediaRouter

    val mDialogFactory = MediaRouteDialogFactory.getDefault()

    private val mediaRouteActionProvider = MediaRouteActionProvider(context)

    val mSelector: MediaRouteSelector = MediaRouteSelector.Builder()
        .addControlCategory(MediaControlIntent.CATEGORY_LIVE_AUDIO)
        .addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
        .build()


    init {
        try {
            isCastingAvailable = true

            val castingContext = CastContext.getSharedInstance(context)
            castingContext.addCastStateListener(this)


        } catch (e: Exception) {
            // handle me please
        }
    }

    /**
     * 检查登录状态
     */
    private fun checkHomePageState() {
    }

    fun dispatch(action: HomePageViewAction) {
        when (action) {
            is HomePageViewAction.CheckHomePageState -> checkHomePageState()
            else -> {

            }
        }
    }


    private var mediaCallback: (context: Context) -> MediaRouter.Callback = {
        object : MediaRouter.Callback() {

        }
    }

    fun shouldShowChooserFragment(): Boolean {
        return when (castingStateLiveData.value) {
            CastingState.NOT_CONNECTED -> true
            CastingState.UNKNOWN -> true
            CastingState.NO_DEVICES_AVAILABLE -> true
            CastingState.CONNECTING -> false
            CastingState.CONNECTED -> false
            else -> false
        }
    }
    override fun onCastStateChanged(p0: Int) {
        val castingState = when (p0) {
            CastState.CONNECTED -> CastingState.CONNECTED
            CastState.CONNECTING -> CastingState.CONNECTING
            CastState.NOT_CONNECTED -> CastingState.NOT_CONNECTED
            CastState.NO_DEVICES_AVAILABLE -> CastingState.NO_DEVICES_AVAILABLE
            else -> CastingState.UNKNOWN
        }

        castingStateLiveData.postValue(castingState)
    }
}

enum class CastingState {
    CONNECTED,
    CONNECTING,
    NOT_CONNECTED,
    NO_DEVICES_AVAILABLE,
    UNKNOWN
}

data class HomePageViewState(
    val email: String = "",
    val mediaData: MediaData = MediaData(
        bannerSrc = "https://img.js.design/assets/img/64b4d729b23f2cad3d25ff2e.png"
    ),
    val isLogging: Boolean = true,
)

sealed class HomePageViewAction {
    object HomePage : HomePageViewAction()
    object CheckHomePageState: HomePageViewAction()
}

sealed class HomePageViewEvent {
    data class NavTo(val route: String) : HomePageViewEvent()
    data class ShowMessage(val message: String) : HomePageViewEvent()
}