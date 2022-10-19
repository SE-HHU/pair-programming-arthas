import java.io.*;
import java.util.*;

//操作数类

public class Intermediate_four_arithmetic_operations {
    public static void main(String[] args) {
        Func_Selection();
    }

    //功能选择

    public static void Func_Selection(){
        Scanner put = new Scanner(System.in);
        System.out.println("please input your choice:\n"+
                "1.Exercises Generation\n"+"2.grade analysis");
        int ch = put.nextInt();
        if(ch == 1){
            System.out.println("please input the number of Exercises you need：");
            int n = put.nextInt();
            String[] range = new String[3];
            System.out.println("please input the range of natural number(such as 1,100):");
            range[0] = put.next();
            System.out.println("please input the range of true fraction(such as 1/2,8/9):");
            range[1] = put.next();
            System.out.println("please input the range of the denominator of the true fraction(such as 2,20):");
            range[2] = put.next();
            System.out.println("please input whether the brackets are required(Y or N):");
            String withBrackets = put.next();
            HashMap<String,String> Exercises = ExeGeneration(n,range,withBrackets);
            ExeAndAnsTxtWrite(Exercises);
        }
        else{
            GradeTxtWrite();
        }
    }

    //题目生成
    public static HashMap<String,String> ExeGeneration(int n,String[] range,String withBrackets){
        Random in = new Random();
        //范围控制
        String[] lim1 = range[0].split(",");//自然数范围
        int start1 = Integer.parseInt(lim1[0]);
        int end1 = Integer.parseInt(lim1[1]);

        String[] lim2 = range[1].split(",");//真分数范围
        String[] sta = lim2[0].split("/");
        String[] end = lim2[1].split("/");
        double start2 = Integer.parseInt(sta[0])/Integer.parseInt(sta[1]);
        double end2 = Integer.parseInt(end[0])/Integer.parseInt(end[1]);

        String[] lim3 = range[2].split(",");//真分数分母范围
        int start3 = Integer.parseInt(lim3[0]);
        int end3 = Integer.parseInt(lim3[1]);

        String[] sign = {"+","-","×","÷"};
        HashMap<String,String> Exercises = new HashMap<>();//存放题目和答案
        for (int i = 1; i <= n; ++i) {//题目生成
            String q = "";

            int sign_num = in.nextInt(3) + 1;//运算符个数
            String[] s = new String[sign_num+1];//运算符数组
            for(int j = 0;j<sign_num;j++){//运算符生成
                s[j] = sign[in.nextInt(4)]+" ";
            }
            s[sign_num] = "";

            for(int k = 1;k<=sign_num+1;k++){//操作数生成并与运算符结合
                Number num;
                int flag = in.nextInt(2);
                if(flag == 0){//整数
                    int numerator = in.nextInt(end1-start1+1)+start1;
                    num = new Number(numerator);
                }
                else{//分数
                    int numerator,denominator;
                    do {
                        denominator = in.nextInt(end3 - start3 + 1) + start3;
                        numerator = in.nextInt(denominator - 1) + 1;
                        //约分一下 再判断条件
                        int factor = gcd(denominator,numerator);
                        denominator /= factor;
                        numerator /= factor;
                    }while((numerator/denominator<start2||numerator/denominator>end2)||(denominator<start3||denominator>end3));
                    num = new Number(numerator,denominator);
                }
                q += num+" "+s[k-1];
            }
//            if(withBrackets == "Y"){
//                //插入括号
//            }
            String a = getResult(q).toString();
            if(JudgeRepetition(q,a,Exercises)){
                n++;
            }
            else if(q.contains("÷")&&a.contains("/")){
                String[] fr = a.split("/");
                int num = Integer.parseInt(fr[0]);
                int den = Integer.parseInt(fr[1]);
                if(Math.abs(num)>Math.abs(den)){
                    n++;
                }else{
                    Exercises.put(q,a);
                }
            }
            else Exercises.put(q,a);
        }
        return Exercises;
    }

