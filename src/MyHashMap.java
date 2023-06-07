import java.util.Iterator;

/**
 * Class for a simple hash map.
 * @author Andrew Jung
 * @version 1.0.1 November 15, 2022
 * Some methods were provided by my professor
 */
public class MyHashMap<K extends Comparable<K>, V> implements MyMap<K, V> {
    // Helpful list of primes available at:
    // https://www2.cs.arizona.edu/icon/oddsends/primes.htm
    private static final int[] primes = new int[] {
            101, 211, 431, 863, 1733, 3467, 6947, 13901, 27803, 55609, 111227,
            222461 };
    private static final double MAX_LOAD_FACTOR = 0.75;
    private MapEntry<K, V>[] table;
    private int primeIndex, numEntries;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new MapEntry[primes[primeIndex]];
    }

    /**
     * Returns the number of buckets in this MyHashMap.
     * @return the number of buckets in this MyHashMap
     */
    public int getTableSize() {
        return table.length;
    }

    /**
     * Returns the number of key-value mappings in this map.
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return numEntries;
    }

    /**
     * Returns true if this map contains no key-value mappings.
     * @return true if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return numEntries == 0;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * @param  key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or null if this
     *         map contains no mapping for the key
     */
    @Override
    public V get(K key) {
        int index = Math.abs(key.hashCode()) % table.length;
        if(table[index] == null){
            return null;
        }
        else{
            MapEntry<K, V> curr = table[index];
            while(curr != null){
                int compare = key.compareTo(curr.key);
                if(compare == 0){
                    return curr.value;
                }
                curr = curr.next;
            }
        }
        return null;
    }

    /**
     * Associates the specified value with the specified key in this map. If the
     * map previously contained a mapping for the key, the old value is replaced
     * by the specified value.
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     * @return the previous value associated with key, or null if there was no
     *         mapping for key
     */
    @Override
    public V put(K key, V value) {
        MapEntry<K, V> n = new MapEntry<>(key, value);
        int index = Math.abs(key.hashCode()) % table.length;
        //if the table at that index is null
        //then just set that index to the new entry
        MapEntry<K, V> chain = table[index];
        if(chain == null){
            table[index] = n;
            numEntries++;
            return null;
        }
        else{
            MapEntry<K, V> pointer = chain;
            MapEntry<K, V> curr = null;
            //iterates through entries till it reaches the end
            while(pointer != null){
                int compare = key.compareTo(pointer.key);
                //if key is found then just replace the value
                if(compare == 0){
                    V oldValue = pointer.value;
                    pointer.value = value;
                    return oldValue;
                }
                curr = pointer;
                pointer = pointer.next;
            }
            //after reaching the end of the list, just add the new entry to the end
            curr.next = n;
            numEntries++;
        }
        if(getLoadFactor() > MAX_LOAD_FACTOR && getTableSize() < 222461){
            rehash();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        MapEntry<K, V>[] entriesToRehash = new MapEntry[numEntries+1];
        int index = 0;
        Iterator<Entry<K, V>> itr = iterator();
        while(itr.hasNext()){
            Entry<K, V> e = itr.next();
            //getting rid of pointers
            MapEntry<K, V> s = new MapEntry<>(e.key, e.value);
            entriesToRehash[index++] = s;
        }
        primeIndex++;
        table = new MapEntry[primes[primeIndex]];
        for(int i = 0; i < entriesToRehash.length-1; i++){
            MapEntry<K, V> entry = entriesToRehash[i];
            K key = entry.key;
            int in = Math.abs(key.hashCode()) % table.length;
            MapEntry<K, V> chain = table[in];
            if(chain == null){
                table[in] = entry;
            }
            else{
                MapEntry<K, V> curr = chain;
                MapEntry<K, V> prev = null;
                while(curr != null){
                    prev = curr;
                    curr = curr.next;
                }
                prev.next = entry;
            }
        }
    }

    /**
     * Removes the mapping for a key from this map if it is present.
     * @param key the key whose mapping is to be removed from the map
     * @return the previous value associated with key, or null if there was no
     *         mapping for key
     */
    @Override
    public V remove(K key) {
        int index = Math.abs(key.hashCode()) % primes[primeIndex];
        if(table[index] == null){
            return null;
        }
        else{
            V oldValue = null;
            MapEntry<K, V> curr = table[index];
            MapEntry<K, V> prev = null;
            while(curr.next != null){
                int compare = key.compareTo(curr.key);
                if(compare == 0){
                    oldValue = curr.value;
                    break;
                }
                prev = curr;
                curr = curr.next;
            }
            //if prev and oldValue are both null,
            //it means that there was only one entry at the index
            if(prev == null && oldValue == null){
                //we have to see if this only entry matches the key
                if(key.compareTo(curr.key) == 0){
                    oldValue = curr.value;
                    table[index] = null;
                    numEntries--;
                    return oldValue;
                }
            }
            //else if prev is just null, that means the first entry
            //is the key we were looking for
            //so set the first entry to be the second
            else if(prev == null){
                table[index] = curr.next;
                numEntries--;
                return oldValue;
            }
            //else if oldValue is not null by itself
            //it means that the key is in the middle of two entries
            //set the next of prev to the next of curr
            else if(oldValue != null){
                prev.next = curr.next;
                numEntries--;
                return oldValue;
            }
            //else if oldValue is null, means that curr is
            //at the last entry. Check whether the last entry is
            //the key we are looking for. If it is, then set
            //prev.next to null
            else if(key.compareTo(curr.key) == 0){
                oldValue = curr.value;
                prev.next = null;
                numEntries--;
                return oldValue;
            }
        }
        //else key is not in the map
        return null;
    }

    /**
     * Returns the load factor of this MyHashMap, defined as the number of
     * entries / table size.
     * @return the load factor of this MyHashMap
     */
    public double getLoadFactor() {
        return (double)numEntries / primes[primeIndex];
    }

    /**
     * Returns the maximum length of a chain in this MyHashMap. This value
     * provides information about how well the hash function is working. With a
     * max load factor of 0.75, we would like to see a max chain length close
     * to 1.
     * @return the maximum length of a chain in this MyHashMap
     */
    public int computeMaxChainLength() {
        int maxChainLength = 0;
        for (MapEntry<K, V> chain : table) {
            if (chain != null) {
                int currentChainLength = 0;
                MapEntry<K, V> chainPtr = chain;
                while (chainPtr != null) {
                    currentChainLength++;
                    chainPtr = chainPtr.next;
                }
                if (currentChainLength > maxChainLength) {
                    maxChainLength = currentChainLength;
                }
            }
        }
        return maxChainLength;
    }

    /**
     * Returns a string representation of this MyHashMap for tables with up
     * to and including 1000 entries.
     * @return a string representation of this MyHashMap
     */
    public String toString() {
        if (numEntries > 1000) {
            return "HashMap too large to represent as a string.";
        }
        if (numEntries == 0) {
            return "HashMap is empty.";
        }
        int maxIndex;
        for (maxIndex = table.length - 1; maxIndex >= 0; maxIndex--) {
            if (table[maxIndex] != null) {
                break;
            }
        }
        int maxIndexWidth = String.valueOf(maxIndex).length();
        StringBuilder builder = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        for (int i = 0; i < table.length; i++) {
            MapEntry<K, V> chain = table[i];
            if (chain != null) {
                int indexWidth = String.valueOf(i).length();
                builder.append(" ".repeat(maxIndexWidth - indexWidth));
                builder.append(i);
                builder.append(": ");
                while (chain != null) {
                    builder.append(chain);
                    if (chain.next != null) {
                        builder.append(" -> ");
                    }
                    chain = chain.next;
                }
                builder.append(newLine);
            }
        }
        return builder.toString();
    }

    /**
     * Returns an iterator over the Entries in this map in the order
     * in which they appear.
     * @return an iterator over the Entries in this map
     */
    public Iterator<Entry<K, V>> iterator() {
        return new MapItr();
    }

    private class MapItr implements Iterator<Entry<K, V>> {
        private MapEntry<K, V> current;
        private int index;

        MapItr() {
            advanceToNextEntry();
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Entry<K, V> next() {
            MapEntry<K, V> e = current;
            if (current.next == null) {
                index++;
                advanceToNextEntry();
            } else {
                current = current.next;
            }
            return e;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void advanceToNextEntry() {
            while (index < table.length && table[index] == null) {
                index++;
            }
            current = index < table.length ? table[index] : null;
        }
    }

    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        int upperLimit = 100;
        int expectedSum = 0;
        for (int i = 1; i <= upperLimit; i++) {
            map.put(String.valueOf(i), i);
            expectedSum += i;
        }
        System.out.println("Size            : " + map.size());
        System.out.println("Table size      : " + map.getTableSize());
        System.out.println("Load factor     : " + map.getLoadFactor());
        System.out.println("Max chain length: " + map.computeMaxChainLength());
        System.out.println();
        System.out.println("Expected sum: " + expectedSum);
        System.out.println(map);

        int receivedSum = 0;
        for (int i = 1; i <= upperLimit; i++) {
            receivedSum += map.get(String.valueOf(i));
        }
        System.out.println("Received sum: " + receivedSum);

        expectedSum = 0;
        for (int i = 1; i <= upperLimit; i++) {
            int newValue = upperLimit - i + 1;
            map.put(String.valueOf(i), newValue);
            expectedSum += newValue;
        }
        System.out.println("Size            : " + map.size());
        System.out.println("Table size      : " + map.getTableSize());
        System.out.println("Load factor     : " + map.getLoadFactor());
        System.out.println("Max chain length: " + map.computeMaxChainLength());
        System.out.println();
        System.out.println("Expected sum: " + expectedSum);

        receivedSum = 0;
        for (int i = 1; i <= upperLimit; i++) {
            receivedSum += map.get(String.valueOf(i));
        }
        System.out.println("Received sum: " + receivedSum);

        receivedSum = 0;
        Iterator<Entry<String, Integer>> iter = map.iterator();
        while (iter.hasNext()) {
            receivedSum += iter.next().value;
        }
        System.out.println("Received sum: " + receivedSum);

        receivedSum = 0;
        for (int i = 1; i <= upperLimit; i++) {
            receivedSum += map.remove(String.valueOf(i));
        }
        System.out.println("Received sum: " + receivedSum);
        System.out.println("Size            : " + map.size());
        System.out.println("Table size      : " + map.getTableSize());
        System.out.println("Load factor     : " + map.getLoadFactor());
        System.out.println("Max chain length: " + map.computeMaxChainLength());
        System.out.println();
        System.out.println("Expected sum: " + expectedSum);
    }
}
