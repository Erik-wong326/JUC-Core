package thread.Volatile;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;

import java.lang.reflect.ParameterizedType;

/**
 Volatile 使用场景之一
 单例模式 DCL Double Check Lock
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/4/11 14:59
 */
public class SingletonDemo {
//    private static SingletonDemo instance = null;
    private static volatile SingletonDemo instance = null;
    private SingletonDemo(){
        System.out.println(Thread.currentThread().getName() + "的构造方法");
    }
    /**
     单例模式的两种实现
     双重校验锁实现单例模式
     双重校验锁能百分百保证线程安全吗?
     */
    //写两个单例模式
    //饿汉式
//    public static SingletonDemo getInstance(){
//        instance = new SingletonDemo();
//        return instance;
//    }
    //懒汉式
//    public static SingletonDemo getInstance(){
//        if (instance == null){
//            instance = new SingletonDemo();
//        }
//        return instance;
//    }


    /**
     * 从结果看,DCL能够保证单例模式正确性，但是仍然存在问题
     * DCL不一定是线程安全的，原因是 指令重排 ， 加入 volatile 可以禁止指令重排
     * 原因:某一个线程执行到第一次检测时,读取到 instance 不为 null,
     * instance 的引用对象可能还没有完成实例化.
     * instance = new SingletonDemo();分为以下3步执行
     * memory = allocate();   // 1、分配对象内存空间
     * instance(memory);   // 2、初始化对象
     * instance = memory;  // 3、设置instance指向刚刚分配的内存地址，此时instance != null
     *
     * 步骤2和步骤3不存在数据依赖关系，并且指令重排前后，程序的执行结果在线程中都是不变的。
     * 因此重排成 1-3-2 优化是被允许的
     * 产生问题: 线程 A 的对象初始化(step2)还没有完成时,线程 B 进来以为 instance == null,就会重新创建一个实例
     * 比喻:明天会来一个转校生坐到 1 排 3 列
     * 所以用 volatile 禁止指令重排，保证单例模式线程安全性
     * @return
     */
    //DCL:double Check Lock
    public static SingletonDemo getInstance(){
        if (null == instance){
            synchronized(SingletonDemo.class){
                if (null == instance){
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args){
        //单线程环境下,单例模式都为true
        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());
        System.out.println(SingletonDemo.getInstance() == SingletonDemo.getInstance());

        //多雄安承环境下,单例模式是否都为同一个对象?
        //答:不是,线程又构造出自己的对象的可能性
        for (int i = 0;i < 1000;i++){
            new Thread(SingletonDemo::getInstance,String.valueOf(i)).start();
        }
        /**
         * 解决方法1,用 synchronized
         * 问: synchronized 是重量级锁,虽然解决了问题，但是降低了并发性，不用 sync 能解决吗?
         * 答: 可以, 使用 DCL
         */
    }

}
