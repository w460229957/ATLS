package com.zhengyuan.lab5

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    private var switch = 0
    @SuppressLint("SetTextI18n")
    fun showPicture(view: View) {
        val image = findViewById<ImageView>(R.id.mainImage)
        val character = findViewById<TextView>(R.id.character)
        val userInput = findViewById<EditText>(R.id.personName).text
        if(switch == 0){
            switch = 1
            image.setImageResource(R.drawable.monkey)
            if(userInput.isEmpty()){
                character.text = "Monkey is play with nothing"
            }
            else{
                character.text = "Monkey is playing with $userInput"
             }


        }
        else{
            switch = 0
            image.setImageResource(R.drawable.unknown)
            if(userInput.isEmpty()){
                character.text = "Unknown is fighting with nothing"
            }
            else{
                character.text = "Unknown is fighting with $userInput"
            }

        }





    }
}