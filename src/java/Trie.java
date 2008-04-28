/*
** Based on Koders - Trie.java
** http://www.koders.com/java/fid0F06E53F2CFCC6E591C38752F355A7178F92FFE5.aspx?s=trie
**
**  Class History
**
**  Date        Name         Description
**  ---------|------------|-----------------------------------------------
**  19Aug98     subtle       start of recorded history
**  17Jul06     mpzarde      removed case in-sensitivity and finite alphabet, now uses maps to determine entries
**  05Sep06     mpzarde      made case sensitivity optional
**
*/

import java.util.*;

/**
 * The only thing that can't be stored in a Trie safely is another Trie - we use
 * instanceof to determine if a Trie is terminated or not
 */
public final class Trie {
// ------------------------------ FIELDS ------------------------------

  // Is this trie case sensitive or not?
  // This is a read-only property which can only be set using the public constructor. If we were to mess with
  // this value after the trie has been instantiated it would cause inconsistent results.
  private boolean _caseSensitive;

  //  Information about the location of this Trie in the
  //  trie tree ;)
  private int _position = 0;
  private Trie _previous = null;

  //  For when a string ends on this trie
  private TrieEntry _entry = null;

  //  The contents of this trie
  private Map _entries = null;

// --------------------------- CONSTRUCTORS ---------------------------

  /**
   * Creates a new trie with no entries
   */
  public Trie() {
    setCaseSensitive(true);
  }

  public Trie(boolean caseSensitive) {
    setCaseSensitive(caseSensitive);
  }

  //  Internal constructor
  private Trie(Trie parent) {
    setCaseSensitive(parent.isCaseSensitive());
    setPrevious(parent);
    setPosition(parent.getPosition() + 1);
  }

  public void setPosition(int position) {
    _position = position;
  }

  public int getPosition() {
    return (_position);
  }

// --------------------- GETTER / SETTER METHODS ---------------------

  public Map getEntries() {
    if (_entries == null) {
      _entries = new HashMap();
    }

    return _entries;
  }

  public void setEntries(Map entries) {
    _entries = entries;
  }

  public TrieEntry getEntry() {
    return _entry;
  }

  public void setEntry(TrieEntry entry) {
    _entry = entry;
  }

  public Trie getPrevious() {
    return _previous;
  }

  public void setPrevious(Trie previous) {
    _previous = previous;
  }

  public boolean isCaseSensitive() {
    return _caseSensitive;
  }

  private void setCaseSensitive(boolean caseSensitive) {
    _caseSensitive = caseSensitive;
  }

// ------------------------ CANONICAL METHODS ------------------------

  public String toString() {
    if (length() > 20) {
      return ("Multiple matches: <too many to list (>20)>");
    } else {
      return ("Multiple matches: " + contents(new StringBuffer()));
    }
  }

  /**
   * Counts the number of linked objects off this trie
   */
  public int length() {
    int count = 0;

    if (getEntry() != null) {
      count++;
    }

    if (getEntries() != null && getEntries().size() > 0) {
      Iterator iterator = getEntries().values().iterator();

      while (iterator.hasNext()) {
        Object object = iterator.next();
        if (object instanceof TrieEntry) {
          count++;
        } else {
          count += ((Trie) object).length();
        }
      }
    }


    return (count);
  }

  /**
   * Take current Trie and dump output to StringBuffer
   *
   * @param buffer
   * @return String holding content of current Trie   *
   */
  public String contents(StringBuffer buffer) {
    if (getEntry() != null) {
      buffer.append(getEntry().getKey());
      buffer.append(" ");
    }

    if (getEntries() != null && getEntries().size() > 0) {
      Iterator iterator = getEntries().values().iterator();

      while (iterator.hasNext()) {
        Object object = iterator.next();

        if (object instanceof Trie && (((Trie) object).getPrevious() == this)) {
          buffer.append(((Trie) object).contents());
        } else {
          buffer.append(((TrieEntry) object).getKey());
          buffer.append(" ");
        }
      }
    }
    return (buffer.toString());
  }

// -------------------------- OTHER METHODS --------------------------

  float averageClash() {
    TrieAverage trieAverage = new TrieAverage();
    averageClash(trieAverage);
    return (((float) trieAverage.getAverage()) / ((float) trieAverage.getNumber()));
  }

  void averageClash(TrieAverage average) {
    if (getEntry() != null) {
      average.add(getPosition());
    }

    if (getEntries() != null && getEntries().size() > 0) {
      Iterator iterator = getEntries().values().iterator();

      while (iterator.hasNext()) {
        Object object = iterator.next();

        if (object instanceof Trie && (((Trie) object).getPrevious() == this)) {
          ((Trie) object).averageClash(average);
        } else {
          average.add(getPosition());
        }
      }
    }
  }

  public String contents() {
    return (contents(new StringBuffer()));
  }

