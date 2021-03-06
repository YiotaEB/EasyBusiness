/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eb_managementapp.UI.Forms;

import Utilities.HTTPConnection;
import com.panickapps.pdfboxutils.PDFBoxUtils;
import java.io.File;
import static eb_managementapp.EB_ManagementApp.addUsersForm;
import static eb_managementapp.EB_ManagementApp.customersForm;
import static eb_managementapp.EB_ManagementApp.addSuppliersForm;
import static eb_managementapp.EB_ManagementApp.addSuppliesForm;
import static eb_managementapp.EB_ManagementApp.addProductsForm;
import static eb_managementapp.EB_ManagementApp.adminForm;
import eb_managementapp.Entities.Countries;
import eb_managementapp.Entities.Customerproducts;
import eb_managementapp.Entities.Customers;
import eb_managementapp.Entities.MyDate;
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
import eb_managementapp.UI.Components.CustomTableModel;
import eb_managementapp.UI.Forms.AddProductsForm;
import eb_managementapp.UI.Forms.AddSuppliersForm;
import eb_managementapp.UI.Forms.AddSuppliesForm;
import eb_managementapp.UI.Forms.CustomersForm;
import eb_managementapp.UI.Forms.AddUsersForm;
import eb_managementapp.UI.Forms.AdminForm;
import eb_managementapp.UI.Forms.SetUpForm;
import org.json.*;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.RowFilter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public final class MainForm extends javax.swing.JFrame {

    final String TITLE = "Easy Business ";

    private String sessionID;

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

    private String companyName;
    private int companyCountryID;
    private String companyTelephone;
    private String companyAddress;
    private String companyCity;

    private TableRowSorter<DefaultTableModel> sorter;
    private TableRowSorter<DefaultTableModel> supplierSorter;
    private TableRowSorter<DefaultTableModel> supplyTransactionsSorter;
    private TableRowSorter<DefaultTableModel> supplierSuppliesSorter;
    private TableRowSorter<DefaultTableModel> customersSorter;
    private TableRowSorter<DefaultTableModel> customerProductsSorter;
    private TableRowSorter<DefaultTableModel> salesSorter;
    private TableRowSorter<DefaultTableModel> productionSorter;
    private TableRowSorter<DefaultTableModel> productsSorter;
    private TableRowSorter<DefaultTableModel> suppliesSorter;

    private void employeesFilter(TableRowSorter<DefaultTableModel> sorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(searchTxt.getText(), 2);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void suppliersFilter(TableRowSorter<DefaultTableModel> sorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(searchSupplierTxt.getText(), 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void supplierSuppliesFilter(TableRowSorter<DefaultTableModel> sorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(searchSuppliesTxt.getText(), 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void supplyTransactionFilter(TableRowSorter<DefaultTableModel> sorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(searchPurchasesTxt.getText(), 2);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void customersFilter(TableRowSorter<DefaultTableModel> sorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(searchCustomerTxt.getText(), 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void customerProductsFilter(TableRowSorter<DefaultTableModel> sorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(searchCustProductsTxt.getText(), 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void salesFilter(TableRowSorter<DefaultTableModel> sorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(searchSale.getText(), 2);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void productionFilter(TableRowSorter<DefaultTableModel> sorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(searchProduction.getText(), 2);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void productsFilter(TableRowSorter<DefaultTableModel> sorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(searchProducts.getText(), 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    private void suppliesFilter(TableRowSorter<DefaultTableModel> sorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(searchSupplies.getText(), 1);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }

    public static String readSetting(String filename) {
        String currentLine;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((currentLine = br.readLine()) != null) {
                return currentLine;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    private String monthToString(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "Febrary";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "Error";

        }
    }

    public MainForm() {
        initComponents();

        sessionID = readSetting(LoginForm.SESSION_FILENAME);
        if (sessionID == null) {
            new LoginForm();
            this.setVisible(false);
        }

        ImageIcon imageIcon = new ImageIcon("C:\\Users\\panay\\Desktop\\EasyBusiness\\EB_ManagementApp\\src\\eb_managementapp\\UI\\Images\\mini_logo.fw.png");
        setIconImage(imageIcon.getImage());

        //Employeess Search
        searchTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                apply();
            }

            public void apply() {
                employeesTab();

            }

        });

        //Supplier Search
        searchSupplierTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                apply();
            }

            public void apply() {
                suppliersTab();

            }

        });

        //Supplier Supplies Search
        searchSuppliesTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                apply();
            }

            public void apply() {
                suppliersTab();

            }

        });

        //Supplier Supplies Search
        searchPurchasesTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                apply();
            }

            public void apply() {

                suppliersTab();

            }

        });

        //Customer Search
        searchCustomerTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                apply();
            }

            public void apply() {
                customersTab();

            }

        });

        //Customer Products Search
        searchCustProductsTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                apply();
            }

            public void apply() {
                customersTab();

            }

        });

        //Sales Search
        searchSale.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                apply();
            }

            public void apply() {
                salesTab();

            }

        });

        //Production Search
        searchProduction.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                apply();
            }

            public void apply() {
                productionTab();

            }

        });

        //Products Search
        searchProducts.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                apply();
            }

            public void apply() {
                productsTab();

            }

        });

        //Supplies Search
        searchSupplies.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                apply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                apply();
            }

            public void apply() {
                suppliesTab();

            }

        });

        tabPanel.addChangeListener(
                new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e
            ) {
                switch (tabPanel.getSelectedIndex()) {

                    //Home
                    case 0:
                        getProductSize();
                        getProducts();
                        getProduction();
                        getCustomers();
                        getSaleProducts();
                        getSales();
                        getSupplies();
                        getSupplyTransactions();
                        getSuppliers();
                        getSupplierSupplies();
                        homeTab();
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
                        getProducts();
                        getCustomerProducts();
                        customersTab();

                        //CHARTS:
                        HashMap<String, Double> salesByCustomer = new HashMap();

                        for (int i = 0; i < salesList.size(); i++) {

                            String customerNameG = "";
                            int customerID = salesList.get(i).getCustomerID();

                            for (int j = 0; j < customersList.size(); j++) {
                                if (customerID == customersList.get(j).getID()) {
                                    customerNameG = customersList.get(j).getName();
                                    break;
                                }
                            }

                            double totalForCustomer = 0.0;
                            for (int j = 0; j < saleProductsList.size(); j++) {
                                double totalForSalesProduct = 0.0;
                                int quantity = saleProductsList.get(j).getQuantitySold();
                                if (saleProductsList.get(j).getSaleID() == salesList.get(i).getID()) {
                                    double price = 0.0;
                                    for (int k = 0; k < productsList.size(); k++) {
                                        if (saleProductsList.get(j).getProductID() == productsList.get(k).getID()) {
                                            price = productsList.get(k).getPrice();
                                            break;
                                        }
                                    }
                                    totalForSalesProduct = quantity * price;
                                }
                                totalForCustomer += totalForSalesProduct;
                            }

                            String customerNameFound = null;
                            for (Map.Entry<String, Double> entry : salesByCustomer.entrySet()) {
                                String customerName = entry.getKey();
                                if (customerNameG.equals(customerName)) {
                                    customerNameFound = customerName;
                                    break;
                                }
                            }

                            //If customer already exists in map:
                            if (customerNameFound != null) {
                                double total = salesByCustomer.get(customerNameFound);
                                total += totalForCustomer;
                                salesByCustomer.put(customerNameFound, total);
                            } //If date does not already exist in map:
                            else {
                                salesByCustomer.put(customerNameG, totalForCustomer);
                            }

                        }

                        DefaultPieDataset dataset = new DefaultPieDataset();

                        for (Map.Entry<String, Double> entry : salesByCustomer.entrySet()) {
                            dataset.setValue(entry.getKey(), entry.getValue());
                        }

                        JFreeChart pieChartObject = ChartFactory.createPieChart("Sales by customer", dataset);

                        pieChartObject.getPlot().setBackgroundPaint(Color.decode("#DDDDDD"));

                        ChartPanel chartPanel = new ChartPanel(pieChartObject);
                        chartPanel.setPreferredSize(new java.awt.Dimension(300, 300));

                        customersGraphsPanel.setLayout(new java.awt.BorderLayout());
                        customersGraphsPanel.add(chartPanel);

                        customersGraphsPanel.validate();

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
                        getSupplierSupplies();
                        suppliersTab();
                        break;

                }
            }
        }
        );

        customerScrollPanel.setViewportView(customerDetailsTable);
        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        this.setVisible(true);
        getProductSize();
        getProducts();
        getProduction();
        getCustomers();
        getCountries();
        getSaleProducts();
        getSales();

        //CHARTS:
        HashMap<MyDate, Double> salesByDay = new HashMap();

        for (int i = 0; i < salesList.size(); i++) {
            long timestamp = salesList.get(i).getSaleTimeDate();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp);
            MyDate myDate = new MyDate();
            myDate.day = cal.get(Calendar.DAY_OF_MONTH);
            myDate.month = cal.get(Calendar.MONTH) + 1;
            myDate.year = cal.get(Calendar.YEAR);

            double totalForSale = 0.0;
            for (int j = 0; j < saleProductsList.size(); j++) {
                double totalForSalesProduct = 0.0;
                int quantity = saleProductsList.get(j).getQuantitySold();
                if (saleProductsList.get(j).getSaleID() == salesList.get(i).getID()) {
                    double price = 0.0;
                    for (int k = 0; k < productsList.size(); k++) {
                        if (saleProductsList.get(j).getProductID() == productsList.get(k).getID()) {
                            price = productsList.get(k).getPrice();
                            break;
                        }
                    }
                    totalForSalesProduct = quantity * price;
                }
                totalForSale += totalForSalesProduct;
            }

            MyDate dateFound = null;
            for (Map.Entry<MyDate, Double> entry : salesByDay.entrySet()) {
                MyDate e = entry.getKey();
                if (e.day == myDate.day && e.month == myDate.month && e.year == myDate.year) {
                    dateFound = e;
                    break;
                }
            }

            //If date already exists in map:
            if (dateFound != null) {
                double total = salesByDay.get(dateFound);
                total += totalForSale;
                salesByDay.put(dateFound, total);
            } //If date does not already exist in map:
            else {
                salesByDay.put(myDate, totalForSale);
            }

        }

        final String salesTag = "Sales";
        DefaultCategoryDataset salesChartset = new DefaultCategoryDataset();

        SortedSet<MyDate> keys = new TreeSet<>(salesByDay.keySet());
        for (MyDate key : keys) {
            salesChartset.addValue(salesByDay.get(key), salesTag, monthToString(key.month));
        }

        JFreeChart lineChartObject = ChartFactory.createBarChart(
                "Monthly Sales", "Days",
                "No. Sales",
                salesChartset, PlotOrientation.VERTICAL,
                true, true, false);

        lineChartObject.getPlot().setBackgroundPaint(Color.decode("#DDDDDD"));

        ChartPanel chartPanel = new ChartPanel(lineChartObject);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));

        statistics.setLayout(new java.awt.BorderLayout());
        statistics.add(chartPanel);

        statistics.validate();

        setTitle(TITLE);

        /******************************************************************************/
        //CHARTS:
        HashMap<String, Double> salesByCustomer = new HashMap();

        for (int i = 0; i < salesList.size(); i++) {

            String customerNameG = "";
            int customerID = salesList.get(i).getCustomerID();

            for (int j = 0; j < customersList.size(); j++) {
                if (customerID == customersList.get(j).getID()) {
                    customerNameG = customersList.get(j).getName();
                    break;
                }
            }

            double totalForCustomer = 0.0;
            for (int j = 0; j < saleProductsList.size(); j++) {
                double totalForSalesProduct = 0.0;
                int quantity = saleProductsList.get(j).getQuantitySold();
                if (saleProductsList.get(j).getSaleID() == salesList.get(i).getID()) {
                    double price = 0.0;
                    for (int k = 0; k < productsList.size(); k++) {
                        if (saleProductsList.get(j).getProductID() == productsList.get(k).getID()) {
                            price = productsList.get(k).getPrice();
                            break;
                        }
                    }
                    totalForSalesProduct = quantity * price;
                }
                totalForCustomer += totalForSalesProduct;
            }

            String customerNameFound = null;
            for (Map.Entry<String, Double> entry : salesByCustomer.entrySet()) {
                String customerName = entry.getKey();
                if (customerNameG.equals(customerName)) {
                    customerNameFound = customerName;
                    break;
                }
            }

            //If customer already exists in map:
            if (customerNameFound != null) {
                double total = salesByCustomer.get(customerNameFound);
                total += totalForCustomer;
                salesByCustomer.put(customerNameFound, total);
            } //If date does not already exist in map:
            else {
                salesByCustomer.put(customerNameG, totalForCustomer);
            }

        }

        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Map.Entry<String, Double> entry : salesByCustomer.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart pieChartObject = ChartFactory.createPieChart("Sales by customer", dataset);

        pieChartObject.getPlot().setBackgroundPaint(Color.decode("#DDDDDD"));

        ChartPanel chartPane2 = new ChartPanel(pieChartObject);
        chartPane2.setPreferredSize(new java.awt.Dimension(300, 300));

        customersGraphsPanel1.setLayout(new java.awt.BorderLayout());
        customersGraphsPanel1.add(chartPane2);

        customersGraphsPanel1.validate();

        getSupplies();
        getSupplyTransactions();
        getSuppliers();
        getSupplierSupplies();
        getCompanyInformation();
        homeTab();

    }

    private void homeTab() {

        //DAILY PRODUCTION
        //Create a new model for the table:
        DefaultTableModel productsTableModel = new DefaultTableModel();

        //Add the table columns:
        productsTableModel.addColumn("No");
        productsTableModel.addColumn("Production Date");
        productsTableModel.addColumn("Product Name");
        productsTableModel.addColumn("Quantity Produced");

        ArrayList<Productionbatches> lastProduction = new ArrayList();
        lastProduction.addAll(productionList);
        lastProduction.sort(new Comparator<Productionbatches>() {
            @Override
            public int compare(Productionbatches o1, Productionbatches o2) {
                return o2.getID() - o1.getID(); 
            }

        });

        int limit = 0;
        if (lastProduction.size() < 5) {
            limit = lastProduction.size();
        } else {
            limit = 5;
        }

        ArrayList<Productionbatches> lastProductionTop5 = new ArrayList();
        for (int i = 0; i < limit; i++) {
            lastProductionTop5.add(lastProduction.get(i));
        }

        //Add each item in the list as a row in the table:
        for (int i = 0; i < lastProductionTop5.size(); i++) {

            //Put productType Name in the Table
            String productName = "";
            for (int j = 0; j < productsList.size(); j++) {
                if (productsList.get(j).getID() == lastProductionTop5.get(i).getProductID()) {
                    productName = productsList.get(j).getName();
                    break;
                }
            }
            Timestamp timestamp = new Timestamp(lastProductionTop5.get(i).getProductionDate());
            Date date = new Date(timestamp.getTime());

            Object[] currentRow = {
                i + 1,
                Users.DATE_FORMAT.format(date),
                productName,
                lastProductionTop5.get(i).getQuantityProduced()

            };
            productsTableModel.addRow(currentRow);
        }
        dailyProductionTable.setModel(productsTableModel);

        //Create a new model for the table:
        DefaultTableModel salesTableModel = new DefaultTableModel();

        //Add the table columns:
        salesTableModel.addColumn("No");
        salesTableModel.addColumn("Date");
        salesTableModel.addColumn("Customer");
        salesTableModel.addColumn("Total Revenue");

        ArrayList<Sales> lastSales = new ArrayList();
        lastSales.addAll(salesList);
        lastSales.sort(new Comparator<Sales>() {
            @Override
            public int compare(Sales o1, Sales o2) {
                return o2.getID() - o1.getID(); 
            }

        });

        int limitSales = 0;
        if (lastSales.size() < 5) {
            limitSales = lastSales.size();
        } else {
            limitSales = 5;
        }

        ArrayList<Sales> lastSalesTop5 = new ArrayList();
        for (int i = 0; i < limitSales; i++) {
            lastSalesTop5.add(lastSales.get(i));
        }

        //Add each item in the list as a row in the table:
        for (int i = 0; i < lastSalesTop5.size(); i++) {

            //Total
            double total = 0.0;

            for (int j = 0; j < saleProductsList.size(); j++) {
                if (salesList.get(i).getID() == saleProductsList.get(j).getSaleID()) {
                    int quantity = saleProductsList.get(j).getQuantitySold();
                    double price = 0.0;
                    for (int k = 0; k < productsList.size(); k++) {
                        if (saleProductsList.get(j).getProductID() == productsList.get(k).getID()) {
                            price = productsList.get(k).getPrice();
                            break;
                        }
                    }
                    total += quantity * price;
                }

            }

            //Put customerName in the Table
            String customerName = "";
            for (int j = 0; j < customersList.size(); j++) {
                if (customersList.get(j).getID() == lastSalesTop5.get(i).getCustomerID()) {
                    customerName = customersList.get(j).getName();
                }
            }

            Timestamp timestamp = new Timestamp(lastSalesTop5.get(i).getSaleTimeDate());
            Date date = new Date(timestamp.getTime());

            Object[] currentRow = {
                i + 1,
                Users.DATE_FORMAT.format(date),
                customerName,
                "€ " + String.format("%.2f", total)
            };

            salesTableModel.addRow(currentRow);
        }
        dailySalesTable.setModel(salesTableModel);

        /***************************************************************************************/
        //SUPPLIER PURCHASES RECORDS TABLE
        //Create a new model for the table:
        DefaultTableModel supplyPurchasesTableModel = new DefaultTableModel();

        //Add the table columns:
        supplyPurchasesTableModel.addColumn("No");
        supplyPurchasesTableModel.addColumn("Date");
        supplyPurchasesTableModel.addColumn("Supplier");
        supplyPurchasesTableModel.addColumn("Total");

        ArrayList<Supplytransactions> lastSupplyTransactions = new ArrayList();
        lastSupplyTransactions.addAll(supplyTransactionList);
        lastSupplyTransactions.sort(new Comparator<Supplytransactions>() {
            @Override
            public int compare(Supplytransactions o1, Supplytransactions o2) {
                return o2.getID() - o1.getID(); 
            }

        });

        int limitSupplyProducction = 0;
        if (lastSupplyTransactions.size() < 5) {
            limitSupplyProducction = lastSupplyTransactions.size();
        } else {
            limitSales = 5;
        }

        ArrayList<Supplytransactions> lastSupplyProducctionTop5 = new ArrayList();
        for (int i = 0; i < limitSupplyProducction; i++) {
            lastSupplyProducctionTop5.add(lastSupplyTransactions.get(i));
        }

        //Add each item in the list as a row in the table:
        for (int i = 0; i < lastSupplyProducctionTop5.size(); i++) {

            double total = 0.0;
            for (int j = 0; j < lastSupplyProducctionTop5.size(); j++) {
                int sold = lastSupplyProducctionTop5.get(j).getQuantity();
                double price = 0.0;
                for (int k = 0; k < suppliesList.size(); k++) {
                    if (suppliesList.get(k).getID() == lastSupplyProducctionTop5.get(j).getSupplierSuppliesID()) {
                        price = suppliesList.get(k).getPrice();
                        break;
                    }

                }
                total += (price * sold);
            }

            //Put supplier Name in the Table
            String supplierName = "";
            for (int j = 0; j < suppliersList.size(); j++) {
                if (suppliersList.get(j).getID() == lastSupplyProducctionTop5.get(i).getSupplierSuppliesID()) {
                    supplierName = suppliersList.get(j).getName();
                }
            }
            Timestamp timestamp = new Timestamp(lastSupplyProducctionTop5.get(i).getDateMade());
            Date date = new Date(timestamp.getTime());

            Object[] currentRow = {
                i + 1,
                Users.DATE_FORMAT.format(date),
                supplierName,
                "€ " + String.format("%.2f", total)
            };
            supplyPurchasesTableModel.addRow(currentRow);
        }
        dailyPurchasesTable.setModel(supplyPurchasesTableModel);

    }

    private void addProduction() {

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

        Date productionDate = dateOfProduction.getDate();
        long date = 0;

        if (productionDate != null) {
            date = productionDate.getTime();
        } else {
            showMessageDialog(null, "Please provide a valid date", "Invalid Date", JOptionPane.PLAIN_MESSAGE);
        }

        quantityInStock += Integer.parseInt(bottleQuantitySpinner.getValue().toString());

        //Make the call:
        String addProductionJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Productionbatches", "Create",
                "SessionID=" + sessionID + "&ID=1&QuantityProduced=" + productionQuantity + "&ProductID=" + productID + "&ProductionDate=" + date
        );

        String updateProductsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Products", "Update",
                "SessionID=" + sessionID + "&ID=" + productID + "&Name=" + productName + "&Price=" + price + "&QuantityInStock="
                + quantityInStock + "&ProductTypeID=" + productTypeID + "&ProductSizeID=" + productSizeID + "&ProductSuppliesID=" + productSuppliesID);

        try {
            JSONObject jsonObject = new JSONObject(addProductionJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                //Reset fields:
                setVisible(true);
                productComboBox.setSelectedIndex(0);
                bottleSizeComboBox.setSelectedIndex(0);
                bottleQuantitySpinner.setValue(0);
                productionQuantitySpinner.setValue(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCustomers() {
        customersList = new ArrayList<>();

        //Get customers from api
        String customersJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Customers", "GetMultiple", "SessionID=" + sessionID);
        try {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < customersList.size(); i++) {
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
        //FILTERING:
        customersSorter = new TableRowSorter<DefaultTableModel>(customersTableModel);
        customersFilter(customersSorter);
        customerDetailsTable.setRowSorter(customersSorter);

        customerDetailsTable.setModel(customersTableModel);
        refreshCusDetailsBtn.setEnabled(true);
        numOfCustomersLabel.setText(String.valueOf(customersList.size()));
        /***********************************************************************************************/

        //CUSTOMER PRODUCTS TABLE
        refreshCustProductsBtn.setEnabled(false);
        //Create a new model for the table:
        DefaultTableModel customerProductsTableModel = new DefaultTableModel();

        //Add the table columns:
        customerProductsTableModel.addColumn("No");
        customerProductsTableModel.addColumn("Customer");
        customerProductsTableModel.addColumn("Product");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < customerProductsList.size(); i++) {

            //Put supplier Name in the Table
            String customerName = "";
            for (int j = 0; j < customersList.size(); j++) {
                if (customersList.get(j).getID() == customerProductsList.get(i).getCustomerID()) {
                    customerName = customersList.get(j).getName();
                }
            }

            //Put supplier Name in the Table
            String product = "";
            for (int j = 0; j < productsList.size(); j++) {
                if (productsList.get(j).getID() == customerProductsList.get(i).getProductID()) {
                    product = productsList.get(j).getName();
                }
            }

            Object[] currentRow = {
                i + 1,
                customerName,
                product
            };
            customerProductsTableModel.addRow(currentRow);
        }
        //FILTERING:
        customerProductsSorter = new TableRowSorter<DefaultTableModel>(customerProductsTableModel);
        customerProductsFilter(customerProductsSorter);
        customerProductsTable.setRowSorter(customerProductsSorter);

        customerProductsTable.setModel(customerProductsTableModel);
        refreshCustProductsBtn.setEnabled(true);
    }

    public void employeesTab() {
        refreshEmployeesBtn.setEnabled(false);

        //Create a new model for the table:
        CustomTableModel employeesTableModel = new CustomTableModel();

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
            Timestamp timestamp = new Timestamp(employeesList.get(i).getDateHired());
            Date date = new Date(timestamp.getTime());

            Object[] currentRow = {
                i + 1,
                employeesList.get(i).getUsername(),
                employeesList.get(i).getFirstname(),
                employeesList.get(i).getLastname(),
                position,
                Users.DATE_FORMAT.format(date),
                employeesList.get(i).getTelephone(),
                employeesList.get(i).getAddress(),
                employeesList.get(i).getCity(),
                countryName
            };
            employeesTableModel.addRow(currentRow);
        }

        //FILTERING:
        sorter = new TableRowSorter<DefaultTableModel>(employeesTableModel);
        employeesFilter(sorter);
        employeesTable.setRowSorter(sorter);

        employeesTable.setModel(employeesTableModel);
        refreshEmployeesBtn.setEnabled(true);
        numOfEmployeesLabel.setText(String.valueOf(employeesList.size()));

    }

    public void getCustomerProducts() {
        customerProductsList = new ArrayList<>();
        refreshCustProductsBtn.setEnabled(false);
        
        //Get customers from api
        String customerProductsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Customerproducts", "GetMultiple", "SessionID=" + sessionID);
        try {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                i + 1,
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
        String suppliersJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Suppliers", "GetMultiple", "SessionID=" + sessionID + "");
        try {
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
        //FILTERING:
        supplierSorter = new TableRowSorter<DefaultTableModel>(supplierTableModel);
        suppliersFilter(supplierSorter);
        supplierDetailsTable.setRowSorter(supplierSorter);

        supplierDetailsTable.setModel(supplierTableModel);
        refreshSuplTableBtn.setEnabled(true);
        noSuppliesLb.setText(String.valueOf(suppliersList.size()));

        //SUPPLIER SUPPLIES TABLE
        refreshSuppliesTableBtn.setEnabled(false);
        //Create a new model for the table:
        DefaultTableModel supplyTableModel = new DefaultTableModel();

        //Add the table columns:
        supplyTableModel.addColumn("No");
        supplyTableModel.addColumn("Supplier");
        supplyTableModel.addColumn("Supply");

        //Add each item in the list as a row in the table:
        for (int i = 0; i < supplierSuppliesList.size(); i++) {

            //Put supplier Name in the Table
            String supplierName = "";
            for (int j = 0; j < suppliersList.size(); j++) {
                if (suppliersList.get(j).getID() == supplierSuppliesList.get(i).getSupplierID()) {
                    supplierName = suppliersList.get(j).getName();
                }
            }

            //Put supplier Name in the Table
            String supplyName = "";
            for (int j = 0; j < suppliesList.size(); j++) {
                if (suppliesList.get(j).getID() == supplierSuppliesList.get(i).getSupplyID()) {
                    supplyName = suppliesList.get(j).getName();
                }
            }

            Object[] currentRow = {
                i + 1,
                supplierName,
                supplyName
            };
            supplyTableModel.addRow(currentRow);
        }
        //FILTERING:
        supplierSuppliesSorter = new TableRowSorter<DefaultTableModel>(supplyTableModel);
        supplierSuppliesFilter(supplierSuppliesSorter);
        suppliesTable.setRowSorter(supplierSuppliesSorter);

        suppliesTable.setModel(supplyTableModel);
        refreshSuppliesTableBtn.setEnabled(true);

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
                double price = 0.0;
                for (int k = 0; k < suppliesList.size(); k++) {
                    if (suppliesList.get(k).getID() == supplyTransactionList.get(j).getSupplierSuppliesID()) {
                        price = suppliesList.get(k).getPrice();
                        break;
                    }

                }
                total += (price * sold);
            }

            //Put supplier Name in the Table
            String supplierName = "";
            for (int j = 0; j < suppliersList.size(); j++) {
                if (suppliersList.get(j).getID() == supplyTransactionList.get(i).getSupplierSuppliesID()) {
                    supplierName = suppliersList.get(j).getName();
                }
            }

            Timestamp timestamp = new Timestamp(supplyTransactionList.get(i).getDateMade());
            Date date = new Date(timestamp.getTime());

            Object[] currentRow = {
                i + 1,
                Users.DATE_FORMAT.format(date),
                supplierName,
                "€ " + String.format("%.2f", total)
            };
            supplyPurchasesTableModel.addRow(currentRow);
        }
        //FILTERING:
        supplyTransactionsSorter = new TableRowSorter<DefaultTableModel>(supplyPurchasesTableModel);
        supplyTransactionFilter(supplyTransactionsSorter);
        purchHistoryTable.setRowSorter(supplyTransactionsSorter);

        purchHistoryTable.setModel(supplyPurchasesTableModel);
        refreshPurchasesTableBtn.setEnabled(true);

    }

    public void getEmployees() {
        employeesList = new ArrayList<>();

        long dateHiredLong = 0;

        //Get customers from api
        String employeesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Users", "GetMultiple", "SessionID=" + sessionID + "");
        try {
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
                    dateHiredLong = currentItem.getLong("DateHired");

                    int countryID = currentItem.getInt("CountryID");
                    String city = currentItem.getString("City");
                    String telephone = currentItem.getString("Telephone");
                    String address = currentItem.getString("Address");
                    int userLevelID = currentItem.getInt("UserLevelID");

                    Users employees = new Users(userID, username, password, userLevelID, firstname, lastname, dateHiredLong, city, address, telephone, countryID);
                    employeesList.add(employees);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getSupplies() {
        suppliesList = new ArrayList<>();

        //Get customers from api
        String suppliersJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Supplies", "GetMultiple", "SessionID=" + sessionID + "");
        try {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getSupplyTransactions() {
        supplyTransactionList = new ArrayList<>();

        long dateMade = 0;

        //Get customers from api
        String supplyTransactionJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Supplytransactions", "GetMultiple", "SessionID=" + sessionID + "");
        try {
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
                    dateMade = currentItem.getLong("DateMade");

                    Supplytransactions c = new Supplytransactions(id, supplierSuppliesID, dateMade, quantity);
                    supplyTransactionList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void suppliesTab() {
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
        //FILTERING:
        suppliesSorter = new TableRowSorter<DefaultTableModel>(suppliesTableModel);
        suppliesFilter(suppliesSorter);
        suppliesDetailsTable.setRowSorter(suppliesSorter);

        suppliesDetailsTable.setModel(suppliesTableModel);
        refreshSuppliesTable.setEnabled(true);

    }

    public void getCountries() {
        countriesList = new ArrayList<>();

        //Get customers from api
        String countriesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Countries", "GetMultiple", "");
        try {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getProductType() {
        productTypesList = new ArrayList<>();

        //Get product types from api
        String productTypesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Producttypes", "GetMultiple", "SessionID=" + sessionID + "");
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getProductSize() {
        productSizesList = new ArrayList<>();

        bottleSizeComboBox.removeAllItems();

        //Get product Sizes from api
        String productSizesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Productsizes", "GetMultiple", "SessionID=" + sessionID + "");
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < productSizesList.size(); i++) {
            bottleSizeComboBox.addItem(productSizesList.get(i).getName());
        }
    }

    public void getProducts() {
        productsList = new ArrayList<>();

        productComboBox.removeAllItems();

        //Get customers from api
        String productsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Products", "GetMultiple", "SessionID=" + sessionID + "");
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < productsList.size(); i++) {
            productComboBox.addItem(productsList.get(i).getName());
        }

    }

    private void productsTab() {
        refreshProductsTable.setEnabled(false);

//        getProductSize();
//        getProductType();
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
                if (productSizesList.get(j).getID() == productsList.get(i).getProductSizeID()) {
                    productSizeName = productSizesList.get(j).getName();
                }
            }

            //Put productType Name in the Table
            String productTypeName = "";
            for (int j = 0; j < productTypesList.size(); j++) {
                if (productTypesList.get(j).getID() == productsList.get(i).getProductTypeID()) {
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
        //FILTERING:
        productsSorter = new TableRowSorter<DefaultTableModel>(productsTableModel);
        productsFilter(productsSorter);
        productsDetailsTable.setRowSorter(productsSorter);

        productsDetailsTable.setModel(productsTableModel);
        refreshProductsTable.setEnabled(true);
    }

    public void getProduction() {
        productionList = new ArrayList<>();

        long productionDate = 0;

        //Get customers from api
        String productsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Productionbatches", "GetMultiple", "SessionID=" + sessionID + "");
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
                    productionDate = currentItem.getLong("ProductionDate");

                    Productionbatches c = new Productionbatches(id, quantityProduced, productID, productionDate);
                    productionList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void productionTab() {
        refreshProductionButton.setEnabled(false);

        //Create a new model for the table:
        DefaultTableModel productionTableModel = new DefaultTableModel();

        //Add the table columns:
        productionTableModel.addColumn("No");
        productionTableModel.addColumn("Production Date");
        productionTableModel.addColumn("Product Name");
        productionTableModel.addColumn("Quantity Produced");

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

            Timestamp timestamp = new Timestamp(productionList.get(i).getProductionDate());
            Date date = new Date(timestamp.getTime());

            Object[] currentRow = {
                i + 1,
                Users.DATE_FORMAT.format(date),
                productName,
                productionList.get(i).getQuantityProduced()

            };
            productionTableModel.addRow(currentRow);
        }
        //FILTERING:
        productionSorter = new TableRowSorter<DefaultTableModel>(productionTableModel);
        productionFilter(productionSorter);
        productionDetailsTable.setRowSorter(productionSorter);

        productionDetailsTable.setModel(productionTableModel);
        refreshProductionButton.setEnabled(true);
    }

    public void getSaleProducts() {
        saleProductsList = new ArrayList<>();

        //Get customers from api
        String saleProductsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Saleproducts", "GetMultiple", "SessionID=" + sessionID + "");
        try {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getSales() {
        salesList = new ArrayList<>();

        long saleTimeDate = 0;

        //Get customers from api
        String salesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Sales", "GetMultiple", "SessionID=" + sessionID + "");
        try {
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
                    saleTimeDate = currentItem.getLong("SaleTimeDate");

                    Sales c = new Sales(id, customerID, saleProductsID, tax, saleTimeDate);
                    salesList.add(c);
                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getCompanyInformation() {

        //Get customers from api
        String companyInfoJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Companyinformation", "GetMultiple", "SessionID=" + sessionID + "&Limit=0");
        try {
            JSONObject jsonObject = new JSONObject(companyInfoJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                JSONArray dataArray = jsonObject.getJSONArray("Data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject currentItem = dataArray.getJSONObject(i);

                    companyName = currentItem.getString("CompanyName");
                    companyCountryID = currentItem.getInt("CountryID");
                    companyTelephone = currentItem.getString("Telephone");
                    companyAddress = currentItem.getString("Address");
                    companyCity = currentItem.getString("City");

                }
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void salesTab() {
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
                if (salesList.get(i).getID() == saleProductsList.get(j).getSaleID()) {
                    int quantity = saleProductsList.get(j).getQuantitySold();
                    double price = 0.0;
                    for (int k = 0; k < productsList.size(); k++) {
                        if (saleProductsList.get(j).getProductID() == productsList.get(k).getID()) {
                            price = productsList.get(k).getPrice();
                            break;
                        }
                    }
                    total += quantity * price;
                }
            }

            //Math.round(total);
            //Put customerName in the Table
            String customerName = "";
            for (int j = 0; j < customersList.size(); j++) {
                if (customersList.get(j).getID() == salesList.get(i).getCustomerID()) {
                    customerName = customersList.get(j).getName();
                }
            }

            Timestamp timestamp = new Timestamp(salesList.get(i).getSaleTimeDate());
            Date date = new Date(timestamp.getTime());

            Object[] currentRow = {
                i + 1,
                Users.DATE_FORMAT.format(date),
                customerName,
                "€ " + String.format("%.2f", total)
            };

            salesTableModel.addRow(currentRow);
        }
        //FILTERING:
        salesSorter = new TableRowSorter<DefaultTableModel>(salesTableModel);
        salesFilter(salesSorter);
        salesDetailsTable.setRowSorter(salesSorter);

        salesDetailsTable.setModel(salesTableModel);
        refreshSalesButton.setEnabled(true);
    }

    public void getSupplierSupplies() {
        supplierSuppliesList = new ArrayList<>();
        refreshSuppliesTableBtn.setEnabled(false);

        //Get customers from api
        String supplierSuppliesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Suppliersupplies", "GetMultiple", "SessionID=" + sessionID + "");
        try {
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
        String positionsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Userlevels", "GetMultiple", "SessionID=" + sessionID + "");
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteEmployee(int id) {
        if (id <= 0) {
            return;
        }

        String deleteUserJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Users", "Delete", "SessionID=" + sessionID + "&UserID=" + id);
        try {
            JSONObject jsonObject = new JSONObject(deleteUserJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSupplier(int id) {
        if (id <= 0) {
            return;
        }

        String deleteSupplierJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Suppliers", "Delete", "SessionID=" + sessionID + "&ID=" + id);
        try {
            JSONObject jsonObject = new JSONObject(deleteSupplierJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSupplierSupplies(int id) {
        if (id <= 0) {
            return;
        }

        String deleteSupplierSuppliesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Suppliersupplies", "Delete", "SessionID=" + sessionID + "&ID=" + id);
        try {
            JSONObject jsonObject = new JSONObject(deleteSupplierSuppliesJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSupplies(int id) {
        if (id <= 0) {
            return;
        }

        String deleteSuppliesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Supplies", "Delete", "SessionID=" + sessionID + "&ID=" + id);
        try {
            JSONObject jsonObject = new JSONObject(deleteSuppliesJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProducts(int id) {
        if (id <= 0) {
            return;
        }

        String deleteProductsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Products", "Delete", "SessionID=" + sessionID + "&ID=" + id);
        try {
            JSONObject jsonObject = new JSONObject(deleteProductsJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomers(int id) {
        if (id <= 0) {
            return;
        }

        String deleteCustomersJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Customers", "Delete", "SessionID=" + sessionID + "&ID=" + id);
        try {
            JSONObject jsonObject = new JSONObject(deleteCustomersJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCustomerProducts(int id) {
        if (id <= 0) {
            return;
        }

        String deleteCustomerProductsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Customerproducts", "Delete", "SessionID=" + sessionID + "&ID=" + id);
        try {
            JSONObject jsonObject = new JSONObject(deleteCustomerProductsJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProduction(int id) {
        if (id <= 0) {
            return;
        }

        String deleteProductionJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Productionbatches", "Delete", "SessionID=" + sessionID + "&ID=" + id);
        try {
            JSONObject jsonObject = new JSONObject(deleteProductionJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            } else {
                showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
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
        latestSales = new javax.swing.JPanel();
        salesScrollPanel = new javax.swing.JScrollPane();
        dailySalesTable = new javax.swing.JTable();
        latestPurchases = new javax.swing.JPanel();
        purchasesScrollPanel = new javax.swing.JScrollPane();
        dailyPurchasesTable = new javax.swing.JTable();
        customersGraphsPanel1 = new javax.swing.JPanel();
        inventory = new javax.swing.JPanel();
        invenrotyTabPanel = new javax.swing.JTabbedPane();
        productsTab = new javax.swing.JPanel();
        productsDetailsPanel = new javax.swing.JPanel();
        importProductsButton = new javax.swing.JButton();
        searchProducts = new javax.swing.JTextField();
        refreshProductsTable = new javax.swing.JButton();
        productsScrollPanel = new javax.swing.JScrollPane();
        productsDetailsTable = new javax.swing.JTable();
        deleteProductsButton = new javax.swing.JButton();
        productsSaveButton = new javax.swing.JButton();
        suppliesInvTabs = new javax.swing.JPanel();
        suppliesPanel = new javax.swing.JPanel();
        suppliesDetailsPanel = new javax.swing.JPanel();
        importSuppliesButton = new javax.swing.JButton();
        searchSupplies = new javax.swing.JTextField();
        refreshSuppliesTable = new javax.swing.JButton();
        suppliesScrollPanel = new javax.swing.JScrollPane();
        suppliesDetailsTable = new javax.swing.JTable();
        deleteSuppliesButton = new javax.swing.JButton();
        suppliesSaveButton = new javax.swing.JButton();
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
        dateOfProduction = new org.jdesktop.swingx.JXDatePicker();
        hireDateLabel = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        productionScrollPanel = new javax.swing.JScrollPane();
        productionDetailsTable = new javax.swing.JTable();
        searchProduction = new javax.swing.JTextField();
        refreshProductionButton = new javax.swing.JButton();
        deleteProductionButton = new javax.swing.JButton();
        sales = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        saleScrollPanel = new javax.swing.JScrollPane();
        salesDetailsTable = new javax.swing.JTable();
        searchSale = new javax.swing.JTextField();
        refreshSalesButton = new javax.swing.JButton();
        printSalesDetailsBtn = new javax.swing.JButton();
        customers = new javax.swing.JPanel();
        noCustomerLb = new javax.swing.JLabel();
        customerDetailsPanel = new javax.swing.JPanel();
        printCusDetailsBtn = new javax.swing.JButton();
        importCustomerBtn = new javax.swing.JButton();
        searchCustomerTxt = new javax.swing.JTextField();
        refreshCusDetailsBtn = new javax.swing.JButton();
        customerScrollPanel = new javax.swing.JScrollPane();
        customerDetailsTable = new javax.swing.JTable();
        deleteCustomerButton = new javax.swing.JButton();
        customersSaveButton = new javax.swing.JButton();
        customerTabPanel = new javax.swing.JTabbedPane();
        custProductsPanel = new javax.swing.JPanel();
        printCustProductsBtn = new javax.swing.JButton();
        searchCustProductsTxt = new javax.swing.JTextField();
        refreshCustProductsBtn = new javax.swing.JButton();
        custProScrollPanel = new javax.swing.JScrollPane();
        customerProductsTable = new javax.swing.JTable();
        deleteCustomerProductsButton = new javax.swing.JButton();
        customersGraphsPanel = new javax.swing.JPanel();
        numOfCustomersLabel = new javax.swing.JLabel();
        employees = new javax.swing.JPanel();
        noEmployeesLb = new javax.swing.JLabel();
        employeeListPanel = new javax.swing.JPanel();
        employeeScrollPanel = new javax.swing.JScrollPane();
        employeesTable = new javax.swing.JTable();
        searchTxt = new javax.swing.JTextField();
        printBtn = new javax.swing.JButton();
        refreshEmployeesBtn = new javax.swing.JButton();
        importEmplBtn = new javax.swing.JButton();
        deletRow = new javax.swing.JButton();
        employeesSaveButton = new javax.swing.JButton();
        numOfEmployeesLabel = new javax.swing.JLabel();
        suppliers = new javax.swing.JPanel();
        noSuppliesLb = new javax.swing.JLabel();
        suppliersDetailsPanel1 = new javax.swing.JPanel();
        printSupDetailsBtn1 = new javax.swing.JButton();
        importSupplierBtn = new javax.swing.JButton();
        searchSupplierTxt = new javax.swing.JTextField();
        refreshSuplTableBtn = new javax.swing.JButton();
        supplierScrollPanel = new javax.swing.JScrollPane();
        supplierDetailsTable = new javax.swing.JTable();
        deleteSupplierButton = new javax.swing.JButton();
        suppliersSaveButton = new javax.swing.JButton();
        suppliesTab = new javax.swing.JTabbedPane();
        suppliesTabPanel = new javax.swing.JPanel();
        searchSuppliesTxt = new javax.swing.JTextField();
        printSuppliesBtn = new javax.swing.JButton();
        refreshSuppliesTableBtn = new javax.swing.JButton();
        suppliesTabPane = new javax.swing.JScrollPane();
        suppliesTable = new javax.swing.JTable();
        deleteSupplyButton = new javax.swing.JButton();
        purchasesHistoryPanel = new javax.swing.JPanel();
        exportPurchFilesBtn = new javax.swing.JButton();
        printPurchasesBtn = new javax.swing.JButton();
        searchPurchasesTxt = new javax.swing.JTextField();
        refreshPurchasesTableBtn = new javax.swing.JButton();
        importPurchaseBtn = new javax.swing.JButton();
        purchHistoryScrollPanel = new javax.swing.JScrollPane();
        purchHistoryTable = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        Menu = new javax.swing.JMenu();
        Forms = new javax.swing.JMenu();
        AdminForm = new javax.swing.JMenuItem();
        SetUpForm2 = new javax.swing.JMenuItem();
        SetUpForm1 = new javax.swing.JMenuItem();
        SetUpForm3 = new javax.swing.JMenuItem();
        SetUpForm = new javax.swing.JMenuItem();
        SetUpForm4 = new javax.swing.JMenuItem();
        logoutMenuItem = new javax.swing.JMenuItem();

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
            .addGap(0, 243, Short.MAX_VALUE)
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

        javax.swing.GroupLayout dialyProductionLayout = new javax.swing.GroupLayout(dialyProduction);
        dialyProduction.setLayout(dialyProductionLayout);
        dialyProductionLayout.setHorizontalGroup(
            dialyProductionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dialyProductionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productionScrollPane)
                .addContainerGap())
        );
        dialyProductionLayout.setVerticalGroup(
            dialyProductionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialyProductionLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(productionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                .addGap(15, 15, 15))
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

        javax.swing.GroupLayout latestSalesLayout = new javax.swing.GroupLayout(latestSales);
        latestSales.setLayout(latestSalesLayout);
        latestSalesLayout.setHorizontalGroup(
            latestSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, latestSalesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(salesScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
                .addContainerGap())
        );
        latestSalesLayout.setVerticalGroup(
            latestSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(latestSalesLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(salesScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
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

        javax.swing.GroupLayout latestPurchasesLayout = new javax.swing.GroupLayout(latestPurchases);
        latestPurchases.setLayout(latestPurchasesLayout);
        latestPurchasesLayout.setHorizontalGroup(
            latestPurchasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(latestPurchasesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(purchasesScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
                .addContainerGap())
        );
        latestPurchasesLayout.setVerticalGroup(
            latestPurchasesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(latestPurchasesLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(purchasesScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        customersGraphsPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customers Sales %", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        javax.swing.GroupLayout customersGraphsPanel1Layout = new javax.swing.GroupLayout(customersGraphsPanel1);
        customersGraphsPanel1.setLayout(customersGraphsPanel1Layout);
        customersGraphsPanel1Layout.setHorizontalGroup(
            customersGraphsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        customersGraphsPanel1Layout.setVerticalGroup(
            customersGraphsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 211, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout homeLayout = new javax.swing.GroupLayout(home);
        home.setLayout(homeLayout);
        homeLayout.setHorizontalGroup(
            homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statistics, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customersGraphsPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(homeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homeLayout.createSequentialGroup()
                        .addComponent(statistics, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(customersGraphsPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(homeLayout.createSequentialGroup()
                        .addComponent(dialyProduction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(latestSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(latestPurchases, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(791, Short.MAX_VALUE))
        );

        tabPanel.addTab("Home", home);

        invenrotyTabPanel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        productsDetailsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Products Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N
        productsDetailsPanel.setToolTipText("");

        importProductsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/ic_add_black_36dp_1x.png"))); // NOI18N
        importProductsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importProductsButtonActionPerformed(evt);
            }
        });

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

        deleteProductsButton.setText("Delete Products");
        deleteProductsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProductsButtonActionPerformed(evt);
            }
        });

        productsSaveButton.setText("Save");
        productsSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productsSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout productsDetailsPanelLayout = new javax.swing.GroupLayout(productsDetailsPanel);
        productsDetailsPanel.setLayout(productsDetailsPanelLayout);
        productsDetailsPanelLayout.setHorizontalGroup(
            productsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productsDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(productsDetailsPanelLayout.createSequentialGroup()
                        .addComponent(deleteProductsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(productsSaveButton)
                        .addGap(18, 18, 18)
                        .addComponent(searchProducts, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshProductsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importProductsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(productsDetailsPanelLayout.createSequentialGroup()
                        .addComponent(productsScrollPanel)
                        .addContainerGap())))
        );
        productsDetailsPanelLayout.setVerticalGroup(
            productsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productsDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(productsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchProducts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(deleteProductsButton)
                        .addComponent(productsSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(productsDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(importProductsButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshProductsTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(productsScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(603, 603, 603))
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

        importSuppliesButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/ic_add_black_36dp_1x.png"))); // NOI18N
        importSuppliesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importSuppliesButtonActionPerformed(evt);
            }
        });

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

        deleteSuppliesButton.setText("Delete Supplies");
        deleteSuppliesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSuppliesButtonActionPerformed(evt);
            }
        });

        suppliesSaveButton.setText("Save");
        suppliesSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliesSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout suppliesDetailsPanelLayout = new javax.swing.GroupLayout(suppliesDetailsPanel);
        suppliesDetailsPanel.setLayout(suppliesDetailsPanelLayout);
        suppliesDetailsPanelLayout.setHorizontalGroup(
            suppliesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliesDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(suppliesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(suppliesDetailsPanelLayout.createSequentialGroup()
                        .addComponent(suppliesScrollPanel)
                        .addContainerGap())
                    .addGroup(suppliesDetailsPanelLayout.createSequentialGroup()
                        .addComponent(deleteSuppliesButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 649, Short.MAX_VALUE)
                        .addComponent(suppliesSaveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchSupplies, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refreshSuppliesTable, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(importSuppliesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13))))
        );
        suppliesDetailsPanelLayout.setVerticalGroup(
            suppliesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliesDetailsPanelLayout.createSequentialGroup()
                .addGroup(suppliesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(refreshSuppliesTable)
                    .addComponent(importSuppliesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suppliesDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(deleteSuppliesButton)
                        .addComponent(searchSupplies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(suppliesSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(suppliesScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(497, Short.MAX_VALUE))
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
                        .addComponent(productNameTextField2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(productionQuantitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(productNameTextField))
                            .addComponent(hireDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateOfProduction, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(productComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(46, 46, 46)
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
                .addContainerGap(478, Short.MAX_VALUE))
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
                .addContainerGap(15, Short.MAX_VALUE))
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

        searchProduction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchProductionActionPerformed(evt);
            }
        });

        refreshProductionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/Refresh_icon.svg.png"))); // NOI18N
        refreshProductionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshProductionButtonActionPerformed(evt);
            }
        });

        deleteProductionButton.setText("Delete Production");
        deleteProductionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProductionButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(productionScrollPanel)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addComponent(deleteProductionButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchProduction, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(refreshProductionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchProduction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteProductionButton)))
                    .addComponent(refreshProductionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(13, 13, 13)
                .addComponent(productionScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
                .addGap(90, 90, 90))
        );

        javax.swing.GroupLayout productionLayout = new javax.swing.GroupLayout(production);
        production.setLayout(productionLayout);
        productionLayout.setHorizontalGroup(
            productionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(productionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        productionLayout.setVerticalGroup(
            productionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(productionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(725, Short.MAX_VALUE))
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

        printSalesDetailsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N
        printSalesDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSalesDetailsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(printSalesDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchSale, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(refreshSalesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(saleScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1067, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(refreshSalesButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchSale))
                    .addComponent(printSalesDetailsBtn))
                .addGap(18, 18, 18)
                .addComponent(saleScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(792, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Daily Sales", jPanel2);

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

        printCusDetailsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N
        printCusDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printCusDetailsBtnActionPerformed(evt);
            }
        });

        importCustomerBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/create employee.png"))); // NOI18N
        importCustomerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importCustomerBtnActionPerformed(evt);
            }
        });

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

        deleteCustomerButton.setText("Delete Customer");
        deleteCustomerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCustomerButtonActionPerformed(evt);
            }
        });

        customersSaveButton.setText("Save");
        customersSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customersSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout customerDetailsPanelLayout = new javax.swing.GroupLayout(customerDetailsPanel);
        customerDetailsPanel.setLayout(customerDetailsPanelLayout);
        customerDetailsPanelLayout.setHorizontalGroup(
            customerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerDetailsPanelLayout.createSequentialGroup()
                .addGroup(customerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(customerDetailsPanelLayout.createSequentialGroup()
                        .addComponent(deleteCustomerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(customersSaveButton)
                        .addGap(18, 18, 18)
                        .addComponent(printCusDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchCustomerTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshCusDetailsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(importCustomerBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(customerScrollPanel))
                .addContainerGap())
        );
        customerDetailsPanelLayout.setVerticalGroup(
            customerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerDetailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(customerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(customerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(deleteCustomerButton)
                        .addComponent(customersSaveButton))
                    .addGroup(customerDetailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(printCusDetailsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshCusDetailsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchCustomerTxt)
                        .addComponent(importCustomerBtn, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addGap(18, 18, 18)
                .addComponent(customerScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );

        customerTabPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        customerTabPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        customerTabPanel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        printCustProductsBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N

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

        deleteCustomerProductsButton.setText("Delete Product");
        deleteCustomerProductsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteCustomerProductsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout custProductsPanelLayout = new javax.swing.GroupLayout(custProductsPanel);
        custProductsPanel.setLayout(custProductsPanelLayout);
        custProductsPanelLayout.setHorizontalGroup(
            custProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(custProductsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(custProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(custProScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, custProductsPanelLayout.createSequentialGroup()
                        .addComponent(deleteCustomerProductsButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(printCustProductsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchCustProductsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshCustProductsBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        custProductsPanelLayout.setVerticalGroup(
            custProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(custProductsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(custProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(custProductsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(printCustProductsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshCustProductsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(searchCustProductsTxt))
                    .addComponent(deleteCustomerProductsButton))
                .addGap(18, 18, 18)
                .addComponent(custProScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        customerTabPanel.addTab("Customer Products", custProductsPanel);

        customersGraphsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customers Sales %", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        javax.swing.GroupLayout customersGraphsPanelLayout = new javax.swing.GroupLayout(customersGraphsPanel);
        customersGraphsPanel.setLayout(customersGraphsPanelLayout);
        customersGraphsPanelLayout.setHorizontalGroup(
            customersGraphsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );
        customersGraphsPanelLayout.setVerticalGroup(
            customersGraphsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 211, Short.MAX_VALUE)
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
                        .addComponent(customersGraphsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addGroup(customersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customersGraphsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addComponent(customerTabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(732, Short.MAX_VALUE))
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

        searchTxt.setToolTipText("Search...");
        searchTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTxtActionPerformed(evt);
            }
        });

        printBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printBtnActionPerformed(evt);
            }
        });

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

        deletRow.setText("Delete employee");
        deletRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletRowActionPerformed(evt);
            }
        });

        employeesSaveButton.setText("Save");
        employeesSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeesSaveButtonActionPerformed(evt);
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
                        .addComponent(deletRow)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(employeesSaveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(printBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(searchTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refreshEmployeesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(importEmplBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)))
                .addContainerGap())
        );
        employeeListPanelLayout.setVerticalGroup(
            employeeListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employeeListPanelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(employeeListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(refreshEmployeesBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(importEmplBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employeeListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(deletRow)
                        .addComponent(employeesSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(15, 15, 15)
                .addComponent(employeeScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 637, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(567, Short.MAX_VALUE))
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

        noSuppliesLb.setText("No. Suppliers: ");

        suppliersDetailsPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Supplier Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 12))); // NOI18N

        printSupDetailsBtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/printer_hardware.png"))); // NOI18N
        printSupDetailsBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSupDetailsBtn1ActionPerformed(evt);
            }
        });

        importSupplierBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/create employee.png"))); // NOI18N
        importSupplierBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importSupplierBtnActionPerformed(evt);
            }
        });

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

        deleteSupplierButton.setText("Delete Supplier");
        deleteSupplierButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSupplierButtonActionPerformed(evt);
            }
        });

        suppliersSaveButton.setText("Save");
        suppliersSaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliersSaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout suppliersDetailsPanel1Layout = new javax.swing.GroupLayout(suppliersDetailsPanel1);
        suppliersDetailsPanel1.setLayout(suppliersDetailsPanel1Layout);
        suppliersDetailsPanel1Layout.setHorizontalGroup(
            suppliersDetailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliersDetailsPanel1Layout.createSequentialGroup()
                .addComponent(deleteSupplierButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(suppliersSaveButton)
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
                    .addComponent(refreshSuplTableBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchSupplierTxt)
                    .addComponent(importSupplierBtn, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suppliersDetailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(deleteSupplierButton)
                        .addComponent(suppliersSaveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(supplierScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addContainerGap())
        );

        suppliesTab.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        suppliesTab.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        suppliesTab.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        searchSuppliesTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchSuppliesTxtActionPerformed(evt);
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

        deleteSupplyButton.setText("Delete Supply");
        deleteSupplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSupplyButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout suppliesTabPanelLayout = new javax.swing.GroupLayout(suppliesTabPanel);
        suppliesTabPanel.setLayout(suppliesTabPanelLayout);
        suppliesTabPanelLayout.setHorizontalGroup(
            suppliesTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliesTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(suppliesTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(suppliesTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 1037, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, suppliesTabPanelLayout.createSequentialGroup()
                        .addComponent(deleteSupplyButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(refreshSuppliesTableBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchSuppliesTxt)
                    .addComponent(deleteSupplyButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(suppliesTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
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
                        .addGap(0, 800, Short.MAX_VALUE)
                        .addComponent(exportPurchFilesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(printPurchasesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchPurchasesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshPurchasesTableBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(importPurchaseBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(purchHistoryScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1037, Short.MAX_VALUE))
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
                    .addComponent(suppliesTab, javax.swing.GroupLayout.DEFAULT_SIZE, 1066, Short.MAX_VALUE)
                    .addComponent(suppliersDetailsPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        suppliersLayout.setVerticalGroup(
            suppliersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(suppliersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(noSuppliesLb)
                .addGap(32, 32, 32)
                .addComponent(suppliersDetailsPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(suppliesTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(732, Short.MAX_VALUE))
        );

        tabPanel.addTab("Suppliers", suppliers);

        Menu.setText("File");

        Forms.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/export.png"))); // NOI18N
        Forms.setText("Forms");

        AdminForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/ic_assignment_ind_black_24dp.png"))); // NOI18N
        AdminForm.setText("Change information");
        AdminForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdminFormActionPerformed(evt);
            }
        });
        Forms.add(AdminForm);

        SetUpForm2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/create employee.png"))); // NOI18N
        SetUpForm2.setText("Add Employee");
        SetUpForm2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetUpForm2ActionPerformed(evt);
            }
        });
        Forms.add(SetUpForm2);

        SetUpForm1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/ic_local_shipping_black_24dp.png"))); // NOI18N
        SetUpForm1.setText("Add Supplier");
        SetUpForm1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetUpForm1ActionPerformed(evt);
            }
        });
        Forms.add(SetUpForm1);

        SetUpForm3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/ic_store_mall_directory_black_24dp.png"))); // NOI18N
        SetUpForm3.setText("Add Customer");
        SetUpForm3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetUpForm3ActionPerformed(evt);
            }
        });
        Forms.add(SetUpForm3);

        SetUpForm.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/ic_widgets_black_36dp.png"))); // NOI18N
        SetUpForm.setText("Add Product");
        SetUpForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetUpFormActionPerformed(evt);
            }
        });
        Forms.add(SetUpForm);

        SetUpForm4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/ic_storage_black_24dp.png"))); // NOI18N
        SetUpForm4.setText("Add Supply");
        SetUpForm4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SetUpForm4ActionPerformed(evt);
            }
        });
        Forms.add(SetUpForm4);

        Menu.add(Forms);

        logoutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eb_managementapp/UI/Images/018_320_door_exit_logout-512.png"))); // NOI18N
        logoutMenuItem.setText("Log out");
        logoutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutMenuItemActionPerformed(evt);
            }
        });
        Menu.add(logoutMenuItem);

        jMenuBar1.add(Menu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPanel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void searchTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchTxtActionPerformed

    private void searchCustomerTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustomerTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchCustomerTxtActionPerformed

    private void searchCustProductsTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCustProductsTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchCustProductsTxtActionPerformed

    private void searchSupplierTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSupplierTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSupplierTxtActionPerformed

    private void searchSuppliesTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSuppliesTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSuppliesTxtActionPerformed

    private void exportPurchFilesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPurchFilesBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_exportPurchFilesBtnActionPerformed

    private void searchPurchasesTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPurchasesTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchPurchasesTxtActionPerformed

    private void importSupplierBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importSupplierBtnActionPerformed
        this.setVisible(true);
        addSuppliersForm = new AddSuppliersForm(this);
    }//GEN-LAST:event_importSupplierBtnActionPerformed

    private void importCustomerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importCustomerBtnActionPerformed
        setVisible(true);
        customersForm = new CustomersForm(this);
    }//GEN-LAST:event_importCustomerBtnActionPerformed

    private void importEmplBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importEmplBtnActionPerformed
        setVisible(true);
        addUsersForm = new AddUsersForm(this);
    }//GEN-LAST:event_importEmplBtnActionPerformed

    private void importProductsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importProductsButtonActionPerformed
        this.setVisible(true);
        addProductsForm = new AddProductsForm(this);
    }//GEN-LAST:event_importProductsButtonActionPerformed

    private void searchProductsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchProductsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchProductsActionPerformed

    private void importSuppliesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importSuppliesButtonActionPerformed
        this.setVisible(true);
        addSuppliesForm = new AddSuppliesForm(this);
    }//GEN-LAST:event_importSuppliesButtonActionPerformed

    private void searchSuppliesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSuppliesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSuppliesActionPerformed

    private void refreshEmployeesBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshEmployeesBtnActionPerformed
        getEmployees();
        employeesTab();
    }//GEN-LAST:event_refreshEmployeesBtnActionPerformed

    private void refreshCustProductsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshCustProductsBtnActionPerformed
        getCustomerProducts();
        customersTab();
    }//GEN-LAST:event_refreshCustProductsBtnActionPerformed

    private void refreshCusDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshCusDetailsBtnActionPerformed
        getCustomers();
        customersTab();
    }//GEN-LAST:event_refreshCusDetailsBtnActionPerformed

    private void refreshSuplTableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshSuplTableBtnActionPerformed
        getSuppliers();
        suppliersTab();
    }//GEN-LAST:event_refreshSuplTableBtnActionPerformed

    private void refreshSuppliesTableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshSuppliesTableBtnActionPerformed
        getSupplierSupplies();
        suppliersTab();
    }//GEN-LAST:event_refreshSuppliesTableBtnActionPerformed

    private void refreshProductsTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshProductsTableActionPerformed
        getProducts();
        productsTab();
    }//GEN-LAST:event_refreshProductsTableActionPerformed

    private void refreshSuppliesTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshSuppliesTableActionPerformed
        getSupplies();
        suppliesTab();
    }//GEN-LAST:event_refreshSuppliesTableActionPerformed

    private void refreshPurchasesTableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshPurchasesTableBtnActionPerformed
        getSupplyTransactions();
        suppliersTab();
    }//GEN-LAST:event_refreshPurchasesTableBtnActionPerformed

    private void deletRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletRowActionPerformed

        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected employee?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            int selectedRow = employeesTable.getSelectedRow();
            int selectedID = employeesList.get(selectedRow).getUserID();
            deleteEmployee(selectedID);
            getEmployees();
            employeesTab();
        }
    }//GEN-LAST:event_deletRowActionPerformed

    private void deleteSupplierButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSupplierButtonActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected supplier?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            int selectedRow = supplierDetailsTable.getSelectedRow();
            int selectedID = suppliersList.get(selectedRow).getID();
            deleteSupplier(selectedID);
            getSuppliers();
            suppliersTab();
        }
    }//GEN-LAST:event_deleteSupplierButtonActionPerformed

    private void deleteSupplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSupplyButtonActionPerformed

        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected supply?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            int selectedRow = suppliesTable.getSelectedRow();
            int selectedID = supplierSuppliesList.get(selectedRow).getID();
            deleteSupplierSupplies(selectedID);
            getSuppliers();
            getSupplierSupplies();
            getSupplies();
            suppliersTab();
        }
    }//GEN-LAST:event_deleteSupplyButtonActionPerformed

    private void deleteCustomerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCustomerButtonActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected customer?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            int selectedRow = customerDetailsTable.getSelectedRow();
            int selectedID = customersList.get(selectedRow).getID();
            deleteCustomers(selectedID);
            getCustomers();
            customersTab();
        }
    }//GEN-LAST:event_deleteCustomerButtonActionPerformed

    private void deleteCustomerProductsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteCustomerProductsButtonActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected product?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            int selectedRow = customerProductsTable.getSelectedRow();
            int selectedID = customerProductsList.get(selectedRow).getID();
            deleteCustomerProducts(selectedID);
            getCustomers();
            getCustomerProducts();
            getProducts();
            customersTab();
        }
    }//GEN-LAST:event_deleteCustomerProductsButtonActionPerformed

    private void deleteProductsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProductsButtonActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected product?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            int selectedRow = productsDetailsTable.getSelectedRow();
            int selectedID = productsList.get(selectedRow).getID();
            deleteProducts(selectedID);;
            getProducts();
            productsTab();
        }
    }//GEN-LAST:event_deleteProductsButtonActionPerformed

    private void deleteSuppliesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteSuppliesButtonActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected supplies?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            int selectedRow = suppliesDetailsTable.getSelectedRow();
            int selectedID = suppliesList.get(selectedRow).getID();
            deleteSupplies(selectedID);;
            getSupplies();
            suppliesTab();
        }
    }//GEN-LAST:event_deleteSuppliesButtonActionPerformed

    private void employeesSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeesSaveButtonActionPerformed
        int currentRow = employeesTable.getSelectedRow();
        if (currentRow < 0) {
            showMessageDialog(null, "No employee selected to save. Please select an employee.", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Map table columns to fields:
        int id = (int) employeesTable.getValueAt(currentRow, 0);
        String username = (String) employeesTable.getValueAt(currentRow, 1);
        String firstname = (String) employeesTable.getValueAt(currentRow, 2);
        String lastname = (String) employeesTable.getValueAt(currentRow, 3);
        String position = (String) employeesTable.getValueAt(currentRow, 4);
        String dateHired = (String) employeesTable.getValueAt(currentRow, 5);
        String telephone = (String) employeesTable.getValueAt(currentRow, 6);
        String address = (String) employeesTable.getValueAt(currentRow, 7);
        String city = (String) employeesTable.getValueAt(currentRow, 8);
        String country = (String) employeesTable.getValueAt(currentRow, 9);

        int positionID = 0;
        long dateOfHire = 0;
        int countryID = 0;

        //Get position ID
        for (int i = 0; i < positionsList.size(); i++) {
            if (positionsList.get(i).getUserLevelName().equals(position)) {
                positionID = positionsList.get(i).getUserLevelID();
                break;
            }
        }
        if (positionID == 0) {
            showMessageDialog(null, "Invalid position selected", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Get date of hire
        try {
            Date date = Users.DATE_FORMAT.parse(dateHired);
            Timestamp timestamp = new Timestamp(date.getTime());
            dateOfHire = timestamp.getTime();
        } catch (Exception e) {
            showMessageDialog(null, "Invalid date/time", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Get country ID
        for (int i = 0; i < countriesList.size(); i++) {
            if (countriesList.get(i).getName().equals(country)) {
                countryID = countriesList.get(i).getID();
                break;
            }
        }
        if (countryID == 0) {
            showMessageDialog(null, "Invalid country selected", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        Users u = new Users(
                employeesList.get(currentRow).getUserID(),
                username,
                employeesList.get(currentRow).getPassword(),
                positionID,
                firstname,
                lastname,
                dateOfHire,
                city,
                address,
                telephone,
                countryID
        );

        editEmployees(u);

    }//GEN-LAST:event_employeesSaveButtonActionPerformed

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printBtnActionPerformed
        int currentRow = employeesTable.getSelectedRow();
        if (currentRow < 0) {
            showMessageDialog(null, "No employee selected to save. Please select an employee.", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Map table columns to fields:
        int id = (int) employeesTable.getValueAt(currentRow, 0);
        String username = (String) employeesTable.getValueAt(currentRow, 1);
        String firstname = (String) employeesTable.getValueAt(currentRow, 2);
        String lastname = (String) employeesTable.getValueAt(currentRow, 3);
        String position = (String) employeesTable.getValueAt(currentRow, 4);
        String dateHired = (String) employeesTable.getValueAt(currentRow, 5);
        String telephone = (String) employeesTable.getValueAt(currentRow, 6);
        String address = (String) employeesTable.getValueAt(currentRow, 7);
        String city = (String) employeesTable.getValueAt(currentRow, 8);
        String country = (String) employeesTable.getValueAt(currentRow, 9);

        PDDocument document;
        URL url = getClass().getResource("");
        String currentFolder = url.getPath();
        String combinedPath = currentFolder + "Documents/employees.pdf";
        File file = new File(combinedPath);
        try {
            document = PDDocument.load(file);

            PDPage page = PDFBoxUtils.getPage(document, 0);

            PDFBoxUtils.addText(document, page, firstname, Color.black, PDType1Font.COURIER, 14, 220, 570);
            PDFBoxUtils.addText(document, page, lastname, Color.black, PDType1Font.COURIER, 14, 220, 545);
            PDFBoxUtils.addText(document, page, position, Color.black, PDType1Font.COURIER, 14, 220, 520);
            PDFBoxUtils.addText(document, page, telephone, Color.black, PDType1Font.COURIER, 14, 220, 495);
            PDFBoxUtils.addText(document, page, address, Color.black, PDType1Font.COURIER, 14, 220, 473);
            PDFBoxUtils.addText(document, page, city, Color.black, PDType1Font.COURIER, 14, 220, 450);
            PDFBoxUtils.addText(document, page, country, Color.black, PDType1Font.COURIER, 14, 220, 425);
            PDFBoxUtils.addText(document, page, dateHired, Color.black, PDType1Font.COURIER, 14, 220, 405);

            String fileName = "employeesDetails.pdf";
            String exportedFileName = currentFolder + "../../../" + fileName;
            document.save(exportedFileName);
            document.close();
            File exportedFile = new File(exportedFileName);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(exportedFile);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_printBtnActionPerformed

    private void suppliersSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliersSaveButtonActionPerformed
        int currentRow = supplierDetailsTable.getSelectedRow();
        if (currentRow < 0) {
            showMessageDialog(null, "No supplier selected to save. Please select an employee.", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Map table columns to fields:
        int id = (int) supplierDetailsTable.getValueAt(currentRow, 0);
        String name = (String) supplierDetailsTable.getValueAt(currentRow, 1);
        String telephone = (String) supplierDetailsTable.getValueAt(currentRow, 2);
        String address = (String) supplierDetailsTable.getValueAt(currentRow, 3);
        String city = (String) supplierDetailsTable.getValueAt(currentRow, 4);
        String country = (String) supplierDetailsTable.getValueAt(currentRow, 5);

        int countryID = 0;

        //Get country ID
        for (int i = 0; i < countriesList.size(); i++) {
            if (countriesList.get(i).getName().equals(country)) {
                countryID = countriesList.get(i).getID();
                break;
            }
        }
        if (countryID == 0) {
            showMessageDialog(null, "Invalid country selected", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        Suppliers s = new Suppliers(
                suppliersList.get(currentRow).getID(),
                name,
                countryID,
                address,
                city,
                telephone
        );

        editSuppliers(s);
    }//GEN-LAST:event_suppliersSaveButtonActionPerformed

    private void customersSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customersSaveButtonActionPerformed
        int currentRow = customerDetailsTable.getSelectedRow();
        if (currentRow < 0) {
            showMessageDialog(null, "No customer selected to save. Please select a customer", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Map table columns to fields:
        int id = (int) customerDetailsTable.getValueAt(currentRow, 0);
        String name = (String) customerDetailsTable.getValueAt(currentRow, 1);
        String telephone = (String) customerDetailsTable.getValueAt(currentRow, 2);
        String address = (String) customerDetailsTable.getValueAt(currentRow, 3);
        String city = (String) customerDetailsTable.getValueAt(currentRow, 4);
        String country = (String) customerDetailsTable.getValueAt(currentRow, 5);

        int countryID = 0;

        //Get country ID
        for (int i = 0; i < countriesList.size(); i++) {
            if (countriesList.get(i).getName().equals(country)) {
                countryID = countriesList.get(i).getID();
                break;
            }
        }
        if (countryID == 0) {
            showMessageDialog(null, "Invalid country selected", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        Customers c = new Customers(
                customersList.get(currentRow).getID(),
                name,
                countryID,
                city,
                address,
                telephone,
                customersList.get(currentRow).getCustomerProductsID()
        );

        editCustomers(c);
    }//GEN-LAST:event_customersSaveButtonActionPerformed

    private void refreshSalesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshSalesButtonActionPerformed
        getSaleProducts();
        salesTab();
    }//GEN-LAST:event_refreshSalesButtonActionPerformed

    private void searchSaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchSaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSaleActionPerformed

    private void productsSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productsSaveButtonActionPerformed
        int currentRow = productsDetailsTable.getSelectedRow();
        if (currentRow < 0) {
            showMessageDialog(null, "No supplier selected to save. Please select a product.", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Map table columns to fields:
        int id = (int) productsDetailsTable.getValueAt(currentRow, 0);
        String name = (String) productsDetailsTable.getValueAt(currentRow, 1);
        String size = (String) productsDetailsTable.getValueAt(currentRow, 2);
        int quantity = (int) productsDetailsTable.getValueAt(currentRow, 3);
        double price = (double) productsDetailsTable.getValueAt(currentRow, 4);
        String type = (String) productsDetailsTable.getValueAt(currentRow, 5);

        int typeID = 0;
        int sizeID = 0;

        //Get productType ID
        for (int i = 0; i < productTypesList.size(); i++) {
            if (productTypesList.get(i).getName().equals(type)) {
                typeID = productTypesList.get(i).getID();
                break;
            }
        }

        //Get productSize ID
        for (int i = 0; i < productSizesList.size(); i++) {
            if (productSizesList.get(i).getName().equals(size)) {
                sizeID = productSizesList.get(i).getID();
                break;
            }
        }
        if (typeID == 0) {
            showMessageDialog(null, "Invalid product type selected", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        if (sizeID == 0) {
            showMessageDialog(null, "Invalid product size selected", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        Products p = new Products(
                productsList.get(currentRow).getID(),
                name,
                price,
                quantity,
                sizeID,
                typeID,
                productsList.get(currentRow).getProductSuppliesID()
        );

        editProducts(p);
    }//GEN-LAST:event_productsSaveButtonActionPerformed

    private void suppliesSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliesSaveButtonActionPerformed
        int currentRow = suppliesDetailsTable.getSelectedRow();
        if (currentRow < 0) {
            showMessageDialog(null, "No supplier selected to save. Please select a product.", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Map table columns to fields:
        int id = (int) suppliesDetailsTable.getValueAt(currentRow, 0);
        String name = (String) suppliesDetailsTable.getValueAt(currentRow, 1);
        String supplier = (String) suppliesDetailsTable.getValueAt(currentRow, 2);
        int quantity = (int) suppliesDetailsTable.getValueAt(currentRow, 3);
        float price = (float) suppliesDetailsTable.getValueAt(currentRow, 4);

        int supplierID = 0;

        //Get supplier ID
        for (int i = 0; i < suppliersList.size(); i++) {
            if (suppliersList.get(i).getName().equals(supplier)) {
                supplierID = suppliersList.get(i).getID();
                break;
            }
        }

        Supplies s = new Supplies(
                suppliesList.get(currentRow).getID(),
                name,
                supplierID,
                quantity,
                price
        );

        editSupplies(s);
    }//GEN-LAST:event_suppliesSaveButtonActionPerformed

    private void printCusDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printCusDetailsBtnActionPerformed
        int currentRow = customerDetailsTable.getSelectedRow();
        if (currentRow < 0) {
            showMessageDialog(null, "No customer selected to save. Please select a customer", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Map table columns to fields:
        int id = (int) customerDetailsTable.getValueAt(currentRow, 0);
        String name = (String) customerDetailsTable.getValueAt(currentRow, 1);
        String telephone = (String) customerDetailsTable.getValueAt(currentRow, 2);
        String address = (String) customerDetailsTable.getValueAt(currentRow, 3);
        String city = (String) customerDetailsTable.getValueAt(currentRow, 4);
        String country = (String) customerDetailsTable.getValueAt(currentRow, 5);

        PDDocument document;
        URL url = getClass().getResource("");
        String currentFolder = url.getPath();
        String combinedPath = currentFolder + "Documents/customer.pdf";
        File file = new File(combinedPath);
        try {
            document = PDDocument.load(file);

            PDPage page = PDFBoxUtils.getPage(document, 0);

            PDFBoxUtils.addText(document, page, name, Color.black, PDType1Font.COURIER, 14, 220, 570);
            PDFBoxUtils.addText(document, page, telephone, Color.black, PDType1Font.COURIER, 14, 220, 545);
            PDFBoxUtils.addText(document, page, address, Color.black, PDType1Font.COURIER, 14, 220, 520);
            PDFBoxUtils.addText(document, page, city, Color.black, PDType1Font.COURIER, 14, 220, 495);
            PDFBoxUtils.addText(document, page, country, Color.black, PDType1Font.COURIER, 14, 220, 473);

            String fileName = "customersDetails.pdf";
            String exportedFileName = currentFolder + "../../../" + fileName;
            document.save(exportedFileName);
            document.close();
            File exportedFile = new File(exportedFileName);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(exportedFile);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_printCusDetailsBtnActionPerformed

    private void printSupDetailsBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSupDetailsBtn1ActionPerformed

        int currentRow = supplierDetailsTable.getSelectedRow();
        if (currentRow < 0) {
            showMessageDialog(null, "No supplier selected to save. Please select an employee.", "Warning", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Map table columns to fields:
        int id = (int) supplierDetailsTable.getValueAt(currentRow, 0);
        String name = (String) supplierDetailsTable.getValueAt(currentRow, 1);
        String telephone = (String) supplierDetailsTable.getValueAt(currentRow, 2);
        String address = (String) supplierDetailsTable.getValueAt(currentRow, 3);
        String city = (String) supplierDetailsTable.getValueAt(currentRow, 4);
        String country = (String) supplierDetailsTable.getValueAt(currentRow, 5);

        PDDocument document;
        URL url = getClass().getResource("");
        String currentFolder = url.getPath();
        String combinedPath = currentFolder + "Documents/suppliers.pdf";
        File file = new File(combinedPath);
        try {
            document = PDDocument.load(file);

            PDPage page = PDFBoxUtils.getPage(document, 0);

            PDFBoxUtils.addText(document, page, name, Color.black, PDType1Font.COURIER, 14, 220, 570);
            PDFBoxUtils.addText(document, page, telephone, Color.black, PDType1Font.COURIER, 14, 220, 545);
            PDFBoxUtils.addText(document, page, address, Color.black, PDType1Font.COURIER, 14, 220, 520);
            PDFBoxUtils.addText(document, page, city, Color.black, PDType1Font.COURIER, 14, 220, 495);
            PDFBoxUtils.addText(document, page, country, Color.black, PDType1Font.COURIER, 14, 220, 473);

            String fileName = "suppliersDetails.pdf";
            String exportedFileName = currentFolder + "../../../" + fileName;
            document.save(exportedFileName);
            document.close();
            File exportedFile = new File(exportedFileName);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(exportedFile);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_printSupDetailsBtn1ActionPerformed

    private void printSalesDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSalesDetailsBtnActionPerformed

        int currentRow = salesDetailsTable.getSelectedRow();
        if (currentRow < 0) {
            PDDocument document;
            URL url = getClass().getResource("");
            String currentFolder = url.getPath();
            String combinedPath = currentFolder + "Documents/sales.pdf";
            File file = new File(combinedPath);
            try {
                document = PDDocument.load(file);

                PDPage page = PDFBoxUtils.getPage(document, 0);
                String country = "";
                for (int j = 0; j < countriesList.size(); j++) {
                    if (countriesList.get(j).getID() == companyCountryID) {
                        country = countriesList.get(j).getName();
                    }
                }

                //Company Details
                PDFBoxUtils.addText(document, page, companyName, Color.black, PDType1Font.COURIER, 11, 75, 645);
                PDFBoxUtils.addText(document, page, companyTelephone, Color.black, PDType1Font.COURIER, 11, 75, 632);
                PDFBoxUtils.addText(document, page, companyAddress, Color.black, PDType1Font.COURIER, 11, 75, 620);
                PDFBoxUtils.addText(document, page, companyCity, Color.black, PDType1Font.COURIER, 11, 75, 609);
                PDFBoxUtils.addText(document, page, country, Color.black, PDType1Font.COURIER, 11, 75, 599);

                int numHeight = 512;
                double subTotal = 0.0;
                String sumTotal = "";

                for (int i = 0; i < salesList.size(); i++) {

                    //Put customerName in the Table
                    String customerName = "";
                    for (int j = 0; j < customersList.size(); j++) {
                        if (customersList.get(j).getID() == salesList.get(i).getCustomerID()) {
                            customerName = customersList.get(j).getName();
                        }
                    }
                    //Date
                    Timestamp timestamp = new Timestamp(salesList.get(i).getSaleTimeDate());
                    Date date = new Date(timestamp.getTime());

                    //Row Number
                    int num = 0;
                    num = i + 1;
                    String number = String.valueOf(num);

                    //Total
                    double total = 0.0;

                    for (int j = 0; j < saleProductsList.size(); j++) {
                        if (salesList.get(i).getID() == saleProductsList.get(j).getSaleID()) {
                            int quantity = saleProductsList.get(j).getQuantitySold();
                            double price = 0.0;
                            for (int k = 0; k < productsList.size(); k++) {
                                if (saleProductsList.get(j).getProductID() == productsList.get(k).getID()) {
                                    price = productsList.get(k).getPrice();
                                    break;
                                }
                            }
                            total += quantity * price;
                            subTotal += total;
                        }

                    }

                    String totalNum = String.format("%.2f", total);

                    sumTotal = String.format("%.2f", subTotal);

                    PDFBoxUtils.addText(document, page, number, Color.black, PDType1Font.COURIER, 13, 75, numHeight);
                    PDFBoxUtils.addText(document, page, Users.DATE_FORMAT.format(date), Color.black, PDType1Font.COURIER, 10, 132, numHeight);
                    PDFBoxUtils.addText(document, page, customerName, Color.black, PDType1Font.COURIER, 13, 200, numHeight);
                    PDFBoxUtils.addTextEuro(document, page, totalNum, Color.black, PDType1Font.COURIER, 13, 448, numHeight);

                    numHeight -= 17;
                }
                PDFBoxUtils.addTextEuro(document, page, sumTotal, Color.black, PDType1Font.COURIER, 13, 448, 160);

                String fileName = "customerSales.pdf";
                String exportedFileName = currentFolder + "../../../" + fileName;
                document.save(exportedFileName);
                document.close();
                File exportedFile = new File(exportedFileName);
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(exportedFile);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {

            PDDocument document;
            URL url = getClass().getResource("");
            String currentFolder = url.getPath();
            String combinedPath = currentFolder + "Documents/invoice.pdf";
            File file = new File(combinedPath);
            try {
                document = PDDocument.load(file);

                PDPage page = PDFBoxUtils.getPage(document, 0);

                String country = "";
                for (int j = 0; j < countriesList.size(); j++) {
                    if (countriesList.get(j).getID() == companyCountryID) {
                        country = countriesList.get(j).getName();
                    }
                }

                //Company Details
                PDFBoxUtils.addText(document, page, companyName, Color.black, PDType1Font.COURIER, 11, 75, 645);
                PDFBoxUtils.addText(document, page, companyTelephone, Color.black, PDType1Font.COURIER, 11, 75, 632);
                PDFBoxUtils.addText(document, page, companyAddress, Color.black, PDType1Font.COURIER, 11, 75, 620);
                PDFBoxUtils.addText(document, page, companyCity, Color.black, PDType1Font.COURIER, 11, 75, 609);
                PDFBoxUtils.addText(document, page, country, Color.black, PDType1Font.COURIER, 11, 75, 599);

                int currentSaleID = salesList.get(currentRow).getID();
                double total = 0.0;
                String sumTotal = "";
                int numHeight = 475;

                for (int j = 0; j < saleProductsList.size(); j++) {
                    if (saleProductsList.get(j).getSaleID() == currentSaleID) {

                        // Date
                        Timestamp timestamp = new Timestamp(salesList.get(currentRow).getSaleTimeDate());
                        Date date = new Date(timestamp.getTime());
                        String stringDate = Users.DATE_FORMAT.format(date);

                        String customerName = "";
                        for (int k = 0; k < customersList.size(); k++) {
                            if (customersList.get(k).getID() == salesList.get(currentRow).getCustomerID()) {
                                customerName = customersList.get(k).getName();
                                break;
                            }
                        }

                        int quantity = saleProductsList.get(j).getQuantitySold();

                        String productName = "";
                        int productSizeID = 0;
                        double price = 0.0;
                        for (int k = 0; k < productsList.size(); k++) {
                            if (productsList.get(k).getID() == saleProductsList.get(j).getProductID()) {
                                productName = productsList.get(k).getName();
                                productSizeID = productsList.get(k).getProductSizeID();
                                price = productsList.get(k).getPrice();
                                break;
                            }
                        }

                        String productSizeName = "";
                        for (int k = 0; k < productSizesList.size(); k++) {
                            if (productSizesList.get(k).getID() == productSizeID) {
                                productSizeName = productSizesList.get(k).getName();
                                break;
                            }
                        }

                        double lineTotal = price * quantity;
                        total += lineTotal;

                        String totalNum = String.format("%.2f", lineTotal);
                        String stringQuantity = String.valueOf(quantity);
                        String stringPrice = String.format("%.2f", price);

                        sumTotal = String.format("%.2f", total);

                        PDFBoxUtils.addText(document, page, customerName, Color.black, PDType1Font.COURIER, 13, 75, 525);
                        PDFBoxUtils.addText(document, page, stringDate, Color.black, PDType1Font.COURIER, 13, 270, 525);

                        PDFBoxUtils.addText(document, page, stringQuantity, Color.black, PDType1Font.COURIER, 13, 80, numHeight);
                        PDFBoxUtils.addText(document, page, productName, Color.black, PDType1Font.COURIER, 13, 132, numHeight);
                        PDFBoxUtils.addText(document, page, productSizeName, Color.black, PDType1Font.COURIER, 13, 300, numHeight);
                        PDFBoxUtils.addTextEuro(document, page, stringPrice, Color.black, PDType1Font.COURIER, 13, 388, numHeight);
                        PDFBoxUtils.addTextEuro(document, page, totalNum, Color.black, PDType1Font.COURIER, 13, 448, numHeight);

                        numHeight -= 18;

                    }

                }

                //Customer name...
                PDFBoxUtils.addTextEuro(document, page, sumTotal, Color.black, PDType1Font.COURIER, 13, 448, 155);
                String fileName = "custsInvoice.pdf";
                String exportedFileName = currentFolder + "../../../" + fileName;
                document.save(exportedFileName);
                document.close();
                File exportedFile = new File(exportedFileName);
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(exportedFile);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


    }//GEN-LAST:event_printSalesDetailsBtnActionPerformed

    private void deleteProductionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProductionButtonActionPerformed
        int dialogResult = JOptionPane.showConfirmDialog(null, "Are you sure you would like to delete the selected production batch?", "Warning", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            int selectedRow = productionDetailsTable.getSelectedRow();
            int selectedID = productionList.get(selectedRow).getID();
            deleteProduction(selectedID);
            getProduction();
            productionTab();
        }
    }//GEN-LAST:event_deleteProductionButtonActionPerformed

    private void refreshProductionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshProductionButtonActionPerformed
        getProduction();
        productionTab();
    }//GEN-LAST:event_refreshProductionButtonActionPerformed

    private void searchProductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchProductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchProductionActionPerformed

    private void dateOfProductionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateOfProductionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dateOfProductionActionPerformed

    private void addBottlesQuantityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBottlesQuantityButtonActionPerformed
        addProduction();
    }//GEN-LAST:event_addBottlesQuantityButtonActionPerformed

    private void SetUpFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetUpFormActionPerformed
        addProductsForm = new AddProductsForm(this);
    }//GEN-LAST:event_SetUpFormActionPerformed

    private void AdminFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdminFormActionPerformed
        adminForm = new AdminForm(this);
    }//GEN-LAST:event_AdminFormActionPerformed

    private void logoutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutMenuItemActionPerformed

        int x = JOptionPane.showConfirmDialog(null, "Are you sure you would like to log out?", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (x == 0) {
            String response = HTTPConnection.executePost(HTTPConnection.API_URL + "/Logout/", "SessionID=" + sessionID);
            try {
                JSONObject jsonObject = new JSONObject(response);
                final String status = jsonObject.getString("Status");
                final String title = jsonObject.getString("Title");
                final String message = jsonObject.getString("Message");

                if (status.equals(HTTPConnection.RESPONSE_ERROR)) {
                    showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
                } else if (status.equals(HTTPConnection.RESPONSE_OK)) {

                    File file = new File(LoginForm.SESSION_FILENAME);
                    File file2 = new File(LoginForm.SESSION_PASSWORD);
                    File file3 = new File(LoginForm.SESSION_USERID);
                    File file4 = new File(LoginForm.SESSION_USERNAME);

                    file.delete();
                    file2.delete();
                    file3.delete();
                    file4.delete();

                    showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        new LoginForm();
        this.setVisible(false);

    }//GEN-LAST:event_logoutMenuItemActionPerformed

    private void SetUpForm1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetUpForm1ActionPerformed
        addSuppliersForm = new AddSuppliersForm(this);
    }//GEN-LAST:event_SetUpForm1ActionPerformed

    private void SetUpForm2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetUpForm2ActionPerformed
        addUsersForm = new AddUsersForm(this);
    }//GEN-LAST:event_SetUpForm2ActionPerformed

    private void SetUpForm3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetUpForm3ActionPerformed
        customersForm = new CustomersForm(this);
    }//GEN-LAST:event_SetUpForm3ActionPerformed

    private void SetUpForm4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SetUpForm4ActionPerformed
        addSuppliesForm = new AddSuppliesForm(this);
    }//GEN-LAST:event_SetUpForm4ActionPerformed

    private void editEmployees(Users u) {
        //Get field values:

        //Check if the firstname is valid
        if (u.getFirstname().trim().isEmpty()) {
            showMessageDialog(null, "Please provide a valid firstname", "Invalid Firstname", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Check if the lastname is valid
        if (u.getLastname().trim().isEmpty()) {
            showMessageDialog(null, "Please provide a valid lastname", "Invalid lastname", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Check if the username is valid
        if (u.getUsername().trim().isEmpty()) {
            showMessageDialog(null, "Please provide a valid username", "Invalid username", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Make the call:
        String editUserJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Users", "Update",
                "SessionID=" + sessionID + "&UserID=" + u.getUserID() + "&Firstname=" + u.getFirstname() + "&Lastname="
                + u.getLastname() + "&Username=" + u.getUsername() + "&City=" + u.getCity()
                + "&Address=" + u.getAddress() + "&Telephone=" + u.getTelephone() + "&CountryID=" + u.getCountryID()
                + "&UserLevelID=" + u.getUserLevelID() + "&Password=" + u.getPassword() + "&DateHired=" + u.getDateHired()
        );
        try {
            JSONObject jsonObject = new JSONObject(editUserJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, "Employee " + u.getFirstname() + " saved.", title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Update table:
        getEmployees();
        employeesTab();
    }

    private void editSuppliers(Suppliers s) {
        //Get field values:

        //Check if the name is valid
        if (s.getName().trim().isEmpty()) {
            showMessageDialog(null, "Please provide a valid name", "Invalid Name", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Make the call:
        String editSupplierJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Suppliers", "Update",
                "SessionID=" + sessionID + "&ID=" + s.getID() + "&Name=" + s.getName() + "&CountryID=" + s.getCountryID()
                + "&Address=" + s.getAddress() + "&City=" + s.getCity() + "&Telephone=" + s.getTelephone()
        );
        try {
            JSONObject jsonObject = new JSONObject(editSupplierJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, "Supplier " + s.getName() + " saved.", title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Update table:
        getSuppliers();
        suppliersTab();
    }

    private void editCustomers(Customers c) {
        //Get field values:

        //Check if the name is valid
        if (c.getName().trim().isEmpty()) {
            showMessageDialog(null, "Please provide a valid customer name", "Invalid Customer Name", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Make the call:
        String editCustomersJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Customers", "Update",
                "SessionID=" + sessionID + "&ID=" + c.getID() + "&Name=" + c.getName() + "&CountryID=" + c.getCountryID()
                + "&City=" + c.getCity() + "&Address=" + c.getAddress() + "&Telephone=" + c.getTelephone() + "&CustomerProductsID=" + c.getCustomerProductsID()
        );

        try {
            JSONObject jsonObject = new JSONObject(editCustomersJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, "Customers " + c.getName() + " saved.", title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Update table:
        getCustomers();
        customersTab();
    }

    private void editProducts(Products p) {
        //Get field values:

        //Check if the name is valid
        if (p.getName().trim().isEmpty()) {
            showMessageDialog(null, "Please provide a valid product", "Invalid Product", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Make the call:
        String editProductsJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Products", "Update",
                "SessionID=" + sessionID + "&ID=" + p.getID() + "&Name=" + p.getName() + "&Price=" + p.getPrice()
                + "&QuantityInStock=" + p.getQuantityInStock() + "&ProductSizeID=" + p.getProductSizeID()
                + "&ProductTypeID=" + p.getProductTypeID() + "&ProductSuppliesID=" + p.getProductSuppliesID()
        );
        try {
            JSONObject jsonObject = new JSONObject(editProductsJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, "Products " + p.getName() + " saved.", title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Update table:
        getProducts();
        productsTab();
    }

    private void editSupplies(Supplies s) {
        //Get field values:

        //Check if the name is valid
        if (s.getName().trim().isEmpty()) {
            showMessageDialog(null, "Please provide a valid supply", "Invalid Supply", JOptionPane.PLAIN_MESSAGE);
            return;
        }

        //Make the call:
        String editSuppliesJSON = HTTPConnection.executePost(HTTPConnection.API_URL, "Supplies", "Update",
                "SessionID=" + sessionID + "&ID=" + s.getID() + "&Name=" + s.getName() + "&SupplierID=" + s.getSupplierID()
                + "&Quantity=" + s.getQuantity() + "&Price=" + s.getPrice()
        );
        try {
            JSONObject jsonObject = new JSONObject(editSuppliesJSON);
            final String status = jsonObject.getString("Status");
            final String title = jsonObject.getString("Title");
            final String message = jsonObject.getString("Message");

            if (status.equals(HTTPConnection.RESPONSE_OK)) {
                showMessageDialog(null, "Supplies " + s.getName() + " saved.", title, JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Update table:
        getSupplies();
        suppliesTab();
    }

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
            java.util.logging.Logger.getLogger(MainForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JMenuItem AdminForm;
    private javax.swing.JMenu Forms;
    private javax.swing.JMenu Menu;
    private javax.swing.JMenuItem SetUpForm;
    private javax.swing.JMenuItem SetUpForm1;
    private javax.swing.JMenuItem SetUpForm2;
    private javax.swing.JMenuItem SetUpForm3;
    private javax.swing.JMenuItem SetUpForm4;
    private javax.swing.JButton addBottlesQuantityButton;
    private javax.swing.JSpinner bottleQuantitySpinner;
    private javax.swing.JComboBox<String> bottleSizeComboBox;
    private javax.swing.JScrollPane custProScrollPanel;
    private javax.swing.JPanel custProductsPanel;
    private javax.swing.JPanel customerDetailsPanel;
    private javax.swing.JTable customerDetailsTable;
    private javax.swing.JTable customerProductsTable;
    private javax.swing.JScrollPane customerScrollPanel;
    private javax.swing.JTabbedPane customerTabPanel;
    private javax.swing.JPanel customers;
    private javax.swing.JPanel customersGraphsPanel;
    private javax.swing.JPanel customersGraphsPanel1;
    private javax.swing.JButton customersSaveButton;
    private javax.swing.JTable dailyProductionTable;
    private javax.swing.JTable dailyPurchasesTable;
    private javax.swing.JTable dailySalesTable;
    private org.jdesktop.swingx.JXDatePicker dateOfProduction;
    private javax.swing.JButton deletRow;
    private javax.swing.JButton deleteCustomerButton;
    private javax.swing.JButton deleteCustomerProductsButton;
    private javax.swing.JButton deleteProductionButton;
    private javax.swing.JButton deleteProductsButton;
    private javax.swing.JButton deleteSupplierButton;
    private javax.swing.JButton deleteSuppliesButton;
    private javax.swing.JButton deleteSupplyButton;
    private javax.swing.JPanel dialyProduction;
    private javax.swing.JPanel employeeListPanel;
    private javax.swing.JScrollPane employeeScrollPanel;
    private javax.swing.JPanel employees;
    private javax.swing.JButton employeesSaveButton;
    private javax.swing.JTable employeesTable;
    private javax.swing.JButton exportPurchFilesBtn;
    private javax.swing.JLabel hireDateLabel;
    private javax.swing.JPanel home;
    private javax.swing.JButton importCustomerBtn;
    private javax.swing.JButton importEmplBtn;
    private javax.swing.JButton importProductsButton;
    private javax.swing.JButton importPurchaseBtn;
    private javax.swing.JButton importSupplierBtn;
    private javax.swing.JButton importSuppliesButton;
    private javax.swing.JTabbedPane invenrotyTabPanel;
    private javax.swing.JPanel inventory;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel latestPurchases;
    private javax.swing.JPanel latestSales;
    private javax.swing.JMenuItem logoutMenuItem;
    private javax.swing.JLabel noCustomerLb;
    private javax.swing.JLabel noEmployeesLb;
    private javax.swing.JLabel noSuppliesLb;
    private javax.swing.JLabel numOfCustomersLabel;
    private javax.swing.JLabel numOfEmployeesLabel;
    private javax.swing.JButton printBtn;
    private javax.swing.JButton printCusDetailsBtn;
    private javax.swing.JButton printCustProductsBtn;
    private javax.swing.JButton printPurchasesBtn;
    private javax.swing.JButton printSalesDetailsBtn;
    private javax.swing.JButton printSupDetailsBtn1;
    private javax.swing.JButton printSuppliesBtn;
    private javax.swing.JComboBox<String> productComboBox;
    private javax.swing.JLabel productNameTextField;
    private javax.swing.JLabel productNameTextField1;
    private javax.swing.JLabel productNameTextField2;
    private javax.swing.JLabel productNameTextField3;
    private javax.swing.JPanel production;
    private javax.swing.JTable productionDetailsTable;
    private javax.swing.JSpinner productionQuantitySpinner;
    private javax.swing.JScrollPane productionScrollPane;
    private javax.swing.JScrollPane productionScrollPanel;
    private javax.swing.JPanel productsDetailsPanel;
    private javax.swing.JTable productsDetailsTable;
    private javax.swing.JButton productsSaveButton;
    private javax.swing.JScrollPane productsScrollPanel;
    private javax.swing.JPanel productsTab;
    private javax.swing.JScrollPane purchHistoryScrollPanel;
    private javax.swing.JTable purchHistoryTable;
    private javax.swing.JPanel purchasesHistoryPanel;
    private javax.swing.JScrollPane purchasesScrollPanel;
    private javax.swing.JButton refreshCusDetailsBtn;
    private javax.swing.JButton refreshCustProductsBtn;
    private javax.swing.JButton refreshEmployeesBtn;
    private javax.swing.JButton refreshProductionButton;
    private javax.swing.JButton refreshProductsTable;
    private javax.swing.JButton refreshPurchasesTableBtn;
    private javax.swing.JButton refreshSalesButton;
    private javax.swing.JButton refreshSuplTableBtn;
    private javax.swing.JButton refreshSuppliesTable;
    private javax.swing.JButton refreshSuppliesTableBtn;
    private javax.swing.JScrollPane saleScrollPanel;
    private javax.swing.JPanel sales;
    private javax.swing.JTable salesDetailsTable;
    private javax.swing.JScrollPane salesScrollPanel;
    private javax.swing.JTextField searchCustProductsTxt;
    private javax.swing.JTextField searchCustomerTxt;
    private javax.swing.JTextField searchProduction;
    private javax.swing.JTextField searchProducts;
    private javax.swing.JTextField searchPurchasesTxt;
    private javax.swing.JTextField searchSale;
    private javax.swing.JTextField searchSupplierTxt;
    private javax.swing.JTextField searchSupplies;
    private javax.swing.JTextField searchSuppliesTxt;
    private javax.swing.JTextField searchTxt;
    private javax.swing.JPanel statistics;
    private javax.swing.JTable supplierDetailsTable;
    private javax.swing.JScrollPane supplierScrollPanel;
    private javax.swing.JPanel suppliers;
    private javax.swing.JPanel suppliersDetailsPanel1;
    private javax.swing.JButton suppliersSaveButton;
    private javax.swing.JPanel suppliesDetailsPanel;
    private javax.swing.JTable suppliesDetailsTable;
    private javax.swing.JPanel suppliesInvTabs;
    private javax.swing.JPanel suppliesPanel;
    private javax.swing.JButton suppliesSaveButton;
    private javax.swing.JScrollPane suppliesScrollPanel;
    private javax.swing.JTabbedPane suppliesTab;
    private javax.swing.JScrollPane suppliesTabPane;
    private javax.swing.JPanel suppliesTabPanel;
    private javax.swing.JTable suppliesTable;
    private javax.swing.JTabbedPane tabPanel;
    // End of variables declaration//GEN-END:variables
}
