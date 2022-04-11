package thread.CAS;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/4/11 15:31
 */
public class CASDemo {
    public static void main(String[] args){
        AtomicInteger atomicInteger = new AtomicInteger(3);
        /**
         一个是期望值,一个是更新值,当期望值和更新值相同时,才能够更改
         假设3s前,期望值是5,需要更新2022
         */
        /*
        //AtomicInteger源码
        private static final Unsafe unsafe = Unsafe.getUnsafe(); //获取unsafe类
        private volatile int value; // volatile 修饰 value 保证多线程之间内存可见性
        //调用了unsafe 类的 getAndAddInt方法
            public final int getAndIncrement() {
                                              //valueOffset:内存偏移量,即内存地址
                return unsafe.getAndAddInt(this, valueOffset, 1)
                                     // this:当前对象
            };
        //Unsafe类的 getAndAddInt 方法
        public final int getAndAddInt(Object var1, long var2, int var4) {
        int var5;
        do {
            //var1: this: 当前对象(AtomicInteger对象), var2:偏移量, var5: 当前对象+偏移量 拿到的主存中的值
            var5 = this.getIntVolatile(var1, var2);
            //如果当前的值(var1和var2在内存中找到的真实值)var5 和期望值(该对象的引用地址值) var2 一样,
            //就相加 var5 + var4 然后再返回true
            //如果不同,则继续循环取值,再比较,直到更新为完成(自旋)
        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
        return var5;
    }
         */
        //true
        System.out.println(atomicInteger.compareAndSet(3, 688) + "\t current data:" + atomicInteger.get());
        //false 因为我们执行了第一个的时候,期望值和原本值是满足的,因此修改688成功,但是第二次后
        // 主内存的值已经修改成了688,不满足期望值,因此返回了false,本次写入失败
        System.out.println(atomicInteger.compareAndSet(3, 101) + "\t current data:" + atomicInteger.get());


    }

}
