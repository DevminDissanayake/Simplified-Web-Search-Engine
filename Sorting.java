import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) <0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable> ArrayList<K> fastSort(HashMap<K, V> results) {
    	// ADD YOUR CODE HERE
    	 ArrayList<K> sortedUrls = new ArrayList<K>();
         sortedUrls.addAll(results.keySet());
    	 for (int i = sortedUrls.size() / 2 - 1; i >= 0; i--) 
             Heapify(results,sortedUrls,i,sortedUrls.size()); 
    	 
    	 for (int i=sortedUrls.size()-1; i>=0; i--) 
         { 
             // Move current root to end 
    		 K temp = sortedUrls.get(0);
    		 sortedUrls.set(0, sortedUrls.get(i));
            
    		 sortedUrls.set(i, temp);
   
             // call max heapify on the reduced heap 
             Heapify(results,sortedUrls, 0, i); 
         } 
    	 return sortedUrls;
    }
    public static <K, V extends Comparable> void Heapify(HashMap<K, V> results,ArrayList<K> sortedUrls,int i,int size) {
    	// ADD YOUR CODE HERE
    	int largest=i;
    	int right=2*i+2;
    	int left=2*i+1;
    	if(left<size && results.get(sortedUrls.get(left)).compareTo(results.get(sortedUrls.get(largest)))<0)
    			{
    					largest=left;
    			}
    	if(right<size && results.get(sortedUrls.get(right)).compareTo(results.get(sortedUrls.get(largest)))<0)
		{
				largest=right;
		}
    	if(largest!=i)
    	{
    		K temp = sortedUrls.get(i);
			sortedUrls.set(i, sortedUrls.get(largest));
			sortedUrls.set(largest, temp);	
			Heapify(results, sortedUrls, largest,size);
    	}
  
    }
}