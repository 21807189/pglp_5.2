package mou;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.List;
import java.io.Serializable;
import java.time.LocalDate;

public final class Personnel implements Composant, Serializable{
	private final String  nom;
	private final String prenom;
	private final java.time.LocalDate dateNaissance;
	private final ArrayList<telephone> numTelephone;

	public static class Builder {
		private final String  nom;
		private final String prenom;
		private final java.time.LocalDate dateNaissance;
		private ArrayList<telephone> numTelephone;
		
		public Builder(final String nom, final String prenom, final java.time.LocalDate dateNaissance, final telephone num) {
			
			this.nom = nom;
			this.prenom = prenom;
			this.dateNaissance = dateNaissance;
			
			this.numTelephone = new ArrayList<telephone>();
			numTelephone.add(num);
		}
		
		public Builder numeroTelephone(final  ArrayList<telephone> numTelephone) {
			this.numTelephone = numTelephone;
			return this;
		}
		
		public Personnel build() {
			return new Personnel(this);
		}

		public Builder addnumTelephone(final telephone num) {
            this.numTelephone.add(num);
            return this;
        }
		}
	
	
	private Personnel(final Builder builder) {
		nom = builder.nom;
		prenom = builder.prenom;
		dateNaissance = builder.dateNaissance;
		this.numTelephone = builder.numTelephone;
	}

	public ArrayList<String> hierarchie() {
		// TODO Auto-generated method stub
		return null;
	}
	
	   public String getNom() {
	        return nom;
	    }

	   
	    public String getPrenom() {
	        return prenom;
	    }

	    public LocalDate getDateNaissance() {
	        return dateNaissance;
	    }
	    public java.util.List<telephone> getnumTelephone() {
	        return Collections.unmodifiableList(numTelephone);
	    }

}
