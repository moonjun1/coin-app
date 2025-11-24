package com.example.fragmentapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

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

        val priceFormat = DecimalFormat("#,###")
        holder.priceText.text = "₩${priceFormat.format(crypto.current_price)}"

        val change = crypto.price_change_percentage_24h
        val changeFormat = DecimalFormat("+0.00;-0.00")
        holder.changeText.text = "${changeFormat.format(change)}%"

        if (change >= 0) {
            holder.changeText.setTextColor(Color.RED)
        } else {
            holder.changeText.setTextColor(Color.BLUE)
        }

        holder.itemView.setOnClickListener {
            onItemClick(crypto.id)
        }
    }

    override fun getItemCount() = cryptoList.size
}
