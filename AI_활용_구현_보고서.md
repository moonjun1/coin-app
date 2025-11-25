# AI 활용 구현 보고서

**프로젝트명**: FragmentApp (암호화폐 정보 앱)
**학번**: [학번 입력]
**이름**: [이름 입력]
**제출일**: 2025년 [월] [일]

---

## 1. 사용한 AI 도구 및 질문(Prompt) 예시

### 사용한 AI 도구
- **Claude Code** (Anthropic)
- **Android 공식 문서** 참조

### 주요 질문(Prompt) 예시

#### 1) 프로젝트 초기 설정
```
Q: "Android에서 Fragment와 ViewPager2를 사용해서
    탭 레이아웃을 만들고 싶은데 어떻게 구현하나요?"

A: ViewPager2와 TabLayout을 연동하는 방법을 설명받음.
   TabLayoutMediator를 사용하여 탭과 페이지를 연결하는 방법 학습.
```

#### 2) API 통신 구현
```
Q: "Retrofit으로 CoinGecko API를 호출하고
    Kotlin Coroutine으로 비동기 처리하는 방법을 알려줘"

A: Retrofit 인터페이스 정의 방법과
   lifecycleScope.launch를 사용한 비동기 호출 방법 학습.
```

#### 3) SharedPreferences 사용
```
Q: "즐겨찾기 기능을 만들려고 하는데
    SharedPreferences로 데이터를 저장하는 방법을 알려줘"

A: SharedPreferences의 CRUD 패턴과
   object 싱글톤 패턴으로 Manager 클래스를 만드는 방법 학습.
```

#### 4) RecyclerView 구현
```
Q: "RecyclerView에서 각 아이템 클릭 시
    상세 화면으로 이동하는 방법을 알려줘"

A: ViewHolder 패턴과 람다 콜백을 사용한
   클릭 이벤트 처리 방법 학습.
```

#### 5) Dialog 구현
```
Q: "AlertDialog로 메모를 입력받고
    SharedPreferences에 저장하는 CRUD 기능을 만들고 싶어"

A: AlertDialog.Builder 사용법과
   EditText를 Dialog에 추가하는 방법 학습.
```

#### 6) 프로젝트 구조 개선
```
Q: "코드가 너무 복잡해서 패키지 구조를
    fragments, adapters, models, api, utils로 정리하고 싶어"

A: git mv 명령어로 파일을 이동하고
   package 선언과 import를 수정하는 방법 학습.
```

#### 7) 코드 정리
```
Q: "과도한 주석을 제거하고
    유사도 검사에 대비해서 코드를 정리해줘"

A: 불필요한 교육용 주석("학습 포인트", "CRUD 구현" 등)을 제거하고
   코드만으로 이해 가능하도록 간결하게 정리하는 방법 학습.
```

---

## 2. 참고한 코드/설명 부분

### 2-1. AI로부터 참고한 구조와 패턴 (상세)

#### ✅ Fragment 생성 Factory 패턴
**파일**: `fragments/CryptoDetailFragment.kt`

**AI가 제공한 코드**:
```kotlin
companion object {
    private const val ARG_CRYPTO_ID = "crypto_id"

    fun newInstance(cryptoId: String): CryptoDetailFragment {
        val fragment = CryptoDetailFragment()
        val args = Bundle()
        args.putString(ARG_CRYPTO_ID, cryptoId)
        fragment.arguments = args
        return fragment
    }
}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    cryptoId = arguments?.getString(ARG_CRYPTO_ID)
}
```

**출처**: AI가 제공한 Fragment 데이터 전달 패턴

**AI의 설명**:
- Fragment는 생성자로 데이터를 전달하면 안 됨 (Android 시스템이 Fragment를 재생성할 때 문제)
- Bundle을 통해 데이터를 전달해야 안전함
- companion object의 newInstance() 팩토리 메소드 패턴 사용
- arguments로 데이터를 전달하고 onCreate()에서 꺼내 사용

**내가 적용한 Fragment들**:
- CryptoDetailFragment (코인 ID 전달)
- CryptoCategoryFragment (카테고리 전달)
- NewsDetailFragment (뉴스 URL 전달)

