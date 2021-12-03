package com.zhengyuan.lab8

data class Storage(var userComments:Map<String,String>){

      fun setComments(whichFruit:String,inputComment:String){
            userComments = userComments.plus(Pair(whichFruit,inputComment))
            //Source:Kotlin official documentation:Map
      }

      fun getComments(whichFruit: String):String?{
            return userComments[whichFruit]
      }

      fun getUniversalUrl():String{
          return "https://en.wikipedia.org/wiki/Fruit"
      }

      fun getCommenIntro():String{
            return "A fruit is a fruit"
      }

}
