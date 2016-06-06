package me.dwtj.ex.openjdk.langtools.expr;

/**
 * @author dwtj
 */
public class StaticRecursiveMethod {

    public static void main() {
        System.out.println(factorial(10));
    }

    public static int factorial(int x) {
        if (x == 0) {
            return 1;
        } else {
            return x * factorial(x-1);
        }
    }

}
