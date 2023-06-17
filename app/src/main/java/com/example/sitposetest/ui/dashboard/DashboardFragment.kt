package com.example.sitposetest.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sitposetest.databinding.FragmentDashboardBinding
import android.widget.Toast
import android.os.CountDownTimer
import java.util.concurrent.TimeUnit
import com.example.sitposetest.R

enum class TimerStatus {
    STARTED, STOPPED
}

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private var timerStatus: TimerStatus = TimerStatus.STOPPED
    private var timeCountInMilliSeconds: Long = 0
    private var countDownTimer: CountDownTimer? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val progressBarCircle = binding.progressBarCircle
        val editTextMinute = binding.editTextMinute
        val textViewTime = binding.textViewTime
        val imageViewReset = binding.imageViewReset
        val imageViewStartStop = binding.imageViewStartStop

        imageViewStartStop.setOnClickListener {
            startStop()
        }

        imageViewReset.setOnClickListener {
            reset()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




    private fun onStartStopClick() {
        startStop()
    }

    private fun onResetClick() {
        reset()
    }


    // 番茄鐘的其他方法
    /**
     * 方法來重置倒計時器
     */
    private fun reset() {
        stopCountDownTimer()
        startCountDownTimer()
    }

    /**
     * 方法來啟動或停止倒計時器
     */
    private fun startStop() {
        if (timerStatus == TimerStatus.STOPPED) {
            // 初始化倒計時器值
            setTimerValues()
            // 初始化進度條值
            setProgressBarValues()
            // 顯示重置按鈕
            binding.imageViewReset.visibility = View.VISIBLE
            // 更改開始/停止圖示為停止圖示
            binding.imageViewStartStop.setImageResource(R.drawable.icon_stop)
            // 禁用編輯框
            binding.editTextMinute.isEnabled = false
            // 設置計時器狀態為已啟動
            timerStatus = TimerStatus.STARTED
            // 啟動倒計時器
            startCountDownTimer()
        } else {
            // 隱藏重置按鈕
            binding.imageViewReset.visibility = View.GONE
            // 更改停止圖示為開始圖示
            binding.imageViewStartStop.setImageResource(R.drawable.icon_start)
            // 啟用編輯框
            binding.editTextMinute.isEnabled = true
            // 設置計時器狀態為已停止
            timerStatus = TimerStatus.STOPPED
            stopCountDownTimer()
        }
    }

    /**
     * 方法來初始化倒計時器的值
     */
    private fun setTimerValues() {
        var time = 0
        if (!binding.editTextMinute.text.toString().isEmpty()) {
            // 從編輯框獲取值並轉換為整數
            time = binding.editTextMinute.text.toString().trim().toInt()
        } else {
            // 若編輯框為空，顯示提示訊息
            Toast.makeText(
                requireContext(),
                getString(R.string.message_minutes),
                Toast.LENGTH_LONG
            ).show()
        }
        // 將值轉換為毫秒並賦值給倒計時器
        timeCountInMilliSeconds = (time * 60 * 1000).toLong()
    }

    /**
     * 方法來啟動倒計時器
     */
    private fun startCountDownTimer() {
        val timer = object : CountDownTimer(timeCountInMilliSeconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.textViewTime.text = hmsTimeFormatter(millisUntilFinished)
                binding.progressBarCircle.progress = (millisUntilFinished / 1000).toInt()
            }

            override fun onFinish() {
                binding.textViewTime.text = hmsTimeFormatter(timeCountInMilliSeconds)
                // 重新初始化進度條值
                setProgressBarValues()
                // 隱藏重置按鈕
                binding.imageViewReset.visibility = View.GONE
                // 更改停止圖示為開始圖示
                binding.imageViewStartStop.setImageResource(R.drawable.icon_start)
                // 啟用編輯框
                binding.editTextMinute.isEnabled = true
                // 設置計時器狀態為已停止
                timerStatus = TimerStatus.STOPPED
            }
        }
        countDownTimer = timer
        timer.start()
    }

    /**
     * 方法來停止倒計時器
     */
    private fun stopCountDownTimer() {
        countDownTimer?.cancel()
    }

    /**
     * 方法來設置圓形進度條的值
     */
    private fun setProgressBarValues() {
        binding.progressBarCircle.max = timeCountInMilliSeconds.toInt() / 1000
        binding.progressBarCircle.progress = timeCountInMilliSeconds.toInt() / 1000
    }

    /**
     * 方法將毫秒轉換為時間格式
     *
     * @param milliSeconds 毫秒數
     * @return HH:mm:ss 格式的時間字串
     */
    private fun hmsTimeFormatter(milliSeconds: Long): String {
        return String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(milliSeconds),
            TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    milliSeconds
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    milliSeconds
                )
            )
        )
    }

}