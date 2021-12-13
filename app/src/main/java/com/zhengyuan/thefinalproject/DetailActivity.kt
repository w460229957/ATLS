package com.zhengyuan.thefinalproject

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class DetailActivity : AppCompatActivity() {
    private lateinit var taskTitle:EditText
    private lateinit var dueDate:CalendarView
    private lateinit var selectedTime:String
    private lateinit var fab:FloatingActionButton
    private var responseCode = -1
    private lateinit var newTask:Task
    private lateinit var complete:CheckBox

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        taskTitle = findViewById(R.id.subTaskTitle)
        dueDate = findViewById(R.id.calendarView)
        complete = findViewById(R.id.subComplete)
        //DetailActivity may start for two reasons.
        //First, FAB in MainActivity=>the intent has not extra data
        //Second, EDIT button of a row in the listview of MainActivity=>the intent has some extra data(That is, the taskitem itself).


        //The fab click event does the same thing as onBackPressed()
        fab = findViewById(R.id.fakeBackButton)

        fab.setOnClickListener {
            this.wrapData()
            finish()
        }
        val formatter = java.text.SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
        //Initialize the calender view as current time
        dueDate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = Calendar.getInstance()
            date.set(year,month,dayOfMonth)
            selectedTime = formatter.format(date.time)
            dueDate.setDate(date.time.time,true,true)
        }
        //Citation:https://stackoverflow.com/questions/47006254/how-to-get-current-local-date-and-time-in-kotlin
        val date = Calendar.getInstance().time
        selectedTime = formatter.format(date)
        dueDate.setDate(date.time,true,true)

        //If this activity is triggered by FAB in the MainActivity
        //Then currentItemCount must not be -1
        val count = intent.getIntExtra("currentItemCount",-1)
        //The count variable also helps I assign a task object a new identifier.
        responseCode = if(count == -1){
            newTask = intent.getParcelableExtra("taskItem")!!
            taskTitle.append(newTask.taskName)
            newTask.reservedDate.let {
                dueDate.setDate(it,true,true)
            }
            selectedTime = newTask.taskDate
            2//Response code:else
        } else{
            newTask = Task("",selectedTime,dueDate.date, emptyList(),count+1)
            1//Response code:1
        }
        //DetailActivity also has a listview for the substeps of a task
        var myadapter = SubStepAdapter(this,newTask.subSteps)
        val stepList:ListView = findViewById(R.id.subStepList)
        stepList.adapter = myadapter

        findViewById<Button>(R.id.subStepButton).setOnClickListener{
            newTask.subSteps = newTask.subSteps.filter { each->each.step != "" }
            newTask.subSteps = newTask.subSteps.plus(Substep("",newTask.subSteps.size+1))
            myadapter = SubStepAdapter(this,newTask.subSteps)
            stepList.adapter = myadapter
        }

        //Dismiss keyboard when user types "enter" key on its keyboard
        //https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin
        taskTitle.setOnEditorActionListener { v, _, event ->
            event.let{
                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            false
        }


    }

    override fun onBackPressed() {
        wrapData()
        super.onBackPressed()
        finish()
    }

    private fun wrapData(){
        val data = Intent()
        if(taskTitle.text.isNotEmpty()){
            newTask.subSteps = newTask.subSteps.filter { each -> each.step != "" }
            if(complete.isChecked){
                data.putExtra("complete",true)
            }
            data.putExtra("response_code",responseCode)
            newTask.taskName = taskTitle.text.toString()
            newTask.taskDate = selectedTime
            newTask.reservedDate = dueDate.date
            //Add the task object
            data.putExtra("TaskObject",newTask)
            setResult(Activity.RESULT_OK,data)
        }
        else{
            //user clicks return button when the text title is empty
            setResult(Activity.RESULT_CANCELED,data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("task",newTask)
        outState.putCharSequence("taskTitle",taskTitle.text)
        outState.putString("taskDate",selectedTime)
        outState.putLong("reservedDate",dueDate.date)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        newTask = savedInstanceState.getParcelable("task")!!
        taskTitle.text = savedInstanceState.getCharSequence("taskTitle") as Editable?
        selectedTime = savedInstanceState.getString("taskDate").toString()
        dueDate.date = savedInstanceState.getLong("reservedDate")
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<ListView>(R.id.subStepList).adapter = SubStepAdapter(this,newTask.subSteps)
    }
    //this is an inner class
    inner class SubStepAdapter(private val context:Context, var subSteps: List<Substep>):BaseAdapter(){
        override fun getCount(): Int {
            return subSteps.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return subSteps[position]
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val root = LayoutInflater.from(context).inflate(R.layout.substep,parent,false)
            val radioButton:RadioButton = root.findViewById(R.id.radioButton)

            radioButton.setOnClickListener{
                Handler().postDelayed({//Wait the radiobutton animation have finished
                        subSteps = subSteps.filter { step -> step.id != (getItem(position) as Substep).id}
                        this@DetailActivity.newTask.subSteps = subSteps//Call newTask variable in the outer class.
                        this.notifyDataSetChanged()//Tells the adapter to render the updated list content.(Like how React works)
                }, 200)
            }


            val stepField = root.findViewById<EditText>(R.id.step)
            stepField.append(subSteps[position].step)

            root.findViewById<EditText>(R.id.step).setOnEditorActionListener { v, _, event ->
                event.let {
                    subSteps[position].step = v.text.toString()
                    subSteps = subSteps.filter { stepField -> stepField.step != "" }
                    this.notifyDataSetChanged()
                    //Hide keyboard
                    //https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin
                    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
               true
            }

            return root
        }
    }
}