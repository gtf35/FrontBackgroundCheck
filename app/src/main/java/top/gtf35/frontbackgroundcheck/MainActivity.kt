package top.gtf35.frontbackgroundcheck

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    companion object {
        const val DISPLAY_STATE_CONNECTED = 2
        private var maxWidth = 1f
            set(value) {
                if (value > field) field = value
            }
        private var maxHeight = 1f
            set(value) {
                if (value > field) field = value
            }
        private var hasChangedViewSize = false
        private var hasChangeAppIntoBackground = false
    }

    private val ui by lazy { MainUI(this) }
    private val rootView by lazy { ui.root }
    private val displayManager by lazy {
        getSystemService(Context.DISPLAY_SERVICE) as DisplayManager }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration?) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
        ui.splitScreenTv.text = if (isInMultiWindowMode) "进入分屏" else "刚刚离开分屏"
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        ui.windowsFocusTv.text = if (hasFocus) "处于焦点" else "离开焦点"
    }

    override fun onDestroy() {
        super.onDestroy()
        ui.frontBackgroundTv.text = "进入后台"
    }

    override fun onStop() {
        super.onStop()
        hasChangeAppIntoBackground = true
        ui.frontBackgroundTv.text = "进入后台"
    }

    override fun onPause() {
        super.onPause()
        hasChangeAppIntoBackground = true
        ui.frontBackgroundTv.text = "进入后台"
    }

    override fun onStart() {
        super.onStart()
        ui.frontBackgroundTv.text = "进入前台"
    }

    override fun onRestart() {
        super.onRestart()
        ui.frontBackgroundTv.text = "重新进入前台"
    }

    override fun onResume() {
        super.onResume()
        ui.frontBackgroundTv.text = if (!hasChangeAppIntoBackground) "正常在前台" else "从后台返回"
    }

    @SuppressLint("SetTextI18n", "PrivateApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(rootView)
        rootView.requestFocus()
        registerDisplayStatusCheckListen()
        registerViewSizeChangeListen()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ui.splitScreenTv.text = if (isInMultiWindowMode) "处于分屏" else "未处于分屏"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun registerViewSizeChangeListen() {
        rootView.viewTreeObserver.addOnDrawListener {
            val nowWidth = rootView.width.toFloat()
            val nowHeight = rootView.height.toFloat()
            // max 内部会自己过滤掉小的值
            maxHeight = nowHeight
            maxWidth = nowWidth

            val differenceHeightPX = abs(nowHeight - maxHeight)
            val differenceWidthPX = abs(nowWidth - maxWidth)
            var foundSthWrong = false
            if ((differenceHeightPX / nowHeight) >= 0.1f) {
                ui.windowsSizeChangeTv.text = "进入分屏"
                hasChangedViewSize = true
                foundSthWrong = true
            }
            if ((differenceWidthPX / nowWidth) >= 0.1f) {
                ui.windowsSizeChangeTv.text = "进入分屏"
                hasChangedViewSize = true
                foundSthWrong = true
            }
            if (!foundSthWrong) ui.windowsSizeChangeTv.text =
                if (hasChangedViewSize) "从分屏恢复正常" else "未发现异常"
        }
    }

    private fun registerDisplayStatusCheckListen() {
        displayManager.registerDisplayListener(object : DisplayManager.DisplayListener {
            override fun onDisplayChanged(displayId: Int) {
                checkWifiDisplay()
            }

            override fun onDisplayAdded(displayId: Int) {
                checkWifiDisplay()
            }

            override fun onDisplayRemoved(displayId: Int) {
                checkWifiDisplay()
            }
        },
            @SuppressLint("HandlerLeak")
            object : Handler() {}
        )
        checkWifiDisplay()
    }

    @SuppressLint("PrivateApi")
    private fun checkWifiDisplay() {
        try {
            val getWifiDisplayStatus = displayManager.javaClass.getMethod("getWifiDisplayStatus")
            val wifiDisplayStatus = getWifiDisplayStatus.invoke(displayManager)
            val getActiveDisplayState =
                Class.forName("android.hardware.display.WifiDisplayStatus")
                    .getMethod("getActiveDisplayState").invoke(wifiDisplayStatus) as Int

            ui.wifiDisplayTv.text =
                if (getActiveDisplayState == DISPLAY_STATE_CONNECTED) "连结了无线显示器" else "没有连结无线显示器"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}