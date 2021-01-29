//Author Name: Davi Albuquerque Vieira de Mello
//Date: 1/31/2021
//Program Name: Mello_text_analyzer
//Purpose: Write a text analyzer that reads a file and outputs statistics about that file. It should output the word frequencies of all words in the file, sorted by the most frequently used word. The output should be a set of pairs, each pair containing a word and how many times it occurred in the file.

// File link: http://shakespeare.mit.edu/macbeth/full.html

package text_analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Mello_text_analyzer {

	public static void main(String[] args) throws IOException {

		// Asks for URL input
		System.out.println("Enter the URL of the file:");
		
		// Creates Scanner
		Scanner scanner = new Scanner(System.in);
		
		// URL input
		URL html = new URL(scanner.next());
		
		// Creates BufferedReader object to read URL input
		BufferedReader br = new BufferedReader(
		new InputStreamReader(html.openStream()));
		
		// Loops through file when it's ready
		while (br.ready()) {
			HashMap<String, Integer> wordList = new HashMap<>();
			
			// Collects the text
			String text = br.lines().collect(Collectors.joining());
			
			// Calls the parameterized method 
			wordList = wordCount(wordSplit(text));
			
			tableData(wordList);
		}
		
		// Closes scanner
		scanner.close();	
	}
	
	
	// Method to remove HTML tags present in the file and split into words
	public static List<String> wordSplit(String text) {
		
		// Creates an ArrayList for the text that will be split into words later on 
		ArrayList<String> list = new ArrayList<>();
		
		// Creates a stream of the list that splits the text into HTML tags, trimming the whitespace between the tags
		list = (ArrayList<String>) Stream.of(text.split("(?=<)"))
			      .map (word -> new String(word).trim())
			      .collect(Collectors.toList());
		
		// Loops over the tags list 
		for (int i = 0; i < list.size(); i++) {
			int lastIndex = list.get(i).length() - 1;
			int charIndex = list.get(i).indexOf(">");
			
			// Removes from ArrayList if element is tag only (no text), contains the title of the file, or ends with a special character  
			if(lastIndex == charIndex || list.get(i).contains("<title>") || list.get(i).endsWith("|")) {
				list.remove(list.get(i));
				i--; // Moves forward to next element
			}
			
			// Removes the tag from text element and trims the whitespace
			else if(list.get(i).contains("<") && list.get(i).contains(">")) {
				String tag = list.get(i).substring(list.get(i).indexOf("<"), list.get(i).indexOf(">") + 1);
				String removeTag = list.get(i).replaceAll(tag, "");
				list.set(i, removeTag.trim());
			}
		}
		
		// Creates a stream of the list to eliminate punctuation marks and split into words. Empty or null elements get filtered out of the list
		list = (ArrayList<String>) list.stream()
	            .map(word -> word.replaceAll("[^A-Za-z0-9]", " ").split(" "))
	            .flatMap(Arrays::stream)
	            .filter(item -> item != null && !item.isEmpty())
	            .collect(Collectors.toList());	
		
		return list;
	}
	
	
	// Receives the ArrayList of split words as a parameter to perform word count
	public static HashMap<String, Integer> wordCount(List<String> list) {
		
//		Creates a HashMap which key = word and value = word count. HashMap doesn't allow duplicate keys, so there won't be duplicate words 
		HashMap<String, Integer> wordList = new HashMap<>();
		int wordCount = 1;
		
		for (String word : list) {
			if (!wordList.containsKey(word)) {
				wordList.put(word, wordCount);
			}
			else {
				int newValue = wordList.get(word) + 1;
				wordList.replace(word, newValue);
			}	
		}
		return wordList;
	}
	
	
	// Formats data in a table
	public static void tableData(HashMap<String, Integer> wordList) {
		String leftAlignFormat = "| %-15s | %-4d |%n";

		System.out.format("%n+-----------------+------+%n");
		System.out.format("| Words     | Word Count   |%n");
		System.out.format("+-----------------+------+%n");
		
		// Creates a stream of the HashMap and sort it in descending order and prints it to the console
		wordList.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.forEach(word -> System.out.format(leftAlignFormat, word.getKey(), word.getValue()));
		
		System.out.format("+-----------------+------+%n");
	}
}
