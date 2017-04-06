import java.util.Comparator;

public class Term implements Comparable<Term> {

	private final String myWord;
	private final double myWeight;

	public Term(String word, double weight) {
		myWord = word;
		myWeight = weight;
		if(myWord == null){
			throw new NullPointerException();
		}
		if(myWeight < 0){
			throw new IllegalArgumentException();
		}
	}

	public static class PrefixOrder implements Comparator<Term> {
		private final int r;

		public PrefixOrder(int r) {
			this.r = r;
		}

		public int compare(Term v, Term w) {

			if(v.myWord.length() < r || w.myWord.length() < r){
				return v.myWord.compareTo(w.myWord);
			}
			return v.myWord.substring(0, r).compareTo(w.myWord.substring(0, r));
		}
	}

	public static class ReverseWeightOrder implements Comparator<Term> {
		public int compare(Term v, Term w) {

			if(v.myWeight > w.myWeight){
				return -1;
			}
			return 1;
		}
	}

	public static class WeightOrder implements Comparator<Term> {
		public int compare(Term v, Term w) {

			if(v.myWeight < w.myWeight){
				return -1;
			}
			return 1;
		}
	}

	public int compareTo(Term that) {
		return myWord.compareTo(that.myWord);
	}

	public String getWord() {
		return myWord;
	}

	public double getWeight() {
		return myWeight;
	}

	public String toString() {
		return String.format("%14.1f\t%s", myWeight, myWord);
	}
}
