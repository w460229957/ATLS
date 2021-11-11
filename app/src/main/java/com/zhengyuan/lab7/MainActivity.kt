package com.zhengyuan.lab7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var submitBottom:Button
    private lateinit var radioGroup:RadioGroup
    private lateinit var title:TextView
    private lateinit var introduction:TextView
    private lateinit var cat:RadioButton
    private lateinit var dog:RadioButton
    private lateinit var cheater:Spinner
    private lateinit var result:TextView
    private lateinit var breed1:CheckBox
    private lateinit var breed2:CheckBox
    private lateinit var breed3:CheckBox
    private lateinit var hair:SwitchCompat
    private lateinit var hairdetail:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        submitBottom = findViewById(R.id.submitButton)
        radioGroup = findViewById(R.id.theGroup)
        title = findViewById(R.id.header)
        introduction = findViewById(R.id.intro)
        cat = findViewById(R.id.cat)
        dog = findViewById(R.id.dog)
        cheater = findViewById(R.id.cheater)
        result = findViewById(R.id.result)
        breed1 = findViewById(R.id.checkBox1)
        breed2 = findViewById(R.id.checkBox2)
        breed3 = findViewById(R.id.checkBox3)
        hair = findViewById(R.id.hairType)
        hairdetail = findViewById(R.id.hairdetail)
        hair.setOnCheckedChangeListener { self, isChecked ->
            run {
                if (isChecked) {
                    val temp: CharSequence = "Long Hair"
                    hairdetail.text = temp
                } else {
                    val temp: CharSequence = "Short Hair"
                    hairdetail.text = temp
                }
            }
        }
    }
    fun showResult(view:View){
        val catOrdogSelected = radioGroup.checkedRadioButtonId
        val isCheated = cheater.selectedItem
        when {
            isCheated  == "I love both animals" -> {
                val temp:CharSequence = "Of course you love them"
                result.text = temp
            }
            catOrdogSelected != -1 -> {
                result.text = ""
                var which = ""
                var breed = ""
                Log.e("catOrDog", "$catOrdogSelected")
                which = findViewById<RadioButton>(catOrdogSelected).text.toString()
                which += "s"
                var flag = 0
                if(breed1.isChecked){
                    breed += "breed1"
                    flag = 1
                }
                if(breed2.isChecked){
                    breed += " breed2"
                    flag = 1
                }
                if(breed3.isChecked) {
                    breed += " breed3"
                    flag = 1
                }
                when (flag) {
                    1 -> {
                        when{
                            hair.isChecked ->{
                                val temp:CharSequence =  "You like $which and their breeds. For example, $breed. they(it) have(has) long hair"
                                result.text = temp
                            }
                            else ->{
                                val temp:CharSequence =  "You like $which and their breeds. For example, $breed. they(it) have(has) short hair"
                                result.text = temp
                            }

                        }
                    }
                    0 -> {
                        val temp:CharSequence =  "You like $which and their breeds $breed. they have long or short hair"
                        result.text = temp
                    }
                }
            }
            else -> {
                Snackbar.make(findViewById(R.id.rootLayout),"You must choose cat or dog, or 'I love everyone' option in the spinner",Snackbar.LENGTH_SHORT).show()

            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("Title", title.text as String?)
        outState.putString("intro",introduction.text as String?)
        outState.putString("cat",cat.text as String?)
        outState.putString("dog",dog.text as String?)
        outState.putString("result",result.text as String?)
        super.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        title.text = savedInstanceState.get("Title") as CharSequence?
        introduction.text = savedInstanceState.get("intro") as CharSequence?
        cat.text = savedInstanceState.get("cat") as CharSequence?
        dog.text = savedInstanceState.get("dog") as CharSequence?
        result.text = savedInstanceState.get("result") as CharSequence?
        super.onRestoreInstanceState(savedInstanceState)
    }
}