package DAO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import mou.Personnel;

public class PersonnelDAO extends DAO<Personnel> {
   
    private String dossier;

    
    public PersonnelDAO(final String dossierDB) {
        dossier = dossierDB + "Personnel\\";
    }

    @Override
    public Personnel create(final Personnel obj) {
        String chemin = dossier;
        if (!new File(chemin).exists()) {
            if (!new File(chemin).mkdirs()) {
                return null;
            }
        }
        chemin += obj.getId() + ".ser";
        File f = new File(chemin);
        if (f.exists()) {
            return null;
        }
        try {
            serialize(obj, chemin);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    @Override
    public Personnel find(final String id) {
        String nomFichier = dossier + id + ".ser";
        try {
            Personnel p = deserialize(nomFichier);
            if (!id.equals(p.getId() + "")) {
                return null;
            } else {
                return p;
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Personnel update(final Personnel obj) {
        String nomFichier = dossier + obj.getId() + ".ser";
        File f;
        f = new File(nomFichier);
        if (!f.exists()) {
            System.err.println("Personnel non modifié car inexistant.");
            return null;
        }
        Personnel p;
        try {
            p = deserialize(nomFichier);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
        if (obj.getId() != p.getId()) {
            return null;
        }

        try {
            serialize(obj, nomFichier);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    @Override
    public void delete(final Personnel obj) {
        String nomFichier = dossier + obj.getId() + ".ser";
        File f;
        f = new File(nomFichier);
        if (!f.exists()) {
            System.err.println("Personnel non supprimé car inexistant.");
            return;
        }
        Personnel p;
        try {
            p = deserialize(nomFichier);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return;
        }
        if (obj.getId() != p.getId()) {
            return;
        }
        if (!obj.equals(p)) {
            return;
        }
        if (f.delete()) {
            System.out.println("Personnel supprimé.");
        } else {
            System.err.println("Personnel non supprimé, erreur.");
        }
    }

    private void serialize(final Personnel p, final String nomFichier)
            throws FileNotFoundException, IOException {
        try (ObjectOutputStream out =
                new ObjectOutputStream(new BufferedOutputStream(
                        new FileOutputStream(new File(nomFichier))))) {
            out.writeObject(p);
        }
    }

 
    private Personnel deserialize(final String nomFichier)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                new ObjectInputStream(new BufferedInputStream(
                        new FileInputStream(new File(nomFichier))))) {
            Object o = in.readObject();
            if (o instanceof Personnel) {
                return (Personnel) o;
            }
        }
        return null;
    }
}
