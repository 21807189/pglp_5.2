package DAO;

import java.io.*;
import java.util.Iterator;

import mou.Annuaire;

public class AnnuaireDAO extends DAO<Annuaire> {
   
	private String dossier;

	 public AnnuaireDAO(final String dossierDB) {
	        dossier = dossierDB + "Annuaire\\";
	    }

	@Override
    public Annuaire create(Annuaire obj) {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("annuaire")))) {
            out.writeObject(obj);
        }
        catch(IOException ioe){

        }
        return obj;
    }

    @Override
    public Annuaire find(String Nom) {
        Annuaire p = Annuaire.getInstance();
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(Nom)))) {
            p = (Annuaire) in.readObject();

          }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }


        return p;
    }


    @Override
    public void delete(String file) {

        try {
            File f = new File(file);

            if (f.delete()) {
                System.out.println("supprimé");
            } else {
                System.out.println("erreur");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

	@Override
	public Annuaire update(Annuaire obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Annuaire obj) {
		// TODO Auto-generated method stub
		
	}
}
