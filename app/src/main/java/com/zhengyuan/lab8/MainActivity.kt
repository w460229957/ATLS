package com.zhengyuan.lab8

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.zhengyuan.lab8.databinding.ActivityMainBinding
import android.content.Intent
import android.net.Uri
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appleButton:Button
    private lateinit var pearButton: Button
    private lateinit var mangoButton: Button
    private var labStorage:Storage = Storage(emptyMap())
    private val requestcode = 9
    private lateinit var detailButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.fab.setOnClickListener { view ->
                val url: String = labStorage.getUniversalUrl()
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.data = url.let{ Uri.parse(url)}
                startActivity(intent)
                Log.e("implicit intent","executed")
        }
        /*my code here*/
        appleButton = findViewById(R.id.buttonApple)
        pearButton = findViewById(R.id.buttonPear)
        mangoButton = findViewById(R.id.buttonMango)
        detailButton = findViewById(R.id.buttonFirst)

        appleButton.setOnClickListener{
            val intent = Intent(this, Detail::class.java)
            intent.putExtra("fruit","apple")
            intent.putExtra("comment",labStorage.getComments("apple"))
            labStorage.getComments("apple")?.let{intent.putExtra("comment",labStorage.getComments("apple"))}
            startActivityForResult(intent,requestcode)
            Log.i("bugCheckone","hahahahahah")
        }

        pearButton.setOnClickListener{
            val intent = Intent(this, Detail::class.java)
            intent.putExtra("fruit","pear")
            labStorage.getComments("pear")?.let{intent.putExtra("comment",labStorage.getComments("pear"))}
            startActivityForResult(intent,requestcode)
        }

        mangoButton.setOnClickListener{
            val intent = Intent(this,Detail::class.java)
            intent.putExtra("fruit","mango")
            labStorage.getComments("mango")?.let{intent.putExtra("comment",labStorage.getComments("mango"))}
            startActivityForResult(intent,requestcode)
        }

        detailButton.setOnClickListener{
            val intent = Intent(this,Detail::class.java)
            intent.putExtra("fruit","intro")
            intent.putExtra("comment",labStorage.getCommenIntro())
            startActivityForResult(intent,requestcode)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("apple",labStorage.getComments("apple"))
        outState.putString("pear",labStorage.getComments("pear"))
        outState.putString("mango",labStorage.getComments("mango"))
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString("apple")?.let { labStorage.setComments("apple", it) }
        savedInstanceState.getString("pear")?.let{labStorage.setComments("pear",it)}
        savedInstanceState.getString("mango")?.let{labStorage.setComments("mango",it)}

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 9 && resultCode == RESULT_OK) {
            val fruit: String = data?.getCharSequenceExtra("fruit").toString()
            val comment: String = data?.getCharSequenceExtra("comment").toString()
            if(fruit != "intro") {
                labStorage.setComments(fruit, comment)
            }
        }
        else{
            Log.e("Request Failed","the result code is either null or wrong")
        }
    }
}