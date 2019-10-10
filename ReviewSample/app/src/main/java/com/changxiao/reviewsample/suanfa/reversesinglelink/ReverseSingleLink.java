package com.changxiao.reviewsample.suanfa.reversesinglelink;

/**
 * 已知单链表的头结构head,写一个函数把这个链表逆序。
 *
 * Created by Chang.Xiao on 2019/9/2.
 *
 * @version 1.0
 */
public class ReverseSingleLink {

  public static Node revSingleLink(Node head) {
    if (head == null) { //链表为空不能逆序
      return head;
    }
    if (head.getNextNode() == null) { //如果只有一个结点，当然逆过来也是同一个
      return head;
    }
    Node rhead = revSingleLink(head.getNextNode());
    head.getNextNode().setNextNode(head);
    head.setNextNode(null);
    return rhead;
  }

  public static void main(String[] args) {
    Node head = new Node(0);
    Node temp1 = null, temp2 = null;
    for (int i = 1; i < 100; i++) {
      temp1 = new Node(i);
      if (i == 1) {
        head.setNextNode(temp1);
      } else {
        temp2.setNextNode(temp1);
      }
      temp2 = temp1;
    }
    head = revSingleLink(head);
    while (head != null) {
      head = head.getNextNode();
    }
  }
}
