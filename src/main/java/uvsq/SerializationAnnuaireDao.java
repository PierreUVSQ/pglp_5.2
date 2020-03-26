package uvsq;

import java.io.*;
import java.util.Iterator;

public class SerializationAnnuaireDao extends Dao<Annuaire> {

  // @Override
  public Annuaire create(Annuaire obj) {
    try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("annuaire")))) {
      out.writeObject(obj);
    } catch (IOException ioe) {

    }
    return obj;
  }

  // @Override
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

  // @Override
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
