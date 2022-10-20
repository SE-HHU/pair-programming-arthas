# Intermediate four arithmetic operations
----
## Member
* 2106050209 王雨乐
* 2106050223 杨祁元
----

## 功能模块

|模块|作用|
| ------------ | ------------------------------ |
|功能选择模块|选择题目生成功能和评分功能|
|题目生成模块|根据选择模块获取的参数得到随机数字、运算符|
|统一化模块|将所有的操作数化为分数处理，操作符格式化|
|运算法则模块|定义分数运算法则            |
|答案生产模块|计算随机得到的题目的结果  |
|化简模块 | 将所得分数进行约分化简 |
|查重模块 | 检查新生成的题目是否与已生成题目集重复 |
|评分模块 | 获取指定文件的正确错误题目个数及题号，重复题目，结果输入新文件|

## 具体代码实现

* 实现类简介
```
//分数类(操作数)
class Number{...}
//运算符类
class Operation{...}
//四则运算类
public class Intermediate_four_arithmetic_operations{...}
```
* 分数类实现
```
int numerator//分子
int denominator//分母

//整数构造方法
public Number(int numerator){
    this.numerator = numerator;
    this.denominator = 1;
    }
    
//分数构造方法
public Number(int numerator, int denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
    //保证负号在分子上
    if(this.denominator<0){
        this.numerator *= -1;
        this.denominator *= -1;
    }
}
//重写toString()方法
public String toString() {
    if(this.denominator == 1){
        return numerator+"";
    }
    else return numerator+"/"+denominator;
}
```
* 运算法则
```
//分数加减乘除
public static Number add(Number num1,Number num2)
public static Number sub(Number num1,Number num2)
public static Number mul(Number num1,Number num2)
public static Number div(Number num1,Number num2)

//返回两个数最大公约数
public static int gcd(int num1,int num2)

//分数化简
public static Number simplify(Number num)
```
* 结果运算
```
public static Number getResult(String q)

//运算符栈
Stack<Operation> OpeStack = new Stack<>()
//操作数栈
Stack<Number> NumStack = new Stack<>()

//设置运算符及其优先级大小
HashMap<String,Integer> OpsAndPri = new HashMap<>()
OpsAndPri.put("+",1)
OpsAndPri.put("-",1)
OpsAndPri.put("×",2)
OpsAndPri.put("÷",2)

//设置括号对优先级产生的增量
int pri = 0

//将题目拆分
String[] arr = q.split(" ")
//遍历，将对应类型压栈
for(String s:arr)

//将题目中的括号作为优先级变化因素
if(s.equals("(")) pri += 1
else if(s.equals(")")) pri -= 1

//利用运算符栈单调的特性反复压栈出栈
NumStack.push(num)
OpeStack.push(ope)

Number sq2 = NumStack.pop()
Number sq1 = NumStack.pop()
Operation op = OpeStack.pop()
```
* 题目查重
```
public static boolean JudgeRepetition(String q,String a,HashMap<String,String> Exes)

//双重判别法
//1.比较答案是否有重复
if(Exes.containsValue(a))

//2.比较题目是否有相同的操作数和运算符
if(!s.equals("(")&&!s.equals(")")&&!_qs[i].toString().contains(s))
```
