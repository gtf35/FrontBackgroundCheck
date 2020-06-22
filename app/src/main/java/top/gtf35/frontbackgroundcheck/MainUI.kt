package top.gtf35.frontbackgroundcheck

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ScrollView
import splitties.views.dsl.core.*
import splitties.views.gravityCenter

class MainUI(override val ctx: Context) : Ui {
    @SuppressLint("SetTextI18n")
    private val wifiDisplayHintTv = textView {
        setTextColor(Color.RED)
        textSize = 30f
        text = "wifi 投屏检测"
        gravity = gravityCenter
    }
    val wifiDisplayTv = textView {
        textSize = 30f
        text = "等待结果"
        gravity = gravityCenter
    }
    private val frontBackgroundHintTv = textView {
        setTextColor(Color.RED)
        textSize = 30f
        text = "前后台检测"
        gravity = gravityCenter
    }
    val frontBackgroundTv = textView {
        textSize = 30f
        text = "等待结果"
        gravity = gravityCenter
    }
    private val windowsFocusHintTv = textView {
        setTextColor(Color.RED)
        textSize = 30f
        text = "窗口焦点"
        gravity = gravityCenter
    }
    val windowsFocusTv = textView {
        textSize = 30f
        text = "等待结果"
        gravity = gravityCenter
    }
    private val splitScreenHintTv = textView {
        setTextColor(Color.RED)
        textSize = 30f
        text = "分屏检测"
        gravity = gravityCenter
    }
    val splitScreenTv = textView {
        textSize = 30f
        text = "等待结果"
        gravity = gravityCenter
    }
    private val windowsSizeChangeHintTv = textView {
        setTextColor(Color.RED)
        textSize = 30f
        text = "窗口大小改变检测"
        gravity = gravityCenter
    }
    val windowsSizeChangeTv = textView {
        textSize = 30f
        text = "等待结果"
        gravity = gravityCenter
    }


    override val root: View
        get() = view({ScrollView(it)}) {
            add(verticalLayout {
                val defaultLP = lParams {
                    width = matchParent
                    height = wrapContent
                }
                add(wifiDisplayHintTv, defaultLP)
                add(wifiDisplayTv, defaultLP)
                add(frontBackgroundHintTv, defaultLP)
                add(frontBackgroundTv, defaultLP)
                add(windowsFocusHintTv, defaultLP)
                add(windowsFocusTv, defaultLP)
                add(splitScreenHintTv, defaultLP)
                add(splitScreenTv, defaultLP)
                add(windowsSizeChangeHintTv, defaultLP)
                add(windowsSizeChangeTv, defaultLP)
            }, lParams {
                width = matchParent
                height = matchParent
            })
        }
}