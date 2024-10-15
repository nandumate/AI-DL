import java.util.*;

class Node {
    int lbm; // left bank missionaries
    int lbc; // left bank cannibals
    int rbm; // right bank missionaries
    int rbc; // right bank cannibals
    int p, id, pid; // p: depth, id: current node id, pid: parent node id
    int boat; // boat position
    int h; // heuristic value
    int fin; // flag to indicate if it's part of the final path
    
    Node() {
        fin = 0;
        boat = 0;
        id = 0;
        pid = 0;
        p = 0;
        lbm = 0;
        lbc = 0;
        rbm = 0;
        rbc = 0;
        h = 0;
    }
}

class Astar {
    int size;
    int tempid, max;
    Node start;
    Node temp;
    ArrayList<Node> open, close;
    
    Astar() {
        size = 5;
        tempid = 0;
        
        start = new Node();
        start.lbm = size;
        start.lbc = size;
        start.rbm = 0;
        start.rbc = 0;
        start.p = 1;
        start.id = 0;
        start.pid = 0;
        start.boat = 1;
        start.h = 1;
        start.fin = 1;
        
        open = new ArrayList<>();
        close = new ArrayList<>();
        
        open.add(start);
    }
    
    Node heuristic(Node n) {
        n.h = n.p * 100 + (n.lbm * n.lbc) + (n.rbm * n.rbc) + n.rbm * 10 + n.rbc;
        return n;
    }
    
    void removeSame() {
        for (Node n1 : close) {
            int j = 0;
            int s = open.size();
            while (j < s) {
                Node n = open.get(j);
                if (n1.lbc == n.lbc && n1.lbm == n.lbm && n1.rbc == n.rbc && n1.rbm == n.rbm && n1.boat == n.boat) {
                    System.out.print("\nCLOSE List COMPARED Removed " + n.lbm + n.lbc);
                    open.remove(j);
                    s = open.size();
                } else {
                    j++;
                }
            }
        }
    }
    
    int compare(Node n) {
        for (Node n1 : open) {
            if (n1.lbc == n.lbc && n1.lbm == n.lbm && n1.rbc == n.rbc && n1.rbm == n.rbm && n1.boat == n.boat) {
                return 1;
            }
        }
        return 0;
    }
    
    void removeOpen() {
        int i = 0;
        int s = open.size();
        while (i < s) {
            Node n = open.get(i);
            if (n.lbc < 0 || n.lbc > size || n.rbc < 0 || n.rbc > size || n.lbm < 0 || n.rbm < 0 || n.lbm > size || n.rbm > size || (n.lbm > 0 && n.lbc > n.lbm) || (n.rbm > 0 && n.rbc > n.rbm)) {
                System.out.print("\nOPEN LOGIC Removed " + n.lbm + n.lbc);
                open.remove(i);
                s = open.size();
            } else {
                i++;
            }
        }
    }
    
    void solve() {
        while (!open.isEmpty()) {
            max = 0;
            int pos = 0;
            for (int i = 0; i < open.size(); i++) {
                Node n = open.get(i);
                if (n.h > max) {
                    max = n.h;
                    pos = i;
                }
            }
            
            Node n = open.get(pos);
            
            if (n.rbc == size && n.rbm == size && n.boat == -1 && n.id != 0) {
                System.out.println("\nGOAL Reached: " + n.lbm + " " + n.lbc + "\t" + n.rbm + " " + n.rbc);
                close.add(n);
                break;
            }
            System.out.println("\nIN\n" + n.lbm + " " + n.lbc + "\t" + n.rbm + " " + n.rbc);

            for (int i = 1; i <= 5; i++) {
                temp = succ(n, i);
                if (compare(temp) == 0) {
                    open.add(temp);
                }
            }
            if (size > 3) {
                for (int i = 6; i <= 9; i++) {
                    temp = succ(n, i);
                    if (compare(temp) == 0) {
                        open.add(temp);
                    }
                }
            }

            n = open.get(pos);
            open.remove(pos);
            close.add(n);
            
            removeOpen();
            removeSame();
        }
    }
    
