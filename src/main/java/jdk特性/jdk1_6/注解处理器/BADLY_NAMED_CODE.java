package jdk特性.jdk1_6.注解处理器;
public class BADLY_NAMED_CODE { 
 
    enum colors { 
        red, blue, green; 
    } 
 
    static final int _FORTY_TWO = 42; 
 
    public static int NOT_A_CONSTANT = _FORTY_TWO; 
 
    protected void BADLY_NAMED_CODE() { 
 
    } 
 
    public void NOTcamelCASEmethodNAME() { 
        return; 
    } 
    public static void main(String[] args) {
        System.out.println("nihao");
    }
}