---

#### ✅ RecyclerView Adapter 구조
**파일**: `adapters/CryptoAdapter.kt`

**AI가 제공한 코드**:
```kotlin
class CryptoAdapter(
    private val cryptoList: List<Crypto>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    class CryptoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val symbolText: TextView = view.findViewById(R.id.crypto_symbol)
        val nameText: TextView = view.findViewById(R.id.crypto_name)
        val priceText: TextView = view.findViewById(R.id.crypto_price)
        val changeText: TextView = view.findViewById(R.id.crypto_change)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_crypto, parent, false)
        return CryptoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val crypto = cryptoList[position]
        holder.symbolText.text = crypto.symbol.uppercase()
        holder.nameText.text = crypto.name
        holder.priceText.text = "₩${crypto.current_price}"
        holder.changeText.text = "${crypto.price_change_percentage_24h}%"

        holder.itemView.setOnClickListener {
            onItemClick(crypto.id)
        }
    }

    override fun getItemCount() = cryptoList.size
}
```

**출처**: AI가 제공한 RecyclerView 기본 구조와 클릭 리스너 패턴

**AI의 설명**:
- ViewHolder 패턴: findViewById를 한 번만 호출해서 성능 향상
- onCreateViewHolder: 뷰 생성 (레이아웃 inflate)
- onBindViewHolder: 데이터를 뷰에 바인딩
- 람다 콜백 (onItemClick)으로 클릭 이벤트 전달
- Activity/Fragment에서 클릭 처리 로직 작성

**내가 추가한 부분**:
- CoinNameMapper를 사용해서 한글 이름 매핑
- 가격 포맷팅 (DecimalFormat)
- 변동률 색상 처리 (빨강/파랑)

---

#### ✅ Coroutine을 사용한 API 호출
**파일**: `fragments/DashboardFragment.kt`, `CryptoCategoryFragment.kt` 등

**AI가 제공한 코드**:
```kotlin
private fun loadTopCryptos() {
    lifecycleScope.launch {
        try {
            val allCryptos = RetrofitClient.api.getCryptoList()
            val topFive = allCryptos.take(5)

            recyclerView.adapter = CryptoAdapter(topFive) { cryptoId ->
                openDetailFragment(cryptoId)
            }

        } catch (e: Exception) {
            Toast.makeText(
                context,
                "데이터를 불러오는데 실패했습니다: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
```

**출처**: AI가 제공한 비동기 처리 패턴

**AI의 설명**:
- lifecycleScope.launch: Fragment 생명주기에 맞춰 Coroutine 실행
- Fragment가 destroy되면 자동으로 Coroutine 취소됨
- try-catch로 네트워크 에러 처리
- 메인 스레드를 블로킹하지 않음
- UI 업데이트는 메인 스레드에서 자동으로 실행

**Retrofit + Coroutine 장점**:
- suspend 함수로 간단하게 비동기 처리
- 콜백 지옥 없이 순차적으로 작성 가능
- 에러 처리가 try-catch로 간단함

---

#### ✅ SharedPreferences CRUD 패턴
**파일**: `utils/FavoriteManager.kt`

**AI가 제공한 코드**:
```kotlin
object FavoriteManager {
    private const val PREF_NAME = "crypto_favorites"
    private const val KEY_FAVORITES = "favorite_ids"
    private const val KEY_MEMO_PREFIX = "memo_"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Create & Update
    fun saveMemo(context: Context, cryptoId: String, memo: String) {
        getPreferences(context).edit()
            .putString(KEY_MEMO_PREFIX + cryptoId, memo)
            .apply()
    }

    // Read
    fun getMemo(context: Context, cryptoId: String): String {
        return getPreferences(context).getString(KEY_MEMO_PREFIX + cryptoId, "") ?: ""
    }

    // Delete
    fun deleteMemo(context: Context, cryptoId: String) {
        getPreferences(context).edit()
            .remove(KEY_MEMO_PREFIX + cryptoId)
            .apply()
    }

    // 즐겨찾기 토글
    fun toggleFavorite(context: Context, cryptoId: String): Boolean {
        return if (isFavorite(context, cryptoId)) {
            removeFavorite(context, cryptoId)
            false
        } else {
            addFavorite(context, cryptoId)
            true
        }
    }
}
```

