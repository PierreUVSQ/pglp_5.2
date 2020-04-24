package uvsq;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Groupe extends Equipe implements Iterable<Equipe>, Serializable {

  private EquipeIterator<Equipe> head;

  public Groupe(String nom) {
    this.nom = nom;
    this.head = new EquipeIterator<Equipe>();
  }

  public void ajoutMembre(Equipe e) {

    this.head.add(e);
  }

  public String getNom() {
    return this.nom;
  }

  /**
   * Affiche le nom de l'equipe et de ceux qui la compose.
   */
  public void printNom() {
    System.out.println(this.nom);
    for (Equipe e : this) {
      e.printNom();
    }
  }

  @Override
  public Iterator<Equipe> iterator() {
    EquipeIterator<Equipe> t = this.head.copy();
    return t;
  }
}
