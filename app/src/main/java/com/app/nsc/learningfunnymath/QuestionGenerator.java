package com.app.nsc.learningfunnymath;

import java.util.Random;

/**
 * Created by Alande on 4/2/2558.
 */
public class QuestionGenerator {
    public static String[] question, answer;
    public static Integer size = 0;

    //สุ่มคำถามและคำตอบ
    //Bingo
    public static void generate(String[] q, String[] a, Boolean[] p, Boolean[] c, Integer s)
    {
        //สลับคำถาม - คำตอบ
        Random random = new Random();
        for (Integer K = size - 1; K > 0; K--)
        {
            Integer index = random.nextInt(K + 1);

            String temp = question[index];
            question[index] = question[K];
            question[K] = temp;

            temp = answer[index];
            answer[index] = answer[K];
            answer[K] = temp;
        }

        for(Integer K = 0;K < s;K++){
            q[K] = question[K];
            a[K] = answer[K];
            p[K] = Boolean.FALSE;
            c[K] = Boolean.FALSE;
        }
    }

    //Matching
    public static void generate(String[] q,String[] r,Integer s){
        //สลับคำถาม - คำตอบ
        Random random = new Random();
        for (Integer K = size - 1; K > 0; K--)
        {
            Integer index = random.nextInt(K + 1);

            String temp = question[index];
            question[index] = question[K];
            question[K] = temp;

            temp = answer[index];
            answer[index] = answer[K];
            answer[K] = temp;
        }

        //เอาข้อมูลใส่ตาราง
        for(Integer K = 0;K < s;K = K + 2){
            q[K] = question[K];
            q[K + 1] = answer[K];
            r[K] = answer[K];
            r[K + 1] = answer[K];
        }

        //สลับข้อมูลในตาราง
        for (Integer K = s - 1; K > 0; K--)
        {
            Integer index = random.nextInt(K + 1);

            String temp = q[index];
            q[index] = q[K];
            q[K] = temp;

            temp = r[index];
            r[index] = r[K];
            r[K] = temp;
        }
    }
}
