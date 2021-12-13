package com.zhengyuan.thefinalproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var taskListView:ListView
    private var dataStorage:Storage = Storage(emptyList())
    private var myCustomAdapter:TaskAdapter = TaskAdapter(this,dataStorage)
    private lateinit var projectTitle:TextView
    private lateinit var fab:FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        taskListView = findViewById(R.id.taskList)
        projectTitle = findViewById(R.id.projectTitle)

        fab = findViewById(R.id.addTaskFAB)
        //when fab is clicked, "add new task" event will happen.
        fab.setOnClickListener {
            //Start DetailActivity.
            //Put current size of task list in intent.(the size will be used for assigning an id for the new task)
            val intent = Intent(this,DetailActivity::class.java)
            intent.putExtra("currentItemCount",dataStorage.taskList.size)
            startActivityForResult(intent,1)
        }

        //Bind the TaskAdapter object to the taskListView so that it displays data held by this adapter
        //Also, pass a dataStore object into the adapter as its data source.
        myCustomAdapter = TaskAdapter(this,dataStorage)
        taskListView.adapter = myCustomAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Task is a costumed class so I added a Kotlin plugin provided by kotlin official
        //Citation:https://developer.android.com/kotlin/parcelize
        outState.putParcelable("dataStorage", dataStorage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        //Task is a  class so I added a Kotlin plugin to parcelize the object
        //Citation:https://developer.android.com/kotlin/parcelize
        val unwindedDataStorage = savedInstanceState.getParcelable<Storage>("dataStorage")
        unwindedDataStorage?.let {
            dataStorage = unwindedDataStorage
            //Initialize listview again so that the task list still preserves after configuration change .
            myCustomAdapter = TaskAdapter(this,dataStorage)
            taskListView.adapter = myCustomAdapter
        }
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_CANCELED &&requestCode == 1){
            //responseCode helps program differentiate which sender starts DetailActivity
            //responseCode 1 =>the fab starts the DetailActivity
            //responseCode else=>the EDIT button of a task starts the DetailActivity
            val responseCode = data?.getIntExtra("response_code",-1)
            if(responseCode == -1){
                throw Exception("Response code cannot be -1")
            }
            //retrieve the returned task object
            val item = data?.getParcelableExtra<Task>("TaskObject")
            //What if the user clicks the "complete" checkbox on DetailActivity page?
            //I need to handle this case as well
            val complete = data?.getBooleanExtra("complete",false)
            item?.let {
                when(complete){
                    true-> dataStorage.deleteTask(it)
                    false-> {
                        when(responseCode){
                            1-> dataStorage.addTask(it)
                            else-> dataStorage.modifyTask(it)
                        }
                    }
                    else -> {throw Exception("unreachable code part has reached.")}
                }
                //Update the listview again
                myCustomAdapter = TaskAdapter(this,dataStorage)
                taskListView.adapter = myCustomAdapter
                if(dataStorage.taskList.isEmpty()){
                    projectTitle.text = getString(R.string.nothing)
                }
                else{
                    projectTitle.text = getString(R.string.ToDO)
                }
            }

        }
    }
    //Costumed Adapter: https://www.youtube.com/watch?v=P2I8PGLZEVc
    //inner class & Kotlin documentation link:https://kotlinlang.org/docs/this-expressions.html
    inner class TaskAdapter(private val context: Context, val taskStorage:Storage): BaseAdapter() {

        override fun getItem(position: Int): Any {
            return taskStorage.getTask(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("ViewHolder")//IDE has suggested a newer way handling the same case. I have no clue how that way works.

        //Override the getView method. this method defines how the adapter formats the layout of each row and its internal data structure.
        //this method is like the render() method in React
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            //Get a reference to the data structure of the row layout, and inflate(insert) it into the main layout
            val taskRow = LayoutInflater.from(context).inflate(R.layout.row, parent, false)

            //Once the edit action on the keyboard has finished, immediately update the corresponding item so that the new task name still exists
            //after configuration change(Rotation)
            taskRow.findViewById<TextView>(R.id.taskName).setOnEditorActionListener { v, _, event ->
                event.let {
                    //Kotlin documentation link:https://developer.android.com/reference/android/widget/TextView.OnEditorActionListener
                    (this.getItem(position) as Task).taskName = (v.text).toString()
                    Log.i("task", (this.getItem(position) as Task).taskName)
                    //Hide keyboard
                    //https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    this.notifyDataSetChanged()
                }
                //dismiss the keyboard
                true
            }

            //This initializes the taskName on each task row with the data from the taskStorage object.
            taskRow.findViewById<TextView>(R.id.taskName).append((this.getItem(position) as Task).taskName as CharSequence)

            //Start an intent and go to DetailActivity with data for this specific row
            taskRow.findViewById<Button>(R.id.detailButton).setOnClickListener {
                (this.getItem(position) as Task).let {
                    val newIntent= Intent(context,DetailActivity::class.java)
                    newIntent.putExtra("taskItem",it)
                    //bind MainActivity to "this" scope
                    this@MainActivity.startActivityForResult(newIntent,1)
                }
            }

            //set dueDate shown on each row
            taskRow.findViewById<TextView>(R.id.dueDate).text = (this.getItem(position) as Task).taskDate

            //Delete a task and render an updated list view
            //Those online tutorials I have seen all reply on an onItemClickListener method, which does not work in my attempts anyway.
            //I finally figured out I can set up a call back function here and use the notifyDataSetChanged() inherited from BaseAdapter class!
            val completeBox = taskRow.findViewById<CheckBox>(R.id.checkBox)
            completeBox.setOnClickListener {
                Handler().postDelayed({//Wait the checkbox animation have finished
                    (this.getItem(position) as Task).let {
                        taskStorage.deleteTask(it)
                        if(taskStorage.taskList.isEmpty()){
                            //This line merely sets the title of MainAcitivty to be "Nothing to do"
                            this@MainActivity.projectTitle.text = getString(R.string.nothing)
                        }
                        this.notifyDataSetChanged()//Tells the adapter to render the updated list content.(Like how React works)
                    }
                }, 200)
            }


            //After setting up everything I need for a task row to work, pass the task row object back.
            return taskRow

        }

        override fun getCount(): Int {
            return taskStorage.taskList.size
        }

    }

}

