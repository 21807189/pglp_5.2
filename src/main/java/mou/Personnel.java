package mou;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Personnel implements Composant, Serializable {

    private final int id;
    private final String nom;
    private final String prenom;
    private final String fonction;
    private final LocalDate dateNaissance;
    private final ArrayList<telephone> numeros;

    public static class Builder {
        
        private final int id;
        private final String nom;
        private final String prenom;
        private final LocalDate dateNaissance;
        private String fonction = "Employé";
        private ArrayList<telephone> numeros;

        public Builder(final int i, final String n, final String p,
                final LocalDate date, final telephone num) {
            this.id = i;
            this.nom = n;
            this.prenom = p;
            this.dateNaissance = date;
            this.numeros = new ArrayList<telephone>();
            numeros.add(num);
        }

        public Builder fonction(final String f) {
            this.fonction = f;
            return this;
        }

        public Builder addNumero(final telephone num) {
            this.numeros.add(num);
            return this;
        }

        public Personnel build() {
            return new Personnel(this);
        }
    }

    private Personnel(final Builder builder) {
        this.id = builder.id;
        this.nom = builder.nom;
        this.prenom = builder.prenom;
        this.dateNaissance = builder.dateNaissance;
        this.fonction = builder.fonction;
        this.numeros = builder.numeros;
    }

    public String getNom() {
        return nom;
    }
    
    public String getPrenom() {
        return prenom;
    }

    public String getFonction() {
        return fonction;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public List<telephone> getNumeros() {
        return Collections.unmodifiableList(numeros);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        String s = this.nom + " " + this.prenom;
        s = s.concat(", " + this.fonction);
        s = s.concat(" (" + this.dateNaissance.toString() + ")");
        s = s.concat(" [");
        for (telephone t : numeros) {
            s = s.concat(t.toString() + ", ");
        }
        s = s.substring(0, s.length() - 2);
        s = s.concat("]");
        return s;
    }

    public ArrayList<String> hierarchie() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(this.toString());
        return list;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((dateNaissance == null) ? 0 : dateNaissance.hashCode());
        result = prime * result
                + ((fonction == null) ? 0 : fonction.hashCode());
        result = prime * result + ((nom == null) ? 0 : nom.hashCode());
        result = prime * result + ((numeros == null) ? 0 : numeros.hashCode());
        result = prime * result + ((prenom == null) ? 0 : prenom.hashCode());
        return result;
    }

}
