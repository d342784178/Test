package 刷题.设计模式.装饰器;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 11:41
 */
public class PersonFlyDecorator  implements IPerson{
    IPerson realPerson;

    public PersonFlyDecorator(IPerson realPerson) {
        this.realPerson = realPerson;
    }

    @Override
    public void action() {
        System.out.println("fly");
        realPerson.action();
    }
}
