package com.example.fragmentapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

/**
 * 암호화폐 RecyclerView 어댑터
 *
 * 학습 포인트:
 * 1. RecyclerView.Adapter 상속
 * 2. ViewHolder 패턴
 * 3. 데이터 바인딩 (가격 포맷팅, 색상 처리)
 */
class CryptoAdapter(
    private val cryptoList: List<Crypto>
) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {

    /**
     * ViewHolder: 각 아이템의 뷰를 보관
     */
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

        // 기본 정보 설정
        holder.symbolText.text = crypto.symbol.uppercase()
        holder.nameText.text = crypto.name

        // 가격 포맷팅 (천단위 콤마)
        val priceFormat = DecimalFormat("#,###")
        holder.priceText.text = "₩${priceFormat.format(crypto.current_price)}"

        // 변동률 포맷팅 및 색상 설정
        val change = crypto.price_change_percentage_24h
        val changeFormat = DecimalFormat("+0.00;-0.00")
        holder.changeText.text = "${changeFormat.format(change)}%"

        // 상승: 빨강, 하락: 파랑
        if (change >= 0) {
            holder.changeText.setTextColor(Color.RED)
        } else {
            holder.changeText.setTextColor(Color.BLUE)
        }
    }

    override fun getItemCount() = cryptoList.size
}
