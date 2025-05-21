import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import java.util.ArrayList;
import java.util.Scanner;

enum Color {
  RED, GREEN
}

abstract class Tree {

  private int value;
  private Color color;
  private int depth;

  public Tree(int value, Color color, int depth) {
    this.value = value;
    this.color = color;
    this.depth = depth;
  }

  public int getValue() {
    return value;
  }

  public Color getColor() {
    return color;
  }

  public int getDepth() {
    return depth;
  }

  public abstract void accept(TreeVis visitor);
}

class TreeNode extends Tree {

  private ArrayList<Tree> children = new ArrayList<>();

  public TreeNode(int value, Color color, int depth) {
    super(value, color, depth);
  }

  public void accept(TreeVis visitor) {
    visitor.visitNode(this);

    for (Tree child : children) {
      child.accept(visitor);
    }
  }

  public void addChild(Tree child) {
    children.add(child);
  }
}

class TreeLeaf extends Tree {

  public TreeLeaf(int value, Color color, int depth) {
    super(value, color, depth);
  }

  public void accept(TreeVis visitor) {
    visitor.visitLeaf(this);
  }
}

abstract class TreeVis
{
  public abstract int getResult();
  public abstract void visitNode(TreeNode node);
  public abstract void visitLeaf(TreeLeaf leaf);

}

class SumInLeavesVisitor extends TreeVis {
  private int sum = 0;

  public int getResult() {
    return sum;
  }

  public void visitNode(TreeNode node) {
    // Nothing to do for non-leaf nodes
  }

  public void visitLeaf(TreeLeaf leaf) {
    sum += leaf.getValue(); // Add value of leaf node
  }}

class ProductOfRedNodesVisitor extends TreeVis {
  private long product = 1;
  private final int MOD = 1000000007; // Prevent overflow

  public int getResult() {
    return (int) product;
  }

  public void visitNode(TreeNode node) {
    if (node.getColor() == Color.RED) {
      product = (product * node.getValue()) % MOD;
    }
  }

  public void visitLeaf(TreeLeaf leaf) {
    if (leaf.getColor() == Color.RED) {
      product = (product * leaf.getValue()) % MOD;
    }
  }
}

class FancyVisitor extends TreeVis {
  private int sumNonLeafEvenDepth = 0;
  private int sumGreenLeaves = 0;

  public int getResult() {
    return Math.abs(sumNonLeafEvenDepth - sumGreenLeaves);
  }

  public void visitNode(TreeNode node) {
    if (node.getDepth() % 2 == 0) { // Even depth non-leaf node
      sumNonLeafEvenDepth += node.getValue();
    }
  }

  public void visitLeaf(TreeLeaf leaf) {
    if (leaf.getColor() == Color.GREEN) {
      sumGreenLeaves += leaf.getValue();
    }
  }
}

public class Solution {

  private static int[] values;
  private static Color[] colors;
  private static Map<Integer, List<Integer>> adjList;

  public static Tree solve() {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt(); // Number of nodes

    values = new int[n];
    colors = new Color[n];
    adjList = new HashMap<>();

    // Read values of the nodes
    for (int i = 0; i < n; i++) {
      values[i] = sc.nextInt();
    }

    // Read colors of the nodes
    for (int i = 0; i < n; i++) {
      colors[i] = (sc.nextInt() == 0) ? Color.RED : Color.GREEN;
    }

    // Read edges and construct adjacency list
    for (int i = 0; i < n - 1; i++) {
      int u = sc.nextInt() - 1;
      int v = sc.nextInt() - 1;

      adjList.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
      adjList.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
    }

    // Build the tree starting from root (node 0)
    return buildTree(0, 0, new boolean[n]);
  }

  private static Tree buildTree(int nodeIndex, int depth, boolean[] visited) {
    visited[nodeIndex] = true;
    List<Integer> children = adjList.get(nodeIndex);

    if (children == null || children.stream().allMatch(visited::clone)) {
      // It's a leaf node
      return new TreeLeaf(values[nodeIndex], colors[nodeIndex], depth);
    } else {
      TreeNode node = new TreeNode(values[nodeIndex], colors[nodeIndex], depth);
      for (int childIndex : children) {
        if (!visited[childIndex]) {
          node.addChild(buildTree(childIndex, depth + 1, visited));
        }
      }
      return node;
    }
  }


  public static void main(String[] args) {
    Tree root = solve();
    SumInLeavesVisitor vis1 = new SumInLeavesVisitor();
    ProductOfRedNodesVisitor vis2 = new ProductOfRedNodesVisitor();
    FancyVisitor vis3 = new FancyVisitor();

    root.accept(vis1);
    root.accept(vis2);
    root.accept(vis3);

    int res1 = vis1.getResult();
    int res2 = vis2.getResult();
    int res3 = vis3.getResult();

    System.out.println(res1);
    System.out.println(res2);
    System.out.println(res3);
  }
}