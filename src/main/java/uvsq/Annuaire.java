package uvsq;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.lang.Iterable;

public class Annuaire implements Iterable<Equipe>, Serializable {

  private static Annuaire ANNUAIRE; // instance
  private EquipeIterator<Equipe> head; // tête de l'itérateur

  /** Constructeur privé. */
  private Annuaire() {
    head = new EquipeIterator<Equipe>();
  }

  /**
   * Getter du singleton.
   *
   * @return
   */
  public static Annuaire getInstance() {

    if (ANNUAIRE == null) ANNUAIRE = new Annuaire();

    return ANNUAIRE;
  }

  @Override
  public Iterator<Equipe> iterator() {
    return this.head.copy();
  }

  /**
   * Ajout d'une équipe.
   *
   * @param e
   */
  public void addEquipe(Equipe e) {

    this.head.add(e);
  }

  public static AbstractDaoFactory createChoisiDaoFactory(String choix) {

    if (choix == "SGBD") {
      return new DaoFactory();
    } else {
      return new SerialDaoFactory();
    }
  }
}
