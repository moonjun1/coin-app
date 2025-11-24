package com.example.fragmentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * TabLayout과 ViewPager2를 활용한 암호화폐 목록 Fragment
 *
 * 학습 포인트:
 * 1. ViewPager2와 TabLayout 연동
 * 2. Child Fragment 사용 (Fragment 안에 Fragment)
 * 3. TabLayoutMediator를 통한 탭-페이지 연결
 */
class CryptoListFragment : Fragment() {

    // 변수 선언
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crypto_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)

        setupViewPager()
    }

    /**
     * ViewPager2와 TabLayout 설정
     */
    private fun setupViewPager() {
        // ViewPager2 어댑터 설정
        val adapter = CryptoViewPagerAdapter(this)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager2를 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "메이저코인"
                1 -> "알트코인"
                else -> "기타"
            }
        }.attach()
    }
}

/**
 * ViewPager2 어댑터
 * 각 탭에 해당하는 Fragment를 생성
 */
class CryptoViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CryptoCategoryFragment.newInstance("major")  // 메이저코인
            1 -> CryptoCategoryFragment.newInstance("alt")    // 알트코인
            else -> CryptoCategoryFragment.newInstance("major")
        }
    }

    override fun getItemCount() = 2  // 뷰페이저에 전달할 프래그먼트 갯수
}
