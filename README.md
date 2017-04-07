# Autocomplete
This project contains three different methods to implement an autocompleting word application.

Each class implements the Autocomplete interface

1. BruteAutocomplete
This class stores each term and its weight read in from the data in a sorted array. The weight represents how often that word appears in the data. To find the top matches based on weight, I use a priority queue that maintains a sorted queue of the number of matches I am looking for according to the weight order similarly implemented in the Term class.

2. BinraySearchAutocomplete
This class also stores each term and its weight read in from the data in a sorted array. It utilizes a binary search method to find the first index of a specified key in the array of terms, and also to find the last index of a specified key in the array of terms. It then uses these methods to cut down the size of the array of terms to iterate over when creating the priority queue from which we find our top matches.

3. TrieAutocomplete
This class stores each letter of each word in a trie data structure. This eliminates the dependence on the number of terms when finding matches because we can use the nodes to quickly navigate to words associated with a given prefix. One of the features that makes this approach faster is memoizing the weights of words in each node's subtrie, in the mySubtreeMaxWeight variable. That way, when iterating over the child nodes of a parent, we can quickly identify which one holds the word with the greatest weight. Therefore, to find the top matches, we can utilize a similar priority queue and a breadth-first search of the trie to find the matches.
