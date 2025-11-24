package com.example.fragmentapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * TabLayout과 ViewPager2를 활용한 뉴스 Fragment
 *
 * 학습 포인트:
 * 1. ViewPager2와 TabLayout 연동
 * 2. Child Fragment 사용 (Fragment 안에 Fragment)
 * 3. TabLayoutMediator를 통한 탭-페이지 연결
 */
class NewsFragment : Fragment() {
    // 변수 선언
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)

        setupViewPager()
    }
    private fun setupViewPager() {
        // ViewPager2 어댑터 설정
        val adapter = NewsViewPagerAdapter(this)
        viewPager.adapter = adapter
        // TabLayout과 ViewPager2를 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "정치"
                1 -> "경제"
                2 -> "사회"
                3 -> "스포츠"
                else -> "기타"
            }
        }.attach()
    }
}
class NewsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment)
{
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewsCategoryFragment.newInstance("정치")
            1 -> NewsCategoryFragment.newInstance("경제")
            2 -> NewsCategoryFragment.newInstance("사회")
            3 -> NewsCategoryFragment.newInstance("스포츠")
            else -> NewsCategoryFragment.newInstance("기타")
        }
    }

    override fun getItemCount() = 4  // 뷰페이저에 전달할 프래그먼트 갯수

}













