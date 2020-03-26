package uvsq;

import java.sql.SQLException;

public interface AbstractDaoFactory {

    AnnuaireDao createAnnuaireDao();
    GroupeDao createGroupeDao();
    PersonnelDao createPersonnelDao();




}
