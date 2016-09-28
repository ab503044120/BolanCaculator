package com.android.bolancaculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.LinkedList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private EditText et;
    private Button btn;
    private Stack<String> s1;
    private Stack<Double> s2;
    private LinkedList<String> s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.et_input);
        btn = (Button) findViewById(R.id.btn_caculate);
        s1 = new Stack<>();
        s2 = new Stack<>();
        s3 = new LinkedList<>();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1.clear();
                s2.clear();
                s3.clear();
                et.setText(caculate(et.getText().toString()));
                ;
            }
        });

    }

    private String caculate(String s) {
        for (int i = 0; i < s.length(); i++) {
            String in = s.charAt(i) + "";
            //如果为数字
            if (in.matches("[0-9]+")) {
                s3.offer(in);
            } else if (in.equals("(")) {
                //如果为{ 就进栈
                s1.push(in);
            } else if (in.equals(")")) {
                while (true) {
                    //如果到了( 那就停止出栈
                    if (s1.peek().equals("(")) {
                        s1.pop();
                        break;
                    }
                    String pop = s1.pop();
                    s3.offer(pop);
                }

            } else {
                if (!s1.isEmpty()) {
                    int peekpPriority = priority(s1.peek());
                    int inPriority = priority(in);
                    //当栈顶优先级高于输入优先级那就出栈
                    if (inPriority < peekpPriority) {
                        s3.offer(s1.pop());
                        while (true) {
                            if (!s1.isEmpty() && priority(s1.peek()) >= inPriority) {
                                //如果接下来的大于等于输入的优先级那么就出栈
                                s3.offer(s1.pop());
                            } else {
                                break;
                            }
                        }
                    }
                }
                s1.push(in);
            }
        }
        //拿出所有的运算符
        while (!s1.isEmpty()) {
            s3.offer(s1.pop());
        }
        while (!s3.isEmpty()) {
            String in = s3.poll();
            if (in.matches("[0-9]+")) {
                s2.push(Double.parseDouble(in));
            } else {
                double right = s2.pop();
                double left = s2.pop();
                s2.push(operate(left, right, in));
            }
        }
        return s2.pop() + "";

    }

    private double operate(double left, double right, String in) {
        switch (in) {
            case "+":
                return left + right;
            case "-":
                return left - right;
            case "*":
                return left * right;
            case "/":
                return left / right;
        }
        return 0;
    }

    public int priority(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
        }
        return 0;
    }

}
