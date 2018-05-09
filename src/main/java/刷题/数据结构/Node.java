package 刷题.数据结构;

import lombok.Data;

@Data
public class Node<T> {
    T       t;
    Node<T> next;

    public Node(T t) {
        this.t = t;
    }
}