  void dump() {
    String pre = spaces();
    if (getEntry() != null) {
      System.out.println(pre + getEntry().getKey());
    }

    if (getEntries() != null && getEntries().size() > 0) {
      Iterator keyIterator = getEntries().keySet().iterator();
      Iterator entryIterator = getEntries().values().iterator();

      while (keyIterator.hasNext() && entryIterator.hasNext()) {
        String key = (String) keyIterator.next();
        Object object = entryIterator.next();

        if (object instanceof Trie && (((Trie) object).getPrevious() == this)) {
          System.out.println(pre + "[" + key + "]");
          ((Trie) object).dump();
        } else {
          System.out.println(pre + ((TrieEntry) object).getKey());
        }
      }
    }
  }

  String spaces() {
    StringBuffer s = new StringBuffer();

    for (int index = 0; index < getPosition(); index++) {
      s.append(" ");
    }

    return (s.toString());
  }

  /**
   * Returns an alphabetically sorted list of
   * the elements in this trie.  This routine
   * is rather inefficient.
   */
  public Enumeration elements() {
    Vector entries = new Vector();
    elements(entries);
    return (entries.elements());
  }

  /**
   * Recursive function to append elements
   * to a linked list
   */
  protected void elements(Vector vector) {
    if (getEntry() != null) {
      vector.addElement(getEntry().getData());
    }

    if (getEntries() != null && getEntries().size() > 0) {
      Iterator iterator = getEntries().values().iterator();

      while (iterator.hasNext()) {
        Object object = iterator.next();

        if (object instanceof Trie && (((Trie) object).getPrevious() == this)) {
          ((Trie) object).elements(vector);
        } else {
          vector.addElement(((TrieEntry) object).getData());
        }
      }
    }
  }

  /**
   * Returns an alphabetically sorted list of
   * the elements in this trie
   */
  public LinkedList getSortedList() {
    LinkedList ll = new LinkedList();

    listElements(ll);

    return (ll);
  }

  /**
   * Recursive function to append elements
   * to a linked list
   */
  protected void listElements(LinkedList ll) {
    if (getEntry() != null) {
      ll.addLast(getEntry().getData());
    }

    if (getEntries() != null && getEntries().size() > 0) {
      Iterator iterator = getEntries().values().iterator();

      while (iterator.hasNext()) {
        Object object = iterator.next();

        if (object instanceof Trie && (((Trie) object).getPrevious() == this)) {
          ((Trie) object).listElements(ll);
        } else {
          ll.addLast(((TrieEntry) object).getData());
        }
      }
    }
  }

  /**
   * Returns a Trie of all the elements starting with
   * 'key', or an Object which is a single exact match
   */
  public Object getTrieFor(String key) {
    String truekey = (isCaseSensitive() ? key : key.toUpperCase());

    if (truekey.length() == getPosition()) {
      return (this);
    }

    String positionKey = getPositionKey(truekey);

    if (getEntries().containsKey(positionKey)) {
      Object object = getEntries().get(positionKey);

      if (object instanceof Trie) {    //  pass it along the chain
        return (((Trie) object).getTrieFor(truekey));
      } else {
        if (((TrieEntry) object).getKey().startsWith(truekey.toLowerCase())) {
          return (((TrieEntry) object).getData());
        }
      }
    }

    return (null);
  }

  private String getPositionKey(String key) {
    int startPos = getPosition();
    int endPos = (getPosition() + 1 <= key.length() ? getPosition() + 1 : getPosition());

    String positionKey = key.substring(startPos, endPos);
    return positionKey;
  }

  /**
   * Inserts the provided object into the trie
   * with the given truekey.
   */
  public void insert(String key, Object item) throws NonUniqueKeyException {
    String truekey = (isCaseSensitive() ? key : key.toUpperCase());

    if (truekey.length() == getPosition()) {    //  this item needs to be inserted _on_ this trie
      if (getEntry() != null) {
        throw new NonUniqueKeyException(truekey);
      }

      setEntry(new TrieEntry(truekey, item));
    } else {
      String positionKey = getPositionKey(truekey);

      if (getEntries().containsKey(positionKey)) {    //  already an item here
        Object object = getEntries().get(positionKey);
        if (object instanceof Trie && (((Trie) object).getPrevious() == this)) {
          ((Trie) object).insert(truekey, item);
          return;
        } else {
          //  already something here - create a new
          //  trie, insert this something, and insert
          //  the new something
          TrieEntry entry = (TrieEntry) object;
          if (entry.getKey().equals(truekey)) {
            throw new NonUniqueKeyException(truekey);
          }

          Trie down = new Trie(this);
          down.insert(entry.getKey(), entry.getData());
          down.insert(truekey, item);

          getEntries().put(positionKey, down);
        }
      } else {    //  the space is empty - simply add it in
        getEntries().put(positionKey, new TrieEntry(truekey, item));
      }
    }
  }

  int numberTries() {
    int accum = 1;

    if (getEntries() != null && getEntries().size() > 0) {
      Iterator iterator = getEntries().values().iterator();

      while (iterator.hasNext()) {
        Object object = iterator.next();

        if (object instanceof Trie && (((Trie) object).getPrevious() == this)) {
          accum += ((Trie) object).numberTries();
        }
      }
    }

    return (accum);
  }

