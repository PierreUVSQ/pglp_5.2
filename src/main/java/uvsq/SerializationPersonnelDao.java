package uvsq;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationPersonnelDao extends Dao<Personnel> {

  /**
   * Creation du personnel en sérialisation.
   * @param obj personnel
   * @return personnel
   */
  public Personnel create(Personnel obj) {
    try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("personnel")))) {
      out.writeObject(obj);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return obj;
  }

  /**
   * Trouve le personnel.
   * @param id personnel
   * @return Personnel
   */
  public Personnel find(String id) {
    Personnel p = null;
    try (ObjectInputStream in =
        new ObjectInputStream(new BufferedInputStream(new FileInputStream(id)))) {
      p = (Personnel) in.readObject();

    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }

    return p;
  }

  /**
   * Supprime le fichier avec le personnel.
   * @param file fichier
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
