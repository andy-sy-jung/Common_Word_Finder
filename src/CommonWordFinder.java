import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * A class for finding the n most common words in a text file
 * using three different data structures
 * @author Andrew Jung asj2156
 * @version December 19 2022
 */

public class CommonWordFinder {

    int userLimit;
    MyMap<String, Integer> userMap;

    public CommonWordFinder() {
    }
    public static String[] validDataStructures = {"avl", "hash", "bst"};

    /**
     * Returns true if string entered in matches any of the strings in the ValidDataStructures array
     * @param dataStructure a command line argument string specifying which data structure to use
     * @return true if the parameter is a valid data structure
     */
    public static boolean isValidDataStructure(String dataStructure){
        for(String structure : validDataStructures){
            if(dataStructure.equals(structure)){
                return true;
            }
        }
        return false;
    }

    /**
     * returns true if the symbol is a valid; i.e. all lowercase letters, -, '; false otherwise
     * @param symbol the symbol to check
     * @return true if symbol is valid
     */
    public static boolean isValidSymbol(char symbol){
        return (symbol >= 97 && symbol <= 122) || symbol == 39 || symbol == 45;
    }

    /**
     * returns true if symbol is any form of spacing character; false otherwise
     * @param symbol the symbol to check
     * @return true if symbol is any form of white space
     */
    public static boolean isWhiteSpace(char symbol){
        return symbol <= 32 || symbol == ' ' || symbol == '\t' || symbol == '\n';
    }

    /**
     * returns true if the symbol is not valid; i.e. everything but the -; false otherwise
     * @param symbol the symbol to check
     * @return true if symbol is illegal
     */
    public static boolean isIllegalSymbol(char symbol){
        return (symbol >= 33 && symbol <= 96 && symbol != 39) || symbol == '{' || symbol == '}'
                || symbol == '|' || symbol == '~';
    }

    /**
     * transfers the data stored in the map to an array of Entry objects, then returns
     * a sorted array of Entry objects in descending value order first and then lexicographically
     * @param map the data structure being used
     * @return a sorted array of the most common words in a text file
     */
    public static Entry<String,Integer>[] mostCommonWords(MyMap<String,Integer> map){
        Entry<String,Integer>[] arrayOfWords = new Entry[map.size()];
        Iterator<Entry<String,Integer>> iterator = map.iterator();
        int index = 0;
        while(iterator.hasNext()){
            Entry<String,Integer> e = iterator.next();
            arrayOfWords[index] = e;
            index++;
        }
        Arrays.sort(arrayOfWords, new sortingByEntry());
        return arrayOfWords;
    }

    /**
     * Takes a file and specific data structure, parses through the file character by character
     * building a string until it forms a word, then adding it to the data structure as the key,
     * incrementing its associated value everytime it comes across the same word
     * @param file the text file to read and parse
     * @param map the data structure to store words in
     * @throws IOException throws an IO exception if there is a problem reading the file
     */
    public static void parseAndStoreWords(File file, MyMap<String,Integer> map) throws IOException{
        // how to use BufferedReader was referenced from
        // https://www.candidjava.com/tutorial/program-to-read-a-file-character-by-character/
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        int c = 0;
        while((c = bufferedReader.read()) != -1){
            StringBuilder sb = new StringBuilder();
            char character = (char) c;
            character = Character.toLowerCase(character);
            // if character is a white space or an illegal symbol, skip loop
            if(isWhiteSpace(character) || isIllegalSymbol(character)){
                continue;
            }
            // otherwise, append to sb
            sb.append(character);
            // continues until it is the end or reaches a white space (indicating the end of a word)
            while(!isWhiteSpace((char) c) && c != -1){
                c = bufferedReader.read();
                character = (char) c;
                character = Character.toLowerCase(character);
                // if there is an invalid symbol in between a word, ignore and continue
                // the white space condition is redundant, but noticed that the program
                // ran significantly faster with it in
                if(isWhiteSpace(character) || !isValidSymbol(character)){
                    continue;
                }
                sb.append(character);
            }
            // sb should now contain a word, so convert to string
            String word = sb.toString();
            // if the word does not exist in map, then add with value 1
            if(map.get(word) == null){
                map.put(word,1);
            }
            // else increment the existing value by 1
            else{
                int result = map.get(word);
                map.put(word,result+1);
            }
        }
        // close file
        bufferedReader.close();
    }

