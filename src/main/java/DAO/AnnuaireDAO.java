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

import mou.Annuaire;


public class AnnuaireDAO extends DAO<Annuaire> {
   
    private String dossier;
    public AnnuaireDAO(final String dossierDB) {
        dossier = dossierDB + "Annuaire\\";
    }

    @Override
    public Annuaire create(final Annuaire obj) {
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
    public Annuaire find(final String id) {
        String nomFichier = dossier + id + ".ser";
        try {
            Annuaire a = deserialize(nomFichier);
            if (!id.equals(a.getId() + "")) {
                return null;
            } else {
                return a;
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

 
    @Override
    public Annuaire update(final Annuaire obj) {
        String nomFichier = dossier + obj.getId() + ".ser";
        File f;
        f = new File(nomFichier);
        if (!f.exists()) {
            System.err.println("Annuaire n'existe pas, impossible de supprimer.");
            return null;
        }
        Annuaire a;
        try {
            a = deserialize(nomFichier);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
        if (obj.getId() != a.getId()) {
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
    public void delete(final Annuaire obj) {
        String nomFichier = dossier + obj.getId() + ".ser";
        File f;
        f = new File(nomFichier);
        if (!f.exists()) {
            System.err.println("Telephone n'existe pas, impossible de supprimer.");
            return;
        }
        Annuaire a;
        try {
            a = deserialize(nomFichier);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return;
        }
        if (obj.getId() != a.getId()) {
            return;
        }
        if (!obj.equals(a)) {
            return;
        }
        if (f.delete()) {
            System.out.println("Annuaire supprimé.");
        } else {
            System.err.println("Annuaire non supprimé, erreur.");
        }
    }

    private void serialize(final Annuaire a, final String nomFichier)
            throws FileNotFoundException, IOException {
        try (ObjectOutputStream out =
                new ObjectOutputStream(new BufferedOutputStream(
                        new FileOutputStream(new File(nomFichier))))) {
            out.writeObject(a);
        }
    }

   
    private Annuaire deserialize(final String nomFichier)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                new ObjectInputStream(new BufferedInputStream(
                        new FileInputStream(new File(nomFichier))))) {
            Object o = in.readObject();
            if (o instanceof Annuaire) {
                return (Annuaire) o;
            }
        }
        return null;
    }
}
