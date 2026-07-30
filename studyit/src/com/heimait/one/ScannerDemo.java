package com.heimait.one;

import java.util.Scanner;

public class ScannerDemo {
    //帮我写一个scanner语法
    public static void main(String[] args) {
        // 创建Scanner对象
        Scanner sc = new Scanner(System.in);

        // 获取用户输入的字符串
        System.out.print("请输入一个字符串: ");
        String str = sc.nextLine();

        // 获取用户输入的整数
        System.out.print("请输入一个整数: ");
        int num = sc.nextInt();


        // 获取用户输入的浮点数
        System.out.print("请输入一个浮点数: ");
        double dou = sc.nextDouble();

        // 输出获取到的数据
        System.out.println("您输入的字符串是: " + str);
        System.out.println("您输入的整数是: " + num);
        System.out.println("您输入的浮点数是: " + dou);

        // 关闭scanner
        sc.close();
    }
}



