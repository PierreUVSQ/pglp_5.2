package uvsq;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GroupeDao extends Dao<Groupe> {

  protected GroupeDao() throws SQLException {
  }

  @Override
  public Groupe create(Groupe obj) {
    /*try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("groupe")))) {
      out.writeObject(obj);
    } catch (IOException ioe) {

    }*/
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
