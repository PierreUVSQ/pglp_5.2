package uvsq;

import java.io.*;

public class GroupeDao implements Dao<Groupe> {

  @Override
  public Groupe create(Groupe obj) {
    try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("groupe")))) {
      out.writeObject(obj);
    } catch (IOException ioe) {

    }
    return obj;
  }

  @Override
  public Groupe find(String id) {
    Groupe g = new Groupe("Vide");
    try (ObjectInputStream in =
        new ObjectInputStream(new BufferedInputStream(new FileInputStream(id)))) {
      g = (Groupe) in.readObject();

    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }

    return g;
  }

  @Override
  public void delete(String file) {

    try {
      File f = new File(file);

      if (f.delete()) {
        System.out.println("Deletion complete");
      } else {
        System.out.println("Failure");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
