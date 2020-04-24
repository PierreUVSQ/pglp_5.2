package uvsq;

import java.io.Serializable;

public class Node<T> implements Serializable {

  private Node<T> precedent = null; // noeud précedent
  private Node<T> suivant = null; // noeud suivant
  private T element; // élément du noeud courant

  /**
   * Constructor.
   *
   * @param element Equipe
   */
  public Node(T element) {

    this.element = element;
  }

  /**
   * Ajoute élément récursivement.
   *
   * @param suivant Equipe
   */
  public void addElement(T suivant) {

    if (this.suivant == null) {
      this.suivant = new Node<T>(suivant);
      this.suivant.precedent = this;
    } else {
      this.suivant.addElement(suivant);
    }
  }

  /**
   * Getter.
   *
   * @return élément courant
   */
  public T getElement() {

    return this.element;
  }

  /** Efface l'élément courant. */
  public void remove() {

    this.precedent.suivant = this.suivant;
    this.suivant.precedent = this.precedent;
    this.element = null;
    this.suivant = null;
    this.precedent = null;
  }

  /**
   * Indique s'l y a un élément suivant.
   *
   * @return boolean
   */
  public boolean hasNext() {

    if (this.suivant != null) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Getter.
   *
   * @return
   */
  public Node<T> getNext() {

    return this.suivant;
  }

  /**
   * Getter.
   *
   * @return
   */
  public Node<T> getPrevious() {

    return this.precedent;
  }
}
