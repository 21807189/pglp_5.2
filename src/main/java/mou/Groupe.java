package mou;

import java.io.Serializable;
import java.util.ArrayList;

public class Groupe implements Composant, IterableComposant, Serializable {
    
    private String nom;
    
    private ArrayList<Composant> per_groupe;

    
    public Groupe(final String grp) {
        this.nom = grp;
        per_groupe = new ArrayList<Composant>();
    }

    public void add(final Composant c) {
    	per_groupe.add(c);
    }

    
    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = prime * result
                + ((per_groupe == null) ? 0 : per_groupe.hashCode());
        result = prime * result
                + ((nom == null) ? 0 : nom.hashCode());
        return result;
    }

    public ArrayList<String> hierarchie() {
        ArrayList<String> liste = new ArrayList<String>();
        liste.add(this.toString());
        IterateurComposant ite = this.iterateur();
        Composant c;
        while (ite.hasNext()) {
            c = ite.next();
            for (String s : c.hierarchie()) {
                if (s.substring(0, 1).equals("\t")) {
                    s = "\t" + s;
                } else {
                    s = "\t|-   " + s;
                }
                liste.add(s);
            }
        }
        return liste;
    }

    
    public String toString() {
        return "Groupe " + this.nom + " (" + this.per_groupe.size() + ")";
    }

   
    public IterateurComposant iterateur() {
        IterateurComposant mou = new IterateurComposant(this.per_groupe);
        return mou;
    }

}
