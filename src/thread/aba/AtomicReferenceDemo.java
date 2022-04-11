package thread.aba;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子引用类的使用
 原子引用和原子包装类的概念类似,
 就是将一个java类,用原子引用类包装起来,
 让这个类具有原子性

 原子引用理解,用于在ABADemo中解决ABA问题
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/4/11 15:59
 */
class User{
    int age;
    String userName;

    public User(int age, String userName) {
        this.age = age;
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", userName='" + userName + '\'' +
                '}';
    }
}
public class AtomicReferenceDemo {

    public static void main(String[] args){
        User u1 = new User(22,"z3");
        User u2 = new User(18,"l4");

        //创建原子引用包装类
        AtomicReference<User> atomicReference = new AtomicReference<>();

        //物理内存的共享变量为u1
        atomicReference.set(u1);
        //demo1
        System.out.println("修改成功与否:" + atomicReference.compareAndSet(u1,u2) + "\t 当前值:" + atomicReference.get().toString());
        System.out.println("修改成功与否:" + atomicReference.compareAndSet(u1,u2) + "\t 当前值:" + atomicReference.get().toString());

        //demo2
        //两种demo效果都一样,只是一个在main线程中,另一个在A B线程里
//        new Thread(()->{
//            boolean res1 = atomicReference.compareAndSet(u1, u2);
//            System.out.println(Thread.currentThread().getName() + "\t 修改成功与否:" + res1 + "\t 当前值:" + atomicReference.get().toString());
//        },"Thread-A").start();
//
//        new Thread(()->{
//            boolean res2 = atomicReference.compareAndSet(u1, u2);
//            System.out.println(Thread.currentThread().getName() + "\t 修改成功与否:" + res2 + "\t 当前值:" + atomicReference.get().toString());
//        },"Thread-B").start();
    }

}
