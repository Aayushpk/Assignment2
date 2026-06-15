package com.example.assignment1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment1.data.model.Entity


class DetailsActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        @Suppress("DEPRECATION") // getSerializableExtra(name) is fine for minSdk 24
        val entity = intent.getSerializableExtra(EXTRA_ENTITY) as? Entity

        val ticker = findViewById<TextView>(R.id.detailTicker)
        val assetType = findViewById<TextView>(R.id.detailAssetType)
        val price = findViewById<TextView>(R.id.detailPrice)
        val yield = findViewById<TextView>(R.id.detailYield)
        val desc = findViewById<TextView>(R.id.detailDescription)
        entity?.let {
            ticker.text = it.ticker
            assetType.text = it.assetType
            price.text = "$${it.currentPrice}"
            yield.text = "${it.dividendYield}%"
            desc.text = it.description
        }
    }

    companion object {
        const val EXTRA_ENTITY = "extra_entity"
    }
}
