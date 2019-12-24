import java.util.HashMap;
import java.util.ArrayList;



public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>();
		this.internet = new MyWebGraph();
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	/*public void crawlAndIndex(String url) throws Exception {

		if (internet.getVisited(url)  != true){
			this.internet.addVertex(url);
			internet.setVisited(url, true);
			wordIndex.put(url, parser.getContent(url));
			ArrayList<String> x = parser.getLinks(url);
			for (int i = 0; i < x.size(); i++) {
				this.internet.addVertex(x.get(i));
				this.internet.addEdge(url, x.get(i));
				}
			for (int i = 0; i < x.size(); i++) {
				crawlAndIndex(x.get(i));
			}
		}

	}
	*/
	public void crawlAndIndex(String url) throws Exception{
		if(internet.getVisited(url) != true){
			this.internet.addVertex(url);
			internet.setVisited(url, true);
			ArrayList<String> z = parser.getContent(url);
			ArrayList<String> wordDuplicate = parser.getContent(url);
			ArrayList<String> tempList = new ArrayList<String>();
			for (String duplicateWord : wordDuplicate) {
				if(!tempList.contains(duplicateWord)){
					tempList.add(duplicateWord);
				}
			}
			for(int i = 0; i < parser.getContent(url).size(); i++){
				wordIndex.put(url, tempList);
			}
			ArrayList<String> x = parser.getLinks(url);
			for(int i = 0; i < x.size(); i++){
				this.internet.addVertex(x.get(i));
				this.internet.addEdge(url, x.get(i));
			}
			for(int i = 0; i < x.size(); i++){
				crawlAndIndex(x.get(i));
			}
		}
	}
	
	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	public void assignPageRanks(double epsilon) {
		for (int i = 0; i < this.internet.getVertices().size(); i++) {
			this.internet.setPageRank(this.internet.getVertices().get(i), 1.0);
		}
		int counter = 0;
		int convergance = 0;
		ArrayList<Double> first = this.computeRanks(this.internet.getVertices());
		ArrayList<Double> last = this.computeRanks(this.internet.getVertices());
		while (convergance != 1){
			for (int i = 0; i < this.internet.getVertices().size(); i++) {
				if (first.get(i) - last.get(i) > epsilon){
					first = last;
					last = this.computeRanks(this.internet.getVertices());
				}
				else {
					int h = i + 1;
					while (h<this.internet.getVertices().size() && first.get(h) - last.get(h) < epsilon) {
						counter = counter + 1;
						h = h + 1;


						if (counter == this.internet.getVertices().size() - 1) {
							convergance = convergance + 1;
							break;
						}
					}
				}
			}
		}

	}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
	    ArrayList<Double> x = new ArrayList<Double>();
        for (int i = 0; i < vertices.size(); i++) {
        	Double sum = 0.0;
        	ArrayList<String> edges = this.internet.getEdgesInto(vertices.get(i));
			for (int j = 0; j < edges.size(); j++) {
				sum = sum + this.internet.getPageRank(edges.get(j))/this.internet.getOutDegree(edges.get(j));

			}
			x.add((0.5 + 0.5*sum));
			this.internet.setPageRank(vertices.get(i), x.get(i));
        }
        return x;
	}

	
	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method should take about 25 lines of code.
	 */
	/*public ArrayList<String> getResults(String query) {
		ArrayList<String> x = new ArrayList<String>();
		ArrayList<String> y = new ArrayList<String>();
		int hi = internet.getVertices().size();
		int count = 0;
		while (wordIndex.values().stream().toArray().length != 0) {
			x = wordIndex.values().stream().findFirst().get();
			if (x.contains(query)) {
				y.add(internet.getVertices().get(count));
			}
			wordIndex.remove(internet.getVertices().get(count));
			count = count +1;
		}
		HashMap<String, Double> stringIntegerHashMap = new HashMap<String, Double>();
		for (int i = 0; i < y.toArray().length; i++) {
			stringIntegerHashMap.put(y.get(i), internet.getPageRank(y.get(i)));
		}

        ArrayList<String> g = (Sorting.fastSort(stringIntegerHashMap));
		return g;
	}
}*/

	public ArrayList<String> getResults(String query){
		ArrayList<String> x = new ArrayList<String>();
		ArrayList<String> y = new ArrayList<String>();
		int hi = internet.getVertices().size();
		int count  = 0;
		HashMap<String, ArrayList<String>> hello = (HashMap<String, ArrayList<String>>) wordIndex.clone();
		while (hello.values().stream().toArray().length != 0){
			x = hello.values().stream().findFirst().get();
			if(x.contains(query)) {
				y.add(internet.getVertices().get(count));
			}
			hello.remove(internet.getVertices().get(count));
			count = count + 1;
		}
		HashMap<String,Double> stringIntegerHashMap = new HashMap<String,Double>();
		for (int i = 0; i < y.toArray().length; i++){
			stringIntegerHashMap.put(y.get(i), internet.getPageRank(y.get(i)));
		}

		ArrayList<String> g = (Sorting.fastSort(stringIntegerHashMap));
		return g;
	}
}