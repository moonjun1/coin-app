package com.example.fragmentapp.fragments

import android.app.AlertDialog
import com.example.fragmentapp.R
import com.example.fragmentapp.api.RetrofitClient
import com.example.fragmentapp.utils.FavoriteManager
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class CryptoDetailFragment : Fragment() {

    private var cryptoId: String? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crypto_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cryptoId?.let { id ->
            loadCryptoDetail(id, view)
            loadPriceChart(id, view)
            setupFavoriteButton(id, view)
            setupMemoButton(id, view)
        }
    }

    private fun setupFavoriteButton(id: String, view: View) {
        val fabFavorite = view.findViewById<FloatingActionButton>(R.id.fab_favorite)

        updateFavoriteIcon(fabFavorite, id)

        fabFavorite.setOnClickListener {
            val isFavorite = FavoriteManager.toggleFavorite(requireContext(), id)
            updateFavoriteIcon(fabFavorite, id)

            val message = if (isFavorite) "즐겨찾기에 추가되었습니다" else "즐겨찾기에서 제거되었습니다"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFavoriteIcon(fab: FloatingActionButton, id: String) {
        val isFavorite = FavoriteManager.isFavorite(requireContext(), id)
        val icon = if (isFavorite) {
            android.R.drawable.star_big_on
        } else {
            android.R.drawable.star_big_off
        }
        fab.setImageResource(icon)
    }

    private fun loadCryptoDetail(id: String, view: View) {
        lifecycleScope.launch {
            try {
                val detail = RetrofitClient.api.getCryptoDetail(id)

                view.findViewById<TextView>(R.id.coin_name).text = detail.name
                view.findViewById<TextView>(R.id.coin_symbol).text = detail.symbol.uppercase()
                view.findViewById<TextView>(R.id.market_cap_rank).text =
                    "시가총액 순위: ${detail.market_cap_rank ?: "N/A"}"

                val currentPrice = detail.market_data.current_price["krw"] ?: 0.0
                view.findViewById<TextView>(R.id.current_price).text =
                    formatPrice(currentPrice)

                val change24h = detail.market_data.price_change_percentage_24h ?: 0.0
                val change7d = detail.market_data.price_change_percentage_7d ?: 0.0
                val change30d = detail.market_data.price_change_percentage_30d ?: 0.0

                view.findViewById<TextView>(R.id.price_change_24h).apply {
                    text = "24시간: ${formatPercentage(change24h)}"
                    setTextColor(getColorForChange(change24h))
                }

                view.findViewById<TextView>(R.id.price_change_7d).apply {
                    text = "7일: ${formatPercentage(change7d)}"
                    setTextColor(getColorForChange(change7d))
                }

                view.findViewById<TextView>(R.id.price_change_30d).apply {
                    text = "30일: ${formatPercentage(change30d)}"
                    setTextColor(getColorForChange(change30d))
                }

                val marketCap = detail.market_data.market_cap["krw"] ?: 0L
                val volume = detail.market_data.total_volume["krw"] ?: 0L
                val high24h = detail.market_data.high_24h["krw"] ?: 0.0
                val low24h = detail.market_data.low_24h["krw"] ?: 0.0
                val ath = detail.market_data.ath["krw"] ?: 0.0
                val atl = detail.market_data.atl["krw"] ?: 0.0

                view.findViewById<TextView>(R.id.market_cap).text =
                    "시가총액: ${formatPrice(marketCap.toDouble())}"
                view.findViewById<TextView>(R.id.total_volume).text =
                    "거래량: ${formatPrice(volume.toDouble())}"
                view.findViewById<TextView>(R.id.high_24h).text =
                    "24시간 최고가: ${formatPrice(high24h)}"
                view.findViewById<TextView>(R.id.low_24h).text =
                    "24시간 최저가: ${formatPrice(low24h)}"
                view.findViewById<TextView>(R.id.ath).text =
                    "역대 최고가: ${formatPrice(ath)}"
                view.findViewById<TextView>(R.id.atl).text =
                    "역대 최저가: ${formatPrice(atl)}"

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "상세 정보를 불러오는데 실패했습니다: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun formatPrice(price: Double): String {
        val formatter = DecimalFormat("#,###")
        return "₩${formatter.format(price)}"
    }

    private fun formatPercentage(value: Double): String {
        return String.format("%.2f%%", value)
    }

    private fun getColorForChange(change: Double): Int {
        return if (change >= 0) {
            android.graphics.Color.parseColor("#FF1744")
        } else {
            android.graphics.Color.parseColor("#2196F3")
        }
    }

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

                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        setDrawLabels(false)
                    }

                    axisLeft.apply {
                        setDrawGridLines(true)
                        gridColor = Color.LTGRAY
                    }

                    axisRight.isEnabled = false

                    animateX(1000)
                    invalidate()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "차트 데이터를 불러오는데 실패했습니다: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupMemoButton(id: String, view: View) {
        val btnAddMemo = view.findViewById<Button>(R.id.btn_add_memo)
        val btnDeleteMemo = view.findViewById<Button>(R.id.btn_delete_memo)
        val memoText = view.findViewById<TextView>(R.id.memo_text)

        loadMemo(id, memoText, btnAddMemo, btnDeleteMemo)

        btnAddMemo.setOnClickListener {
            showMemoDialog(id, memoText, btnAddMemo, btnDeleteMemo)
        }

        btnDeleteMemo.setOnClickListener {
            showDeleteMemoDialog(id, memoText, btnAddMemo, btnDeleteMemo)
        }
    }

    private fun loadMemo(id: String, memoText: TextView, btnAddMemo: Button, btnDeleteMemo: Button) {
        val memo = FavoriteManager.getMemo(requireContext(), id)
        if (memo.isNotEmpty()) {
            memoText.text = memo
            btnAddMemo.text = "수정"
            btnDeleteMemo.visibility = View.VISIBLE
        } else {
            memoText.text = "메모를 추가하려면 위 버튼을 눌러주세요."
            btnAddMemo.text = "추가"
            btnDeleteMemo.visibility = View.GONE
        }
    }

    private fun showMemoDialog(id: String, memoText: TextView, btnAddMemo: Button, btnDeleteMemo: Button) {
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
                    loadMemo(id, memoText, btnAddMemo, btnDeleteMemo)
                    Toast.makeText(
                        context,
                        if (isEdit) "메모가 수정되었습니다" else "메모가 추가되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(context, "메모를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    private fun showDeleteMemoDialog(id: String, memoText: TextView, btnAddMemo: Button, btnDeleteMemo: Button) {
        AlertDialog.Builder(requireContext())
            .setTitle("메모 삭제")
            .setMessage("이 메모를 삭제하시겠습니까?")
            .setPositiveButton("삭제") { dialog, _ ->
                FavoriteManager.deleteMemo(requireContext(), id)
                loadMemo(id, memoText, btnAddMemo, btnDeleteMemo)
                Toast.makeText(context, "메모가 삭제되었습니다", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }
}
