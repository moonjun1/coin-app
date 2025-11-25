package com.example.fragmentapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fragmentapp.R

/**
 * 설정 Fragment
 * 앱 설정을 관리하는 Fragment
 */
class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.text_view).text =
            "설정 화면\n\n여기에서 다양한 설정을 변경할 수 있습니다.\n\n" +
                    "- 알림 설정\n- 테마 설정\n- 언어 설정\n- 개인정보 설정"
    }
}