    void print() {
        System.out.println("\t Missionaries and Cannibals\n");
        
        Node n = close.get(close.size() - 1);
        while (n.id != 0) {
            for (Node n1 : close) {
                if (n1.id == n.pid) {
                    n = n1;
                    break;
                }
            }
        }
        
        close.removeIf(node -> node.fin == 0);
        
        for (Node node : close) {
            for (int j = 0; j < node.lbm; j++) {
                System.out.print("M ");
            }
            
            for (int j = 0; j < node.lbc; j++) {
                System.out.print("C ");
            }
            for (int j = 0; j < (size - node.lbc); j++) {
                System.out.print("  ");
            }
            for (int j = 0; j < (size - node.lbm); j++) {
                System.out.print("  ");
            }
            System.out.print("\t\t");
            for (int j = 0; j < node.rbm; j++) {
                System.out.print("M ");
            }
            
            for (int j = 0; j < node.rbc; j++) {
                System.out.print("C ");
            }
            for (int j = 0; j < (size - node.rbc); j++) {
                System.out.print("  ");
            }
            for (int j = 0; j < (size - node.rbm); j++) {
                System.out.print("  ");
            }
            System.out.println("\n");
        }
    }
    
    void move(Node n, Node cur) {
        n.lbc = cur.lbc;
        n.lbm = cur.lbm;
        n.rbc = cur.rbc;
        n.rbm = cur.rbm;
        
        n.boat = cur.boat;
        n.h = cur.h;
        
        n.id = cur.id;
        n.p = cur.p;
        n.pid = cur.pid;
    }
    
    Node succ(Node cur, int ch) {
        Node n = new Node();
        move(n, cur);
        n.p++;
        n.pid = n.id;
        n.id = tempid++;
        
        if (n.boat == 1) {
            switch (ch) {
                case 1:
                    n.lbc--;
                    n.rbc++;
                    break;
                case 2:
                    n.lbm--;
                    n.rbm++;
                    break;
                case 3:
                    n.lbm--;
                    n.lbc--;
                    n.rbm++;
                    n.rbc++;
                    break;
                case 4:
                    n.lbm--;
                    n.lbm--;
                    n.rbm++;
                    n.rbm++;
                    break;
                case 5:
                    n.lbc--;
                    n.lbc--;
                    n.rbc++;
                    n.rbc++;
                    break;
                case 6:
                    n.lbm--;
                    n.rbm++;
                    n.lbm--;
                    n.rbm++;
                    n.lbm--;
                    n.rbm++;
                    break;
                case 7:
                    n.lbc--;
                    n.rbc++;
                    n.lbc--;
                    n.rbc++;
                    n.lbc--;
                    n.rbc++;
                    break;
                case 8:
                    n.lbc--;
                    n.rbc++;
                    n.lbc--;
                    n.rbc++;
                    n.lbm--;
                    n.rbm++;
                    break;
                case 9:
                    n.lbm--;
                    n.rbm++;
                    n.lbm--;
                    n.rbm++;
                    n.lbc--;
                    n.rbc++;
                    break;
            }
        } else if (n.boat == -1) {
            switch (ch) {
                case 1:
                    n.lbc++;
                    n.rbc--;
                    break;
                case 2:
                    n.lbm++;
                    n.rbm--;
                    break;
                case 3:
                    n.lbm++;
                    n.lbc++;
                    n.rbm--;
                    n.rbc--;
                    break;
                case 4:
                    n.lbm++;
                    n.lbm++;
                    n.rbm--;
                    n.rbm--;
                    break;
                case 5:
                    n.lbc++;
                    n.lbc++;
                    n.rbc--;
                    n.rbc--;
                    break;
                case 6:
                    n.lbm++;
                    n.rbm--;
                    n.lbm++;
                    n.rbm--;
                    n.lbm++;
                    n.rbm--;
                    break;
                case 7:
                    n.lbc++;
                    n.rbc--;
                    n.lbc++;
                    n.rbc--;
                    n.lbc++;
                    n.rbc--;
                    break;
                case 8:
                    n.lbc++;
                    n.rbc--;
                    n.lbc++;
                    n.rbc--;
                    n.lbm++;
                    n.rbm--;
                    break;
                case 9:
                    n.lbm++;
                    n.rbm--;
                    n.lbm++;
                    n.rbm--;
                    n.lbc++;
                    n.rbc--;
                    break;
            }
        }
        n.boat = n.boat * (-1);
        n = heuristic(n);

        return n;
    }
}

public class AI_1 {
    public static void main(String[] args) {
        Astar a = new Astar();
        a.solve();
        a.print();
    }
}