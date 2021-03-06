


/**********************************************************************************/
/*** THIS FILE HAS BEEN AUTOMATICALLY GENERATED BY THE PANICKAPPS API GENERATOR ***/

/*                It is HIGHLY suggested that you do not edit this file.          */

//  DATABASE:     panayiota_easybusiness
//  FILE:         saleproducts.java
//  TABLE:        saleproducts
//  DATETIME:     2018-02-10 12:04:41pm
//  DESCRIPTION:  N/A

/**********************************************************************************/

package com.easybusiness.eb_androidapp.Entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class SaleProducts implements Serializable {


	//-------------------- Supporting Finals --------------------

	final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	//-------------------- Attributes --------------------

    private int ID;
    private int SaleID;
    private int ProductID;
    private int QuantitySold;

	//-------------------- Constructor --------------------

    public SaleProducts(
		int ID, 
		int SaleID, 
		int ProductID, 
		int QuantitySold
		) {
        this.ID = ID;
		this.SaleID = SaleID;
		this.ProductID = ProductID;
		this.QuantitySold = QuantitySold;
    }

	//-------------------- Getter Methods --------------------

	/**
     * @return int
     */
     public int getID() { return this.ID; }

	/**
     * @return int
     */
     public int getSaleID() { return this.SaleID; }

	/**
     * @return int
     */
     public int getProductID() { return this.ProductID; }

	/**
     * @return int
     */
     public int getQuantitySold() { return this.QuantitySold; }


	//-------------------- Setter Methods --------------------

	/**
     * @param value int(11)
     */
     public void setSaleID(int value) { this.SaleID = value; }

	/**
     * @param value int(11)
     */
     public void setProductID(int value) { this.ProductID = value; }

	/**
     * @param value int(11)
     */
     public void setQuantitySold(int value) { this.QuantitySold = value; }


	//-------------------- JSON Generation Methods --------------------

    /**
     * Specifies how objects of this class should be converted to JSON format.
     * @return String
     */
    public String toJSON() {
        return "\r\n{\r\n\t\"ID\": " + this.ID + ",\r\n\t\"SaleID\": " + this.SaleID + ",\r\n\t\"ProductID\": " + this.ProductID + ",\r\n\t\"QuantitySold\": " + this.QuantitySold + "\r\n}";
    }
    
    /**
     * Converts an array of SaleProducts objects to a JSON Array.
     * @param saleProducts_array
     * @return String
     */
    public static String toJSONArray(SaleProducts[] saleProducts_array) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final SaleProducts i : saleProducts_array) {
            strArray.append(i.toJSON());
            strArray.append(", ");
        }
        strArray = new StringBuilder(strArray.substring(0, strArray.length() - 3));
        strArray.append("} ] ");
        return strArray.toString();
    }
    
    /**
     * Converts an ArrayList of SaleProducts objects to a JSON Array.
     * @param saleProducts_arraylist ArrayList of SaleProducts to convert to JSON.
     * @return String
     */
    public static String toJSONArray(ArrayList<SaleProducts> saleProducts_arraylist) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final SaleProducts i : saleProducts_arraylist) {
            strArray.append(i.toJSON());
            strArray.append(", ");
        }
        strArray = new StringBuilder(strArray.substring(0, strArray.length() - 3));
        strArray.append("} ] ");
        return strArray.toString();
    }
    
    /**
     * Converts an Vector of SaleProducts objects to a JSON Array.
     * @param saleProducts_vector Vector of SaleProducts to convert to JSON.
     * @return String
     */
    public static String toJSONArray(Vector<SaleProducts> saleProducts_vector) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final SaleProducts i : saleProducts_vector) {
            strArray.append(i.toJSON());
            strArray.append(", ");
        }
        strArray = new StringBuilder(strArray.substring(0, strArray.length() - 3));
        strArray.append("} ] ");
        return strArray.toString();
    }
    
    /**
     * Converts a List of SaleProducts objects to a JSON Array.
     * @param saleProducts_list List of SaleProducts to convert to JSON.
     * @return String
     */
    public static String toJSONArray(List<SaleProducts> saleProducts_list) {
        StringBuilder strArray = new StringBuilder("[ ");
        for (final SaleProducts i : saleProducts_list) {
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

