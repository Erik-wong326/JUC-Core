package thread.Volatile;

/**
 * 指令重排demo
 volatile 禁止指令重排优化, 避免多线程环境下程序出现乱序的情况
 内存屏障(Memory Barrier):是一条cpu指令
 作用1:保证特定操作的执行顺序
 作用2:保证某些变量的内存可见性
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/4/11 14:48
 */
public class ResortSeqDemo {
    /*
      demo1
      指令执行顺序
      1234
      2134
      1324
      问题:语句4可以在指令重排之后变为第一条吗?
      答:不行,因为存在数据依赖
     */
    public void mySort(){
        int x = 11;//语句1
        int y = 12;//语句2
        x = x + 6;//语句3
        y = x * x;//语句4
    }
    /*
    demo2
    线程操作资源类,线程1访问method1,线程2访问method2 正常情况顺序执行， retValue = 6
    多线程下假设出现了指令重排,语句2在语句1之前,当执行完 flag = true 之后,
    另一个线程马上执行method2 a = 5.
     */
    int a = 0;
    boolean flag = false;
    public void method1(){
        a = 1;
        flag = true;
    }
    public void method2(){
        if (flag){
            a += 5;
            System.out.println("retValue: " + a);
        }
    }
}
