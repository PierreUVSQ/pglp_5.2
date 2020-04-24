package uvsq;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnnuaireDao extends Dao<Annuaire> {

  private Dao ag;
  private Dao agroupe;

  protected AnnuaireDao() {

    ag = Annuaire.createChoisiDaoFactory("SGBD").createPersonnelDao();
    agroupe = Annuaire.createChoisiDaoFactory("SGBD").createGroupeDao();
  }

  @Override
  public Annuaire create(Annuaire obj) {
    this.connect();

    try (PreparedStatement EquipeInsert1 =
            connect.prepareStatement("INSERT INTO AnnuairePersonnel(nom) VALUES(?) ");
        PreparedStatement EquipeInsert2 =
            connect.prepareStatement("INSERT INTO AnnuaireGroupe(gnom) VALUES(?)"); ) {

      for (Equipe e : obj) {

        if (e instanceof Personnel) {
          this.ag.create((Personnel) e);
          EquipeInsert1.setString(1, e.getNom());
          EquipeInsert1.executeUpdate();

        } else if (e instanceof Groupe) {
          Annuaire.createChoisiDaoFactory("SGBD").createGroupeDao().create((Groupe) e);
          EquipeInsert2.setString(1, e.getNom());
          EquipeInsert2.executeUpdate();
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
    this.connect();
    String selectItGroupe = "SELECT * FROM AnnuaireGroupe AG ";
    String selectItPersonnel = "SELECT * FROM AnnuairePersonnel AP";
    try {
      this.stmt = connect.createStatement();
      /*Ajout de la liste des personnels*/
      stmt.execute(selectItPersonnel);
      ResultSet resIt = stmt.getResultSet();
      while (resIt.next()) {
        p.addEquipe((Personnel) this.ag.find(resIt.getString("nom")));
      }
      /*Ajout de la liste des Groupes*/
      stmt.execute(selectItGroupe);
      ResultSet resItG = stmt.getResultSet();
      while (resItG.next()) {
        p.addEquipe((Groupe) this.agroupe.find(resItG.getString("gnom")));
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
