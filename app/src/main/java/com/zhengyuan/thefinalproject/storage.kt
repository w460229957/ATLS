package com.zhengyuan.thefinalproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Task(var taskName:String, var taskDate: String, var reservedDate:Long, var subSteps:List<Substep>,var id:Int): Parcelable {

}

@Parcelize
data class Substep(var step:String,var id:Int):Parcelable

@Parcelize
class Storage(var taskList:List<Task>):Parcelable{
     fun addTask(newTask:Task){
        if(taskList.find { task -> task.taskName == newTask.taskName && task.taskDate == newTask.taskDate } == null){
            taskList = taskList.plus(newTask)
            taskList = sort()
        }
     }

    fun getTask(position:Int):Task{
        return taskList[position]
    }

    fun deleteTask(whichTask:Task){
        taskList = taskList.filter { task -> task.id != whichTask.id }
    }

    fun modifyTask(whichTask: Task){
        taskList = taskList.map { each ->
            when(each.id == whichTask.id){
                true -> whichTask
                false ->each
            }
        }
        taskList = sort()
    }

    private fun sort():List<Task>{
        return taskList.sortedBy { task -> task.taskDate }
    }

}
