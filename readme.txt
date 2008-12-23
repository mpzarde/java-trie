Introduction
============

A trie (pronounce "try") is a tree-based data structure in order to support
fast pattern matching. The "trie" comes from the word "retrieval".

This structure is particularly useful for any application requiring prefix
based (read "starts with") pattern matching.

A good example is any kind of application where we let the user type and 
quickly come up with a list of words starting with what the user typed in.

The classes included in the package are an adaptation of the trie Abstract
Data Type (ADT) to Java. The original work was done by Koders at 
http://www.koders.com/java/fid0F06E53F2CFCC6E591C38752F355A7178F92FFE5.aspx?s=trie.

I adapted the Koders classes to eliminate fixed alphabets and add case 
sensitivity to the searches (might be something I make optional at some point).

I'm including an IntelliJ IDEA project if you'd like to play with these classes
(run the com.truecool.trie.TrieRunner.main method after you compile) but you should be able
to incorporate these with ease into your project by copying into your project
source and modifying package declarations accordingly.

Advantages
==========

Numerous applications today depend on a database to perform their prefix based
pattern matching. This is probably adequate for finding valid data values for 
a given entry field but this approach has limitations which are overcome by 
the use of an in-memory trie data structure.

As an example imagine that we want to let the user enter a given value and then
not only return valid data values but in fact also return "matching" objects 
such that these can be used right away by our application.

Using a database would give us the valid data values but each "matching" object 
would then have to be found and then subsequently instantiated whereas all of 
these objects could easily be cached in memory and immediately accessed using 
our trie.

Usage
=====

Step one is to instantiate and load our trie;

The trie can be instantiated simply by invoking the default constructor:

com.truecool.trie.Trie main = new com.truecool.trie.Trie();

To add entries to the trie simply use the insert method as follows:

main.insert(key, object);

where key is the String pattern you may wish to match against and object is 
the data item to retrieve when a search is performed.

The key and object parameters could in fact be the same if you wanted to do
simple pattern matching.

Step two is to perform a search against our trie and use the resulting data:

Object result = null;
result = main.search(entry);
if (result == null) {
	// No match found
  System.out.println("Not found");
} else if (result instanceof com.truecool.trie.Trie) {
	// Multiple matches, data will hold sorted list of matching objects
  System.out.println("Multiple matches:");
  com.truecool.trie.Trie trie = (com.truecool.trie.Trie) result;
  List data = trie.getSortedList();
} else {
	// Exact match result is our object from inser operation
  System.out.println("Found. Value is " + result);
}

Additionally we also have a delete operation if we need to remove a data entry
from the trie:

main.remove(entry);

Notes
=====

This implementation isn't quite finished but should work adequately where 
needed.

To do:

1) Make case sensitivity optional
2) Separate dump type functions into separate class to have clean trie class
3) Clean up the java interface and add javadocs everywhere.

If you have any questions or feedback feel free to drop me an email at
mpzarde@truecool.com.
