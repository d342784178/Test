package MyRx;

public class DeadLock implements Runnable {
    private int flag = 1;  
    // 此对象必须是静态的，同步必须是被多个线程共享  
    static Object resource1 = new Object(), resource2 = new Object();  
  
    public void run() {  
        if (flag == 1) {  
            synchronized (resource1) {  
                System.out.println("resource1");  
                try {  
                    Thread.sleep(1000);  
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
                synchronized (resource2) {
                    System.out.println("resource2");  
                }
            }  
        }  
        if (flag == 0) {  
            synchronized (resource2) {  
                System.out.println("resource2");  
                try {  
                    Thread.sleep(1000);  
                } catch (InterruptedException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
                synchronized (resource1) {
                    System.out.println("resource1");  
                }
            }  
        }  
    }  
  
    public static void main(String[] args) {  
        DeadLock lock1 = new DeadLock();
        DeadLock lock2 = new DeadLock();
        lock1.flag = 1;  
        lock2.flag = 0;  
        Thread ll1 = new Thread(lock1);  
        Thread ll2 = new Thread(lock2);  
        ll1.start();  
        ll2.start();  
    }  
}  