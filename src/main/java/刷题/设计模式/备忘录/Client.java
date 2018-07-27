package 刷题.设计模式.备忘录;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-07-27
 * Time: 15:53
 */
public class Client {
    public static void main(String args[]) {
        Originnator originnator = new Originnator();
        System.out.println(originnator.getState());
        Memento<Integer, Originnator> memento = originnator.store();
        System.out.println(originnator.changeState());
        originnator.backup(memento);
        System.out.println(originnator.getState());


        BackupManagerDecorator<Integer, Originnator> backupManagerDecorator = new BackupManagerDecorator<>(originnator);
        backupManagerDecorator.autoStore();
        System.out.println(originnator.changeState());
        backupManagerDecorator.autoStore();
        System.out.println(originnator.changeState());

        backupManagerDecorator.backup();
        System.out.println(originnator.getState());
        backupManagerDecorator.backup();
        System.out.println(originnator.getState());
    }
}