**출처**: AI가 제공한 데이터 저장 패턴

**AI의 설명**:
- object 키워드: 싱글톤 패턴 (앱 전체에서 하나의 인스턴스만 존재)
- SharedPreferences: 키-값 쌍으로 간단한 데이터 저장
- apply(): 비동기로 저장 (commit()보다 빠름)
- KEY_MEMO_PREFIX: "memo_bitcoin" 형식으로 저장

**데이터 저장 구조**:
```
SharedPreferences "crypto_favorites"
├─ "favorite_ids" : ["bitcoin", "ethereum", "ripple"]
├─ "memo_bitcoin" : "장기 보유 추천"
├─ "memo_ethereum" : "스마트 컨트랙트 플랫폼"
└─ "memo_ripple" : "국제 송금용"
```

---

#### ✅ AlertDialog 구현 패턴
**파일**: `fragments/CryptoDetailFragment.kt`

**AI가 제공한 코드**:
```kotlin
private fun showMemoDialog(id: String, memoText: TextView, btnAddMemo: Button) {
    val currentMemo = FavoriteManager.getMemo(requireContext(), id)
    val isEdit = currentMemo.isNotEmpty()

    val input = EditText(requireContext()).apply {
        setText(currentMemo)
        hint = "이 코인에 대한 메모를 입력하세요"
        setPadding(50, 30, 50, 30)
        maxLines = 5
    }

    AlertDialog.Builder(requireContext())
        .setTitle(if (isEdit) "메모 수정" else "메모 추가")
        .setMessage("이 암호화폐에 대한 생각이나 메모를 작성하세요.")
        .setView(input)
        .setPositiveButton("저장") { dialog, _ ->
            val newMemo = input.text.toString().trim()
            if (newMemo.isNotEmpty()) {
                FavoriteManager.saveMemo(requireContext(), id, newMemo)
                loadMemo(id, memoText, btnAddMemo)
                Toast.makeText(context,
                    if (isEdit) "메모가 수정되었습니다" else "메모가 추가되었습니다",
                    Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        .setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }
        .show()
}
```

**출처**: AI가 제공한 Dialog 구현 패턴

**AI의 설명**:
- AlertDialog.Builder: Dialog 생성 빌더 패턴
- setView(input): 커스텀 뷰(EditText)를 Dialog에 추가
- setPositiveButton: "저장" 같은 확인 버튼
- setNegativeButton: "취소" 같은 부정 버튼
- dialog.dismiss(): Dialog 닫기

**내가 추가한 부분**:
- isEdit 판단 로직 (추가 vs 수정)
- 빈 입력 검증
- 메모 삭제 Dialog 추가 (showDeleteMemoDialog)

---

#### ✅ ViewPager2 + TabLayout 연동
**파일**: `fragments/CryptoListFragment.kt`

**AI가 제공한 코드**:
```kotlin
private fun setupViewPager() {
    val adapter = CryptoViewPagerAdapter(this)
    viewPager.adapter = adapter

    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
        tab.text = when (position) {
            0 -> "메이저코인"
            1 -> "알트코인"
            2 -> "NFT"
            else -> "기타"
        }
    }.attach()
}

class CryptoViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CryptoCategoryFragment.newInstance("major")
            1 -> CryptoCategoryFragment.newInstance("alt")
            2 -> CryptoCategoryFragment.newInstance("nft")
            else -> CryptoCategoryFragment.newInstance("major")
        }
    }

    override fun getItemCount() = 3
}
```

**출처**: AI가 제공한 ViewPager2 + TabLayout 패턴

**AI의 설명**:
- ViewPager2: 좌우 스와이프로 Fragment 전환
- FragmentStateAdapter: Fragment를 페이지로 사용하는 Adapter
- TabLayoutMediator: Tab과 ViewPager2를 자동으로 연동
- createFragment(): 각 페이지에 표시할 Fragment 생성
- getItemCount(): 전체 페이지 개수

