package com.zhengyuan.lab8

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.zhengyuan.lab8.databinding.ActivityDetailBinding

class Detail : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var heading:TextView
    private lateinit var userInput:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar2)
        heading = findViewById(R.id.detailTitle)
        userInput = findViewById(R.id.input)
        heading.text = intent.getStringExtra("fruit")
        userInput.setText(intent.getStringExtra("comment"))
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("fruit",heading.text)
        intent.putExtra("comment",userInput.text)
        setResult(RESULT_OK,intent)
        super.onBackPressed()
    }
}