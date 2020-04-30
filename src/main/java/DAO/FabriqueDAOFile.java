package DAO;

import mou.Annuaire;
import mou.Groupe;
import mou.Personnel;
import mou.telephone;

public class FabriqueDAOFile extends FabriqueDAO {

    private static String dossierDB;

    public static final String Dossier = "donneesPourDB\\";
    
    public FabriqueDAOFile() {
        dossierDB = Dossier + "simulDB\\";
    }

    public void setDossierDB(final String nom_du_Dossier) {
        dossierDB = Dossier + nom_du_Dossier;
        if (dossierDB.charAt(dossierDB.length() - 1) != '\\') {
            dossierDB += "\\";
        }
    }

    public String getDossierDB() {
        return dossierDB;
    }
    
    public DAO<telephone> gettelephoneDAO() {
        return new telephoneDAO(dossierDB);
    }

    public DAO<Personnel> getPersonnelDAO() {
        return new PersonnelDAO(dossierDB);
    }

    public DAO<Groupe> getGroupeDAO() {
        return new GroupeDAO(dossierDB);
    }

 
    public DAO<Annuaire> getAnnuaireDAO() {
        return new AnnuaireDAO(dossierDB);
    }

	@Override
	public DAO<telephone> getTelephoneDAO() {
		// TODO Auto-generated method stub
		return null;
	}
}
