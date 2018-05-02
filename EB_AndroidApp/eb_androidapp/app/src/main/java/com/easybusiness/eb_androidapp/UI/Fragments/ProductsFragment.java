package com.easybusiness.eb_androidapp.UI.Fragments;


import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;


import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.Entities.Products;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Adapters.ProductAdapter;
import com.easybusiness.eb_androidapp.UI.Dialogs;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    public static final String TAG = "ViewProductsFragment";
    public static final String TITLE = "View Products";

    private SearchView searchView;
    private ListView productListView;
    private ImageButton addProductBtn;
    private Button refreshButton;
    private static ProductAdapter allProductsAdapter;
    private View v;
    private ProgressBar progressBar;
    private View layout;

    private ArrayList<Products> productsList;

    public ProductsFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_products, container, false);

        progressBar = v.findViewById(R.id.view_products_progress);
        layout = v.findViewById(R.id.view_products_layout);

        productListView = v.findViewById(R.id.productsList);
        searchView = v.findViewById(R.id.products_search_view);
        addProductBtn = v.findViewById(R.id.addProductButton);
        refreshButton = v.findViewById(R.id.refresh_products);

        //VIEW (Short click)
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                MainActivity mainActivity = (MainActivity) getActivity();
                bundle.putInt(ViewProductFragment.PRODUCT_ID_KEY, mainActivity.PRODUCT_DATA.get(i).getID());
                bundle.putString(ViewProductFragment.PRODUCT_NAME_KEY, mainActivity.PRODUCT_DATA.get(i).getName());
                bundle.putString(ViewProductFragment.PRODUCT_PRICE, String.valueOf(mainActivity.PRODUCT_DATA.get(i).getPrice()));
                bundle.putString(ViewProductFragment.PRODUCT_QUANTITY_IN_STOCK, String.valueOf(mainActivity.PRODUCT_DATA.get(i).getQuantityInStock()));
                bundle.putString(ViewProductFragment.PRODUCT_SIZE,String.valueOf(mainActivity.PRODUCT_DATA.get(i).getProductSizeID()));
                bundle.putString(ViewProductFragment.PRODUCT_TYPE, String.valueOf(mainActivity.PRODUCT_DATA.get(i).getProductTypeID()));
                bundle.putInt(ViewProductFragment.PRODUCT_SIZE_ID, mainActivity.PRODUCT_DATA.get(i).getProductSizeID());
                bundle.putInt(ViewProductFragment.PRODUCT_TYPE_ID, mainActivity.PRODUCT_DATA.get(i).getProductTypeID());

                Fragment newFragment = new ViewProductFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, ViewProductFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
            }
        });

        //DELETE (Long click)
        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity mainActivity = (MainActivity) getActivity();
                AlertDialog dialog = Dialogs.createDeleteDialog(getActivity(), view, "Products", mainActivity.PRODUCT_DATA.get(i).getID(), mainActivity.PRODUCT_DATA.get(i).getName(), new ProductsFragment());
                dialog.show();
                v.invalidate();
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
                String query = builder.build().getEncodedQuery();
                new GetProductsAsyncTask(query, getActivity(), v).execute();
                return true;
            }
        });
        //Make list view searchable:
        productListView.setTextFilterEnabled(true);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
                String query = builder.build().getEncodedQuery();
                new GetProductsAsyncTask(query, getActivity(), v).execute();
            }
        });

        //Searchview properties:
        setupSearchView();

        return v;
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (TextUtils.isEmpty(s)) {
                    productListView.clearTextFilter();
                } else {
                    productListView.setFilterText(s);
                }
                return true;
            }
        });
        searchView.setQueryHint("Search...");
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle(TITLE);

        addProductBtn = v.findViewById(R.id.addProductButton);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new AddProductFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                getActivity().setTitle(AddProductFragment.TITLE);
                fragmentTransaction.replace(R.id.frame, newFragment, AddProductFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
                ((MainActivity) getActivity()).setMenuItemChecked(newFragment);
            }
        });

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sessionID = sharedPreferences.getString(MainActivity.PREFERENCE_SESSIONID, "None");

        Uri.Builder builder = new Uri.Builder().appendQueryParameter("SessionID", sessionID);
        String query = builder.build().getEncodedQuery();

        new GetProductsAsyncTask(query, getActivity(), v).execute();

    }

    public class GetProductsAsyncTask extends AsyncTask<Void,Void,Void> {

        private String query;
        private String responseData;

        public GetProductsAsyncTask(String query, Activity activity, View view) {
            this.query = query;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(getActivity().getString(R.string.baseURL), "Products", "GetMultiple"));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                byte[] outputBytes = query.getBytes("UTF-8");
                urlConnection.setRequestMethod("POST");
                urlConnection.connect();
                OutputStream os = urlConnection.getOutputStream();
                os.write(outputBytes);
                os.close();
                int statusCode = urlConnection.getResponseCode();
                urlConnection.disconnect();

                //OK
                if (statusCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    responseData = AsyncTasks.convertStreamToString(inputStream);

                    JSONObject outterObject = new JSONObject(responseData);
                    final String status = outterObject.getString("Status");
                    final String title = outterObject.getString("Title");
                    final String message = outterObject.getString("Message");

                    if (status.equals(AsyncTasks.RESPONSE_OK)) {
                        productsList = new ArrayList<>();
                        JSONArray dataArray = outterObject.getJSONArray("Data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject jsonObject = dataArray.getJSONObject(i);
                            int id = jsonObject.getInt("ID");
                            String name = jsonObject.getString("Name");
                            double price = jsonObject.getDouble("Price");
                            int productSizeID = jsonObject.getInt("ProductSizeID");
                            int productTypeID = jsonObject.getInt("ProductTypeID");
                            int productSuppliesID = jsonObject.getInt("ProductSuppliesID");
                            int quantityInStock = jsonObject.getInt("QuantityInStock");
                            Products p = new Products(id, name, price, quantityInStock, productSizeID, productTypeID, productSuppliesID);
                            productsList.add(p);
                        }

                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.PRODUCT_DATA = productsList;

                        String [] items = new String[productsList.size()];
                        for (int i = 0; i < items.length; i++)
                            items[i] = productsList.get(i).getName();
                        final ProductAdapter productAdapter = new ProductAdapter(getActivity(), productsList);
                        allProductsAdapter = new ProductAdapter(getActivity(), productsList);

                        final ListView productsListview = getActivity().findViewById(R.id.productsList);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                productsListview.setAdapter(productAdapter);
                                progressBar.setVisibility(View.GONE);
                                layout.setVisibility(View.VISIBLE);
                            }
                        });


                    }
                    else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        final android.app.AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(getActivity(), title, message);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.show();
                            }
                        });
                    }


                }
                //CONNECTION ERROR
                else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final android.app.AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(getActivity());
                            alertDialog.show();
                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }

}