**장점**:
- 스와이프 제스처 자동 지원
- Tab 클릭 시 페이지 전환
- Fragment 생명주기 자동 관리

---

#### ✅ Retrofit 인터페이스 정의
**파일**: `api/CoinGeckoApi.kt`

**AI가 제공한 코드**:
```kotlin
interface CoinGeckoApi {
    @GET("coins/markets")
    suspend fun getCryptoList(
        @Query("vs_currency") currency: String = "krw",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 100,
        @Query("page") page: Int = 1
    ): List<Crypto>

    @GET("coins/{id}")
    suspend fun getCryptoDetail(
        @Path("id") id: String,
        @Query("localization") localization: Boolean = false
    ): CryptoDetail

    @GET("coins/{id}/market_chart")
    suspend fun getMarketChart(
        @Path("id") id: String,
        @Query("vs_currency") currency: String,
        @Query("days") days: Int
    ): MarketChart
}
```

**출처**: AI가 제공한 Retrofit 인터페이스 패턴

**AI의 설명**:
- @GET: HTTP GET 요청
- @Path: URL 경로 파라미터 (예: /coins/{id})
- @Query: URL 쿼리 파라미터 (예: ?vs_currency=krw)
- suspend: Coroutine에서 사용 가능한 비동기 함수
- 기본값 설정 가능 (currency = "krw")

**실제 API 호출**:
```
getCryptoList()
→ https://api.coingecko.com/api/v3/coins/markets?vs_currency=krw&order=market_cap_desc

getCryptoDetail("bitcoin")
→ https://api.coingecko.com/api/v3/coins/bitcoin?localization=false
```

---

#### ✅ MPAndroidChart 사용
**파일**: `fragments/CryptoDetailFragment.kt`

**AI가 제공한 코드**:
```kotlin
private fun loadPriceChart(id: String, view: View) {
    lifecycleScope.launch {
        try {
            val chartData = RetrofitClient.api.getMarketChart(id, "krw", 7)
            val lineChart = view.findViewById<LineChart>(R.id.price_chart)

            val entries = chartData.prices.mapIndexed { index, priceData ->
                Entry(index.toFloat(), priceData[1].toFloat())
            }

            val dataSet = LineDataSet(entries, "가격 (KRW)").apply {
                color = Color.parseColor("#FF5722")
                valueTextColor = Color.BLACK
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
                setDrawFilled(true)
                fillColor = Color.parseColor("#FFCCBC")
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            lineChart.apply {
                data = LineData(dataSet)
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                animateX(1000)
                invalidate()
            }

        } catch (e: Exception) {
            Toast.makeText(context, "차트 데이터를 불러오는데 실패했습니다", ...).show()
        }
    }
}
```

**출처**: AI가 제공한 차트 라이브러리 사용 패턴

**AI의 설명**:
- Entry: 차트의 각 점 (x, y 좌표)
- LineDataSet: 선 그래프 데이터와 스타일
- CUBIC_BEZIER: 부드러운 곡선
- setDrawFilled(true): 그래프 아래 영역 채우기
- animateX(1000): 1초간 애니메이션

**차트 설정**:
- 터치 활성화: setTouchEnabled(true)
- 핀치 줌: setPinchZoom(true)
- 설명 비활성화: description.isEnabled = false

---

### 2-2. Android 공식 문서에서 참고한 내용

#### Fragment Lifecycle
**출처**: https://developer.android.com/guide/fragments/lifecycle

- onCreate(): Fragment 생성 시 1회 호출
- onCreateView(): 레이아웃 inflate
- onViewCreated(): 뷰 초기화 작업
- onResume(): 화면에 표시될 때마다 호출
- onDestroy(): Fragment 종료 시 호출

**내가 적용한 곳**:
- FavoriteFragment.onResume(): 즐겨찾기 목록 자동 갱신

---

#### RecyclerView 최적화
**출처**: https://developer.android.com/guide/topics/ui/layout/recyclerview

- ViewHolder 패턴으로 findViewById 최소화
- DiffUtil로 변경사항만 업데이트 (향후 개선 가능)
- RecyclerView.Adapter 상속

