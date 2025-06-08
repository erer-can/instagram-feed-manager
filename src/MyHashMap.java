public class MyHashMap {

    private MyHashNode[] chains;
    private int capacity;
    private int size;
    private final double LOAD_FACTOR = 0.75;

    public MyHashMap(int initialCapacity) {
        this.capacity = initialCapacity;
        this.size = 0;
        this.chains = new MyHashNode[this.capacity];
    }

    private int hashFunction(String key) {
        int hash = 0;
        char[] var3 = key.toCharArray();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            char c = var3[var5];
            hash = (hash * 127 + c) % this.capacity;
        }

        if (hash < 0) {
            hash += this.capacity;
        }

        return hash;
    }

    public void insert(String key, Object value) {
        int index = this.hashFunction(key);
        MyHashNode head = this.chains[index];
        if (!this.contains(key)) {
            MyHashNode newNode = new MyHashNode(key, value);
            if (head == null) {
                this.chains[index] = newNode;
            } else {
                while (head.next != null) {
                    head = head.next;
                }

                head.next = newNode;
            }

            ++this.size;
            if ((double) this.size / (double) this.capacity > 0.75) {
                this.rehash();
            }
        }
    }

    public Object find(String key) {
        int index = this.hashFunction(key);

        for (
            MyHashNode head = this.chains[index];
            head != null;
            head = head.next
        ) {
            if (head.key.equals(key)) {
                return head.value;
            }
        }

        return null;
    }

    public boolean contains(String key) {
        return this.find(key) != null;
    }

    public void remove(String key) {
        int index = this.hashFunction(key);
        MyHashNode head = this.chains[index];
        if (head != null) {
            if (head.key.equals(key)) {
                this.chains[index] = head.next;
                --this.size;
            } else {
                MyHashNode prev = head;

                for (head = head.next; head != null; head = head.next) {
                    if (head.key.equals(key)) {
                        prev.next = head.next;
                        --this.size;
                        return;
                    }

                    prev = head;
                }
            }
        }
    }

    private void rehash() {
        MyHashNode[] oldChain = this.chains;
        this.capacity = 2 * this.capacity;
        this.chains = new MyHashNode[this.capacity];
        this.size = 0;
        MyHashNode[] var2 = oldChain;
        int var3 = oldChain.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            for (MyHashNode head = var2[var4]; head != null; head = head.next) {
                this.insert(head.key, head.value);
            }
        }
    }

    public String[] keys() {
        String[] keysArray = new String[this.size];
        int index = 0;
        MyHashNode[] var3 = this.chains;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            MyHashNode chain = var3[var5];

            for (
                MyHashNode current = chain;
                current != null;
                current = current.next
            ) {
                keysArray[index++] = current.key;
            }
        }

        return keysArray;
    }

    public Object[] values() {
        Object[] valuesArray = new Object[this.size];
        int index = 0;
        MyHashNode[] var3 = this.chains;
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            MyHashNode chain = var3[var5];

            for (
                MyHashNode current = chain;
                current != null;
                current = current.next
            ) {
                valuesArray[index++] = current.value;
            }
        }

        return valuesArray;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.capacity; ++i) {
            sb.append("Chain ").append(i).append(": ");

            for (
                MyHashNode head = this.chains[i];
                head != null;
                head = head.next
            ) {
                sb
                    .append("(")
                    .append(head.key)
                    .append(", ")
                    .append(head.value)
                    .append(") -> ");
            }

            sb.append("null\n");
        }

        return sb.toString();
    }

    private class MyHashNode {

        String key;
        Object value;
        MyHashNode next;

        public MyHashNode(String key, Object value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
}
