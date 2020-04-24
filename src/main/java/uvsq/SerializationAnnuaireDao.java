package uvsq;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationAnnuaireDao extends Dao<Annuaire> {

  /**
   * Creation de l'annuaire.
   * @param obj annuaire
   * @return annuaire
   */
  public Annuaire create(Annuaire obj) {
    try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("annuaire")))) {
      out.writeObject(obj);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return obj;
  }

  /**
   * Trouve l'annuaire.
   * @param id fichier
   * @return annuaire correspondant
   */
  public Annuaire find(String id) {
    Annuaire p = Annuaire.getInstance();
    try (ObjectInputStream in =
        new ObjectInputStream(new BufferedInputStream(new FileInputStream("annuaire")))) {
      p = (Annuaire) in.readObject();

    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }

    return p;
  }

  /**
   * Supprime le fichier de l'annuaire.
   * @param file fichier
   */
  public void delete(String file) {

    try {
      File f = new File("annuaire");

      if (!f.delete()) {
        System.out.println("Failure");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() throws Exception {}
}