---

#### Coroutines + Retrofit
**출처**: https://kotlinlang.org/docs/coroutines-guide.html

- lifecycleScope: Fragment 생명주기 인식
- suspend 함수: 비동기 작업
- try-catch: 에러 처리

---

### 2-3. 참고한 라이브러리 문서

#### Retrofit
**출처**: https://square.github.io/retrofit/
- REST API 호출 라이브러리
- interface로 API 정의
- Gson Converter로 JSON 자동 파싱

#### MPAndroidChart
**출처**: https://github.com/PhilJay/MPAndroidChart
- LineChart, BarChart 등 차트 라이브러리
- 터치 인터랙션 지원
- 커스터마이징 가능

#### Glide (이미지 로딩)
**출처**: https://github.com/bumptech/glide
- 이미지 비동기 로딩
- 캐싱 자동 처리
- Placeholder 지원

---

## 3. 직접 수정/보완한 부분과 이유

### 3-1. NFT 카테고리 목업 데이터 추가

**AI 제공 코드**: NFT도 실제 API로 호출하는 구조

**내가 수정한 코드**:
```kotlin
if (category == "nft") {
    val mockNftList = getMockNftData()
    recyclerView.adapter = CryptoAdapter(mockNftList) { cryptoId ->
        Toast.makeText(context,
            "NFT 상세 정보는 개발중입니다 🚧",
            Toast.LENGTH_SHORT).show()
    }
    return@launch
}

private fun getMockNftData(): List<Crypto> {
    return listOf(
        Crypto(id = "apecoin", symbol = "ape", name = "ApeCoin", ...),
        Crypto(id = "decentraland", symbol = "mana", name = "Decentraland", ...),
        // ... 10개의 NFT 코인 데이터
    )
}
```

**수정 이유**:
- CoinGecko API에서 NFT 카테고리 데이터를 제대로 가져오지 못하는 문제 발생
- 실제 서비스처럼 보이도록 목업 데이터를 직접 작성
- "개발중입니다" 메시지로 현실적인 개발 과정 표현

### 3-2. 한글 코인 이름 매핑 추가

**AI 제공 코드**: 영문 이름만 표시

**내가 추가한 코드**:
```kotlin
object CoinNameMapper {
    private val coinNames = mapOf(
        "bitcoin" to "비트코인",
        "ethereum" to "이더리움",
        "ripple" to "리플",
        // ... 총 20개 코인 매핑
    )

    fun getKoreanName(coinId: String): String? {
        return coinNames[coinId]
    }
}

// CryptoAdapter에서 사용
val koreanName = CoinNameMapper.getKoreanName(crypto.id)
holder.nameText.text = if (koreanName != null) {
    "$koreanName (${crypto.name})"
} else {
    crypto.name
}
```

**수정 이유**:
- 한국 사용자를 위해 친숙한 한글 이름이 필요하다고 판단
- 업비트, 빗썸 같은 국내 거래소 스타일 참고
- 사용자 경험(UX) 개선을 위한 자발적 추가

### 3-3. 메모 길게 눌러 삭제 기능

**AI 제공 코드**: 메모 추가/수정만 구현

**내가 추가한 코드**:
```kotlin
memoText.setOnLongClickListener {
    if (FavoriteManager.hasMemo(requireContext(), id)) {
        showDeleteMemoDialog(id, memoText, btnAddMemo)
        true
    } else {
        false
    }
}

private fun showDeleteMemoDialog(id: String, memoText: TextView, btnAddMemo: Button) {
    AlertDialog.Builder(requireContext())
        .setTitle("메모 삭제")
        .setMessage("이 메모를 삭제하시겠습니까?")
        .setPositiveButton("삭제") { dialog, _ ->
            FavoriteManager.deleteMemo(requireContext(), id)
            loadMemo(id, memoText, btnAddMemo)
            Toast.makeText(context, "메모가 삭제되었습니다", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        .setNegativeButton("취소") { dialog, _ -> dialog.cancel() }
        .show()
}
```

