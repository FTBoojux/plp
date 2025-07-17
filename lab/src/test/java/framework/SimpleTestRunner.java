package framework;

import java.lang.reflect.Method;

public class SimpleTestRunner {
    private int total = 0;
    private int passed = 0;
    private int failed = 0;
    private static final String LINE = "-".repeat(50);
    public void runAllTests(Object instance){
        Class<?> aClass = instance.getClass();
        System.out.println("run all tests:" + aClass.getName());
        System.out.println("-".repeat(50));
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method m : declaredMethods) {
            FTest fTest = m.getAnnotation(FTest.class);
            if(fTest != null){
                printLine();
                runTestByMethodName(instance, m);
            }
        }
        printTestResult();
    }
    public void printLine(){
        System.out.println(LINE);
    }
    private void printTestResult(){
        printLine();
        System.out.println("Total tests: " + total);
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
    }
    private void runTestByMethodName(Object instance, Method m) {
        try {
            m.invoke(instance);
            System.out.println("method: " + m.getName() + " in class " + instance.getClass().getName() + " passed ");
            ++passed;
        } catch (Exception e) {
            System.out.println("method: " + m.getName() + " in class " + instance.getClass().getName() + " failed");
            ++failed;
            System.out.println(e.getCause().getMessage());
        } finally {
            ++total;
        }
    }
}
