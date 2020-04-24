package uvsq;

import java.io.Serializable;
import java.util.Iterator;

public class EquipeIterator<Equipe> implements Iterator<Equipe>, Serializable {

  private int index;
  private Node<Equipe> elem;

  @Override
  public boolean hasNext() {
    if (elem != null) {
      return true;
    }
    return false;
  }

  @Override
  public Equipe next() {
    Equipe tmp = elem.getElement();
    elem = elem.getNext();
    index++;
    return tmp;
  }

  /**
   * Ajout élément.
   * @param n equipe
   */
  public void add(Equipe n) {

    if (elem == null) {
      this.elem = new Node<Equipe>(n);
    } else {

      this.elem.addElement(n);
    }
  }

  /**
   * Retourne une copie du header.
   * @return copie du header
   */
  public EquipeIterator<Equipe> copy() {
    EquipeIterator<Equipe> res = new EquipeIterator<>();
    res.elem = this.elem;
    return res;
  }
}
