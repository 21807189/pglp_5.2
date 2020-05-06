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
import mou.telephone;

public class telephoneDAO extends DAO<telephone> {
    
    private String dossier;

    public telephoneDAO(final String dossierDB) {
        dossier = dossierDB + "Telephone\\";
    }

    @Override
    public telephone create(final telephone obj) {
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
    public telephone find(final String id) {
        String nomFichier = dossier + id + ".ser";
        try {
            telephone t = deserialize(nomFichier);
            if (!id.equals(t.getId() + "")) {
                return null;
            } else {
                return t;
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public telephone update(final telephone obj) {
        String nomFichier = dossier + obj.getId() + ".ser";
        File f;
        f = new File(nomFichier);
        if (!f.exists()) {
            System.err.println("Telephone non modifié, inexistant.");
            return null;
        }
        telephone t;
        try {
            t = deserialize(nomFichier);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
        if (obj.getId() != t.getId()) {
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
    public void delete(final telephone obj) {
        String nomFichier = dossier + obj.getId() + ".ser";
        File f;
        f = new File(nomFichier);
        if (!f.exists()) {
            System.err.println("Suppression du telephone échouée, tel n'existe pas.");
            return;
        }
        telephone t;
        try {
            t = deserialize(nomFichier);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return;
        }
        if (obj.getId() != t.getId()) {
            return;
        }
        if (!obj.equals(t)) {
            return;
        }
        if (f.delete()) {
            System.out.println("Telephone supprimé.");
        } else {
            System.err.println("Telephone non supprimé, erreur.");
        }
    }

    private void serialize(final telephone t, final String nomFichier)
            throws FileNotFoundException, IOException {
        try (ObjectOutputStream out =
                new ObjectOutputStream(new BufferedOutputStream(
                        new FileOutputStream(new File(nomFichier))))) {
            out.writeObject(t);
        }
    }

 
    private telephone deserialize(final String nomFichier)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                new ObjectInputStream(new BufferedInputStream(
                        new FileInputStream(new File(nomFichier))))) {
            Object o = in.readObject();
            if (o instanceof telephone) {
                return (telephone) o;
            }
        }
        return null;
    }
}
