/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eb_managementapp.Entities;

public class MyDate implements Comparable {
    
    public int day;
    public int month;
    public int year;

    @Override
    public int compareTo(Object o) {
        MyDate d = (MyDate) o;
        return this.month - d.month;
    }
    
}