    /**
     * takes the sorted array of most common words and prints the limit amount in a list format
     * @param limit the number of most common words to be printed
     * @param arrayOfWords the sorted array of most common words
     */
    public static void printMostCommonWords(int limit, Entry<String,Integer>[] arrayOfWords){
        // checking to see what the longest word is in the list that is to be printed
        int trailingSpace = 0;
        for (int i = 0; i < limit; i++) {
            if (trailingSpace < arrayOfWords[i].key.length()) {
                trailingSpace = arrayOfWords[i].key.length();
            }
        }
        // add one to account for space between occurrence and word
        trailingSpace++;
        // the leading space for the numbers of the list
        int leadingSpace = String.valueOf(limit).length();
        // iterate through the array until the limit
        for(int i = 0; i < limit; i++){
            // String.format was referenced from https://www.javatpoint.com/java-string-format
            String word = arrayOfWords[i].key;
            int occurrence = arrayOfWords[i].value;
            // right justify number to the biggest number that will appear
            String numberFormat = "%" + leadingSpace + "d";
            String number = String.format(numberFormat, i+1) + ". ";
            // left justify the words with the longest word that will appear
            String wordFormat = "%-" + trailingSpace + "s";
            String commonWord = String.format(wordFormat, word) + occurrence;
            System.out.print(number + commonWord + System.lineSeparator());
        }
    }

    /**
     * takes the sorted array of most common words and returns a single string that contains the list.
     * @param limit the number of most common words to be printed
     * @param arrayOfWords the sorted array of most common words
     */
    public static String stringOfMostCommonWords(int limit, Entry<String, Integer>[] arrayOfWords){
        // checking to see what the longest word is in the list that is to be printed
        int trailingSpace = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < limit; i++) {
            if (trailingSpace < arrayOfWords[i].key.length()) {
                trailingSpace = arrayOfWords[i].key.length();
            }
        }
        // add one to account for space between occurrence and word
        trailingSpace++;
        // the leading space for the numbers of the list
        int leadingSpace = String.valueOf(limit).length();
        // iterate through the array until the limit
        for(int i = 0; i < limit; i++){
            // String.format was referenced from https://www.javatpoint.com/java-string-format
            String word = arrayOfWords[i].key;
            int occurrence = arrayOfWords[i].value;
            // right justify number to the biggest number that will appear
            String numberFormat = "%" + leadingSpace + "d";
            String number = String.format(numberFormat, i+1) + ". ";
            // left justify the words with the longest word that will appear
            String wordFormat = "%-" + trailingSpace + "s";
            String commonWord = String.format(wordFormat, word) + occurrence;
            sb.append(number).append(commonWord).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static void main(String[] args){
        // checks whether the command line arguments are formatted correctly
        if(args.length < 2 || args.length > 3){
            System.err.println("Usage: java CommonWordFinder <filename> <bst|avl|hash> [limit]");
            System.exit(1);
        }
        // assuming file will be in src file with program
        String filename = args[0];
        File file = new File(filename);
        String dataStructure = args[1];
        // initialize limit as 10, which is the base case
        int limit = 10;
        // checks whether the file exists or not
        if(!file.isFile()){
            System.err.println("Error: Cannot open file " + args[0] +  " for input.");
            System.exit(1);
        }
        // checks if data structure entered is valid
        else if(!isValidDataStructure(dataStructure)){
            System.err.println("Error: Invalid data structure " + dataStructure +  " received.");
            System.exit(1);
        }
        // if user decides to provide the limit, change limit
        // if limit is not a positive integer, print error statement
        else if(args.length == 3){
            limit = Integer.parseInt(args[2]);
            if(limit <= 0){
                System.err.println("Error: Invalid limit " + args[2] + " received.");
                System.exit(1);
            }
        }
        // initialize map based on the user input
        MyMap<String, Integer> map;
        if(dataStructure.equals("bst")){
            map = new BSTMap<>();
        }
        else if(dataStructure.equals("avl")){
            map = new AVLTreeMap<>();
        }
        else{
            map = new MyHashMap<>();
        }
        // parse and store words in map from file
        try{
            parseAndStoreWords(file, map);
        }
        // catches an IO exception when reading
        catch(IOException e){
            System.err.println("Error: An I/O error occurred reading " + args[0] + ".");
        }
        // map size indicates the number of unique words
        System.out.print("Total unique words: " + map.size() + System.lineSeparator());
        // get sorted array of most common words
        Entry<String,Integer>[] arrayOfWords = mostCommonWords(map);
        // if limit is greater than the number of unique words,
        // set limit to map size
        if(map.size() < limit){
            limit = map.size();
        }
        printMostCommonWords(limit, arrayOfWords);
    }

}

/**
 * A helper class extending the Comparator interface
 * Allows an array of Entry objects to be sorted by value (descending order)
 * and then by key
 * Referenced https://www.geeksforgeeks.org/arrays-sort-in-java-with-examples/
 * on how to use Arrays.Sort
 */
class sortingByEntry implements Comparator<Entry<String,Integer>> {

    @Override
    public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
        int compare;
        // compare first by value
        if(o1.value < o2.value){
            compare = 1;
        }
        else if(o1.value > o2.value){
            compare = -1;
        }
        // then by key if values are equal
        else{
            // Strings have built in compare method
            compare = o1.key.compareTo(o2.key);
        }
        return compare;
    }
}
