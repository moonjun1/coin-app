package com.example.fragmentapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

/**
 * 암호화폐 시세 앱
 * Fragment, Navigation Drawer, TabLayout 활용
 */
class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Edge-to-Edge 비활성화 (상태바 겹침 방지)
        // API 30 이상에서는 WindowInsetsController 사용 권장
        //window.decorView.systemUiVisibility = 0
        WindowCompat.setDecorFitsSystemWindows(window, true)

        // 뷰 초기화
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        // 툴바 설정
        setSupportActionBar(toolbar)

        // 드로워 토글 버튼 설정
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // 네비게이션 메뉴 아이템 클릭 리스너
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> {
                    replaceFragment(DashboardFragment())
                    toolbar.title = "대시보드"
                }
                R.id.nav_crypto -> {
                    replaceFragment(CryptoListFragment())
                    toolbar.title = "암호화폐 시세"
                }
                R.id.nav_favorite -> {
                    replaceFragment(FavoriteFragment())
                    toolbar.title = "즐겨찾기"
                }
                R.id.nav_settings -> {
                    replaceFragment(SettingsFragment())
                    toolbar.title = "설정"
                }
            }
            // 드로워 닫기
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // 초기 Fragment 설정
        if (savedInstanceState == null) {
            replaceFragment(DashboardFragment())
            navigationView.setCheckedItem(R.id.nav_dashboard)
        }
    }

    /**
     * Fragment 교체 메서드
     * FragmentTransaction을 사용한 기본적인 Fragment 전환
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    /**
     * 뒤로가기 버튼 처리
     * 드로워가 열려있으면 닫고, 아니면 기본 동작 수행

    override fun onBackPressed()  {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    } */
}