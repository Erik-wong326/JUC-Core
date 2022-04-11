package thread.Volatile;

import java.util.concurrent.TimeUnit;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/4/11 10:08
 */
class Data{
    volatile int num = 0;
    public void addTo88(){
        this.num = 80;
    }
}
public class VolatileDemo {
    public static void main(String[] args){
        System.out.println("===================验证 volatile 可见性=====================");
        Data data = new Data();
        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + "\t 启动");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            data.addTo88();
            System.out.println(Thread.currentThread().getName() + "\t num: " + data.num);
        },"Thread-A").start();

        //如果不加 volatile 则 main 线程对 num 不可见,程序就会停滞在这里
        while (data.num == 0){

        }

        System.out.println(Thread.currentThread().getName() + "\t add Mission over");
    }

}
