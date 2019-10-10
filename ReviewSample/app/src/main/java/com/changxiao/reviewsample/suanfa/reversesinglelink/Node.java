package com.changxiao.reviewsample.suanfa.reversesinglelink;

/**
 * $desc$
 *
 * Created by Chang.Xiao on 2019/9/2.
 *
 * @version 1.0
 */
public class Node {

  private Integer count;
  private Node nextNode;

  public Node() {
  }

  public Node(int count) {
    this.count = new Integer(count);
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Node getNextNode() {
    return nextNode;
  }

  public void setNextNode(Node nextNode) {
    this.nextNode = nextNode;
  }
}
