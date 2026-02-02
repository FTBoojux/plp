package framework;

public final class Bassert {
    public static void fAssert(boolean condition){
        if(!condition){
            throw new AssertionError();
        }
    }
    public static void fAssert(boolean condition, String message){
        if(!condition){
            throw new AssertionError(message);
        }
    }
    private Bassert(){}
}
