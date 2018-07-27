package 刷题.设计模式.装饰器;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 11:42
 */
public class Client {
    public static void main(String args[]) {
        IPerson person = new Person();
        person.action();
        person = new PersonFlyDecorator(person);
        person.action();
    }
}
