import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BinarySearchAutocomplete implements Autocompletor {

	Term[] myTerms;

	public BinarySearchAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		myTerms = new Term[terms.length];
		for (int i = 0; i < terms.length; i++) {
			myTerms[i] = new Term(terms[i], weights[i]);
		}
		Arrays.sort(myTerms);
	}

	public static int firstIndexOf(Term[] a, Term key, Comparator<Term> comparator) {
		if(a.length == 0){
			return -1;
		}
		int low = -1;
		int high = a.length - 1;
		int mid = 0;
		boolean found = false;
		while(high - low > 1){
			mid = (low + high)/2;
			if(comparator.compare(a[mid], key) == 0){
				high = mid;
				found = true;
			}
			else if(comparator.compare(a[mid], key) < 0){
				low = mid;
			}
			else{
				high = mid;
			}
		}

		if(comparator.compare(a[a.length - 1], key) == 0 && found == false){
			return a.length - 1;
		}
		else if(found){
			return high;
		}
		else{
			return -1;
		}
	}

	public static int lastIndexOf(Term[] a, Term key, Comparator<Term> comparator) {

		if(a.length == 0){
			return -1;
		}
		int low = 0;
		int high = a.length;
		int mid = 0;
		boolean found = false;
		while(high - low > 1){
			mid = (low + high)/2;
			if(comparator.compare(a[mid], key) == 0){
				low = mid;
				found = true;
			}
			else if(comparator.compare(a[mid], key) < 0){
				low = mid;
			}
			else{
				high = mid;
			}
		}
		if(comparator.compare(a[0], key) == 0 && found == false){
			return 0;
		}
		else if(found){
			return low;
		}
		else{
			return -1;
		}
	}

	public String[] topKMatches(String prefix, int k) {
		if(prefix == null){
			throw new NullPointerException();
		}

		PriorityQueue<Term> pq = new PriorityQueue<Term>(k, new Term.ReverseWeightOrder());

		Comparator<Term> comp = new Term.PrefixOrder(prefix.length());
		Term pre = new Term(prefix, 0);
		int last = lastIndexOf(myTerms, pre, comp);
		int first = firstIndexOf(myTerms, pre, comp);

		if(first == -1 || last == -1){
			return new String[0];
		}

		for(int i = first; i <= last; i++){
			pq.add(myTerms[i]);
		}

		int numResults = Math.min(k, pq.size());
		String[] ret = new String[numResults];
		for (int i = 0; i < numResults; i++) {
			ret[i] = pq.remove().getWord();
		}
		return ret;
	}

	public String topMatch(String prefix) {

		if(prefix == null){
			throw new NullPointerException();
		}

		Term pre = new Term(prefix, 1);
		Comparator<Term> comp = new Term.PrefixOrder(prefix.length());
		int last = lastIndexOf(myTerms, pre, comp);
		int first = firstIndexOf(myTerms, pre, comp);
		if(last == -1 || first == -1){
			return "";
		}	

		String maxTerm = "";
		double maxWeight = -1;
		for (int i = first; i <= last; i++) {
			if (myTerms[i].getWeight() > maxWeight) {
				maxTerm = myTerms[i].getWord();
				maxWeight = myTerms[i].getWeight();
			}
		}
		return maxTerm;
	}

}
