import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Set;

public class TrieAutocomplete implements Autocompletor {

	protected Node myRoot;

	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);

		for (int i = 0; i < terms.length; i++) {
			add(terms[i], weights[i]);
		}
	}

	private void add(String word, double weight) {

		if(word == null){
			throw new NullPointerException();
		}
		if(weight < 0){
			throw new IllegalArgumentException();
		}

		Node current = myRoot;
		for(int i = 0; i < word.length(); i++){
			char nextCh = word.charAt(i);
			if(current.mySubtreeMaxWeight < weight){
				current.mySubtreeMaxWeight = weight;
			}

			if(!current.children.containsKey(nextCh)){
				current.children.put(nextCh, new Node(nextCh, current, weight));
			}

			current = current.getChild(nextCh);
		}

		if(current.mySubtreeMaxWeight < weight){
			current.mySubtreeMaxWeight = weight;
		}

		double firstCurrent = current.myWeight;
		current.myWeight = weight;
		current.isWord = true;
		current.myWord = word;

		if(firstCurrent > current.myWeight){
			while(current != myRoot){

				current.mySubtreeMaxWeight = current.myWeight;
				for(Character i : current.children.keySet()){
					if(current.children.get(i).myWeight > current.mySubtreeMaxWeight){
						current.mySubtreeMaxWeight = current.children.get(i).myWeight;
					}
				}
				current = current.parent;
			}
		}
	}

	public String[] topKMatches(String prefix, int k) {

		if(prefix == null){
			throw new NullPointerException();
		}

		Node current = myRoot;

		PriorityQueue<Node> pqMax = new PriorityQueue<Node>(k, new Node.ReverseSubtreeMaxWeightComparator());
		ArrayList<Term> terms = new ArrayList<Term>();

		for(int i = 0; i < prefix.length(); i++){
			char nextCh = prefix.charAt(i);
			if(!current.children.containsKey(nextCh)){
				return new String[0];
			}
			current = current.children.get(nextCh);
		}

		pqMax.add(current);

		while(!pqMax.isEmpty()){
			current = pqMax.remove();
			if(current.isWord){
				Term curr = new Term(current.myWord, current.myWeight);
				terms.add(curr);
			}

			if(terms.size() >= k){
				Collections.sort(terms, new Term.ReverseWeightOrder());
				if(terms.get(k-1).getWeight() >= current.mySubtreeMaxWeight){
					break;
				}
			}

			for(Character i : current.children.keySet()){
				pqMax.add(current.children.get(i));
			}
		}

		if(k > terms.size()){
			k = terms.size();
		}

		String[] ret = new String[k];
		for(int i = 0; i < k; i++){
			ret[i] = terms.get(i).getWord();
		}
		return ret;
	}

	public String topMatch(String prefix) {

		if(prefix == null){
			throw new NullPointerException();
		}

		Node current = myRoot;

		for(int i = 0; i < prefix.length(); i++){
			char nextCh = prefix.charAt(i);
			if(!current.children.containsKey(nextCh)){
				return "";
			}
			current = current.children.get(nextCh);
		}

		while(current.mySubtreeMaxWeight != current.myWeight){
			Set<Character> s = current.children.keySet();
			for(Character i : s){
				if(current.children.get(i).mySubtreeMaxWeight == current.mySubtreeMaxWeight){
					current = current.children.get(i);
					break;
				}
			}
		}
		return current.myWord;

	}
}
