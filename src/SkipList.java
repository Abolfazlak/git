import java.util.Scanner;

class Node {
    int key;
    Node next, prev, down, up;

    public Node(int k) {
        this.key = k;
        this.down = null;
        this.prev = null;
        this.next = null;
        this.up = null;
    }
}

class SkipList {
    Node[] head = new Node[10];
    Node[] tail = new Node[10];

    public SkipList() {
        for (int i = 9; i > -1; i--) {
            head[i] = new Node(Integer.MIN_VALUE);
            tail[i] = new Node(Integer.MAX_VALUE);
            head[i].next = tail[i];
            tail[i].prev = head[i];
        }
        for (int i = 9; i > -1; i--) {
            if (i > 0) {
                head[i].down = head[i - 1];
                head[i - 1].up = head[i];
                tail[i].down = tail[i - 1];
                tail[i - 1].up = tail[i];
            }
        }
    }

    public Node search(int k) {
        Node current = head[9];
        while (current != null) {
            if (k > current.next.key) {
                current = current.next;
            } else if (k == current.next.key) {
                current = current.next;
                while (current.down != null) {
                    current = current.down;
                }
                return current;
            } else {
                if (current.down != null) {
                    current = current.down;
                } else {
                    return current;
                }
            }
        }
        return null;
    }

    public void insert(int k) {
        int level = 0;
        Node current = search(k);
        Node inserted = new Node(k);
        Node duplicate;
        inserted.prev = current;
        inserted.next = current.next;
        current.next.prev = inserted;
        current.next = inserted;
        int random = (int) (7 * Math.random()) + 1;
        while ((random % 4 == 0) && level < 10) {
            duplicate = new Node(k);
            level++;
            inserted.up = duplicate;
            duplicate.down = inserted;
            current = head[level];
            while (k > current.next.key) {
                current = current.next;
            }
            duplicate.prev = current;
            duplicate.next = current.next;
            current.next.prev = duplicate;
            current.next = duplicate;
            inserted = duplicate;
            random = (int) (7 * Math.random()) + 1;
        }
    }

    public Node deleteSearch(int k) {
        Node current = head[9];
        while (current != null) {
            if (k > current.next.key) {
                current = current.next;
            } else if (k == current.next.key) {
                return current.next;
            } else {
                if (current.down != null) {
                    current = current.down;
                } else {
                    return current;
                }
            }
        }
        return null;
    }

    public void delete(int k) {
        Node current = deleteSearch(k);
        if (current.key != k) {
            System.out.println("error");
        } else {
            while (true) {
                while (current.up != null) {
                    current = current.up;
                }
                while (current.down != null) {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                    current = current.down;
                    current.up = null;
                }
                current.next.prev = current.prev;
                current.prev.next = current.next;
                if (current.prev.key == k || current.next.key == k) {
                    if (current.next.key == k) {
                        current = current.next;
                    } else if (current.prev.key == k) {
                        current = current.prev;
                    }
                } else {
                    return;
                }
            }

        }

    }

    public void print() {
        Node current = head[0];
        if (current.next.key == tail[0].key) {
            System.out.println("empty");
        } else {
            current = current.next;
            while (current.next != null) {
                System.out.print(current.key + " ");
                current = current.next;
            }
            System.out.println();
        }
    }

    public boolean mainSearch(int key) {
        Node current = head[9];
        while (current != null) {
            if (key == current.next.key) {
                return true;
            } else if (key > current.next.key) {
                current = current.next;
            } else {
                current = current.down;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        SkipList skipList = new SkipList();
        String entry = "";
        String[] splitEntry;
        while (input.hasNext()) {
            entry = input.nextLine();
            splitEntry = entry.split(" ");
            switch (splitEntry[0]) {
                case "Insert":
                    skipList.insert(Integer.parseInt(splitEntry[1]));
                    break;
                case "Search":
                    if (skipList.mainSearch(Integer.parseInt(splitEntry[1]))) {
                        System.out.println("true");
                    } else {
                        System.out.println("false");
                    }
                    break;
                case "Delete":
                    skipList.delete(Integer.parseInt(splitEntry[1]));
                    break;
                case "Print":
                    skipList.print();
                    break;
            }
        }
    }
}

