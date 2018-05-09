package jdk特性.jdk1_6;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class CompilerDemo {
    //JavaCompiler 动态编译java代码
    private final static String srcFileName = "Test.java";
    private final static String classFileName = "Test.class";
    private final static String className = "Test";
    
    public static void main( String[] args ) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if( compiler == null ) {
            System.err.println( "Compiler is null!" );
            return;
        }
        StandardJavaFileManager fileManager = compiler.getStandardFileManager( null, null, null );
        generateJavaClass();
        
        Iterable < ? extends JavaFileObject> sourceFiles = fileManager.getJavaFileObjects( new String[]{ srcFileName } );
        compiler.getTask( null, fileManager, null, null, null, sourceFiles ).call();
        try {
            fileManager.close();
            Class.forName( className ).newInstance();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static void generateJavaClass() {
        try {
            FileWriter rw = new FileWriter( srcFileName );
            BufferedWriter bw = new BufferedWriter( rw );
            bw.write( "public class " + className + " {" );
            bw.newLine();
            
            bw.write( "public " + className + "() {");
            bw.newLine();
            bw.write( "System.out.println( 'you are in the constructor of Class Test' );" );
            bw.write( "}" );
            bw.newLine();
            
            bw.write( "}" );
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}