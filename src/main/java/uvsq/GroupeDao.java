package uvsq;

import org.apache.bcel.generic.INSTANCEOF;

import java.io.*;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupeDao extends Dao<Groupe> {

  private Dao ag;

  protected GroupeDao() throws SQLException {
    ag = DaoFactory.getPersonnelDao();
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
              "INSERT INTO Groupe(nom) values('"
                      + obj.getNom()
                      + "')";
      stmt.execute(personnelInsert);
      for (Equipe e : obj) {

        if( e instanceof  Personnel){
          this.ag.create((Personnel) e);
          String EquipeInsert =
                  "INSERT INTO FaitPartiePersonnel(gnom, nom) VALUES('"
                          + obj.getNom()
                          + "','"
                          + e.getNom()
                          + "')";

          stmt.execute(EquipeInsert);

        }
        else if (e instanceof Groupe){
         DaoFactory.getGroupeDao().create((Groupe) e);
          String EquipeInsert =
                  "INSERT INTO FaitPartieGroupe(gnom, nom) VALUES('"
                          + obj.getNom()
                          + "','"
                          + e.getNom()
                          + "')";

          stmt.execute(EquipeInsert);
        }


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
    Groupe g = null;
    /*try (ObjectInputStream in =
        new ObjectInputStream(new BufferedInputStream(new FileInputStream(id)))) {
      g = (Groupe) in.readObject();

    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }*/

    this.connect();

    String select = "SELECT * FROM Groupe G WHERE G.nom = '" + id + "'";
    String selectItGroupe = "SELECT FPG.nom FROM FaitPartieGroupe FPG WHERE FPG.gnom = '" + id + "'";
    String selectItPersonnel = "SELECT FPP.nom FROM FaitPartiePersonnel FPP WHERE FPP.gnom = '" + id + "'";
    try {
      this.stmt = connect.createStatement();
      stmt.execute(select);
      ResultSet res = stmt.getResultSet();
      if ((stmt.getResultSet().next())) {
        g =
                new Groupe(res.getString("nom"));
        /*Ajout de la liste des personnels*/
        stmt.execute(selectItPersonnel);
        ResultSet resIt = stmt.getResultSet();
        while (resIt.next()) {
          g.ajoutMembre((Personnel)this.ag.find(resIt.getString("nom")));
        }
        /*Ajout de la liste des Groupes*/
        stmt.execute(selectItGroupe);
        ResultSet resItG = stmt.getResultSet();
        while (resItG.next()) {
          g.ajoutMembre((Groupe)this.find(resItG.getString("nom")));
        }
      }


    } catch (SQLException e) {
      e.printStackTrace();
    }

    this.disconnect();

    return g;
  }

  @Override
  public void delete(String id) {

    this.connect();
    String delete = "DELETE FROM GROUPE G WHERE G.nom = '" + id + "'";
    try {
      this.stmt = connect.createStatement();
      this.stmt.execute(delete);
    } catch (SQLException e) {
      e.printStackTrace();
    }


    this.disconnect();

  }


  @Override
  public void close() throws Exception {
    super.connect.close();
  }
}