  void quickDump() {
    if (getEntry() != null) {
      System.out.print(getEntry().getKey() + " ");
    }

    if (getEntries() != null && getEntries().size() > 0) {
      Iterator iterator = getEntries().values().iterator();

      while (iterator.hasNext()) {
        Object object = iterator.next();

        if (object instanceof Trie && (((Trie) object).getPrevious() == this)) {
          ((Trie) object).quickDump();
        } else {
          System.out.print(((TrieEntry) object).getKey() + " ");
        }
      }
    }
  }

  /**
   * Removes the object with the given key from the
   * trie
   */
  public void remove(String key) throws NonUniqueKeyException, NoSuchElementException {
    String truekey = (isCaseSensitive() ? key : key.toUpperCase());

    if (truekey.length() == getPosition()) {
      //  item is on the '_entry' for this trie
      if (getEntry() != null) {
        setEntry(null);
        return;
      } else {
        throw new NonUniqueKeyException(truekey);
      }
    } else {
      String positionKey = getPositionKey(truekey);

      if (getEntries().containsKey(positionKey)) {
        Object object = getEntries().get(positionKey);


        if (object instanceof Trie && ((Trie) object).getPrevious() == this) {
          Trie trie = ((Trie) object);
          trie.remove(truekey);

          int subTrieSize = trie.length();
          if (subTrieSize == 1) {
            //  lower trie is now not required
            TrieEntry entry = trie.firstElement();
            if (entry != null) {
              getEntries().put(positionKey, entry);
            } else {
              getEntries().remove(positionKey);
            }
          }
        } else {
          TrieEntry entry = (TrieEntry) object;
          if (entry.getKey().equals(truekey)) {
            getEntries().remove(positionKey);
          }
          return;
        }
      } else {
        throw new java.util.NoSuchElementException(truekey);
      }
    }
  }

  /**
   * Returns the first element in a trie -
   * is generally only used when an element
   * is erased from the trie and a trie delete
   * and swapup is required.
   */
  TrieEntry firstElement() {
    if (getEntry() != null) {
      return getEntry();
    }

    if (getEntries() != null && getEntries().size() > 0) {
      Iterator iterator = getEntries().values().iterator();

      while (iterator.hasNext()) {
        Object object = iterator.next();

        if (!(object instanceof Trie && (((Trie) object).getPrevious() == this))) {
          return ((TrieEntry) object);
        }
      }
    }

    return (null);
  }

  /**
   * Returns a trie for multiple matches,
   * else returns null for no object matched,
   * or returns the object matched.  could
   * easily be optimised
   */
  public Object search(String key) {
    String truekey = (isCaseSensitive() ? key : key.toUpperCase());

    if (truekey.length() == getPosition()) {
      //  check _entry
      if (getEntry() != null) {
        //  this must be it
        return (getEntry().getData());
      } else {
        //  multiple matches in this case
        return (this);
      }
    }

    String positionKey = getPositionKey(truekey);

    if (!getEntries().containsKey(positionKey)) {
      //  act as if this was the end of the
      //  string - return the _entry, if there is one
      if (getEntry() != null) {
        return (getEntry().getData());
      } else {
        return (null);
      }
    } else {
      Object object = getEntries().get(positionKey);

      if (object instanceof Trie) {    //  pass it along the chain
        return (((Trie) object).search(truekey));
      } else {    //  found it (this check may not be required?)
        if (((TrieEntry) object).getKey().startsWith(truekey)) {
          return (((TrieEntry) object).getData());
        } else {
          return (null);
        }
      }
    }
  }

  /**
   * searchExact does *not* return a Trie for 'multiple matches',
   * but will return null in that case.
   */
  public Object searchExact(String key) {
    String truekey = (isCaseSensitive() ? key : key.toUpperCase());

    if (truekey.length() == getPosition()) {    //  check _entry
      if (getEntry() != null) {    //  this must be it
        return (getEntry().getData());
      } else {    //  multiple matches in this case
        return (null);
      }
    }

    String positionKey = getPositionKey(truekey);

    if (!getEntries().containsKey(positionKey)) {    //  act as if this was the end of the
      //  string - return the _entry, if there is one
      if (getEntry() != null) {
        return (getEntry().getData());
      } else {
        return (null);
      }
    } else {
      Object object = getEntries().get(positionKey);

      if (object instanceof Trie) {    //  pass it along the chain
        return (((Trie) object).searchExact(truekey));
      } else {
        if ((getPosition() + 1) < truekey.length()) {
          if (truekey.substring(0, getPosition() + 1).equalsIgnoreCase(((TrieEntry) object).getKey())) {
            return (((TrieEntry) object).getData());
          }
        }

        if (truekey.equalsIgnoreCase(((TrieEntry) object).getKey())) {
          return (((TrieEntry) object).getData());
        } else {
          return (null);
        }
      }
    }
  }
}

