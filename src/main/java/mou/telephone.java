package mou;

import java.io.Serializable;

public class telephone implements Serializable{
    private String information;
    private String numero;

   
    public telephone(final String num, final String info) {
        this.information = info;
        this.numero = num;
    }

    
    @Override
    public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = prime * result
                + ((information == null) ? 0 : information.hashCode());
        result = prime * result
                + ((numero == null) ? 0 : numero.hashCode());
        return result;
    }

    //comparer deux tel
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof telephone)) {
            return false;
        }
        telephone other = (telephone) obj;
        if (information == null) {
            if (other.information != null) {
                return false;
            }
        } else if (!information.equals(other.information)) {
            return false;
        }
        if (numero == null) {
            if (other.numero != null) {
                return false;
            }
        } else if (!numero.equals(other.numero)) {
            return false;
        }
        return true;
    }

    public String getInformation() {
        return information;
    }

    
    public String getNumero() {
        return numero;
    }

    
    @Override
    public String toString() {
        return "(" + this.information + ") " + this.numero;
    }

}
