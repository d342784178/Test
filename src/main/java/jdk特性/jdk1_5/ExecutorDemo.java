package jdk特性.jdk1_5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorDemo extends Thread {
    private int index;  
    public ExecutorDemo(int i) {
        this.index = i;  
    }  
    public void run() {  
        try {  
            System.out.println("[" + this.index + "] start....");  
            Thread.sleep((int) (Math.random() * 1000));  
            System.out.println("[" + this.index + "] end.");  
        }catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public static void main(String args[]) {  
        ExecutorService service = Executors.newFixedThreadPool(4);  
        for (int i = 0; i < 10; i++) {  
            service.execute(new ExecutorDemo(i));
        }
        System.out.println("submit finish");  
        service.shutdown();

    }
  
}  

