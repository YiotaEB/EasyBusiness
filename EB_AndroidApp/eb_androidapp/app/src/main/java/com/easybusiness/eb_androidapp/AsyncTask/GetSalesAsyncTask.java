//package com.easybusiness.eb_androidapp.AsyncTask;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.os.AsyncTask;
//import android.view.View;
//import android.widget.ListView;
//
//import com.easybusiness.eb_androidapp.Entities.Sales;
//import com.easybusiness.eb_androidapp.R;
//import com.easybusiness.eb_androidapp.UI.Adapters.SalesAdapter;
//import com.easybusiness.eb_androidapp.UI.MainActivity;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedInputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//
//
//public class GetSalesAsyncTask extends AsyncTask<Void,Void,Void> {
//
//    private String query;
//    private String responseData;
//    private Activity activity;
//    private View view;
//    ArrayList<Sales> sales = null;
//
//    public GetSalesAsyncTask(String query, Activity activity, View view) {
//        this.query = query;
//        this.activity = activity;
//        this.view = view;
//    }
//
//    @Override
//    protected Void doInBackground(Void... params) {
//
//        if (query == null) query = "";
//
//        try {
//            URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "Sales", "GetMultiple"));
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setDoOutput(true);
//            byte[] outputBytes = query.getBytes("UTF-8");
//            urlConnection.setRequestMethod("POST");
//            urlConnection.connect();
//            OutputStream os = urlConnection.getOutputStream();
//            os.write(outputBytes);
//            os.close();
//            int statusCode = urlConnection.getResponseCode();
//            urlConnection.disconnect();
//
//            //OK
//            if (statusCode == HttpURLConnection.HTTP_OK) {
//                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
//                responseData = AsyncTasks.convertStreamToString(inputStream);
//
//                JSONObject outterObject = new JSONObject(responseData);
//                final String status = outterObject.getString("Status");
//                final String title = outterObject.getString("Title");
//                final String message = outterObject.getString("Message");
//
//                if (status.equals(AsyncTasks.RESPONSE_OK)) {
//                    sales = new ArrayList<>();
//                    JSONArray dataArray = outterObject.getJSONArray("Data");
//                    for (int i = 0; i < dataArray.length(); i++) {
//                        JSONObject jsonObject = dataArray.getJSONObject(i);
//                        double tax = jsonObject.getDouble("Tax");
//                        int saleTimeDate = jsonObject.getInt("SaleTimeDate");
//                        int id = jsonObject.getInt("ID");
//                        int customerID = jsonObject.getInt("CustomerID");
//                        int saleProductsID = jsonObject.getInt("SaleProductsID");
//                        Sales p = new Sales(id, customerID, saleProductsID, tax, saleTimeDate);
//                        sales.add(p);
//                    }
//
////                    MainActivity mainActivity = (MainActivity) activity;
////                    mainActivity.SALES_DATA = sales;
//
//                    String [] items = new String[sales.size()];
//                    for (int i = 0; i < items.length; i++)
//                        items[i] = String.valueOf(sales.get(i).getCustomerID());
//                    final SalesAdapter salesAdapter = new SalesAdapter(activity, sales);
//
//                    final ListView salesListview = activity.findViewById(R.id.daily_sales_list_view);
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            salesListview.setAdapter(salesAdapter);
//                        }
//                    });
//
//
//                }
//                else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
//                    final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(activity, title, message);
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            alertDialog.show();
//                        }
//                    });
//                }
//
//
//            }
//            //CONNECTION ERROR
//            else {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(activity);
//                        alertDialog.show();
//                    }
//                });
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//}