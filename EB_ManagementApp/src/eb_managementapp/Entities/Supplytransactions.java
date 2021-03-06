


/**********************************************************************************/
/*** THIS FILE HAS BEEN AUTOMATICALLY GENERATED BY THE PANICKAPPS API GENERATOR ***/

/*                It is HIGHLY suggested that you do not edit this file.          */

//  DATABASE:     panayiota_easybusiness
//  FILE:         supplytransactions.java
//  TABLE:        supplytransactions
//  DATETIME:     2018-02-10 12:04:41pm
//  DESCRIPTION:  N/A

/**********************************************************************************/

package eb_managementapp.Entities;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Supplytransactions implements Serializable {


	//-------------------- Supporting Finals --------------------

	final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	//-------------------- Attributes --------------------

    private int ID;
    private int SupplierSuppliesID;
    private long DateMade;
    private int Quantity;

	//-------------------- Constructor --------------------

    public Supplytransactions(
		int ID, 
		int SupplierSuppliesID, 
		long DateMade, 
		int Quantity
		) {
        this.ID = ID;
		this.SupplierSuppliesID = SupplierSuppliesID;
		this.DateMade = DateMade;
		this.Quantity = Quantity;
    }

	//-------------------- Getter Methods --------------------

	/**
     * @return int
     */
     public int getID() { return this.ID; }

	/**
     * @return int
     */
     public int getSupplierSuppliesID() { return this.SupplierSuppliesID; }

	/**
     * @return int
     */
     public long getDateMade() { return this.DateMade; }

	/**
     * @return int
     */
     public int getQuantity() { return this.Quantity; }


	//-------------------- Setter Methods --------------------

	/**
     * @param value int(11)
     */
     public void setSupplierSuppliesID(int value) { this.SupplierSuppliesID = value; }

	/**
     * @param value int(13)
     */
     public void setDateMade(long value) { this.DateMade = value; }

	/**
     * @param value int(11)
     */
     public void setQuantity(int value) { this.Quantity = value; }


	//-------------------- JSON Generation Methods --------------------

    /**
     * Specifies how objects of this class should be converted to JSON format.
     * @return String
     */
    public String toJSON() {
        return "\r\n{\r\n\t\"ID\": " + this.ID + ",\r\n\t\"SupplierSuppliesID\": " + this.SupplierSuppliesID + ",\r\n\t\"DateMade\": " + this.DateMade + ",\r\n\t\"Quantity\": " + this.Quantity + "\r\n}";
    }
    
    /**
     * Converts an array of Supplytransactions objects to a JSON Array.
     * @param supplytransactions_array
     * @return String
     */
    public static String toJSONArray(Supplytransactions [] supplytransactions_array) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final Supplytransactions i : supplytransactions_array) {
            strArray.append(i.toJSON());
            strArray.append(", ");
        }
        strArray = new StringBuilder(strArray.substring(0, strArray.length() - 3));
        strArray.append("} ] ");
        return strArray.toString();
    }
    
    /**
     * Converts an ArrayList of Supplytransactions objects to a JSON Array.
     * @param supplytransactions_arraylist ArrayList of Supplytransactions to convert to JSON.
     * @return String
     */
    public static String toJSONArray(ArrayList<Supplytransactions> supplytransactions_arraylist) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final Supplytransactions i : supplytransactions_arraylist) {
            strArray.append(i.toJSON());
            strArray.append(", ");
        }
        strArray = new StringBuilder(strArray.substring(0, strArray.length() - 3));
        strArray.append("} ] ");
        return strArray.toString();
    }
    
    /**
     * Converts an Vector of Supplytransactions objects to a JSON Array.
     * @param supplytransactions_vector Vector of Supplytransactions to convert to JSON.
     * @return String
     */
    public static String toJSONArray(Vector<Supplytransactions> supplytransactions_vector) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final Supplytransactions i : supplytransactions_vector) {
            strArray.append(i.toJSON());
            strArray.append(", ");
        }
        strArray = new StringBuilder(strArray.substring(0, strArray.length() - 3));
        strArray.append("} ] ");
        return strArray.toString();
    }
    
    /**
     * Converts a List of Supplytransactions objects to a JSON Array.
     * @param supplytransactions_list List of Supplytransactions to convert to JSON.
     * @return String
     */
    public static String toJSONArray(List<Supplytransactions> supplytransactions_list) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final Supplytransactions i : supplytransactions_list) {
            strArray.append(i.toJSON());
            strArray.append(", ");
        }
        strArray = new StringBuilder(strArray.substring(0, strArray.length() - 3));
        strArray.append("} ] ");
        return strArray.toString();
    }
    
    @Override
    public String toString() {
        return toJSON();
    }

}

