package uvsq;

public class DaoFactory {

  public static Dao<Annuaire> getAnnuaireDao() {

    return new AnnuaireDao();
  }

  public static Dao<Groupe> getGroupeDao() {

    return new GroupeDao();
  }

  public static Dao<Personnel> getPersonnelDao() {

    return new PersonnelDao();
  }
}
