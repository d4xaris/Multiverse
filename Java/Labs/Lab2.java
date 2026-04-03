public class Lab2 {

    static class Node {
        String data;
        Node next;
        Node(String data) { this.data = data; }
    }

    static class LinkedList {
        Node head;
        Node tail;

        void addLast(String data) {
            Node node = new Node(data);
            if (head == null) { head = tail = node; }
            else              { tail.next = node; tail = node; }
        }

        void print() {
            Node cur = head;
            int i = 1;
            while (cur != null) {
                System.out.printf("%3d: %s%n", i++, cur.data);
                cur = cur.next;
            }
        }

        void clear() {
            Node cur = head;
            while (cur != null) {
                Node next = cur.next;
                cur.next = null;
                cur = next;
            }
            head = tail = null;
        }
    }

    static LinkedList reorder(LinkedList list) {
        LinkedList result = new LinkedList();

        Node blockStart = list.head;

        while (blockStart != null) {

            Node mid = blockStart;
            for (int i = 0; i < 10; i++) {
                mid = mid.next;          
            }

            Node blockEnd = mid;
            for (int i = 0; i < 9; i++) {
                blockEnd = blockEnd.next; 
            }
            Node nextBlock = blockEnd.next;

            Node p1 = blockStart;  
            Node p2 = mid;        

            for (int i = 0; i < 10; i++) {
                result.addLast(p1.data);
                result.addLast(p2.data);
                p1 = p1.next;
                p2 = p2.next;
            }

            blockStart = nextBlock;
        }

        return result;
    }

    public static void main(String[] args) {

        int n = 40;

        LinkedList list = new LinkedList();

        for (int i = 1; i <= n; i++) {
            list.addLast("word" + i);
        }

        System.out.println("=== Original list ===");
        list.print();

        LinkedList reordered = reorder(list);

        System.out.println("\n=== After reorder ===");
        reordered.print();

        list.clear();
        reordered.clear();
    }
}