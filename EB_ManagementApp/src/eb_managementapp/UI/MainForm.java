/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eb_managementapp.UI;

import Utilities.HTTPConnection;
import static eb_managementapp.EB_ManagementApp.addUsersForm;
import static eb_managementapp.EB_ManagementApp.customersForm;
import static eb_managementapp.EB_ManagementApp.addSuppliersForm;
import static eb_managementapp.EB_ManagementApp.addSuppliesForm;
import static eb_managementapp.EB_ManagementApp.addProductsForm;
import eb_managementapp.Entities.Countries;
import eb_managementapp.Entities.Customerproducts;
import eb_managementapp.Entities.Customers;
import eb_managementapp.Entities.Productionbatches;
import eb_managementapp.Entities.Products;
import eb_managementapp.Entities.Suppliers;
import eb_managementapp.Entities.Suppliersupplies;
import eb_managementapp.Entities.Supplies;
import eb_managementapp.Entities.Users;
import eb_managementapp.Entities.Producttypes;
import eb_managementapp.Entities.Productsizes;
import eb_managementapp.Entities.Saleproducts;
import eb_managementapp.Entities.Sales;
import eb_managementapp.Entities.Supplytransactions;
import eb_managementapp.Entities.Userlevels;
import eb_managementapp.UI.Forms.AddProductsForm;
import eb_managementapp.UI.Forms.AddSuppliersForm;
import eb_managementapp.UI.Forms.AddSuppliesForm;
import eb_managementapp.UI.Forms.CustomersForm;
import eb_managementapp.UI.Forms.AddUsersForm;
import org.json.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public final class MainForm extends javax.swing.JFrame {

    private ArrayList<Products> productsList;
    private ArrayList<Countries> countriesList;
    private ArrayList<Customers> customersList;
    private ArrayList<Users> employeesList;
    private ArrayList<Suppliers> suppliersList;
    private ArrayList<Supplies> suppliesList;
    private ArrayList<Customerproducts> customerProductsList;
    private ArrayList<Suppliersupplies> supplierSuppliesList;
    private ArrayList<Producttypes> productTypesList;
    private ArrayList<Productsizes> productSizesList;
    private ArrayList<Productionbatches> productionList;
    private ArrayList<Saleproducts> saleProductsList;
    private ArrayList<Sales> salesList;
    private ArrayList<Userlevels> positionsList;
    private ArrayList<Supplytransactions> supplyTransactionList;

    public MainForm() {
        initComponents();

        tabPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (tabPanel.getSelectedIndex()) {

                    //Home
                    case 0:
                        //TODO
                        break;

                    //Inventory
                    case 1:
                        //Products
                        getProducts();
                        getProductType();
                        getProduction();
                        getProductSize();
                        productsTab();
                        //Supplies
                        getSuppliers();
                        getSupplies();
                        suppliesTab();
                        break;

                    //Production
                    case 2:
                        getProductSize();
                        getProducts();
                        getProduction();
                        productionTab();
                        break;

                    //Sales
                    case 3:
                        getCustomers();
                        getProducts();
                        getProductSize();
                        getSaleProducts();
                        getSales();
                        salesTab();
                        break;

                    //Customers
                    case 4:
                        getCountries();
                        getCustomers();
                        customersTab();

                        break;

                    //Employess
                    case 5:
                        getPositions();
                        getCountries();
                        getEmployees();
                        employeesTab();

                        break;
                    //Suppliers
                    case 6:
                        getSupplies();
                        getSupplyTransactions();
                        getCountries();
                        getSuppliers();
                        suppliersTab();

                        break;

                }
            }
        });

        customerScrollPanel.setViewportView(customerDetailsTable);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(80, "Marks", "Value 1");
        dataset.setValue(70, "Marks", "Value 2");
        dataset.setValue(75, "Marks", "Value 3");
        JFreeChart chart = ChartFactory.createBarChart("Student's Score", "Student's Name", "Marks", dataset, PlotOrientation.VERTICAL, false, true, false);
        CategoryPlot p = chart.getCategoryPlot();
        p.setRangeGridlinePaint(Color.black);

        //Panel (same window):
        ChartPanel chartPanel = new ChartPanel(chart);
        statistics.setLayout(new java.awt.BorderLayout());
        statistics.add(chartPanel, BorderLayout.CENTER);
        statistics.validate();

        //Panel (same window):
        ChartPanel chartPanel2 = new ChartPanel(chart);
        customersGraphsPanel2.setLayout(new java.awt.BorderLayout());
        customersGraphsPanel2.add(chartPanel2, BorderLayout.CENTER);
        customersGraphsPanel2.validate();

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.setVisible(true);
    }

    private void addSales() {

        //Update the sales:
//        getCustomers();
//        getProducts();
//        getSales();
        //Get fields to add a new sale:
        int customerID = -1;
        int salesProductID = -1;
        int tax = 0;
        long salesTimeDate = System.currentTimeMillis();

        customerID = customersList.get(customerComboBox.getSelectedIndex()).getID();
        salesProductID = 0; //Update later

        //Add the new sale:
        String addSaleJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Sales", "Create",
                "SessionID=aa&ID=1&CustomerID=" + customerID + "&SaleProductID=" + salesProductID + "&Tax=" + tax + "&SaleTimeDate=" + salesTimeDate
        );

        System.out.println("Add Sale HTTP -> " + addSaleJSON);

        //Get the ID of the added Sale:
        String lastSaleIDJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Sales", "GetMaxID", "");

        if (lastSaleIDJSON.equals("0")) {
            showMessageDialog(null, "Failed to add Sale", "Error", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Get fields for SalesProducts:
        String saleID = lastSaleIDJSON;
        int productID = productsList.get(productComboBox.getSelectedIndex()).getID();
        int quantitySold = Integer.parseInt(productionQuantitySpinner.getValue().toString());

        //Add SalesProduct:
        String addSalesProductJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Saleproducts", "Create",
                "SessionID=aa&ID=1&SaleID=" + saleID + "&ProductID=" + productID + "&QuantitySold=" + quantitySold
        );

        System.out.println("Add SaleProducts HTTP -> " + addSalesProductJSON);

        //Get ID of the added SaleProducts:
        String lastSaleProductsIDJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Saleproducts", "GetMaxID", "");
        salesProductID = Integer.parseInt(lastSaleProductsIDJSON);

        //Update the latest Sales' SaleProductsID:
        String updateSalesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Sales", "Update",
                "SessionID=aa&ID=" + saleID + "&SaleProductID=" + salesProductID + "&Tax=" + tax + "&SaleTimeDate=" + salesTimeDate
        );

        System.out.println("Update Sales HTTP -> " + updateSalesJSON);

    }

    private void addProduction() {
//        getProducts();

        //Get field values:
        String productionQuantity = productionQuantitySpinner.getValue().toString();
        int bottleSize = productSizesList.get(bottleSizeComboBox.getSelectedIndex()).getID();
        int currentID = productComboBox.getSelectedIndex();
        int productID = productsList.get(currentID).getID();
        String productName = productsList.get(currentID).getName();
        double price = productsList.get(currentID).getPrice();
        int quantityInStock = productsList.get(currentID).getQuantityInStock();
        int productSizeID = productsList.get(currentID).getProductSizeID();
        int productTypeID = productsList.get(currentID).getProductTypeID();
        int productSuppliesID = productsList.get(currentID).getProductSuppliesID();

//        Date productionDate = dateOfProduction.getDate();
//        int dateProduction = productionDate.getDate(); //TODO Show correct day.
        quantityInStock += Integer.parseInt(bottleQuantitySpinner.getValue().toString());

        //Make the call:
        String addProductionJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Productionbatches", "Create",
                "SessionID=aa&ID=1&QuantityProduced=" + productionQuantity + "&ProductID=" + productID + "&ProductionDate=0"
        );
        System.out.println("Add Production HTTP -> " + addProductionJSON);

        String updateProductsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Products", "Update",
                "SessionID=aa&ID=" + productID + "&Name=" + productName + "&Price=" + price + "&QuantityInStock="
                + quantityInStock + "&ProductTypeID=" + productTypeID + "&ProductSizeID=" + productSizeID + "&ProductSuppliesID=" + productSuppliesID);

        System.out.println("Update Products HTTP -> " + addProductionJSON);

        try {
            JSONObject jsonObject = new JSONObject(addProductionJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);

            if (status.equals(HTTPConnection.RESPONSE_ERROR)) {
                System.out.println("Fail " + addProductionJSON);
            } else if (status.equals(HTTPConnection.RESPONSE_OK)) {
                //Reset fields:
                setVisible(true);
                productComboBox.setSelectedIndex(0);
                bottleSizeComboBox.setSelectedIndex(0);
                bottleQuantitySpinner.setValue(0);
                productionQuantitySpinner.setValue(0);
                //todo date
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCustomers() {
        customersList = new ArrayList<>();
        customerComboBox.removeAllItems();

        //Get customers from api
        String customersJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Customers", "GetMultiple", "SessionID=aa");
        try {
            System.out.println("Get Customers HTTP -> " + customersJSON);
            JSONObject jsonObject = new JSONObject(customersJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    String name = currentItem.getString("Name");
                    int countryID = currentItem.getInt("CountryID");
                    String city = currentItem.getString("City");
                    String telephone = currentItem.getString("Telephone");
                    String address = currentItem.getString("Address");
                    int customerProductsID = currentItem.getInt("CustomerProductsID");

                    Customers customer = new Customers(id, name, countryID, city, telephone, address, customerProductsID);
                    customersList.add(customer);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + customersJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < customersList.size(); i++) {
            customerComboBox.addItem(customersList.get(i).getName());
        }

    }

    private void customersTab() {
        refreshCusDetailsBtn.setEnabled(false);

        //Create a new model for the table:
        DefaultTableModel customersTableModel = new DefaultTableModel();

        //Add the table columns:
        customersTableModel.addColumn("No");
        customersTableModel.addColumn("Name");
        customersTableModel.addColumn("Telephone");
        customersTableModel.addColumn("Address");
        customersTableModel.addColumn("City");
        customersTableModel.addColumn("Country");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < customersList.size(); i++) {

            //Put Countries Name in the Table
            String countryName = "";
            for (int j = 0; j < countriesList.size(); j++) {
                if (countriesList.get(j).getID() == customersList.get(i).getCountryID()) {
                    countryName = countriesList.get(j).getName();
                }
            }

            Object[] currentRow = {
                i + 1,
                customersList.get(i).getName(),
                customersList.get(i).getTelephone(),
                customersList.get(i).getAddress(),
                customersList.get(i).getCity(),
                countryName
            };
            customersTableModel.addRow(currentRow);
        }
        customerDetailsTable.setModel(customersTableModel);
        refreshCusDetailsBtn.setEnabled(true);
        numOfCustomersLabel.setText(String.valueOf(customersList.size()));
    }

    private void employeesTab() {
        refreshEmployeesBtn.setEnabled(false);

        //Create a new model for the table:
        DefaultTableModel employeesTableModel = new DefaultTableModel();

        //Add the table columns:
        employeesTableModel.addColumn("No");
        employeesTableModel.addColumn("Username");
        employeesTableModel.addColumn("Firstname");
        employeesTableModel.addColumn("Lastname");
        employeesTableModel.addColumn("Position");
        employeesTableModel.addColumn("Date Hired");
        employeesTableModel.addColumn("Telephone");
        employeesTableModel.addColumn("Address");
        employeesTableModel.addColumn("City");
        employeesTableModel.addColumn("Country");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < employeesList.size(); i++) {

            //Put Countries Name in the Table
            String countryName = "";
            for (int j = 0; j < countriesList.size(); j++) {
                if (countriesList.get(j).getID() == employeesList.get(i).getCountryID()) {
                    countryName = countriesList.get(j).getName();
                }
            }

            //Put position in the Table
            String position = "";
            for (int j = 0; j < positionsList.size(); j++) {
                if (employeesList.get(i).getUserLevelID() == positionsList.get(j).getUserLevelID()) {
                    position = positionsList.get(j).getUserLevelName();
                    break;
                }
            }
            Object[] currentRow = {
                i + 1,
                employeesList.get(i).getUsername(),
                employeesList.get(i).getFirstname(),
                employeesList.get(i).getLastname(),
                position,
                Users.DATE_FORMAT.format(new Date(employeesList.get(i).getDateHired())),
                employeesList.get(i).getTelephone(),
                employeesList.get(i).getAddress(),
                employeesList.get(i).getCity(),
                countryName
            };
            employeesTableModel.addRow(currentRow);
        }
        employeesTable.setModel(employeesTableModel);
        refreshEmployeesBtn.setEnabled(true);
        numOfEmployeesLabel.setText(String.valueOf(employeesList.size()));

    }

    public void getCustomerProducts() {
        customerProductsList = new ArrayList<>();
        refreshCustProductsBtn.setEnabled(false);
//        getProducts();
//        getCustomers();

        //Get customers from api
        String customerProductsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Customerproducts", "GetMultiple", "SessionID=aa");
        try {
            System.out.println("Get Customer Products HTTP -> " + customerProductsJSON);
            JSONObject jsonObject = new JSONObject(customerProductsJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    int customerID = currentItem.getInt("CustomerID");
                    int productsID = currentItem.getInt("ProductID");

                    Customerproducts customerProducts = new Customerproducts(id, customerID, productsID);
                    customerProductsList.add(customerProducts);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + customerProductsJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < customersList.size(); i++) {
            customerComboBox.addItem(customersList.get(i).getName());
        }

        //Create a new model for the table:
        DefaultTableModel customersProductsTableModel = new DefaultTableModel();

        //Add the table columns:
        customersProductsTableModel.addColumn("ID");
        customersProductsTableModel.addColumn("Customer");
        customersProductsTableModel.addColumn("Product");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < customerProductsList.size(); i++) {
            //Put Customers Name in the Table
            String customerName = "";
            for (int j = 0; j < customersList.size(); j++) {
                if (customersList.get(j).getID() == customerProductsList.get(i).getCustomerID()) {
                    customerName = customersList.get(j).getName();
                }
            }
            //Put Products Name in the Table
            String productName = "";
            for (int j = 0; j < productsList.size(); j++) {
                if (productsList.get(j).getID() == customerProductsList.get(i).getProductID()) {
                    productName = productsList.get(j).getName();
                }
            }

            Object[] currentRow = {
                customerProductsList.get(i).getID(),
                customerName,
                productName
            };
            customersProductsTableModel.addRow(currentRow);
        }
        customerProductsTable.setModel(customersProductsTableModel);
        refreshCustProductsBtn.setEnabled(true);

    }

    public void getSuppliers() {
        suppliersList = new ArrayList<>();

        //Get customers from api
        String suppliersJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Suppliers", "GetMultiple", "SessionID=aa");
        try {
            System.out.println("Get Suppliers HTTP -> " + suppliersJSON);
            JSONObject jsonObject = new JSONObject(suppliersJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int supplierID = currentItem.getInt("ID");
                    String name = currentItem.getString("Name");
                    int countryID = currentItem.getInt("CountryID");
                    String address = currentItem.getString("Address");
                    String telephone = currentItem.getString("Telephone");
                    String city = currentItem.getString("City");

                    Suppliers supplier = new Suppliers(supplierID, name, countryID, address, telephone, city);
                    suppliersList.add(supplier);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + suppliersJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void suppliersTab() {

        //SUPPLIER DETAILS TABLE
        refreshSuplTableBtn.setEnabled(false);
        //Create a new model for the table:
        DefaultTableModel supplierTableModel = new DefaultTableModel();

        //Add the table columns:
        supplierTableModel.addColumn("No");
        supplierTableModel.addColumn("Name");
        supplierTableModel.addColumn("Telephone");
        supplierTableModel.addColumn("Address");
        supplierTableModel.addColumn("City");
        supplierTableModel.addColumn("Country");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < suppliersList.size(); i++) {

            //Put Countries Name in the Table
            String countryName = "";
            for (int j = 0; j < countriesList.size(); j++) {
                if (countriesList.get(j).getID() == suppliersList.get(i).getCountryID()) {
                    countryName = countriesList.get(j).getName();
                }
            }

            Object[] currentRow = {
                i + 1,
                suppliersList.get(i).getName(),
                suppliersList.get(i).getTelephone(),
                suppliersList.get(i).getAddress(),
                suppliersList.get(i).getCity(),
                countryName
            };
            supplierTableModel.addRow(currentRow);
        }
        supplierDetailsTable.setModel(supplierTableModel);
        refreshSuplTableBtn.setEnabled(true);

        //SUPPLIER PURCHASES RECORDS TABLE
        refreshPurchasesTableBtn.setEnabled(false);
        //Create a new model for the table:
        DefaultTableModel supplyPurchasesTableModel = new DefaultTableModel();

        //Add the table columns:
        supplyPurchasesTableModel.addColumn("No");
        supplyPurchasesTableModel.addColumn("Date");
        supplyPurchasesTableModel.addColumn("Supplier");
        supplyPurchasesTableModel.addColumn("Total");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < supplyTransactionList.size(); i++) {

            double total = 0.0;
            for (int j = 0; j < supplyTransactionList.size(); j++) {
                int sold = supplyTransactionList.get(j).getQuantity();
                System.out.println("Sold:" + sold);
                double price = 0.0;
                for (int k = 0; k < suppliesList.size(); k++) {
                    if (suppliesList.get(k).getID() == supplyTransactionList.get(j).getSupplierSuppliesID()) {
                        price = suppliesList.get(k).getPrice();
                        System.out.println("Price:" + price);
                        break;
                    }
                    
                }
                total += (price * sold);
            }

            //Put customerName in the Table
            String supplierName = "";
            for (int j = 0; j < suppliersList.size(); j++) {
                if (suppliersList.get(j).getID() == supplyTransactionList.get(i).getSupplierSuppliesID()) {
                    supplierName = suppliersList.get(j).getName();
                }
            }
            Object[] currentRow = {
                i + 1,
                supplyTransactionList.get(i).getDateMade(),
                supplierName,
                "€" + String.format("%.2g%n", total)
            };
            supplyPurchasesTableModel.addRow(currentRow);
        }
        purchHistoryTable.setModel(supplyPurchasesTableModel);
        refreshPurchasesTableBtn.setEnabled(true);

    }

    public void getEmployees() {
        employeesList = new ArrayList<>();

        //Get customers from api
        String employeesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Users", "GetMultiple", "SessionID=aa");
        try {
            System.out.println("Get Users HTTP -> " + employeesJSON);
            JSONObject jsonObject = new JSONObject(employeesJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int userID = currentItem.getInt("UserID");
                    String username = currentItem.getString("Username");
                    String firstname = currentItem.getString("Firstname");
                    String lastname = currentItem.getString("Lastname");
                    String password = currentItem.getString("Password");

                    long dateHiredLong = currentItem.getLong("DateHired");
                    int dateHired = (int) dateHiredLong;

                    int countryID = currentItem.getInt("CountryID");
                    String city = currentItem.getString("City");
                    String telephone = currentItem.getString("Telephone");
                    String address = currentItem.getString("Address");
                    int userLevelID = currentItem.getInt("UserLevelID");

                    Users employees = new Users(userID, username, password, userLevelID, firstname, lastname, dateHired, city, address, telephone, countryID);
                    employeesList.add(employees);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + employeesJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getSupplies() {
        suppliesList = new ArrayList<>();

        //Get customers from api
        String suppliersJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Supplies", "GetMultiple", "SessionID=aa");
        try {
            System.out.println("Get Supplies  HTTP -> " + suppliersJSON);
            JSONObject jsonObject = new JSONObject(suppliersJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int suppliesID = currentItem.getInt("ID");
                    String name = currentItem.getString("Name");
                    int supplierID = currentItem.getInt("SupplierID");
                    int quantity = currentItem.getInt("Quantity");
                    float price = currentItem.getFloat("Price");

                    Supplies supplies = new Supplies(suppliesID, name, supplierID, quantity, price);
                    suppliesList.add(supplies);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + suppliersJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getSupplyTransactions() {
        supplyTransactionList = new ArrayList<>();

        //Get customers from api
        String supplyTransactionJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Supplytransactions", "GetMultiple", "SessionID=aa");
        try {
            System.out.println("Get SupplyTransactions HTTP -> " + supplyTransactionJSON);
            JSONObject jsonObject = new JSONObject(supplyTransactionJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    int supplierSuppliesID = currentItem.getInt("SupplierSuppliesID");
                    int quantity = currentItem.getInt("Quantity");
                    int dateMade = currentItem.getInt("DateMade");

                    Supplytransactions c = new Supplytransactions(id, supplierSuppliesID, dateMade, quantity);
                    supplyTransactionList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + supplyTransactionJSON);
            }
        } catch (JSONException e) {
            System.out.println(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void suppliesTab() {
        refreshSuppliesTable.setEnabled(false);

        //Create a new model for the table:
        DefaultTableModel suppliesTableModel = new DefaultTableModel();

        //Add the table columns:
        suppliesTableModel.addColumn("No");
        suppliesTableModel.addColumn("Name");
        suppliesTableModel.addColumn("Supplier");
        suppliesTableModel.addColumn("Quantity");
        suppliesTableModel.addColumn("Price");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < suppliesList.size(); i++) {

            //Put Supplier Name in the Table
            String supplierName = "";
            for (int j = 0; j < suppliersList.size(); j++) {
                if (suppliersList.get(j).getID() == suppliesList.get(i).getSupplierID()) {
                    supplierName = suppliersList.get(j).getName();
                }
            }

            Object[] currentRow = {
                i + 1,
                suppliesList.get(i).getName(),
                supplierName,
                suppliesList.get(i).getQuantity(),
                suppliesList.get(i).getPrice()

            };
            suppliesTableModel.addRow(currentRow);
        }
        suppliesDetailsTable.setModel(suppliesTableModel);
        refreshSuppliesTable.setEnabled(true);

    }

    public void getCountries() {
        countriesList = new ArrayList<>();

        //Get customers from api
        String countriesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Countries", "GetMultiple", "");
        try {
            System.out.println("Get countries HTTP -> " + countriesJSON);
            JSONObject jsonObject = new JSONObject(countriesJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    String name = currentItem.getString("Name");

                    Countries c = new Countries(id, name);
                    countriesList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + countriesJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getProductType() {
        productTypesList = new ArrayList<>();

        //Get product types from api
        String productTypesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Producttypes", "GetMultiple", "SessionID=aa");
        try {
            JSONObject jsonObject = new JSONObject(productTypesJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    String name = currentItem.getString("Name");

                    Producttypes c = new Producttypes(id, name);
                    productTypesList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + productTypesJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getProductSize() {
        productSizesList = new ArrayList<>();

        bottleSizeComboBox.removeAllItems();
        productSizeComboBox.removeAllItems();

        //Get product Sizes from api
        String productSizesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Productsizes", "GetMultiple", "SessionID=aa");
        try {
            JSONObject jsonObject = new JSONObject(productSizesJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    String name = currentItem.getString("Name");
                    int unitTypeID = currentItem.getInt("UnitTypeID");

                    Productsizes c = new Productsizes(id, name, unitTypeID);
                    productSizesList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + productSizesJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < productSizesList.size(); i++) {
            bottleSizeComboBox.addItem(productSizesList.get(i).getName());
            productSizeComboBox.addItem(productSizesList.get(i).getName());
        }
    }

    public void getProducts() {
        productsList = new ArrayList<>();

        productComboBox.removeAllItems();
        productsComboBox.removeAllItems();

        //Get customers from api
        String productsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Products", "GetMultiple", "SessionID=aa");
        System.out.println("Product HTTP -> " + productsJSON);
        try {
            JSONObject jsonObject = new JSONObject(productsJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    String name = currentItem.getString("Name");
                    double price = currentItem.getDouble("Price");
                    int quantity = currentItem.getInt("QuantityInStock");
                    int productSizeID = currentItem.getInt("ProductSizeID");
                    int productTypeID = currentItem.getInt("ProductTypeID");
                    int productSuppliesID = currentItem.getInt("ProductSuppliesID");

                    Products c = new Products(id, name, price, quantity, productSizeID, productTypeID, productSuppliesID);
                    productsList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + productsJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < productsList.size(); i++) {
            productsComboBox.addItem(productsList.get(i).getName());
            productComboBox.addItem(productsList.get(i).getName());
        }

    }

    public void productsTab() {
        refreshProductsTable.setEnabled(false);

        //Create a new model for the table:
        DefaultTableModel productsTableModel = new DefaultTableModel();

        //Add the table columns:
        productsTableModel.addColumn("No");
        productsTableModel.addColumn("Name");
        productsTableModel.addColumn("Size");
        productsTableModel.addColumn("Quantity");
        productsTableModel.addColumn("Price");
        productsTableModel.addColumn("Type");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < productsList.size(); i++) {

            //Put productSizes Name in the Table
            String productSizeName = "";
            for (int j = 0; j < productSizesList.size(); j++) {
                if (productSizesList.get(j).getID() == productsList.get(i).getID()) {
                    productSizeName = productSizesList.get(j).getName();
                }
            }

            //Put productType Name in the Table
            String productTypeName = "";
            for (int j = 0; j < productTypesList.size(); j++) {
                if (productTypesList.get(j).getID() == productsList.get(i).getID()) {
                    productTypeName = productTypesList.get(j).getName();
                }
            }

            Object[] currentRow = {
                i + 1,
                productsList.get(i).getName(),
                productSizeName,
                productsList.get(i).getQuantityInStock(),
                productsList.get(i).getPrice(),
                productTypeName

            };
            productsTableModel.addRow(currentRow);
        }
        productsDetailsTable.setModel(productsTableModel);
        refreshProductsTable.setEnabled(true);
    }

    public void getProduction() {
        productionList = new ArrayList<>();

        //Get customers from api
        String productsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Productionbatches", "GetMultiple", "SessionID=aa");
        try {
            JSONObject jsonObject = new JSONObject(productsJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    int quantityProduced = currentItem.getInt("QuantityProduced");
                    int productID = currentItem.getInt("ProductID");
                    int productionDate = currentItem.getInt("ProductionDate");

                    Productionbatches c = new Productionbatches(id, quantityProduced, productID, productionDate);
                    productionList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + productsJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void productionTab() {
        refreshProductionButton.setEnabled(false);

        //Create a new model for the table:
        DefaultTableModel productsTableModel = new DefaultTableModel();

        //Add the table columns:
        productsTableModel.addColumn("No");
        productsTableModel.addColumn("Production Date");
        productsTableModel.addColumn("Product Name");
        productsTableModel.addColumn("Quantity Produced");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < productionList.size(); i++) {

            //Put productType Name in the Table
            String productName = "";
            for (int j = 0; j < productsList.size(); j++) {
                if (productsList.get(j).getID() == productionList.get(i).getProductID()) {
                    productName = productsList.get(j).getName();
                    break;
                }
            }

            Object[] currentRow = {
                i + 1,
                productionList.get(i).getProductionDate(),
                productName,
                productionList.get(i).getQuantityProduced()

            };
            productsTableModel.addRow(currentRow);
        }
        productionDetailsTable.setModel(productsTableModel);
        refreshProductionButton.setEnabled(true);
    }

    public void getSaleProducts() {
        saleProductsList = new ArrayList<>();

        //Get customers from api
        String saleProductsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Saleproducts", "GetMultiple", "SessionID=aa");
        try {
            System.out.println("Get Sale Products HTTP -> " + saleProductsJSON);
            JSONObject jsonObject = new JSONObject(saleProductsJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    int saleID = currentItem.getInt("SaleID");
                    int productID = currentItem.getInt("ProductID");
                    int quantitySold = currentItem.getInt("QuantitySold");

                    Saleproducts c = new Saleproducts(id, saleID, productID, quantitySold);
                    saleProductsList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + saleProductsJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getSales() {
        salesList = new ArrayList<>();

        //Get customers from api
        String salesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Sales", "GetMultiple", "SessionID=aa");
        try {
            System.out.println("Get Sales HTTP -> " + salesJSON);
            JSONObject jsonObject = new JSONObject(salesJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("ID");
                    int customerID = currentItem.getInt("CustomerID");
                    int saleProductsID = currentItem.getInt("SaleProductsID");
                    int tax = currentItem.getInt("Tax");
                    int saleTimeDate = currentItem.getInt("SaleTimeDate");

                    Sales c = new Sales(id, customerID, saleProductsID, tax, saleTimeDate);
                    salesList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + salesJSON);
            }
        } catch (JSONException e) {
            System.out.println(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void salesTab() {
        refreshSalesButton.setEnabled(false);

        //Create a new model for the table:
        DefaultTableModel salesTableModel = new DefaultTableModel();

        //Add the table columns:
        salesTableModel.addColumn("No");
        salesTableModel.addColumn("Date");
        salesTableModel.addColumn("Customer");
        salesTableModel.addColumn("Total Revenue");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < salesList.size(); i++) {

            double total = 0.0;
            for (int j = 0; j < saleProductsList.size(); j++) {
                int sold = saleProductsList.get(j).getQuantitySold();
                System.out.println("Sold:" + sold);
                double price = 0.0;
                for (int k = 0; k < productsList.size(); k++) {
                    if (productsList.get(k).getID() == saleProductsList.get(j).getProductID()) {
                        price = productsList.get(k).getPrice();
                        System.out.println("Price:" + price);
                        break;
                    }

                }
                total += (price * sold);
            }

            //Math.round(total);
            //Put customerName in the Table
            String customerName = "";
            for (int j = 0; j < customersList.size(); j++) {
                if (customersList.get(j).getID() == salesList.get(i).getID()) {
                    customerName = customersList.get(j).getName();
                }
            }

            Object[] currentRow = {
                i + 1,
                salesList.get(i).getSaleTimeDate(),
                customerName,
                String.valueOf(total)
            };

            salesTableModel.addRow(currentRow);
        }
        salesDetailsTable.setModel(salesTableModel);
        refreshSalesButton.setEnabled(true);
    }

    public void getSupplierSupplies() {
        supplierSuppliesList = new ArrayList<>();
        refreshSuppliesTableBtn.setEnabled(false);
//        getSupplies();
//        getSuppliers();

        //Get customers from api
        String supplierSuppliesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Suppliersupplies", "GetMultiple", "SessionID=aa");
        try {
            System.out.println("Get supplier supplies HTTP -> " + supplierSuppliesJSON);
            JSONObject jsonObject = new JSONObject(supplierSuppliesJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int supplierSuppliesID = currentItem.getInt("ID");
                    int supplierID = currentItem.getInt("SupplierID");
                    int supplyID = currentItem.getInt("SupplyID");

                    Suppliersupplies supplierSupplies = new Suppliersupplies(supplierSuppliesID, supplierID, supplyID);
                    supplierSuppliesList.add(supplierSupplies);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + supplierSuppliesJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Create a new model for the table:
        DefaultTableModel supplierSuppliesTableModel = new DefaultTableModel();

        //Add the table columns:
        supplierSuppliesTableModel.addColumn("No");
        supplierSuppliesTableModel.addColumn("Supplier");
        supplierSuppliesTableModel.addColumn("Supplies");
        supplierSuppliesTableModel.addColumn("Price");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < supplierSuppliesList.size(); i++) {

            //Put Supplier Name in the Table
            String supplierName = "";
            for (int j = 0; j < suppliersList.size(); j++) {
                if (suppliersList.get(j).getID() == supplierSuppliesList.get(i).getSupplierID()) {
                    supplierName = suppliersList.get(j).getName();
                }
            }

            //Put Supplies Name in the Table
            String suppliesName = "";
            float suppliesPrice = 0;
            for (int j = 0; j < suppliesList.size(); j++) {
                if (suppliesList.get(j).getID() == supplierSuppliesList.get(i).getSupplyID()) {
                    suppliesName = suppliesList.get(j).getName();

                }
            }

            Object[] currentRow = {
                i + 1,
                supplierName,
                suppliesName,
                suppliesPrice = suppliesList.get(i).getPrice()};
            supplierSuppliesTableModel.addRow(currentRow);
        }
        suppliesTable.setModel(supplierSuppliesTableModel);
        refreshSuppliesTableBtn.setEnabled(false);
    }

    public void getPositions() {
        positionsList = new ArrayList<>();

        //Get customers from api
        String positionsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Userlevels", "GetMultiple", "SessionID=aa");
        try {
            JSONObject jsonObject = new JSONObject(positionsJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    int id = currentItem.getInt("UserLevelID");
                    String name = currentItem.getString("UserLevelName");
                    boolean show;
                    int showInt = currentItem.getInt("Show");
                    if (showInt > 0) {
                        show = true;
                    } else {
                        show = false;
                    }
                    Userlevels u = new Userlevels(id, name, show);
                    positionsList.add(u);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                System.out.println("Fail " + positionsJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPanel = new javax.swing.JTabbedPane();
        home = new javax.swing.JPanel();
        statistics = new javax.swing.JPanel();
        dialyProduction = new javax.swing.JPanel();
        productionScrollPane = new javax.swing.JScrollPane();
        dailyProductionTable = new javax.swing.JTable();
        moreProdDetailsBtn = new javax.swing.JButton();
        latestSales = new javax.swing.JPanel();
        salesScrollPanel = new javax.swing.JScrollPane();
        dailySalesTable = new javax.swing.JTable();
        moreSalesDetailsBtn = new javax.swing.JButton();
        latestPurchases = new javax.swing.JPanel();
        purchasesScrollPanel = new javax.swing.JScrollPane();
        dailyPurchasesTable = new javax.swing.JTable();
        morePurchDetailsBtn = new javax.swing.JButton();
        inventory = new javax.swing.JPanel();
        invenrotyTabPanel = new javax.swing.JTabbedPane();
        productsTab = new javax.swing.JPanel();
        productsDetailsPanel = new javax.swing.JPanel();
        importProductsButton = new javax.swing.JButton();
        searchProducts = new javax.swing.JTextField();
        refreshProductsTable = new javax.swing.JButton();
        productsScrollPanel = new javax.swing.JScrollPane();
        productsDetailsTable = new javax.swing.JTable();
        suppliesInvTabs = new javax.swing.JPanel();
        suppliesPanel = new javax.swing.JPanel();
        suppliesDetailsPanel = new javax.swing.JPanel();
        importSuppliesButton = new javax.swing.JButton();
        searchSupplies = new javax.swing.JTextField();
        refreshSuppliesTable = new javax.swing.JButton();
        suppliesScrollPanel = new javax.swing.JScrollPane();
        suppliesDetailsTable = new javax.swing.JTable();
        production = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        productNameTextField = new javax.swing.JLabel();
        productNameTextField1 = new javax.swing.JLabel();
        productComboBox = new javax.swing.JComboBox<>();
        productNameTextField2 = new javax.swing.JLabel();
        bottleSizeComboBox = new javax.swing.JComboBox<>();
        productionQuantitySpinner = new javax.swing.JSpinner();
        productNameTextField3 = new javax.swing.JLabel();
        bottleQuantitySpinner = new javax.swing.JSpinner();
        addBottlesQuantityButton = new javax.swing.JButton();
        productNameTextField4 = new javax.swing.JLabel();
        productNameTextField5 = new javax.swing.JLabel();
        addBottlesQuantity = new javax.swing.JButton();
        dateOfProduction = new org.jdesktop.swingx.JXDatePicker();
        hireDateLabel = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        productionScrollPanel = new javax.swing.JScrollPane();
        productionDetailsTable = new javax.swing.JTable();
        searchProductionDate = new javax.swing.JTextField();
        refreshProductionButton = new javax.swing.JButton();
        refreshSuplTableBtn2 = new javax.swing.JButton();
        sales = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        saleScrollPanel = new javax.swing.JScrollPane();
        salesDetailsTable = new javax.swing.JTable();
        searchSale = new javax.swing.JTextField();
        refreshSalesButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jPanel7 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        productNameTextField6 = new javax.swing.JLabel();
        productNameTextField7 = new javax.swing.JLabel();
        customerComboBox = new javax.swing.JComboBox<>();
        productNameTextField8 = new javax.swing.JLabel();
        productSizeComboBox = new javax.swing.JComboBox<>();
        quantitySpinner = new javax.swing.JSpinner();
        addSaleButton = new javax.swing.JButton();
        productNameTextField9 = new javax.swing.JLabel();
        productsComboBox = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        weeklySalesScrollPanel = new javax.swing.JScrollPane();
        weeklySalesDetailsTable = new javax.swing.JTable();
        searchWeeklySale = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        monthlySalesScrollPanel = new javax.swing.JScrollPane();
        monthlySalesDetailsTable = new javax.swing.JTable();
        searceMonthlySale = new javax.swing.JTextField();
        customers = new javax.swing.JPanel();
        noCustomerLb = new javax.swing.JLabel();
        customerDetailsPanel = new javax.swing.JPanel();
        expCusDetailsBtn = new javax.swing.JButton();
        printCusDetailsBtn = new javax.swing.JButton();
        importCustomerBtn = new javax.swing.JButton();
        searchCustomerTxt = new javax.swing.JTextField();
        refreshCusDetailsBtn = new javax.swing.JButton();
        customerScrollPanel = new javax.swing.JScrollPane();
        customerDetailsTable = new javax.swing.JTable();
        customerTabPanel = new javax.swing.JTabbedPane();
        custSalesPanel = new javax.swing.JPanel();
        importCustSalesBtn = new javax.swing.JButton();
        searchCustSalesTxt = new javax.swing.JTextField();
        exportCustSalesBtn = new javax.swing.JButton();
        printCustSalesBtn = new javax.swing.JButton();
        refreshCustSalesTable = new javax.swing.JButton();
        customersSalesTabPane = new javax.swing.JScrollPane();
        custSalesTable = new javax.swing.JTable();
        custProductsPanel = new javax.swing.JPanel();
        exportCustProductsBtn = new javax.swing.JButton();
        printCustProductsBtn = new javax.swing.JButton();
        searchCustProductsTxt = new javax.swing.JTextField();
        refreshCustProductsBtn = new javax.swing.JButton();
        custProScrollPanel = new javax.swing.JScrollPane();
        customerProductsTable = new javax.swing.JTable();
        customersGraphsPanel = new javax.swing.JPanel();
        customersGraphsPanel2 = new javax.swing.JPanel();
        numOfCustomersLabel = new javax.swing.JLabel();
        employees = new javax.swing.JPanel();
        noEmployeesLb = new javax.swing.JLabel();
        employeeListPanel = new javax.swing.JPanel();
        employeeScrollPanel = new javax.swing.JScrollPane();
        employeesTable = new javax.swing.JTable();
        searchTxt = new javax.swing.JTextField();
        exportFilesBtn = new javax.swing.JButton();
        printBtn = new javax.swing.JButton();
        refreshEmployeesBtn = new javax.swing.JButton();
        importEmplBtn = new javax.swing.JButton();
        numOfEmployeesLabel = new javax.swing.JLabel();
        suppliers = new javax.swing.JPanel();
        noSuppliesLb = new javax.swing.JLabel();
        suppliersDetailsPanel1 = new javax.swing.JPanel();
        expSupDetailsBtn1 = new javax.swing.JButton();
        printSupDetailsBtn1 = new javax.swing.JButton();
        importSupplierBtn = new javax.swing.JButton();
        searchSupplierTxt = new javax.swing.JTextField();
        refreshSuplTableBtn = new javax.swing.JButton();
        supplierScrollPanel = new javax.swing.JScrollPane();
        supplierDetailsTable = new javax.swing.JTable();
        suppliesTab = new javax.swing.JTabbedPane();
        suppliesTabPanel = new javax.swing.JPanel();
        searchSuppliesTxt = new javax.swing.JTextField();
        exportSuppliesFilesBtn = new javax.swing.JButton();
        printSuppliesBtn = new javax.swing.JButton();
        refreshSuppliesTableBtn = new javax.swing.JButton();
        suppliesTabPane = new javax.swing.JScrollPane();
        suppliesTable = new javax.swing.JTable();
        purchasesHistoryPanel = new javax.swing.JPanel();
        exportPurchFilesBtn = new javax.swing.JButton();
        printPurchasesBtn = new javax.swing.JButton();
        searchPurchasesTxt = new javax.swing.JTextField();
        refreshPurchasesTableBtn = new javax.swing.JButton();
        importPurchaseBtn = new javax.swing.JButton();
        purchHistoryScrollPanel = new javax.swing.JScrollPane();
        purchHistoryTable = new javax.swing.JTable();
        supplierGraphPanel = new javax.swing.JPanel();
        suppliesGraphPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        file = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        view = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        options = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        tools = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        jMenu11 = new javax.swing.JMenu();
        jMenu12 = new javax.swing.JMenu();
        help = new javax.swing.JMenu();
        helpContentMenu = new javax.swing.JMenu();
        aboutMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabPanel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        statistics.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Statistics", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        javax.swing.GroupLayout statisticsLayout = new javax.swing.GroupLayout(statistics);
        statistics.setLayout(statisticsLayout);
        statisticsLayout.setHorizontalGroup(
            statisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 302, Short.MAX_VALUE)
        );
        statisticsLayout.setVerticalGroup(
            statisticsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        dialyProduction.setBorder(javax.swing.BorderFactory.createTitledBorder("Daily Production"));

        dailyProductionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        productionScrollPane.setViewportView(dailyProductionTable);

        moreProdDetailsBtn.setText("+ More Details");
        moreProdDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moreProdDetailsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dialyProductionLayout = new javax.swing.GroupLayout(dialyProduction);
        dialyProduction.setLayout(dialyProductionLayout);
        dialyProductionLayout.setHorizontalGroup(
            dialyProductionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialyProductionLayout.createSequentialGroup()
                .addGroup(dialyProductionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productionScrollPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialyProductionLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(moreProdDetailsBtn)))
                .addContainerGap())
        );
        dialyProductionLayout.setVerticalGroup(
            dialyProductionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialyProductionLayout.createSequentialGroup()
                .addComponent(moreProdDetailsBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(productionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        latestSales.setBorder(javax.swing.BorderFactory.createTitledBorder("Latest Sales"));

        dailySalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        salesScrollPanel.setViewportView(dailySalesTable);

        moreSalesDetailsBtn.setText("+ More Details");
        moreSalesDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moreSalesDetailsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout latestSalesLayout = new javax.swing.GroupLayout(latestSales);
        latestSales.setLayout(latestSalesLayout);
        latestSalesLayout.setHorizontalGroup(
            latestSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, latestSalesLayout.createSequentialGroup()
                .addGroup(latestSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(latestSalesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(salesScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
                    .addGroup(latestSalesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(moreSalesDetailsBtn)))
                .addContainerGap())
        );
        latestSalesLayout.setVerticalGroup(
            latestSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(latestSalesLayout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(moreSalesDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(salesScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        latestPurchases.setBorder(javax.swing.BorderFactory.createTitledBorder("Latest Purchases"));

        dailyPurchasesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        purchasesScrollPanel.setViewportView(dailyPurchasesTable);

        morePurchDetailsBtn.setText("+ More Details");
        morePurchDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                morePurchDetailsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout latestPurchasesLayout = new javax.swing.GroupLayout(latestPurchases);
        latestPurchases.setLayout(latestPurchasesLayout);
        latestPurchasesLayout.setHorizontalGroup(
            latestPurchasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(latestPurchasesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(latestPurchasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchasesScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, latestPurchasesLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(morePurchDetailsBtn)))
                .addContainerGap())
        );
        latestPurchasesLayout.setVerticalGroup(
            latestPurchasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(latestPurchasesLayout.createSequentialGroup()
                .addComponent(morePurchDetailsBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(purchasesScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statistics, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(latestPurchases, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(latestSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dialyProduction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        homeLayout.setVerticalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statistics, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(homeLayout.createSequentialGroup()
                        .addComponent(dialyProduction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(latestSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(latestPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(468, Short.MAX_VALUE))
        );

        tabPanel.addTab("Home", home);

        invenrotyTabPanel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        productsDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Products Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        productsDetailsPanel.setToolTipText("");

        importProductsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/create employee.png"))); // NOI18N
        importProductsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importProductsButtonActionPerformed(evt);
            }
        });

        searchProducts.setText("Search....");
        searchProducts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchProductsActionPerformed(evt);
            }
        });

        refreshProductsTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshProductsTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshProductsTableActionPerformed(evt);
            }
        });

        productsDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        productsScrollPanel.setViewportView(productsDetailsTable);

        javax.swing.GroupLayout productsDetailsPanelLayout = new javax.swing.GroupLayout(productsDetailsPanel);
        productsDetailsPanel.setLayout(productsDetailsPanelLayout);
        productsDetailsPanelLayout.setHorizontalGroup(
            productsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, productsDetailsPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(searchProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshProductsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(importProductsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(productsScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1057, Short.MAX_VALUE)
        );
        productsDetailsPanelLayout.setVerticalGroup(
            productsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productsDetailsPanelLayout.createSequentialGroup()
                .addGroup(productsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(refreshProductsTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchProducts)
                    .addComponent(importProductsButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(productsScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(363, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout productsTabLayout = new javax.swing.GroupLayout(productsTab);
        productsTab.setLayout(productsTabLayout);
        productsTabLayout.setHorizontalGroup(
            productsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productsTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productsDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        productsTabLayout.setVerticalGroup(
            productsTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productsTabLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(productsDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(74, 74, 74))
        );

        invenrotyTabPanel.addTab("Products", productsTab);

        suppliesDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Supplies Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        suppliesDetailsPanel.setToolTipText("");

        importSuppliesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/create employee.png"))); // NOI18N
        importSuppliesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importSuppliesButtonActionPerformed(evt);
            }
        });

        searchSupplies.setText("Search....");
        searchSupplies.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchSuppliesActionPerformed(evt);
            }
        });

        refreshSuppliesTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshSuppliesTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshSuppliesTableActionPerformed(evt);
            }
        });

        suppliesDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        suppliesScrollPanel.setViewportView(suppliesDetailsTable);

        javax.swing.GroupLayout suppliesDetailsPanelLayout = new javax.swing.GroupLayout(suppliesDetailsPanel);
        suppliesDetailsPanel.setLayout(suppliesDetailsPanelLayout);
        suppliesDetailsPanelLayout.setHorizontalGroup(
            suppliesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suppliesDetailsPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(searchSupplies, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshSuppliesTable, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(importSuppliesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(suppliesScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1057, Short.MAX_VALUE)
        );
        suppliesDetailsPanelLayout.setVerticalGroup(
            suppliesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliesDetailsPanelLayout.createSequentialGroup()
                .addGroup(suppliesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(refreshSuppliesTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchSupplies)
                    .addComponent(importSuppliesButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(suppliesScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 606, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout suppliesPanelLayout = new javax.swing.GroupLayout(suppliesPanel);
        suppliesPanel.setLayout(suppliesPanelLayout);
        suppliesPanelLayout.setHorizontalGroup(
            suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(suppliesDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        suppliesPanelLayout.setVerticalGroup(
            suppliesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(suppliesDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(112, 112, 112))
        );

        javax.swing.GroupLayout suppliesInvTabsLayout = new javax.swing.GroupLayout(suppliesInvTabs);
        suppliesInvTabs.setLayout(suppliesInvTabsLayout);
        suppliesInvTabsLayout.setHorizontalGroup(
            suppliesInvTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliesInvTabsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(suppliesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        suppliesInvTabsLayout.setVerticalGroup(
            suppliesInvTabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliesInvTabsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(suppliesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        invenrotyTabPanel.addTab("Supplies", suppliesInvTabs);

        javax.swing.GroupLayout inventoryLayout = new javax.swing.GroupLayout(inventory);
        inventory.setLayout(inventoryLayout);
        inventoryLayout.setHorizontalGroup(
            inventoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(invenrotyTabPanel)
        );
        inventoryLayout.setVerticalGroup(
            inventoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(invenrotyTabPanel)
        );

        tabPanel.addTab("Inventory", inventory);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Add Production"));

        productNameTextField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productNameTextField.setText("Product Name:");

        productNameTextField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productNameTextField1.setText("Bottle Sizes:");

        productComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        productNameTextField2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productNameTextField2.setText("Production Quantity:");

        bottleSizeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        productNameTextField3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productNameTextField3.setText("Bottle Quantity:");

        addBottlesQuantityButton.setText("Add Bottles Quantity");
        addBottlesQuantityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBottlesQuantityButtonActionPerformed(evt);
            }
        });

        productNameTextField4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productNameTextField4.setText("Remaining Quantity:");

        productNameTextField5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productNameTextField5.setText("example - 500 litres");

        addBottlesQuantity.setText("Finish");
        addBottlesQuantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBottlesQuantityActionPerformed(evt);
            }
        });

        dateOfProduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateOfProductionActionPerformed(evt);
            }
        });

        hireDateLabel.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        hireDateLabel.setText("Production Date:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(productNameTextField))
                            .addComponent(hireDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(productComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateOfProduction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(productNameTextField2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(productionQuantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(72, 72, 72)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(productNameTextField1)
                            .addComponent(productNameTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bottleQuantitySpinner)
                            .addComponent(bottleSizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(addBottlesQuantityButton)))
                .addGap(297, 297, 297))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(productNameTextField4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(addBottlesQuantity))
                    .addComponent(productNameTextField5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productNameTextField3)
                    .addComponent(bottleQuantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hireDateLabel)
                    .addComponent(dateOfProduction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productNameTextField1)
                    .addComponent(bottleSizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productNameTextField)
                    .addComponent(productComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(productNameTextField2)
                            .addComponent(productionQuantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(addBottlesQuantityButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productNameTextField4)
                    .addComponent(productNameTextField5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addBottlesQuantity)
                .addContainerGap())
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Supplies for each Production"));

        productionDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        productionScrollPanel.setViewportView(productionDetailsTable);

        searchProductionDate.setText("Search....");
        searchProductionDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchProductionDateActionPerformed(evt);
            }
        });

        refreshProductionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshProductionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshProductionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productionScrollPanel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(refreshProductionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)))
                .addContainerGap())
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                    .addGap(730, 730, 730)
                    .addComponent(searchProductionDate, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(77, Short.MAX_VALUE)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(refreshProductionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productionScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addGap(96, 96, 96))
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel9Layout.createSequentialGroup()
                    .addGap(6, 6, 6)
                    .addComponent(searchProductionDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(348, Short.MAX_VALUE)))
        );

        refreshSuplTableBtn2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshSuplTableBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshSuplTableBtn2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout productionLayout = new javax.swing.GroupLayout(production);
        production.setLayout(productionLayout);
        productionLayout.setHorizontalGroup(
            productionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(161, Short.MAX_VALUE))
            .addGroup(productionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(productionLayout.createSequentialGroup()
                    .addGap(532, 532, 532)
                    .addComponent(refreshSuplTableBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(533, Short.MAX_VALUE)))
        );
        productionLayout.setVerticalGroup(
            productionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(productionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(productionLayout.createSequentialGroup()
                    .addGap(352, 352, 352)
                    .addComponent(refreshSuplTableBtn2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(352, 352, 352)))
        );

        tabPanel.addTab("Production", production);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Items Daily Sales"));

        salesDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        saleScrollPanel.setViewportView(salesDetailsTable);

        searchSale.setText("Search....");
        searchSale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchSaleActionPerformed(evt);
            }
        });

        refreshSalesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshSalesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshSalesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(564, Short.MAX_VALUE)
                .addComponent(searchSale, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(refreshSalesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(saleScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(refreshSalesButton, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(searchSale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 236, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                    .addContainerGap(37, Short.MAX_VALUE)
                    .addComponent(saleScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item - Customer 1", "Item - Customer 2" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Customers Daily Sales"));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 173, Short.MAX_VALUE)
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Add Sale"));

        productNameTextField6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productNameTextField6.setText("Customer:");

        productNameTextField7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productNameTextField7.setText("Product Size:");

        customerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        customerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerComboBoxActionPerformed(evt);
            }
        });

        productNameTextField8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productNameTextField8.setText("Product Quantity:");

        productSizeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addSaleButton.setText("Add Sale");
        addSaleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSaleButtonActionPerformed(evt);
            }
        });

        productNameTextField9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        productNameTextField9.setText("Product :");

        productsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        productsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productsComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(productNameTextField6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(customerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(productNameTextField9)
                            .addComponent(productNameTextField7)
                            .addComponent(productNameTextField8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(productsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(productSizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(quantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(addSaleButton))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(23, 23, 23))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productNameTextField6)
                    .addComponent(customerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productNameTextField9)
                    .addComponent(productsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(productSizeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(productNameTextField7))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(productNameTextField8)
                    .addComponent(quantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(addSaleButton)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(14, 14, 14))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(530, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Daily Sales", jPanel2);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Items Weekly Sales"));

        weeklySalesDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        weeklySalesScrollPanel.setViewportView(weeklySalesDetailsTable);

        searchWeeklySale.setText("Search....");
        searchWeeklySale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchWeeklySaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(943, Short.MAX_VALUE)
                .addComponent(searchWeeklySale, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel11Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(weeklySalesScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(searchWeeklySale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 254, Short.MAX_VALUE))
            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                    .addContainerGap(44, Short.MAX_VALUE)
                    .addComponent(weeklySalesScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(732, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Weekly Sales", jPanel4);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Items Monthly Sales"));

        monthlySalesDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        monthlySalesScrollPanel.setViewportView(monthlySalesDetailsTable);

        searceMonthlySale.setText("Search....");
        searceMonthlySale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searceMonthlySaleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(943, Short.MAX_VALUE)
                .addComponent(searceMonthlySale, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel12Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(monthlySalesScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(searceMonthlySale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 254, Short.MAX_VALUE))
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                    .addContainerGap(44, Short.MAX_VALUE)
                    .addComponent(monthlySalesScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(732, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Monthly Sales", jPanel3);

        javax.swing.GroupLayout salesLayout = new javax.swing.GroupLayout(sales);
        sales.setLayout(salesLayout);
        salesLayout.setHorizontalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        salesLayout.setVerticalGroup(
            salesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tabPanel.addTab("Sales", sales);

        noCustomerLb.setText("No. Customers: ");

        customerDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        expCusDetailsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/export.png"))); // NOI18N
        expCusDetailsBtn.setPreferredSize(new java.awt.Dimension(49, 25));
        expCusDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expCusDetailsBtnActionPerformed(evt);
            }
        });

        printCusDetailsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N

        importCustomerBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/create employee.png"))); // NOI18N
        importCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importCustomerBtnActionPerformed(evt);
            }
        });

        searchCustomerTxt.setText("Search....");
        searchCustomerTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCustomerTxtActionPerformed(evt);
            }
        });

        refreshCusDetailsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshCusDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshCusDetailsBtnActionPerformed(evt);
            }
        });

        customerDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        customerScrollPanel.setViewportView(customerDetailsTable);

        javax.swing.GroupLayout customerDetailsPanelLayout = new javax.swing.GroupLayout(customerDetailsPanel);
        customerDetailsPanel.setLayout(customerDetailsPanelLayout);
        customerDetailsPanelLayout.setHorizontalGroup(
            customerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerDetailsPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(expCusDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printCusDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchCustomerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshCusDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(importCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(customerDetailsPanelLayout.createSequentialGroup()
                .addComponent(customerScrollPanel)
                .addContainerGap())
        );
        customerDetailsPanelLayout.setVerticalGroup(
            customerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerDetailsPanelLayout.createSequentialGroup()
                .addGroup(customerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printCusDetailsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(expCusDetailsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refreshCusDetailsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchCustomerTxt)
                    .addComponent(importCustomerBtn, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(customerScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addContainerGap())
        );

        customerTabPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        customerTabPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        customerTabPanel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        importCustSalesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/create employee.png"))); // NOI18N

        searchCustSalesTxt.setText("Search....");
        searchCustSalesTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCustSalesTxtActionPerformed(evt);
            }
        });

        exportCustSalesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/export.png"))); // NOI18N
        exportCustSalesBtn.setPreferredSize(new java.awt.Dimension(49, 25));
        exportCustSalesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportCustSalesBtnActionPerformed(evt);
            }
        });

        printCustSalesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N

        refreshCustSalesTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N

        custSalesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        customersSalesTabPane.setViewportView(custSalesTable);

        javax.swing.GroupLayout custSalesPanelLayout = new javax.swing.GroupLayout(custSalesPanel);
        custSalesPanel.setLayout(custSalesPanelLayout);
        custSalesPanelLayout.setHorizontalGroup(
            custSalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(custSalesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(custSalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, custSalesPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(exportCustSalesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(printCustSalesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchCustSalesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshCustSalesTable, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(importCustSalesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(customersSalesTabPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE))
                .addContainerGap())
        );
        custSalesPanelLayout.setVerticalGroup(
            custSalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(custSalesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(custSalesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printCustSalesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exportCustSalesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refreshCustSalesTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchCustSalesTxt)
                    .addComponent(importCustSalesBtn, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(customersSalesTabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        customerTabPanel.addTab("Customer Sales", custSalesPanel);

        exportCustProductsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/export.png"))); // NOI18N
        exportCustProductsBtn.setPreferredSize(new java.awt.Dimension(49, 25));
        exportCustProductsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportCustProductsBtnActionPerformed(evt);
            }
        });

        printCustProductsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N

        searchCustProductsTxt.setText("Search....");
        searchCustProductsTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCustProductsTxtActionPerformed(evt);
            }
        });

        refreshCustProductsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshCustProductsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshCustProductsBtnActionPerformed(evt);
            }
        });

        customerProductsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        custProScrollPanel.setViewportView(customerProductsTable);

        javax.swing.GroupLayout custProductsPanelLayout = new javax.swing.GroupLayout(custProductsPanel);
        custProductsPanel.setLayout(custProductsPanelLayout);
        custProductsPanelLayout.setHorizontalGroup(
            custProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(custProductsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(custProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(custProScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, custProductsPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(exportCustProductsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(printCustProductsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchCustProductsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshCustProductsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        custProductsPanelLayout.setVerticalGroup(
            custProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(custProductsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(custProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printCustProductsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exportCustProductsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refreshCustProductsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchCustProductsTxt))
                .addGap(43, 43, 43)
                .addComponent(custProScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        customerTabPanel.addTab("Customer Prooducts", custProductsPanel);

        customersGraphsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customers Sales %", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        javax.swing.GroupLayout customersGraphsPanelLayout = new javax.swing.GroupLayout(customersGraphsPanel);
        customersGraphsPanel.setLayout(customersGraphsPanelLayout);
        customersGraphsPanelLayout.setHorizontalGroup(
            customersGraphsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );
        customersGraphsPanelLayout.setVerticalGroup(
            customersGraphsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        customersGraphsPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customer Sales per month", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        javax.swing.GroupLayout customersGraphsPanel2Layout = new javax.swing.GroupLayout(customersGraphsPanel2);
        customersGraphsPanel2.setLayout(customersGraphsPanel2Layout);
        customersGraphsPanel2Layout.setHorizontalGroup(
            customersGraphsPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        customersGraphsPanel2Layout.setVerticalGroup(
            customersGraphsPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        numOfCustomersLabel.setName("numOfCustomersLabel"); // NOI18N

        javax.swing.GroupLayout customersLayout = new javax.swing.GroupLayout(customers);
        customers.setLayout(customersLayout);
        customersLayout.setHorizontalGroup(
            customersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customersLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(customersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(customersLayout.createSequentialGroup()
                        .addComponent(noCustomerLb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numOfCustomersLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(customersLayout.createSequentialGroup()
                        .addGroup(customersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(customerDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(customerTabPanel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(customersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customersGraphsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(customersGraphsPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        customersLayout.setVerticalGroup(
            customersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noCustomerLb)
                    .addComponent(numOfCustomersLabel))
                .addGap(32, 32, 32)
                .addGroup(customersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(customersGraphsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(customerDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(customersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(customersLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(customerTabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(customersLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(customersGraphsPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(470, Short.MAX_VALUE))
        );

        tabPanel.addTab("Customers", customers);

        noEmployeesLb.setText("No. Employees: ");

        employeeListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "List Of Employess", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        employeesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        employeeScrollPanel.setViewportView(employeesTable);

        searchTxt.setText("Search....");
        searchTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTxtActionPerformed(evt);
            }
        });

        exportFilesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/export.png"))); // NOI18N
        exportFilesBtn.setPreferredSize(new java.awt.Dimension(49, 25));
        exportFilesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportFilesBtnActionPerformed(evt);
            }
        });

        printBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N

        refreshEmployeesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshEmployeesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshEmployeesBtnActionPerformed(evt);
            }
        });

        importEmplBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/create employee.png"))); // NOI18N
        importEmplBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importEmplBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout employeeListPanelLayout = new javax.swing.GroupLayout(employeeListPanel);
        employeeListPanel.setLayout(employeeListPanelLayout);
        employeeListPanelLayout.setHorizontalGroup(
            employeeListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(employeeListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employeeScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1042, Short.MAX_VALUE)
                    .addGroup(employeeListPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(exportFilesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshEmployeesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(importEmplBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        employeeListPanelLayout.setVerticalGroup(
            employeeListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employeeListPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(employeeListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exportFilesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refreshEmployeesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchTxt)
                    .addComponent(importEmplBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(employeeScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(292, Short.MAX_VALUE))
        );

        numOfEmployeesLabel.setName("numOfEmployeesLabel"); // NOI18N

        javax.swing.GroupLayout employeesLayout = new javax.swing.GroupLayout(employees);
        employees.setLayout(employeesLayout);
        employeesLayout.setHorizontalGroup(
            employeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(employeeListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(employeesLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(noEmployeesLb)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numOfEmployeesLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        employeesLayout.setVerticalGroup(
            employeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(employeesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(noEmployeesLb)
                    .addComponent(numOfEmployeesLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(employeeListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabPanel.addTab("Employees", employees);

        noSuppliesLb.setText("No. Suppliers: ###");

        suppliersDetailsPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Supplier Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        expSupDetailsBtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/export.png"))); // NOI18N
        expSupDetailsBtn1.setPreferredSize(new java.awt.Dimension(49, 25));
        expSupDetailsBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expSupDetailsBtn1ActionPerformed(evt);
            }
        });

        printSupDetailsBtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N

        importSupplierBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/create employee.png"))); // NOI18N
        importSupplierBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importSupplierBtnActionPerformed(evt);
            }
        });

        searchSupplierTxt.setText("Search....");
        searchSupplierTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchSupplierTxtActionPerformed(evt);
            }
        });

        refreshSuplTableBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshSuplTableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshSuplTableBtnActionPerformed(evt);
            }
        });

        supplierDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        supplierScrollPanel.setViewportView(supplierDetailsTable);

        javax.swing.GroupLayout suppliersDetailsPanel1Layout = new javax.swing.GroupLayout(suppliersDetailsPanel1);
        suppliersDetailsPanel1.setLayout(suppliersDetailsPanel1Layout);
        suppliersDetailsPanel1Layout.setHorizontalGroup(
            suppliersDetailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suppliersDetailsPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(expSupDetailsBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printSupDetailsBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchSupplierTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(refreshSuplTableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(importSupplierBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(suppliersDetailsPanel1Layout.createSequentialGroup()
                .addComponent(supplierScrollPanel)
                .addContainerGap())
        );
        suppliersDetailsPanel1Layout.setVerticalGroup(
            suppliersDetailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliersDetailsPanel1Layout.createSequentialGroup()
                .addGroup(suppliersDetailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printSupDetailsBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(expSupDetailsBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refreshSuplTableBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchSupplierTxt)
                    .addComponent(importSupplierBtn, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addComponent(supplierScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addContainerGap())
        );

        suppliesTab.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        suppliesTab.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        suppliesTab.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        searchSuppliesTxt.setText("Search....");
        searchSuppliesTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchSuppliesTxtActionPerformed(evt);
            }
        });

        exportSuppliesFilesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/export.png"))); // NOI18N
        exportSuppliesFilesBtn.setPreferredSize(new java.awt.Dimension(49, 25));
        exportSuppliesFilesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportSuppliesFilesBtnActionPerformed(evt);
            }
        });

        printSuppliesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N

        refreshSuppliesTableBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshSuppliesTableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshSuppliesTableBtnActionPerformed(evt);
            }
        });

        suppliesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        suppliesTabPane.setViewportView(suppliesTable);

        javax.swing.GroupLayout suppliesTabPanelLayout = new javax.swing.GroupLayout(suppliesTabPanel);
        suppliesTabPanel.setLayout(suppliesTabPanelLayout);
        suppliesTabPanelLayout.setHorizontalGroup(
            suppliesTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliesTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(suppliesTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(suppliesTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suppliesTabPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(exportSuppliesFilesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(printSuppliesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchSuppliesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshSuppliesTableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        suppliesTabPanelLayout.setVerticalGroup(
            suppliesTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliesTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(suppliesTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printSuppliesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exportSuppliesFilesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refreshSuppliesTableBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchSuppliesTxt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(suppliesTabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        suppliesTab.addTab("Supplies", suppliesTabPanel);

        exportPurchFilesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/export.png"))); // NOI18N
        exportPurchFilesBtn.setPreferredSize(new java.awt.Dimension(49, 25));
        exportPurchFilesBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPurchFilesBtnActionPerformed(evt);
            }
        });

        printPurchasesBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N

        searchPurchasesTxt.setText("Search....");
        searchPurchasesTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchPurchasesTxtActionPerformed(evt);
            }
        });

        refreshPurchasesTableBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshPurchasesTableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshPurchasesTableBtnActionPerformed(evt);
            }
        });

        importPurchaseBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/create employee.png"))); // NOI18N

        purchHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        purchHistoryScrollPanel.setViewportView(purchHistoryTable);

        javax.swing.GroupLayout purchasesHistoryPanelLayout = new javax.swing.GroupLayout(purchasesHistoryPanel);
        purchasesHistoryPanel.setLayout(purchasesHistoryPanelLayout);
        purchasesHistoryPanelLayout.setHorizontalGroup(
            purchasesHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchasesHistoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(purchasesHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, purchasesHistoryPanelLayout.createSequentialGroup()
                        .addGap(0, 475, Short.MAX_VALUE)
                        .addComponent(exportPurchFilesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(printPurchasesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchPurchasesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshPurchasesTableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(importPurchaseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(purchHistoryScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE))
                .addContainerGap())
        );
        purchasesHistoryPanelLayout.setVerticalGroup(
            purchasesHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(purchasesHistoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(purchasesHistoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printPurchasesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exportPurchFilesBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(refreshPurchasesTableBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchPurchasesTxt)
                    .addComponent(importPurchaseBtn, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(43, 43, 43)
                .addComponent(purchHistoryScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        suppliesTab.addTab("Purchases Records", purchasesHistoryPanel);

        supplierGraphPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Supplier purchases %", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        javax.swing.GroupLayout supplierGraphPanelLayout = new javax.swing.GroupLayout(supplierGraphPanel);
        supplierGraphPanel.setLayout(supplierGraphPanelLayout);
        supplierGraphPanelLayout.setHorizontalGroup(
            supplierGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );
        supplierGraphPanelLayout.setVerticalGroup(
            supplierGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        suppliesGraphPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Supplies purchases  per month", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        javax.swing.GroupLayout suppliesGraphPanelLayout = new javax.swing.GroupLayout(suppliesGraphPanel);
        suppliesGraphPanel.setLayout(suppliesGraphPanelLayout);
        suppliesGraphPanelLayout.setHorizontalGroup(
            suppliesGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        suppliesGraphPanelLayout.setVerticalGroup(
            suppliesGraphPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout suppliersLayout = new javax.swing.GroupLayout(suppliers);
        suppliers.setLayout(suppliersLayout);
        suppliersLayout.setHorizontalGroup(
            suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliersLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(suppliersLayout.createSequentialGroup()
                        .addComponent(noSuppliesLb)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(suppliersLayout.createSequentialGroup()
                        .addGroup(suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(suppliersDetailsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(suppliesTab))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supplierGraphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(suppliesGraphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        suppliersLayout.setVerticalGroup(
            suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noSuppliesLb)
                .addGap(32, 32, 32)
                .addGroup(suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(supplierGraphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(suppliersDetailsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(suppliersLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(suppliesTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(suppliersLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(suppliesGraphPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(470, Short.MAX_VALUE))
        );

        tabPanel.addTab("Suppliers", suppliers);

        file.setText("File");

        jMenu1.setText("jMenu1");
        file.add(jMenu1);

        jMenu2.setText("jMenu1");
        file.add(jMenu2);

        jMenu3.setText("jMenu1");
        file.add(jMenu3);

        menuBar.add(file);

        view.setText("View");

        jMenu4.setText("jMenu1");
        view.add(jMenu4);

        jMenu5.setText("jMenu1");
        view.add(jMenu5);

        jMenu6.setText("jMenu1");
        view.add(jMenu6);

        menuBar.add(view);

        options.setText("Options");

        jMenu7.setText("jMenu1");
        options.add(jMenu7);

        jMenu8.setText("jMenu1");
        options.add(jMenu8);

        jMenu9.setText("jMenu1");
        options.add(jMenu9);

        menuBar.add(options);

        tools.setText("Tools");

        jMenu10.setText("jMenu1");
        tools.add(jMenu10);

        jMenu11.setText("jMenu1");
        tools.add(jMenu11);

        jMenu12.setText("jMenu1");
        tools.add(jMenu12);

        menuBar.add(tools);

        help.setText("Help");

        helpContentMenu.setText("jMenu1");
        help.add(helpContentMenu);

        aboutMenu.setText("jMenu1");
        help.add(aboutMenu);

        menuBar.add(help);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPanel)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void moreProdDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moreProdDetailsBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_moreProdDetailsBtnActionPerformed

    private void moreSalesDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moreSalesDetailsBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_moreSalesDetailsBtnActionPerformed

    private void morePurchDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_morePurchDetailsBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_morePurchDetailsBtnActionPerformed

    private void searchTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTxtActionPerformed

    private void exportFilesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportFilesBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportFilesBtnActionPerformed

    private void expCusDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expCusDetailsBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_expCusDetailsBtnActionPerformed

    private void searchCustomerTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustomerTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchCustomerTxtActionPerformed

    private void exportCustSalesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportCustSalesBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportCustSalesBtnActionPerformed

    private void searchCustSalesTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustSalesTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchCustSalesTxtActionPerformed

    private void exportCustProductsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportCustProductsBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportCustProductsBtnActionPerformed

    private void searchCustProductsTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustProductsTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchCustProductsTxtActionPerformed

    private void expSupDetailsBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expSupDetailsBtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_expSupDetailsBtn1ActionPerformed

    private void searchSupplierTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSupplierTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSupplierTxtActionPerformed

    private void searchSuppliesTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSuppliesTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSuppliesTxtActionPerformed

    private void exportSuppliesFilesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportSuppliesFilesBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportSuppliesFilesBtnActionPerformed

    private void exportPurchFilesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPurchFilesBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportPurchFilesBtnActionPerformed

    private void searchPurchasesTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPurchasesTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchPurchasesTxtActionPerformed

    private void importSupplierBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importSupplierBtnActionPerformed
        this.setVisible(true);
        addSuppliersForm = new AddSuppliersForm();
    }//GEN-LAST:event_importSupplierBtnActionPerformed

    private void addBottlesQuantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBottlesQuantityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addBottlesQuantityActionPerformed

    private void importCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importCustomerBtnActionPerformed
        setVisible(true);
        customersForm = new CustomersForm();
    }//GEN-LAST:event_importCustomerBtnActionPerformed

    private void importEmplBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importEmplBtnActionPerformed
        setVisible(true);
        addUsersForm = new AddUsersForm();
    }//GEN-LAST:event_importEmplBtnActionPerformed

    private void importProductsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importProductsButtonActionPerformed
        this.setVisible(true);
        addProductsForm = new AddProductsForm();
    }//GEN-LAST:event_importProductsButtonActionPerformed

    private void searchProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchProductsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchProductsActionPerformed

    private void importSuppliesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importSuppliesButtonActionPerformed
        this.setVisible(true);
        addSuppliesForm = new AddSuppliesForm();
    }//GEN-LAST:event_importSuppliesButtonActionPerformed

    private void searchSuppliesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSuppliesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSuppliesActionPerformed

    private void dateOfProductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateOfProductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateOfProductionActionPerformed

    private void addBottlesQuantityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBottlesQuantityButtonActionPerformed
        addProduction();
    }//GEN-LAST:event_addBottlesQuantityButtonActionPerformed

    private void searchProductionDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchProductionDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchProductionDateActionPerformed

    private void addSaleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSaleButtonActionPerformed
        addSales();
    }//GEN-LAST:event_addSaleButtonActionPerformed

    private void customerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerComboBoxActionPerformed

    private void productsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productsComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productsComboBoxActionPerformed

    private void searchSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSaleActionPerformed

    private void searchWeeklySaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchWeeklySaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchWeeklySaleActionPerformed

    private void searceMonthlySaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searceMonthlySaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searceMonthlySaleActionPerformed

    private void refreshEmployeesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshEmployeesBtnActionPerformed
        getEmployees();
    }//GEN-LAST:event_refreshEmployeesBtnActionPerformed

    private void refreshCustProductsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshCustProductsBtnActionPerformed
        getCustomerProducts();
    }//GEN-LAST:event_refreshCustProductsBtnActionPerformed

    private void refreshCusDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshCusDetailsBtnActionPerformed
        getCustomers();
    }//GEN-LAST:event_refreshCusDetailsBtnActionPerformed

    private void refreshSuplTableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshSuplTableBtnActionPerformed
        getSuppliers();
    }//GEN-LAST:event_refreshSuplTableBtnActionPerformed

    private void refreshSuppliesTableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshSuppliesTableBtnActionPerformed
        getSupplierSupplies();
    }//GEN-LAST:event_refreshSuppliesTableBtnActionPerformed

    private void refreshProductsTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshProductsTableActionPerformed
        getProducts();
    }//GEN-LAST:event_refreshProductsTableActionPerformed

    private void refreshSuppliesTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshSuppliesTableActionPerformed
        getSupplies();
    }//GEN-LAST:event_refreshSuppliesTableActionPerformed

    private void refreshSuplTableBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshSuplTableBtn2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refreshSuplTableBtn2ActionPerformed

    private void refreshProductionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshProductionButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refreshProductionButtonActionPerformed

    private void refreshSalesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshSalesButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refreshSalesButtonActionPerformed

    private void refreshPurchasesTableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshPurchasesTableBtnActionPerformed
        suppliersTab();
    }//GEN-LAST:event_refreshPurchasesTableBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JButton addBottlesQuantity;
    private javax.swing.JButton addBottlesQuantityButton;
    private javax.swing.JButton addSaleButton;
    private javax.swing.JSpinner bottleQuantitySpinner;
    private javax.swing.JComboBox<String> bottleSizeComboBox;
    private javax.swing.JScrollPane custProScrollPanel;
    private javax.swing.JPanel custProductsPanel;
    private javax.swing.JPanel custSalesPanel;
    private javax.swing.JTable custSalesTable;
    private javax.swing.JComboBox<String> customerComboBox;
    private javax.swing.JPanel customerDetailsPanel;
    private javax.swing.JTable customerDetailsTable;
    private javax.swing.JTable customerProductsTable;
    private javax.swing.JScrollPane customerScrollPanel;
    private javax.swing.JTabbedPane customerTabPanel;
    private javax.swing.JPanel customers;
    private javax.swing.JPanel customersGraphsPanel;
    private javax.swing.JPanel customersGraphsPanel2;
    private javax.swing.JScrollPane customersSalesTabPane;
    private javax.swing.JTable dailyProductionTable;
    private javax.swing.JTable dailyPurchasesTable;
    private javax.swing.JTable dailySalesTable;
    private org.jdesktop.swingx.JXDatePicker dateOfProduction;
    private javax.swing.JPanel dialyProduction;
    private javax.swing.JPanel employeeListPanel;
    private javax.swing.JScrollPane employeeScrollPanel;
    private javax.swing.JPanel employees;
    private javax.swing.JTable employeesTable;
    private javax.swing.JButton expCusDetailsBtn;
    private javax.swing.JButton expSupDetailsBtn1;
    private javax.swing.JButton exportCustProductsBtn;
    private javax.swing.JButton exportCustSalesBtn;
    private javax.swing.JButton exportFilesBtn;
    private javax.swing.JButton exportPurchFilesBtn;
    private javax.swing.JButton exportSuppliesFilesBtn;
    private javax.swing.JMenu file;
    private javax.swing.JMenu help;
    private javax.swing.JMenu helpContentMenu;
    private javax.swing.JLabel hireDateLabel;
    private javax.swing.JPanel home;
    private javax.swing.JButton importCustSalesBtn;
    private javax.swing.JButton importCustomerBtn;
    private javax.swing.JButton importEmplBtn;
    private javax.swing.JButton importProductsButton;
    private javax.swing.JButton importPurchaseBtn;
    private javax.swing.JButton importSupplierBtn;
    private javax.swing.JButton importSuppliesButton;
    private javax.swing.JTabbedPane invenrotyTabPanel;
    private javax.swing.JPanel inventory;
    private javax.swing.JList<String> jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel latestPurchases;
    private javax.swing.JPanel latestSales;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JTable monthlySalesDetailsTable;
    private javax.swing.JScrollPane monthlySalesScrollPanel;
    private javax.swing.JButton moreProdDetailsBtn;
    private javax.swing.JButton morePurchDetailsBtn;
    private javax.swing.JButton moreSalesDetailsBtn;
    private javax.swing.JLabel noCustomerLb;
    private javax.swing.JLabel noEmployeesLb;
    private javax.swing.JLabel noSuppliesLb;
    private javax.swing.JLabel numOfCustomersLabel;
    private javax.swing.JLabel numOfEmployeesLabel;
    private javax.swing.JMenu options;
    private javax.swing.JButton printBtn;
    private javax.swing.JButton printCusDetailsBtn;
    private javax.swing.JButton printCustProductsBtn;
    private javax.swing.JButton printCustSalesBtn;
    private javax.swing.JButton printPurchasesBtn;
    private javax.swing.JButton printSupDetailsBtn1;
    private javax.swing.JButton printSuppliesBtn;
    private javax.swing.JComboBox<String> productComboBox;
    private javax.swing.JLabel productNameTextField;
    private javax.swing.JLabel productNameTextField1;
    private javax.swing.JLabel productNameTextField2;
    private javax.swing.JLabel productNameTextField3;
    private javax.swing.JLabel productNameTextField4;
    private javax.swing.JLabel productNameTextField5;
    private javax.swing.JLabel productNameTextField6;
    private javax.swing.JLabel productNameTextField7;
    private javax.swing.JLabel productNameTextField8;
    private javax.swing.JLabel productNameTextField9;
    private javax.swing.JComboBox<String> productSizeComboBox;
    private javax.swing.JPanel production;
    private javax.swing.JTable productionDetailsTable;
    private javax.swing.JSpinner productionQuantitySpinner;
    private javax.swing.JScrollPane productionScrollPane;
    private javax.swing.JScrollPane productionScrollPanel;
    private javax.swing.JComboBox<String> productsComboBox;
    private javax.swing.JPanel productsDetailsPanel;
    private javax.swing.JTable productsDetailsTable;
    private javax.swing.JScrollPane productsScrollPanel;
    private javax.swing.JPanel productsTab;
    private javax.swing.JScrollPane purchHistoryScrollPanel;
    private javax.swing.JTable purchHistoryTable;
    private javax.swing.JPanel purchasesHistoryPanel;
    private javax.swing.JScrollPane purchasesScrollPanel;
    private javax.swing.JSpinner quantitySpinner;
    private javax.swing.JButton refreshCusDetailsBtn;
    private javax.swing.JButton refreshCustProductsBtn;
    private javax.swing.JButton refreshCustSalesTable;
    private javax.swing.JButton refreshEmployeesBtn;
    private javax.swing.JButton refreshProductionButton;
    private javax.swing.JButton refreshProductsTable;
    private javax.swing.JButton refreshPurchasesTableBtn;
    private javax.swing.JButton refreshSalesButton;
    private javax.swing.JButton refreshSuplTableBtn;
    private javax.swing.JButton refreshSuplTableBtn2;
    private javax.swing.JButton refreshSuppliesTable;
    private javax.swing.JButton refreshSuppliesTableBtn;
    private javax.swing.JScrollPane saleScrollPanel;
    private javax.swing.JPanel sales;
    private javax.swing.JTable salesDetailsTable;
    private javax.swing.JScrollPane salesScrollPanel;
    private javax.swing.JTextField searceMonthlySale;
    private javax.swing.JTextField searchCustProductsTxt;
    private javax.swing.JTextField searchCustSalesTxt;
    private javax.swing.JTextField searchCustomerTxt;
    private javax.swing.JTextField searchProductionDate;
    private javax.swing.JTextField searchProducts;
    private javax.swing.JTextField searchPurchasesTxt;
    private javax.swing.JTextField searchSale;
    private javax.swing.JTextField searchSupplierTxt;
    private javax.swing.JTextField searchSupplies;
    private javax.swing.JTextField searchSuppliesTxt;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JTextField searchWeeklySale;
    private javax.swing.JPanel statistics;
    private javax.swing.JTable supplierDetailsTable;
    private javax.swing.JPanel supplierGraphPanel;
    private javax.swing.JScrollPane supplierScrollPanel;
    private javax.swing.JPanel suppliers;
    private javax.swing.JPanel suppliersDetailsPanel1;
    private javax.swing.JPanel suppliesDetailsPanel;
    private javax.swing.JTable suppliesDetailsTable;
    private javax.swing.JPanel suppliesGraphPanel;
    private javax.swing.JPanel suppliesInvTabs;
    private javax.swing.JPanel suppliesPanel;
    private javax.swing.JScrollPane suppliesScrollPanel;
    private javax.swing.JTabbedPane suppliesTab;
    private javax.swing.JScrollPane suppliesTabPane;
    private javax.swing.JPanel suppliesTabPanel;
    private javax.swing.JTable suppliesTable;
    private javax.swing.JTabbedPane tabPanel;
    private javax.swing.JMenu tools;
    private javax.swing.JMenu view;
    private javax.swing.JTable weeklySalesDetailsTable;
    private javax.swing.JScrollPane weeklySalesScrollPanel;
    // End of variables declaration//GEN-END:variables
}
