package uvsq;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonnelDao extends Dao<Personnel> {

  protected PersonnelDao() throws SQLException {}

  int compteur = 0;

  @Override
  public Personnel create(Personnel obj) {
    /*   try (ObjectOutputStream out =
        new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("personnel")))) {
      out.writeObject(obj);
    } catch (IOException ioe) {

    }*/
    this.connect();

    try {
      this.stmt = connect.createStatement();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    String personnelInsert =
        "INSERT INTO Personnel(persid, nom, prenom, fonction, naissance) values("
            + this.compteur
            + ",'"
            + obj.getNom()
            + "','"
            + obj.getPrenom()
            + "','"
            + obj.getFonction()
            + "','"
            + java.sql.Date.valueOf(obj.getLocalDate())
            + "')";
    try {
      stmt.execute(personnelInsert);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    for (String e : obj.getTel()) {

      String telInsert =
          "INSERT INTO Telephone(id, tel) VALUES("
              + this.compteur
              + ","
              + Integer.parseInt(e)
              + ")";
      try {
        stmt.execute(telInsert);
      } catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    this.compteur++;
    this.disconnect();
    return obj;
  }

  @Override
  public Personnel find(String id) {
    Personnel p = null;
    /*try (ObjectInputStream in =
        new ObjectInputStream(new BufferedInputStream(new FileInputStream(id)))) {
      p = (Personnel) in.readObject();

    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }*/
    this.connect();

    String select = "SELECT * FROM Personnel P WHERE P.persid = " + Integer.parseInt(id);
    String selectTel = "SELECT T.tel FROM Telephone T WHERE T.id = " + Integer.parseInt(id);
    try {
      this.stmt = connect.createStatement();
      stmt.execute(selectTel);
      ResultSet resTel = stmt.getResultSet();
      List<String> tel = new ArrayList<>();
      while (resTel.next()) {
        tel.add(String.valueOf(resTel.getInt("tel")));
      }
      stmt.execute(select);
      ResultSet res = stmt.getResultSet();
      if ((stmt.getResultSet().next())) {
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
  public void delete(String file) {}

  @Override
  public void close() throws Exception {

    super.connect.close();
  }
}
