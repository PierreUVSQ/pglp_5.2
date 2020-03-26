package uvsq;

import java.sql.SQLException;

public interface AbstractDaoFactory {

  Dao<Annuaire> createAnnuaireDao();

  Dao<Groupe> createGroupeDao();

  Dao<Personnel> createPersonnelDao();
}
