package com.king.ultraswiperefresh

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.VibratorManager
import android.util.Log
import android.view.HapticFeedbackConstants
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

private const val VibrationDurationMs = 40L

/**
 * 一个提供标准下拉/上拉刷新振动行为的工具对象。
 */
object PullRefreshVibrations {

    /**
     * 提供默认的振动行为。
     * 内部使用 Vibrator.vibrate(createOneShot) 实现自定义时长的振动。
     *
     * @return 一个包含振动逻辑的 lambda，如果全局禁用或设备不支持则返回 null。
     */
    @Composable
    fun default(): (() -> Unit)? {
        return vibrationEffect(VibrationDurationMs, VibrationEffect.DEFAULT_AMPLITUDE)
    }

    /**
     * 提供自定义振动行为。
     *
     * @param milliseconds 振动时长
     * @param amplitude 振动强度
     * @return 一个包含振动逻辑的 lambda，如果全局禁用或设备不支持则返回 null。
     */
    @Composable
    fun vibrationEffect(milliseconds: Long, amplitude: Int): (() -> Unit)? {
        val vibrator = rememberVibrator()
        if (!vibrator.hasVibrator()) {
            Log.w(TAG, "hasVibrator: false")
            return null
        }
        return {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        VibrationDurationMs,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(VibrationDurationMs)
            }
        }
    }


    @Composable
    fun hapticFeedback(): (() -> Unit) {
        val view = LocalView.current
        return {
            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
    }


    /**
     * 明确表示“无振动”的行为。
     */
    fun none(): (() -> Unit)? = null

    /**
     * Vibrator
     */
    @Suppress("DEPRECATION")
    @Composable
    private fun rememberVibrator(): Vibrator {
        val context = LocalContext.current
        return remember("Vibrator") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
            } else {
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
        }
    }
}
