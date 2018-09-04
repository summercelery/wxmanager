package springboot.service.async;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.math.BigDecimal.ROUND_DOWN;

public class test {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.println("输入文件路径");
        String path = scan.next().trim();
        File file = new File(path);

        //如果输入的是文件夹
        if (file.isDirectory()) {

            Boolean notContinue;
            File[] files = file.listFiles();
            if (null == files) {
                System.out.println("文件夹内无可修改文件");
                return;
            }

            //判断输入行数是否大于总行数
            int allFileNum = 0;
            List<Integer> fileLineNum = new ArrayList<>();
            for (File f : files) {
                Integer fileLine = getFileLineNum(f);
                fileLineNum.add(fileLine);
                allFileNum += fileLine;
            }
            System.out.println("总行数为  " + allFileNum);
            System.out.println("输入修改的总行数");
            String num = scan.next().trim();
            if (allFileNum < Integer.parseInt(num)) {
                System.out.println("输入行数大于总行数！");
                return;
            }

            System.out.println("文件数量：" + files.length + "    修改的的行数为：" + num + "  总行数为: " + allFileNum);

            do {
                notContinue = true;
                System.out.println("修改详情:");
                List<Integer> list = randomSplit(Integer.parseInt(num), files.length, fileLineNum);

                //输出每个文件随机分配的修改行数
                int i = 0;
                for (File f : files) {
                    System.out.println(f.getName() + "  修改的行数为  " + list.get(i) + "/" + fileLineNum.get(i));
                    i = ++i;

                }

                //当y时进行修改，其他时重新分配每个文件的修改行数
                System.out.println("输入y确认,输入其他重新分配修改行数");
                String str = scan.next().trim();
                if ("y".equals(str)) {
                    notContinue = false;
                    i = 0;
                    for (File f : files) {
                        write(f, read(f, list.get(i)));
                        i = ++i;
                    }
                }
            } while (notContinue);

        } else {
            System.out.println("文件行数为 " + getFileLineNum(file));
            System.out.println("输入修改的行数");
            String num = scan.next().trim();
            write(file, read(file, Integer.parseInt(num)));
        }
    }


    //读取及修改判定
    private static String read(File file, Integer num) {

        StringBuilder context = new StringBuilder();
        Integer fileLineNum = getFileLineNum(file);

        String line = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int nowLineNum = 0;
            //随机生成需要修改的行号
            List<Integer> randomNum = randomCommon(1, fileLineNum, num);
            while (null != (line = br.readLine())) {
                nowLineNum = ++nowLineNum;
                //如果需要修改的行号里面有当前行数
                if (randomNum == null || randomNum.contains(nowLineNum)) {
                    context.append(line).append("//注释").append(System.getProperty("line.separator"));
                } else {
                    context.append(line).append(System.getProperty("line.separator"));
                }
            }
            if (randomNum == null) {
                for (int i = 0; i < num - fileLineNum; i++) {
                    context.append("//注释").append(System.getProperty("line.separator"));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return context.toString();

    }


    //写文件
    private static void write(File file, String context) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //计算一个file的总行数
    private static Integer getFileLineNum(File file) {
        int num = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (null != br.readLine()) {
                num = ++num;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return num;

    }

    //一定范围内随机生成n个不重复数
    private static List<Integer> randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        List<Integer> integers = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            int randomNumber = (int) Math.round(Math.random() * (max - min) + min);
            if (integers.contains(randomNumber)) {
                i--;
            } else {
                integers.add(randomNumber);
            }
        }
        return integers;
    }

    //将num随机分割成splitNum个数 每份不得大于文件行数
    private static List<Integer> randomSplit(Integer num, Integer splitNum, List<Integer> fileLineNums) {

        List<Integer> randomResult = new ArrayList<>(splitNum);
        List<Integer> result = new ArrayList<>(splitNum);
        Boolean flag;
        do {
            Integer allNum = 0;
            Integer n = 0;
            flag = false;
            result.clear();
            randomResult.clear();

            for (int i = 0; i < splitNum; i++) {
                Integer randomNum = ThreadLocalRandom.current().nextInt(100);
                randomResult.add(randomNum);
                allNum += randomNum;
            }

            for (int i = 0; i < (splitNum - 1); i++) {
                Integer randomNum = new BigDecimal(randomResult.get(i).toString()).divide(new BigDecimal(allNum.toString()), 10, ROUND_DOWN).multiply(new BigDecimal(num.toString())).setScale(0, ROUND_DOWN).intValue();
                if (fileLineNums.get(i) < randomNum) {
                    flag = true;
                    break;
                }
                result.add(i, randomNum);
                n += randomNum;
            }
            if (!flag) {
                if (fileLineNums.get(splitNum - 1) < num - n) {
                    flag = true;
                } else {
                    result.add(splitNum - 1, num - n);
                }
            }
        } while (flag);


        return result;
    }
}
