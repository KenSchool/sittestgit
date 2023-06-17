package com.example.sitposetest.ui.notifications

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sitposetest.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //提醒聲
    private lateinit var audioManager: AudioManager

    //震動
    private lateinit var vibrator: Vibrator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //提醒聲
        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // 檢查並請求勿擾模式權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                val intent = Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
            }
        }

        // 開啟或關閉提示音
        binding.muteSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableSound()
            } else {
                disableSound()
            }
        }

        // 開啟或關閉震動
        binding.vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableVibration()
            } else {
                disableVibration()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 開啟提示音
    private fun enableSound() {
//        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
//        Toast.makeText(requireContext(), "提示音已開啟", Toast.LENGTH_SHORT).show()

//        audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI)
//        Toast.makeText(requireContext(), "提示音已開啟", Toast.LENGTH_SHORT).show()

        audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI)
        val toast = Toast.makeText(requireContext(), "提示音已開啟", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.START or Gravity.CENTER_VERTICAL, 0, 0)
        toast.show()
    }

    // 關閉提示音
    private fun disableSound() {
//        audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
//        Toast.makeText(requireContext(), "提示音已關閉", Toast.LENGTH_SHORT).show()

        audioManager.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI)
        Toast.makeText(requireContext(), "提示音已關閉", Toast.LENGTH_SHORT).show()
    }

    // 開啟震動
    private fun enableVibration() {
//        audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
//        Toast.makeText(requireContext(), "震動已開啟", Toast.LENGTH_SHORT).show()
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        Toast.makeText(requireContext(), "震動已開啟", Toast.LENGTH_SHORT).show()
    }

    // 關閉震動
    private fun disableVibration() {
//        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
//        Toast.makeText(requireContext(), "震動已關閉", Toast.LENGTH_SHORT).show()
        vibrator.cancel()
        Toast.makeText(requireContext(), "震動已關閉", Toast.LENGTH_SHORT).show()
    }

}