package uvsq;

import java.io.Serializable;
import java.util.Iterator;

public class EquipeIterator<Equipe> implements Iterator<Equipe>, Serializable {

  private int index;
  private Node<Equipe> e;

  @Override
  public boolean hasNext() {
    if (e != null) {
      return true;
    }
    return false;
  }

  @Override
  public Equipe next() {
    Equipe tmp = e.getElement();
    e = e.getNext();
    index++;
    return tmp;
  }

  public void add(Equipe n) {

    if (e == null) {
      this.e = new Node<Equipe>(n);
    } else {

      this.e.addElement(n);
    }
  }

  public EquipeIterator<Equipe> copy() {
    EquipeIterator<Equipe> res = new EquipeIterator<>();
    res.e = this.e;
    return res;
  }
}
