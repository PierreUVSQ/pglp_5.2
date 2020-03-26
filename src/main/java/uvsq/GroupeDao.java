package uvsq;

import org.apache.bcel.generic.INSTANCEOF;

import java.io.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupeDao extends Dao<Groupe> {

  private Dao ag;

  protected GroupeDao() throws SQLException {
    ag = DaoFactory.getPersonnelDao();
  }

  @Override
  public Groupe create(Groupe obj) {
    /*try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("groupe")))) {
      out.writeObject(obj);
    } catch (IOException ioe) {

    }*/
    this.connect();

    try (PreparedStatement personnelInsert =
            this.connect.prepareStatement("INSERT INTO Groupe(nom) values(?)");
        PreparedStatement EquipeInsert =
            this.connect.prepareStatement(
                "INSERT INTO FaitPartiePersonnel(gnom, nom) VALUES(?, ?)");
        PreparedStatement EquipeInsertG =
            this.connect.prepareStatement("INSERT INTO FaitPartieGroupe(gnom, nom) VALUES(?, ?)")) {
      this.stmt = connect.createStatement();
      personnelInsert.setString(1, obj.getNom());
      personnelInsert.executeUpdate();
      for (Equipe e : obj) {

        if (e instanceof Personnel) {
          this.ag.create((Personnel) e);
          EquipeInsert.setString(1, obj.getNom());
          EquipeInsert.setString(2, e.getNom());
          EquipeInsert.executeUpdate();

        } else if (e instanceof Groupe) {
          DaoFactory.getGroupeDao().create((Groupe) e);
          EquipeInsertG.setString(1, obj.getNom());
          EquipeInsertG.setString(2, e.getNom());
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    this.disconnect();

    return obj;
  }

  @Override
  public Groupe find(String id) {
    Groupe g = null;

    this.connect();

    try (PreparedStatement select =
            this.connect.prepareStatement("SELECT * FROM Groupe G WHERE G.nom = ?");
        PreparedStatement selectItGroupe =
            this.connect.prepareStatement(
                "SELECT FPG.nom FROM FaitPartieGroupe FPG WHERE FPG.gnom =  ?");
        PreparedStatement selectItPersonnel =
            this.connect.prepareStatement(
                "SELECT FPP.nom FROM FaitPartiePersonnel FPP WHERE FPP.gnom = ?"); ) {
      select.setString(1, id);
      ResultSet res = select.executeQuery();
      while (res.next()) {
        g = new Groupe(res.getString("nom"));
        /*Ajout de la liste des personnels*/
        selectItPersonnel.setString(1, id);
        ResultSet resIt = selectItPersonnel.executeQuery();
        while (resIt.next()) {
          g.ajoutMembre((Personnel) this.ag.find(resIt.getString("nom")));
        }
        /*Ajout de la liste des Groupes*/
        selectItGroupe.setString(1, id);
        ResultSet resItG = selectItGroupe.executeQuery();
        while (resItG.next()) {
          g.ajoutMembre((Groupe) this.find(resItG.getString("nom")));
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

    try (PreparedStatement delete =
        this.connect.prepareStatement("DELETE FROM GROUPE G WHERE G.nom = ?"); ) {
      delete.setString(1, id);
      delete.executeUpdate();
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
