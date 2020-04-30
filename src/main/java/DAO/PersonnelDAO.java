package DAO;

import java.io.*;

import mou.Personnel;

public  class PersonnelDAO extends DAO<Personnel> {


    public PersonnelDAO(String dossierDB) {
		// TODO Auto-generated constructor stub
	}

	@Override
    
    public Personnel create(Personnel obj) {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("personnel")))) {
            out.writeObject(obj);
        }
        catch(IOException ioe){

        }
        return obj;


    }

    @Override
    public Personnel find(String nom) {
        Personnel p = null;
        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nom)))) {
            p = (Personnel) in.readObject();

        }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }


        return p;
    }

    @Override
    public void delete(String file) {

    }

	@Override
	public Personnel update(Personnel obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Personnel obj) {
		// TODO Auto-generated method stub
		
	}
}
