package top.berthua

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.contact.Group

import java.util.ArrayList

object MyData : AutoSavePluginConfig("config"){
    var list : MutableList<String> by value(mutableListOf("a","b"))
    var keywords : MutableList<String> by value(mutableListOf())
    var qq : Long by value()
    var password :String by value()
    var groups : MutableList<Long> by value()
    var m : Int by value()
    var d : Int by value()
    var h : Int by value()
    var min : Int by value()
    var s : Int by value()
}