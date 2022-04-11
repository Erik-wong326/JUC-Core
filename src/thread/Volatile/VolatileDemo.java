package thread.Volatile;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/4/11 10:08
 */
class Data{
    volatile int num = 0;
    AtomicInteger atomicInteger = new AtomicInteger();
    public void addTo88(){
        this.num = 80;
    }
    public void add(){
        this.num++;
    }
    public void atomicAdd(){
        atomicInteger.getAndIncrement();
    }
}

/**
 验证 volatile 的可见性
 1.假设 int num = 0; 如果不加 volatile 修饰, main 线程对资源类不可见,则 while 循环会卡住
 2.添加 volatile , 可以解决可见性问题
 验证 volatile 不保证原子性
 1.原子性是什么意思?
 原子性:指不可分割性,完整性,即线程A在处理某个业务时.中间不能加塞,分割,需要保证整体完整性
 2.面试:写一个 volatile 不保证原子性的 demo
 3.为什么会出现数值丢失?
 4.如何解决原子性问题?
 答: 在方法上加 synchronized.
 问:synchronized 虽然能解决原子问题,但是性能会大幅下降,不用 synchronized 怎么解决?
 答:使用 juc 下的 AtomicInteger

 */
public class VolatileDemo {
    public static void main(String[] args){
//        System.out.println("===================验证 volatile 可见性=====================");
//        Data data = new Data();
//        new Thread(()->{
//            System.out.println(Thread.currentThread().getName() + "\t 启动");
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            data.addTo88();
//            System.out.println(Thread.currentThread().getName() + "\t num: " + data.num);
//        },"Thread-A").start();
//
//        //如果不加 volatile 则 main 线程对 num 不可见,程序就会停滞在这里
//        while (data.num == 0){
//
//        }
//
//        System.out.println(Thread.currentThread().getName() + "\t add Mission over");

        System.out.println("===================验证 volatile 不保证原子性=====================");
        Data data = new Data();
        //20个线程都操作资源类 1000 次
        for (int i = 1;i <= 20;i++){
            new Thread(()->{
                for (int j = 0;j < 1000;j++){
                    data.add();
                    data.atomicAdd();
                }
            },"Thread-" + String.valueOf(i)).start();
        }


        //等待上面的20个线程执行完,在 main 线程查看最终结果值
        //这里判断线程数是否 > 2. 为什么是 2? 因为默认 2 个线程,一个 main 一个 gc 线程
        while (Thread.activeCount() > 2){
            Thread.yield();
        }

        //假设 volatile 可以保证原子性, 如果每个线程都操作 num 1000次,则num最后的值为 20*1000 =20000
        System.out.println(Thread.currentThread().getName() + "\t num: " + data.num);
        //原子性的解决方案: 原子引用类
        System.out.println(Thread.currentThread().getName() + "\t AtomicNum: " + data.atomicInteger);

    }

}