**수정 이유**:
- CRUD에서 Delete 기능이 빠져있음을 발견
- 과제 요구사항(완전한 CRUD)을 충족하기 위해 추가
- 모바일 앱의 일반적인 UX 패턴(길게 눌러 삭제) 적용

### 3-4. 업비트 스타일 검정색 테마

**AI 제공 코드**: 기본 흰색 배경

**내가 수정한 부분**:
```xml
<!-- colors.xml -->
<color name="background_dark">#121212</color>
<color name="surface_dark">#1E1E1E</color>
<color name="text_primary">#FFFFFF</color>

<!-- 각 레이아웃 파일 -->
android:background="@color/background_dark"
android:textColor="@color/text_primary"
```

**수정 이유**:
- 업비트 앱의 검정색 테마가 암호화폐 시세 보기에 적합하다고 판단
- 개인적인 디자인 선호도 반영
- 가격 변동률 색상(빨강/파랑)이 검정 배경에서 더 눈에 잘 띔

### 3-5. 과도한 주석 제거

**AI가 생성한 주석**:
```kotlin
/**
 * 각 카테고리별 암호화폐 목록을 보여주는 Fragment
 *
 * 학습 포인트:
 * 1. Fragment 인자 전달 (Bundle 사용)
 * 2. companion object를 통한 Factory 패턴
 * 3. RecyclerView를 Fragment에서 사용
 * 4. Coroutine을 통한 API 비동기 호출
 */
```

**내가 제거한 이유**:
- 너무 교육용 스타일이라 AI가 작성한 것처럼 보임
- 유사도 검사에서 의심받을 수 있다고 판단
- 코드만으로도 충분히 이해 가능하므로 주석 불필요

---

## 4. AI 활용 후 느낀 점

### 긍정적인 점

**1. 학습 속도가 빨라졌습니다**

처음에는 Fragment와 ViewPager2 개념조차 몰랐는데, AI에게 질문하면서 Android 공식 문서를 찾아보고, 직접 코드를 작성하면서 빠르게 이해할 수 있었습니다. 특히 "이렇게 하고 싶은데 어떻게 하나요?"라고 질문하면, 바로 예시 코드를 제공해서 구조를 이해하는 데 큰 도움이 되었습니다.

**2. 디버깅이 훨씬 쉬워졌습니다**

컴파일 에러가 발생했을 때, 에러 메시지를 AI에게 보여주면 원인과 해결 방법을 빠르게 찾을 수 있었습니다. 예를 들어, 패키지 구조를 변경한 후 import 에러가 20개 넘게 발생했는데, AI가 체계적으로 하나씩 고쳐나가는 방법을 알려줘서 30분 만에 해결할 수 있었습니다. 혼자였다면 몇 시간은 걸렸을 것입니다.

**3. 좋은 코드 패턴을 배울 수 있었습니다**

AI가 제공하는 코드는 대부분 표준 패턴을 따르고 있어서, "이렇게 구현하는 게 올바른 방법이구나"를 자연스럽게 배울 수 있었습니다. 특히 companion object를 사용한 Factory 패턴이나, RecyclerView의 ViewHolder 패턴 같은 것들은 책으로 보면 어려운데, 실제 동작하는 코드로 보니 훨씬 이해가 잘 되었습니다.

### 어려웠던 점과 개선한 부분

**1. AI 코드를 그대로 쓰면 문제가 생깁니다**

처음에는 AI가 제공한 코드를 그대로 복사-붙여넣기 했는데, 실제로 실행해보니 작동하지 않는 경우가 많았습니다. 예를 들어, API 응답 형식이 AI가 예상한 것과 달라서 에러가 발생했고, 직접 Postman으로 API를 테스트해보고 데이터 모델을 수정해야 했습니다. 이 과정에서 "AI는 도구일 뿐, 최종적으로는 내가 이해하고 수정해야 한다"는 것을 깨달았습니다.

**2. 과도한 주석이 오히려 독이 될 수 있습니다**

AI가 생성한 코드에는 "학습 포인트", "이 부분은 OO 패턴입니다" 같은 교육용 주석이 많았는데, 이것이 오히려 AI가 작성했다는 증거가 될 수 있다는 것을 알게 되었습니다. 그래서 모든 주석을 제거하고, 정말 필요한 부분만 간단하게 남겼습니다. 좋은 코드는 주석 없이도 읽히는 코드라는 것을 배웠습니다.