    //结果运算
    public static Number getResult(String q){//栈运算
        Stack<Operation> OpeStack = new Stack<>();//符栈
        Stack<Number> NumStack = new Stack<>();//数栈

        HashMap<String,Integer> OpsAndPri = new HashMap<>();//运算符及其优先级表
        OpsAndPri.put("+",1);
        OpsAndPri.put("-",1);
        OpsAndPri.put("×",2);
        OpsAndPri.put("÷",2);
        int pri = 0;//括号优先级增量

        String[] arr = q.split(" ");//分解题目
        for(String s:arr){
            if(Character.isDigit(s.charAt(0))){//是操作数
                Number num;
                if(s.contains("/")){
                    String[] fr = s.split("/");
                    num = new Number(Integer.parseInt(fr[0]),Integer.parseInt(fr[1]));
                }
                else{
                    num = new Number(Integer.parseInt(s));
                }
                NumStack.push(num);
            }
            else if(OpsAndPri.containsKey(s)){//是运算符
                Operation ope = new Operation(s,pri+OpsAndPri.get(s));
                //将ope与peek比较优先级，从数栈弹出两个数，符栈弹出一个运算符，结果存入数栈，反复比较
                while(!OpeStack.isEmpty() && ope.priority<=OpeStack.peek().priority){

                    Number sq2 = NumStack.pop();
                    Number sq1 = NumStack.pop();
                    Operation op = OpeStack.pop();
                    Number result;//结果
                    switch (op.toString()){
                        case "+":result = add(sq1,sq2);NumStack.push(result);break;
                        case "-":result = sub(sq1,sq2);NumStack.push(result);break;
                        case "×":result = mul(sq1,sq2);NumStack.push(result);break;
                        case "÷":result = div(sq1,sq2);NumStack.push(result);break;
                    }
                }
                OpeStack.push(ope);
            }
            else if(s.equals("(")){
                pri += 1;
            }
            else if(s.equals(")")){
                pri -= 1;
            }
        }
        while(!OpeStack.isEmpty()){
            Number result;
            Number sq2 = NumStack.pop();
            Number sq1 = NumStack.pop();
            Operation op = OpeStack.pop();
            switch (op.toString()){
                case "+":result = add(sq1,sq2);NumStack.push(result);break;
                case "-":result = sub(sq1,sq2);NumStack.push(result);break;
                case "×":result = mul(sq1,sq2);NumStack.push(result);break;
                case "÷":result = div(sq1,sq2);NumStack.push(result);break;
            }
        }
        return NumStack.pop();
    }

    //分数加减乘除
    public static Number add(Number num1,Number num2){
        int numerator = num1.numerator*num2.denominator+num1.denominator*num2.numerator;
        int denominator = num1.denominator*num2.denominator;
        Number sum = new Number(numerator,denominator);
        return simplify(sum);
    }

    public static Number sub(Number num1,Number num2){
        int numerator = num1.numerator*num2.denominator-num1.denominator*num2.numerator;
        int denominator = num1.denominator*num2.denominator;
        Number dif = new Number(numerator,denominator);
        return simplify(dif);
    }

    public static Number mul(Number num1,Number num2){
        int numerator = num1.numerator*num2.numerator;
        int denominator = num1.denominator*num2.denominator;
        Number pro = new Number(numerator,denominator);
        return simplify(pro);
    }

    public static Number div(Number num1,Number num2){
        int numerator = num1.numerator*num2.denominator;
        int denominator = num1.denominator*num2.numerator;
        Number quo = new Number(numerator,denominator);
        return simplify(quo);
    }

    //返回两个数最大公约数
    public static int gcd(int num1,int num2){
        if(num1 == 0||num2 == 0) return 1;
        int a = Math.max(Math.abs(num1), Math.abs(num2));
        int b = Math.min(Math.abs(num1), Math.abs(num2));
        while(a%b != 0){
            int t = a;
            a = b;b = t%b;
        }
        return b;
    }

    //分数化简
    public static Number simplify(Number num){
        int factor = gcd(num.numerator, num.denominator);
        num.numerator /= factor;
        num.denominator /= factor;
        return num;
    }

