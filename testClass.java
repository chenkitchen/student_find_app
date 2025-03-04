import java.util.Scanner;
public class testClass {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个数：");
        int num = scanner.nextInt();
        System.out.println("输入的数是：" + num);
    }
}