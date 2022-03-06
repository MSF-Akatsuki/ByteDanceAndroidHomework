package com.bytedance.jstu.homework


const val Mod=65537
const val Mul=13331

class ProblemContents {

    companion object {
        val tagsMap : List<String> = listOf("DP","字符串","模拟","贪心","搜索","DS","图论","几何","多项式","组合","博弈论")
    }

    fun getSize()=100

    fun getAbstractById(id : Int) : ProblemAbstract {
        val title = "这是第${id}个标题"

        var rngVal = id * id % Mod * Mul % Mod
        val tags: MutableList<String> = mutableListOf()
        for (i in (1..(rngVal%5+1))){
            rngVal = Mul * rngVal % Mod
            tags.add(tagsMap[rngVal % tagsMap.size])
        }
        rngVal = Mul * rngVal % Mod
        return  ProblemAbstract(title,tags,rngVal)
    }

    fun getContentById(id : Int) : ProblemContent {

        val title = "这是第${id}个标题"

        var rngVal = id * id % Mod * Mul % Mod
        val tags: MutableList<String> = mutableListOf()
        for (i in (1..(rngVal%5+1))){
            rngVal = Mul * rngVal % Mod
            tags.add(tagsMap[rngVal % tagsMap.size])
        }
        rngVal = Mul * rngVal % Mod
        val num = rngVal
        rngVal = Mul * rngVal % Mod

        val contents : MutableList<Pair<String,String>> = mutableListOf()

        for (i in (1..rngVal%9+1)) {
            rngVal = Mul * rngVal % Mod
            contents.add(
                Pair("第${id}题的第${i}个小标题",
                    "这是这节的题目内容，用a填充"+(1..rngVal%293).map { i -> 'a' }.joinToString(""))
            )
        }

        return  ProblemContent(title,tags,num,contents)
    }

    class ProblemAbstract (title: String,tagsId: List<String>, num : Int) {
        val title : String = title
        val tags : List<String> = tagsId
        val numPassed : Int = num
    }

    class ProblemContent (title : String, tags : List<String>, num : Int, contents: List<Pair<String,String>>) {
        val title = title
        val tags = tags
        val numPassed : Int = num
        val contents = contents
    }

}