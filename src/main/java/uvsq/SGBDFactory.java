package uvsq;

import java.sql.SQLException;

public class SGBDFactory implements AbstractDaoFactory {
    @Override
    public AnnuaireDao createAnnuaireDao() {
        return new AnnuaireDao();
    }

    @Override
    public GroupeDao createGroupeDao() {
        return new GroupeDao();
    }

    @Override
    public PersonnelDao createPersonnelDao() {
        return new PersonnelDao();
    }
}
