package uvsq;

import java.sql.SQLException;

public class DaoFactory {

  public static Dao<Annuaire> getAnnuaireDao() throws SQLException {

    return new AnnuaireDao();
  }

  public static Dao<Groupe> getGroupeDao() throws SQLException {

    return new GroupeDao();
  }

  public static Dao<Personnel> getPersonnelDao() throws SQLException {

    return new PersonnelDao();
  }
}
