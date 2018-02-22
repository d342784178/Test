package designpatterns.command;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2018-02-22
 * Time: 11:20
 */
public interface IReceiver {
    void action(String s);

    void redo(String s);
}