    //写文件
    public static void ExeAndAnsTxtWrite(HashMap<String,String> Exercises){
        String ExePath = "E:\\javacode\\pairCoding\\Exercises.txt";
        String AnsPath = "E:\\javacode\\pairCoding\\Answers.txt";
        try{
            File f1 = new File(ExePath);
            File f2 = new File(AnsPath);
            BufferedWriter Exe = new BufferedWriter(new FileWriter(f1));
            BufferedWriter Ans = new BufferedWriter(new FileWriter(f2));
            Set<String> keys = Exercises.keySet();
            Collection<String> values = Exercises.values();
            int i = 1;
            for(String q: keys){
                Exe.write(i+". "+q);
                Exe.newLine();
                ++i;
            }
            int j = 1;
            for(String a: values){
                Ans.write(j+". "+a);
                Ans.newLine();
                ++j;
            }
            Exe.close();
            Ans.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    //评分
    public static void GradeTxtWrite(){
        Scanner in = new Scanner(System.in);
        System.out.println("please input the QuePath:");
        String QuePath = in.next();
        System.out.println("please input the AnsPath:");
        String AnsPath = in.next();
        String GraPath = "E:\\javacode\\pairCoding\\Grade.txt";

        try{
            File fq = new File(QuePath);
            File fa = new File(AnsPath);
            File fg = new File(GraPath);
            BufferedReader Que = new BufferedReader(new FileReader(fq));
            BufferedReader Ans = new BufferedReader(new FileReader(fa));
            BufferedWriter Gra = new BufferedWriter(new FileWriter(fg));

            ArrayList<String> questionList = new ArrayList<>();
            ArrayList<String> answerList = new ArrayList<>();

            //读文件
            String exercise ;
            String answer ;
            while ((exercise = Que.readLine())!= null) {
                questionList.add(exercise);
            }
            while ((answer = Ans.readLine())!= null) {
                answerList.add(answer.split(" ")[1]);
            }

            //判断对错
            int Correct = 0,Wrong = 0;
            String CSN = "",WSN = "";
            for(int i = 0;i < questionList.size();i++){
                int separate = questionList.get(i).indexOf(".")+2;
                String q = questionList.get(i).substring(separate);
                String a = answerList.get(i);
                if(getResult(q).toString().equals(a)){
                    Correct++;CSN += (i+1)+",";
                }else{
                    Wrong++;WSN += (i+1)+",";
                    answerList.remove(i);
                    answerList.add(i,getResult(q).toString());//答案错误修改答案，方便查重的进行
                }
            }

            //文件查重
            ArrayList<ArrayList<String>> repeatList = new ArrayList<>();
            while(answerList.size()>0){
                String firstQuestion = questionList.remove(0);//移除两个集合中的第一个元素
                String firstAnswer = answerList.remove(0);

                if(answerList.contains(firstAnswer)){
                    ArrayList<String> details = new ArrayList<>();//存放一组重复的题目
                    for(int i = 0;i < answerList.size();i++){//从剩下的开始遍历
                        boolean flag = true;//默认为真
                        if(answerList.get(i).equals(firstAnswer)){//有相同的答案
                            int sep = firstQuestion.indexOf(".")+2;
                            String[] SepFirQues = firstQuestion.substring(sep).split(" ");//去除题号，并拆分
                            String toCompare = questionList.get(i);//答案相同的待比较的题目
                            for(String s: SepFirQues){
                                //不是括号但是待比较的题目中不含有该字符
                                if(!s.equals("(")&&!s.equals(")")&&!toCompare.contains(s)){
                                    flag = false;break;
                                }
                            }
                            if(flag){
                                if(details.isEmpty())
                                    details.add(firstQuestion.replace(". ",","));
                                details.add(questionList.get(i).replace(". ",","));
                                questionList.remove(i);
                                answerList.remove(i);
                            }
                        }
                    }
                    repeatList.add(details);//保存一组数据
                }
            }

            //写文件
            Gra.write("Correct:"+Correct+"("+CSN+")"+"\n");
            Gra.write("Wrong:"+Wrong+"("+WSN+")"+"\n");

            if(repeatList.size()>0){
                Gra.write("Repeat:"+repeatList.size()+"\n");
                Gra.write("RepeatDetail:"+"\n");
                for(int i = 0;i < repeatList.size();i++){
                    ArrayList<String> details = repeatList.get(i);
                    Gra.write("("+(i+1)+") "+details.get(0));
                    for(int j = 1;j < details.size();j++){
                        Gra.write(" Repeat "+details.get(j));
                    }
                    Gra.newLine();
                }
            }
            else Gra.write("Repeat:O");

            Que.close();
            Ans.close();
            Gra.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //查重
    public static boolean JudgeRepetition(String q,String a,HashMap<String,String> Exes){
        if(Exes.containsValue(a)){//是否含相同答案
            Set<String> qs = Exes.keySet();//题目集合
            Object[] _qs = qs.toArray();//题目拆分数组
            Collection<String> as = Exes.values();//答案集合
            int i = 0;
            for(String _a: as){
                if(_a.equals(a)){//找到答案的索引
                    break;
                }
                i++;
            }
            String[] arr = q.split(" ");//将运算符与数组分开
            for(String s: arr){
                if(!s.equals("(")&&!s.equals(")")&&!_qs[i].toString().contains(s))//不是括号但是已存题目中不含有该字符
                    return false;
            }
            return true;
        }
        return false;//无则返回false
    }

}
