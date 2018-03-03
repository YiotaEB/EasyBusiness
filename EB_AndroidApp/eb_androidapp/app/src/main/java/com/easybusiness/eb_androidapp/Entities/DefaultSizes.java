


/**********************************************************************************/
/*** THIS FILE HAS BEEN AUTOMATICALLY GENERATED BY THE PANICKAPPS API GENERATOR ***/

/*                It is HIGHLY suggested that you do not edit this file.          */

//  DATABASE:     panayiota_easybusiness
//  FILE:         defaultsizes.java
//  TABLE:        defaultsizes
//  DATETIME:     2018-02-10 12:04:41pm
//  DESCRIPTION:  N/A

/**********************************************************************************/

package com.easybusiness.eb_androidapp.Entities;

import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class Defaultsizes implements Serializable {


	//-------------------- Supporting Finals --------------------

	final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	//-------------------- Attributes --------------------

    private int ID;
    private String Name;
    private int UnitTypeID;

	//-------------------- Constructor --------------------

    public Defaultsizes(
		int ID, 
		String Name, 
		int UnitTypeID
		) {
        this.ID = ID;
		this.Name = Name;
		this.UnitTypeID = UnitTypeID;
    }

	//-------------------- Getter Methods --------------------

	/**
     * @return int
     */
     public int getID() { return this.ID; }

	/**
     * @return String
     */
     public String getName() { return this.Name; }

	/**
     * @return int
     */
     public int getUnitTypeID() { return this.UnitTypeID; }


	//-------------------- Setter Methods --------------------

	/**
     * @param value varchar(255)
     */
     public void setName(String value) { this.Name = value; }

	/**
     * @param value int(11)
     */
     public void setUnitTypeID(int value) { this.UnitTypeID = value; }


	//-------------------- JSON Generation Methods --------------------

    /**
     * Specifies how objects of this class should be converted to JSON format.
     * @return String
     */
    public String toJSON() {
        return "\r\n{\r\n\t\"ID\": " + this.ID + ",\r\n\t\"Name\": \"" + this.Name+ "\",\r\n\t\"UnitTypeID\": " + this.UnitTypeID + "\r\n}";
    }
    
    /**
     * Converts an array of Defaultsizes objects to a JSON Array.
     * @param defaultsizes_array
     * @return String
     */
    public static String toJSONArray(Defaultsizes [] defaultsizes_array) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final Defaultsizes i : defaultsizes_array) {
            strArray.append(i.toJSON());
            strArray.append(", ");
        }
        strArray = new StringBuilder(strArray.substring(0, strArray.length() - 3));
        strArray.append("} ] ");
        return strArray.toString();
    }
    
    /**
     * Converts an ArrayList of Defaultsizes objects to a JSON Array.
     * @param defaultsizes_arraylist ArrayList of Defaultsizes to convert to JSON.
     * @return String
     */
    public static String toJSONArray(ArrayList<Defaultsizes> defaultsizes_arraylist) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final Defaultsizes i : defaultsizes_arraylist) {
            strArray.append(i.toJSON());
            strArray.append(", ");
        }
        strArray = new StringBuilder(strArray.substring(0, strArray.length() - 3));
        strArray.append("} ] ");
        return strArray.toString();
    }
    
    /**
     * Converts an Vector of Defaultsizes objects to a JSON Array.
     * @param defaultsizes_vector Vector of Defaultsizes to convert to JSON.
     * @return String
     */
    public static String toJSONArray(Vector<Defaultsizes> defaultsizes_vector) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final Defaultsizes i : defaultsizes_vector) {
            strArray.append(i.toJSON());
            strArray.append(", ");
        }
        strArray = new StringBuilder(strArray.substring(0, strArray.length() - 3));
        strArray.append("} ] ");
        return strArray.toString();
    }
    
    /**
     * Converts a List of Defaultsizes objects to a JSON Array.
     * @param defaultsizes_list List of Defaultsizes to convert to JSON.
     * @return String
     */
    public static String toJSONArray(List<Defaultsizes> defaultsizes_list) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final Defaultsizes i : defaultsizes_list) {
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

