package uvsq;

public class DaoFactory implements AbstractDaoFactory {

  public Dao<Annuaire> createAnnuaireDao() {

    return new AnnuaireDao();
  }

  public Dao<Groupe> createGroupeDao() {

    return new GroupeDao();
  }

  public Dao<Personnel> createPersonnelDao() {

    return new PersonnelDao();
  }
}
