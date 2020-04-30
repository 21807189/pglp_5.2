package DAO;

import mou.Annuaire;

import mou.Groupe;
import mou.Personnel;
import mou.telephone;

public class DAOFactory {

    public static DAO<Annuaire> getAnnuaireDAO(){

        return new AnnuaireDAO(null);
    }

    public static DAO<Groupe> getGroupeDAO(){

        return new GroupeDAO(null);
    }

    public static DAO<Personnel> getPersonnelDAO(){

        return new PersonnelDAO(null); }
        

    public static DAO<telephone> gettelephoneDAO(){

        return new telephoneDAO(null);}
   
}
