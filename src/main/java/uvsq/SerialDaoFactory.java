package uvsq;

public class SerialDaoFactory implements AbstractDaoFactory {

  public Dao<Annuaire> createAnnuaireDao() {

    return new SerializationAnnuaireDao();
  }

  public Dao<Groupe> createGroupeDao() {

    return new SerializationGroupeDao();
  }

  public Dao<Personnel> createPersonnelDao() {

    return new SerializationPersonnelDao();
  }
}
