package uvsq;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonnelDao extends Dao<Personnel> {

  protected PersonnelDao() {}

  @Override
  public Personnel create(Personnel obj) {

    this.connect();

    try (PreparedStatement personnelInsert =
            this.connect.prepareStatement(
                "INSERT INTO Personnel(nom, prenom, fonction, naissance) values(?, ?, ?, ?)");
        PreparedStatement telInsert =
            this.connect.prepareStatement("INSERT INTO Telephone(nom, tel) VALUES(?, ?)"); ) {
      personnelInsert.setString(1, obj.getNom());
      personnelInsert.setString(2, obj.getPrenom());
      personnelInsert.setString(3, obj.getFonction());
      personnelInsert.setDate(4, java.sql.Date.valueOf(obj.getLocalDate()));
      personnelInsert.executeUpdate();
      for (String e : obj.getTel()) {

        telInsert.setString(1, obj.getNom());
        telInsert.setInt(2, Integer.parseInt(e));
        telInsert.executeUpdate();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    this.disconnect();
    return obj;
  }

  @Override
  public Personnel find(String id) {
    Personnel p = null;
    this.connect();
    try (PreparedStatement select =
            this.connect.prepareStatement("SELECT * FROM Personnel P WHERE P.nom = ?");
        PreparedStatement selectTel =
            this.connect.prepareStatement("SELECT T.tel FROM Telephone T WHERE T.nom = ?"); ) {
      selectTel.setString(1, id);
      ResultSet resTel = selectTel.executeQuery();
      List<String> tel = new ArrayList<>();
      while (resTel.next()) {
        tel.add(String.valueOf(resTel.getInt("tel")));
      }
      select.setString(1, id);
      ResultSet res = select.executeQuery();
      if ((res.next())) {
        p =
            new Personnel.Builder(
                    res.getString("nom"), res.getString("prenom"), res.getString("fonction"))
                .updatePhoneList(tel)
                .updateDateNaissance(res.getDate("naissance").toLocalDate())
                .build();
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
    try (PreparedStatement delete =
        this.connect.prepareStatement("DELETE FROM Personnel P WHERE P.nom = ?"); ) {
      delete.setString(1, id);
      delete.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void close() throws Exception {

    super.connect.close();
  }
}
