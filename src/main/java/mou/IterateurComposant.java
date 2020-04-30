package mou;

import java.util.ArrayList;


public class IterateurComposant {
    
    private ArrayList<Composant> liste;

    
    public IterateurComposant(final ArrayList<Composant> groupe) {
        this.liste = new ArrayList<Composant>();
        this.liste.addAll(groupe);
    }

    
    public boolean hasNext() {
        return !this.liste.isEmpty();
    }

    
    public Composant next() {
        return liste.remove(0);
    }

}

