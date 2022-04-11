package thread.aba;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/4/11 15:51
 */
public class ABADemo {
    //普通原子引用包装类
    private static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    //解决ABA问题:AtomicStampedReference<Integer> 传递两个值,一个初始值,一个版本号
    private static AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(100,1);
    public static void main(String[] args){
        System.out.println("===============================ABA问题演示====================================");
        new Thread(()->{
            //模拟ABA问题  把 100 -> 101 ,再把 101 -> 100
            atomicReference.compareAndSet(100,110);
            atomicReference.compareAndSet(110,100);
        },"thread-1").start();

        new Thread(()->{
            try {
                //sleep 1s 保证ABA模拟完成
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //如果线程知道100被修改过,这里应该是 false,但是显示的答案是true,所以ABA问题存在
            boolean res = atomicReference.compareAndSet(100, 120);
            System.out.println(Thread.currentThread().getName() + "\t 修改成功与否:" + res + "\t 当前值:" + atomicReference.get().toString());
        },"thread-2").start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * 解决ABA问题 : 新增一种机制,修改版本号,类似于时间戳的概念
         * AtomicStampedReference
         */

        System.out.println("===============================规避ABA问题====================================");
        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();//时间戳,获取第一次版本号
            System.out.println(Thread.currentThread().getName() + "\t 第一次版本号:" + stamp);
            try {
                //保证 thread-4 能拿到第一次版本号
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //模拟ABA
            boolean res1 = atomicStampedReference.compareAndSet(100, 110, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t 修改成功与否:" + res1 + "\t 当前版本号:" + atomicStampedReference.getStamp() + "\t当前值:" + atomicStampedReference.getReference());
            boolean res2 = atomicStampedReference.compareAndSet(110,100,atomicStampedReference.getStamp(),atomicStampedReference.getStamp()+1);
            System.out.println(Thread.currentThread().getName() + "\t 修改成功与否:" + res2 + "\t 当前版本号:" + atomicStampedReference.getStamp() + "\t当前值:" + atomicStampedReference.getReference());
        },"thread-3").start();

        new Thread(()->{
            int stamp = atomicStampedReference.getStamp();//获取第一次版本号
            System.out.println(Thread.currentThread().getName() + "\t 第一次版本号:" + stamp);
            try {
                //保证ABA模拟完成
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean res = atomicStampedReference.compareAndSet(100, 120, stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + "\t 修改成功与否:" + res + "\t 当前实际版本号:" + atomicStampedReference.getStamp() + "\t当前实际值:" + atomicStampedReference.getReference());
        },"thread-4").start();


    }
}
