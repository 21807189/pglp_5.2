package DAO;

import java.io.*;


import mou.Groupe;

public class GroupeDAO extends DAO<Groupe> {


    public GroupeDAO(String dossierDB) {
		// TODO Auto-generated constructor stub
	}

	@Override
    public Groupe create(Groupe obj) {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("groupe")))) {
            out.writeObject(obj);
        }
        catch(IOException ioe){

        }
        return obj;
    }

    @Override
    public Groupe find(String nom) {
        Groupe g = new Groupe("Vide");
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nom)))) {
            g = (Groupe) in.readObject();

        }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
            }

        return g;
    }

    @Override
    public void delete(String file) {

        try {
            File f = new File(file);

            if (f.delete()) {
                System.out.println("supprimé ");
            } else {
                System.out.println("erreur");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

	@Override
	public Groupe update(Groupe obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Groupe obj) {
		// TODO Auto-generated method stub
		
	}
}
