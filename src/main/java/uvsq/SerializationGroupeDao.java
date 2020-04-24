package uvsq;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationGroupeDao extends Dao<Groupe> {

  /**
   * Create du DAO.
   * @param obj objet
   * @return groupe
   */
  public Groupe create(Groupe obj) {
    try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(obj.getNom())))) {
      out.writeObject(obj);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return obj;
  }

  /**
   * Find du DAO.
   * @param id objet
   * @return groupe
   */
  public Groupe find(String id) {
    Groupe g = null;
    try (ObjectInputStream in =
        new ObjectInputStream(new BufferedInputStream(new FileInputStream(id)))) {
      g = (Groupe) in.readObject();

    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }

    return g;
  }

  /**
   * Delete du DAO.
   * @param file objet
   */
  public void delete(String file) {

    try {
      File f = new File(file);

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
