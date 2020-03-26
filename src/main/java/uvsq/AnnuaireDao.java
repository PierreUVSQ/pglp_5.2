package uvsq;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnnuaireDao extends Dao<Annuaire> {

  private Dao ag;
  private Dao aGroupe;

  protected AnnuaireDao() throws SQLException {
    ag = DaoFactory.getPersonnelDao();
    aGroupe = DaoFactory.getGroupeDao();
  }

  @Override
  public Annuaire create(Annuaire obj) {
    /*try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("annuaire")))) {
      out.writeObject(obj);
    } catch (IOException ioe) {

    }*/

    this.connect();

    try {
      this.stmt = connect.createStatement();
      for (Equipe e : obj) {

        if( e instanceof  Personnel){
          this.ag.create((Personnel) e);
          String EquipeInsert =
                  "INSERT INTO AnnuairePersonnel(nom) VALUES('"
                          + e.getNom()
                          + "')";

          stmt.execute(EquipeInsert);

        }
        else if (e instanceof Groupe){
          DaoFactory.getGroupeDao().create((Groupe) e);
          String EquipeInsert =
                  "INSERT INTO AnnuaireGroupe(gnom) VALUES('"
                          + e.getNom()
                          + "')";

          stmt.execute(EquipeInsert);
        }


      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    this.disconnect();
    return obj;
  }

  @Override
  public Annuaire find(String id) {
    Annuaire p = Annuaire.getInstance();
    /*try (ObjectInputStream in =
        new ObjectInputStream(new BufferedInputStream(new FileInputStream(id)))) {
      p = (Annuaire) in.readObject();

    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }*/
    this.connect();

    //String select = "SELECT * FROM Groupe G WHERE G.nom = '" + id + "'";
    String selectItGroupe = "SELECT * FROM AnnuaireGroupe AG ";
    String selectItPersonnel = "SELECT * FROM AnnuairePersonnel AP";
    try {
      this.stmt = connect.createStatement();
        /*Ajout de la liste des personnels*/
        stmt.execute(selectItPersonnel);
        ResultSet resIt = stmt.getResultSet();
        while (resIt.next()) {
          p.addEquipe((Personnel)this.ag.find(resIt.getString("nom")));
        }
        /*Ajout de la liste des Groupes*/
        stmt.execute(selectItGroupe);
        ResultSet resItG = stmt.getResultSet();
        while (resItG.next()) {
          p.addEquipe((Groupe)this.aGroupe.find(resItG.getString("gnom")));
        }



    } catch (SQLException e) {
      e.printStackTrace();
    }

    this.disconnect();

    return p;
  }

  @Override
  public void delete(String id) {
    this.connect();
    try {
      this.stmt = connect.createStatement();
      String delete1 = "DELETE FROM AnnuaireGroupe";
      String delete2 = "DELETE FROM AnnuairePersonnel";
      this.stmt.execute(delete1);
      this.stmt.execute(delete2);
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void close() throws Exception {
    super.connect.close();
  }
}
