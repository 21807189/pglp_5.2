package DAO;

import mou.Annuaire;

import mou.Groupe;
import mou.Personnel;
import mou.telephone;

public abstract class FabriqueDAO {
   
    public enum TypeDAO {
        
        mouwafak;
    }
    public abstract DAO<telephone> getTelephoneDAO();

    public abstract DAO<Personnel> getPersonnelDAO();

    public abstract DAO<Groupe> getGroupeDAO();

    public abstract DAO<Annuaire> getAnnuaireDAO();

    public static FabriqueDAO getFabriqueDAO(final TypeDAO type) {
        if (type == TypeDAO.mouwafak) {
            return new FabriqueDAOFile();
        }
        return null;
    }
}
