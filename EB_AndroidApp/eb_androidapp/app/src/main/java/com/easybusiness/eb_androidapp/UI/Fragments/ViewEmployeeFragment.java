package com.easybusiness.eb_androidapp.UI.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.easybusiness.eb_androidapp.AsyncTask.AsyncTasks;
import com.easybusiness.eb_androidapp.AsyncTask.GetCountriesAsyncTask;
import com.easybusiness.eb_androidapp.Entities.Users;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.MainActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static com.easybusiness.eb_androidapp.UI.MainActivity.PREFERENCE_SESSIONID;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewEmployeeFragment extends Fragment {

    public static final String TAG = "ViewEmployeeFragment";

    public static final String EMPLOYEE_KEY = "employee-key";

    private View v;
    private String title = "Employee";
    public static Users user;

    private View layout;
    private ProgressBar progressBar;

    private TextView positionTextview;
    private TextView usernameTextview;
    private TextView cityTextview;
    private TextView addressTextview;
    private TextView countryTextview;
    private TextView dateHiredTextview;
    private TextView nameTextview;
    private TextView telephoneTextview;

    private Button editButton;
    private Button toPDFButton;

    public ViewEmployeeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_employee, container, false);

        layout = v.findViewById(R.id.view_single_employee_layout);
        progressBar = v.findViewById(R.id.view_single_employee_progress);

        positionTextview = v.findViewById(R.id.viewEmployee_position_textview);
        usernameTextview = v.findViewById(R.id.viewEmployee_username_textview);
        cityTextview = v.findViewById(R.id.viewEmployee_city_textview);
        addressTextview = v.findViewById(R.id.viewEmployee_address_textview);
        countryTextview = v.findViewById(R.id.viewEmployee_country_textview);
        dateHiredTextview = v.findViewById(R.id.viewEmployee_datehired_textview);
        nameTextview = v.findViewById(R.id.viewEmployee_name);
        telephoneTextview = v.findViewById(R.id.viewEmployee_telephone_textview);

        editButton = v.findViewById(R.id.viewEmployee_editButton);
        toPDFButton = v.findViewById(R.id.viewEmployee_ToPDFButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(EMPLOYEE_KEY, user);

                Fragment newFragment = new EditEmployeesFragment();
                newFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_left_to_right, R.anim.slide_right_to_left, R.anim.slide_left_to_right, R.anim.slide_right_to_left);
                fragmentTransaction.replace(R.id.frame, newFragment, EditEmployeesFragment.TAG);
                fragmentTransaction.addToBackStack(newFragment.getTag());
                fragmentTransaction.commit();
            }
        });

        toPDFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "TODO", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        final String SESSION_ID = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(PREFERENCE_SESSIONID, "");

        new GetCountriesAsyncTask("SessionID=" + SESSION_ID, getActivity(), v).execute();


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = (Users) bundle.getSerializable(EMPLOYEE_KEY);
            title = user.getFirstname() + " " + user.getLastname();
        }

        MainActivity activity = ((MainActivity) getActivity());
        String positionName = activity.getUserLevelNameFromID(user.getUserLevelID());
        String countryName = activity.getCountryFromCountryID(user.getCountryID());
        String formattedDate = MainActivity.DATE_FORMAT.format(new Date(user.getDateHired()));

        getActivity().setTitle(title);
        usernameTextview.setText(user.getUsername());
        positionTextview.setText(positionName);
        cityTextview.setText(user.getCity());
        addressTextview.setText(user.getAddress());
        countryTextview.setText(countryName);
        dateHiredTextview.setText(formattedDate);
        nameTextview.setText(title);
        telephoneTextview.setText(user.getTelephone());

        System.out.println("----------- ON RESUME CALLED ------------");

        new GetSingleEmployeeAsyncTask(getActivity(), "UserID=" + user.getUserID() + "&SessionID=" + SESSION_ID).execute();

    }

    private class GetSingleEmployeeAsyncTask extends AsyncTask<Void, Void, Void> {

        private String responseData;
        private String query;
        private Activity activity;

        public GetSingleEmployeeAsyncTask(Activity activity, String query) {
            this.query = query;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });


            if (query == null) query = "";

            try {
                URL url = new URL(AsyncTasks.encodeForAPI(activity.getString(R.string.baseURL), "Users", "GetByID"));
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
                    System.out.println(responseData);

                    final String status = outterObject.getString("Status");
                    final String title = outterObject.getString("Title");
                    final String message = outterObject.getString("Message");

                    if (status.equals(AsyncTasks.RESPONSE_OK)) {
                        JSONObject jsonObject = outterObject.getJSONObject("Data");
                        String firstName = jsonObject.getString("Firstname");
                        String lastName = jsonObject.getString("Lastname");
                        String telephone = jsonObject.getString("Telephone");
                        int userlevelID = jsonObject.getInt("UserLevelID");
                        int id = jsonObject.getInt("UserID");
                        int dateHired = jsonObject.getInt("DateHired");
                        String city = jsonObject.getString("City");
                        String address = jsonObject.getString("Address");
                        int countryID = jsonObject.getInt("CountryID");
                        String username = jsonObject.getString("Username");

                        user = new Users(id, username, "", userlevelID, firstName, lastName, dateHired, city, address, telephone, countryID);

                        MainActivity mainActivity = (MainActivity) activity;
                        final String positionName = mainActivity.getUserLevelNameFromID(userlevelID);
                        final String countryName = mainActivity.getCountryFromCountryID(countryID);
                        final String formattedDate = MainActivity.DATE_FORMAT.format(new Date(dateHired));


                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                usernameTextview.setText(user.getUsername());
                                nameTextview.setText(user.getFirstname() + " " + user.getLastname());
                                activity.setTitle(user.getFirstname() + " " + user.getLastname());
                                positionTextview.setText(positionName);
                                cityTextview.setText(user.getCity());
                                addressTextview.setText(user.getAddress());
                                countryTextview.setText(countryName);
                                dateHiredTextview.setText(formattedDate);
                                telephoneTextview.setText(user.getTelephone());
                            }
                        });


                    } else if (status.equals(AsyncTasks.RESPONSE_ERROR)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog alertDialog = AsyncTasks.createGeneralErrorDialog(activity, title, message);
                                alertDialog.show();
                            }
                        });
                    }

                    //CONNECTION ERROR
                    else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog alertDialog = AsyncTasks.createConnectionErrorDialog(activity);
                                alertDialog.show();
                            }
                        });
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });

            return null;

        }

    }

}