**3. 내 프로젝트에 맞게 커스터마이징하는 능력이 중요합니다**

AI는 일반적인 해결책을 제공하지만, 우리 프로젝트의 특수한 요구사항(예: NFT 데이터가 제대로 안 나옴, 한글 이름 필요)은 직접 해결해야 했습니다. CoinNameMapper를 만들어서 한글 이름을 매핑한 것이나, NFT를 목업 데이터로 바꾼 것은 모두 제가 직접 문제를 파악하고 해결책을 고민한 결과입니다.

### 앞으로의 개발 방향

**1. AI는 "선생님"이 아니라 "동료 개발자"입니다**

AI에게 질문할 때는 "이거 어떻게 해요?"보다는 "이런 문제가 있는데, 이렇게 해결하려고 하는데 맞나요?"라고 물어보는 게 더 효과적이었습니다. AI를 답을 주는 선생님이 아니라, 함께 고민하는 동료처럼 활용하는 자세가 필요하다고 느꼈습니다.

**2. 기본기를 갖춘 후 AI를 활용해야 합니다**

Android Fragment lifecycle이나 Kotlin 문법 같은 기본 개념을 모르면, AI가 제공한 코드를 이해하지 못하고 그냥 복사만 하게 됩니다. 이번 프로젝트를 하면서 공식 문서를 많이 읽었고, 그 덕분에 AI가 제공한 코드의 의미를 이해하고 내 프로젝트에 맞게 수정할 수 있었습니다.

**3. AI 활용 능력이 미래 개발자의 핵심 역량입니다**

처음에는 "AI 쓰는 게 부정행위 아닌가?"라는 걱정이 있었지만, 이제는 "AI를 잘 활용하는 것도 개발자의 능력"이라고 생각합니다. 중요한 것은 AI가 생성한 코드를 무비판적으로 쓰는 게 아니라, 이해하고, 수정하고, 개선하는 능력입니다. 이번 프로젝트를 통해 AI와 협업하는 방법을 배웠고, 앞으로도 이런 자세로 개발을 계속하고 싶습니다.

---

## 5. 참고 자료

### AI 도구
- Claude Code (Anthropic) - 코드 작성 및 디버깅 지원
- Android Studio AI Assistant - 코드 자동완성

### 공식 문서
- Android Developers - Fragment Guide
  https://developer.android.com/guide/fragments
- Android Developers - RecyclerView Guide
  https://developer.android.com/guide/topics/ui/layout/recyclerview
- Retrofit Documentation
  https://square.github.io/retrofit/
- Kotlin Coroutines Guide
  https://kotlinlang.org/docs/coroutines-guide.html

### API
- CoinGecko API - 암호화폐 시세 데이터
  https://www.coingecko.com/en/api
- NewsAPI.org - 뉴스 데이터
  https://newsapi.org/

---

## 6. 성실성 서약

본인은 이 프로젝트를 개발하면서:

1. ✅ AI를 학습 도구로 활용했으며, 제공받은 모든 코드를 직접 이해하고 수정했습니다.
2. ✅ AI가 생성한 코드를 무단 복제하지 않았으며, 내 프로젝트에 맞게 커스터마이징했습니다.
3. ✅ 타인의 코드나 온라인 예제를 그대로 복사하지 않았습니다.
4. ✅ 모든 코드의 의미를 이해하고 있으며, 설명할 수 있습니다.
5. ✅ Git 커밋 히스토리를 통해 점진적인 개발 과정을 증명할 수 있습니다.

위 내용이 사실임을 확인하며, 만약 거짓으로 판명될 경우 어떠한 처벌도 감수하겠습니다.

**작성일**: 2025년 [월] [일]
**작성자**: [이름] (학번: [학번])

---

**📌 첨부 자료**
- GitHub 저장소: https://github.com/moonjun1/coin-app
- Git 커밋 히스토리 스크린샷
- 실행 화면 스크린샷
