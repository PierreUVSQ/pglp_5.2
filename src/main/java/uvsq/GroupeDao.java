package uvsq;

import java.io.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GroupeDao extends Dao<Groupe> {

  protected GroupeDao() throws SQLException {
  }
  public static int compteur = 0;
  @Override
  public Groupe create(Groupe obj) {
    /*try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("groupe")))) {
      out.writeObject(obj);
    } catch (IOException ioe) {

    }*/
    this.connect();

    try {
      this.stmt = connect.createStatement();
      String personnelInsert =
              "INSERT INTO Groupe(gid, nom) values("
                      + this.compteur
                      + ",'"
                      + obj.getNom()
                      + "')";
      stmt.execute(personnelInsert);
      for (Equipe e : obj) {

        String EquipeInsert =
                "INSERT INTO Telephone(id, tel) VALUES("
                        + this.compteur
                        + ","
  //                      + Integer.parseInt(e)
                        + ")";

//        stmt.execute(telInsert);

      }
      this.compteur++;
    } catch (SQLException e) {
      e.printStackTrace();
    }

    this.disconnect();

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

  @Override
  public void close() throws Exception {
    super.connect.close();
  }
}
