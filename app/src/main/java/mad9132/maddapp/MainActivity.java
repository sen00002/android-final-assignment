package mad9132.maddapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import mad9132.maddapp.MyServices.MyService;
import mad9132.maddapp.model.CoursePOJO;
import mad9132.maddapp.utils.NetworkHelper;
import mad9132.maddapp.utils.RequestPackage;

/**
 * MAD&D App: basic CRUD app to manage the courses of the MAD&D program.
 * <p>
 * Features:
 * a) Master / Detail - list of courses, select a course for detail
 * a) CRUD app - Create a MAD Course, Read / Retrieve list of courses, Update a course, Delete a course
 * b) RecyclerView + Adapter + ViewHolder
 * c) persistent data: read / write JSON data using local storage
 * <p>
 * Usage:
 * a) Create a course - menu > Add New Course
 * b) Update a course - select & long-press to see edit option
 * c) Delete a course - select & long-press to see delete option
 * d) Course detail - select & click
 * e) Reset course data to original list - menu > Reset Courses
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 */
public class MainActivity extends Activity {

    public static final String TAG_DEBUG = "Debug";
    public static final String NEW_COURSE_DATA = "NEW_COURSE_DATA";
    public static final String EDIT_COURSE_DATA = "EDIT_COURSE_DATA";
    public static final int REQUEST_NEW_COURSE = 1;
    public static final int REQUEST_EDIT_COURSE = 2;
//    private static final String JSON_FILE = "courses.json";
    public static final String JSON_FILE = "http://madd.mybluemix.net/courses/";
    public static final String JSON_URI_LOCAL = "courses.json";
    public static final String JSON_URI_SERVER = "http://madd.mybluemix.net/courses/";
    private CourseAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private List<CoursePOJO> mBuildingsList;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG_DEBUG, "onReceive: get data");
            if (intent.hasExtra(MyService.MY_SERVICE_PAYLOAD)) {
                CoursePOJO[] buildingsArray = (CoursePOJO[]) intent
                        .getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
//                Toast.makeText(MainActivity.this,
//                        "Received " + buildingsArray.length + " buildings from service",
//                        Toast.LENGTH_SHORT).show();
                mBuildingsList = Arrays.asList(buildingsArray);
                displayBuildings();
            } else if (intent.hasExtra(MyService.MY_SERVICE_RESPONSE)) {
                CoursePOJO myBuilding = intent.getParcelableExtra(MyService.MY_SERVICE_RESPONSE);
            } else if (intent.hasExtra(MyService.MY_SERVICE_EXCEPTION)) {
                String message = intent.getStringExtra(MyService.MY_SERVICE_EXCEPTION);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Read the course data as a JSON string.
         * Does the local file exist?
         * Yes - read the data, and display it
         * No - read the initial data found in res/raw
         *
         * FYI... res/* is readable only. So... use res/raw to store orignal data, and local file
         * system for modified data.
         */

//        String courseDataJSON;
//        if (JSONFileManager.exitsAsLocalFile(this, JSON_FILE)) {
//            courseDataJSON = JSONFileManager.readJSON(this, JSON_FILE);
//        } else {
//            courseDataJSON = JSONFileManager.readJSON(this, R.raw.courses);
//        }

//        mRecyclerView = (RecyclerView) findViewById(R.id.rvCourses);
//        mAdapter = new CourseAdapter(this, courseDataJSON);
//        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvCourses);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));
        SharedPreferences settings = getSharedPreferences( getResources().getString(R.string.app_name), Context.MODE_PRIVATE );
        fetchBuildings();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_add_course:
                Intent intent = new Intent(this, NewCourseActivity.class);
                startActivityForResult(intent, REQUEST_NEW_COURSE);
                return true;

            case R.id.action_reset:
                fetchBuildings();
                //String originalCourseDataJSON = JSONFileManager.readJSON(this, R.raw.courses);
                //mAdapter.setCourseDataWithString(originalCourseDataJSON);
                return true;

            case  R.id.action_about:
                Toast.makeText(this, "Rajat Sen (00002)", Toast.LENGTH_SHORT).show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onStop() {
        /* Write the course data as a JSON string to local storage */
//        boolean isSuccess = JSONFileManager.writeJSON(this, JSON_FILE, mAdapter.getCourseDataAsString());
//
//        if (!isSuccess) {
//            Toast.makeText(this, "Error: writing to local file: " + JSON_FILE, Toast.LENGTH_LONG).show();
//        }
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_COURSE) {
            if (resultCode == RESULT_OK) {
                CoursePOJO newCourse = data.getExtras().getParcelable(NEW_COURSE_DATA);
                Toast.makeText(this, "Added Course: " + newCourse.getName(), Toast.LENGTH_SHORT).show();
                fetchBuildings();
                //mAdapter.addCourse(newCourse);
            }

            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled: Add New Course", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_EDIT_COURSE) {
            if (resultCode == RESULT_OK) {
                CoursePOJO updatedCourse = data.getExtras().getParcelable(EDIT_COURSE_DATA);
                Toast.makeText(this, "Updated Course: " + updatedCourse.getName(), Toast.LENGTH_SHORT).show();
                mAdapter.updateCourse(updatedCourse);
            }

            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled: Edit Course", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchBuildings(){
        Log.d(TAG_DEBUG, "fetchBuildings: Called");
        if (NetworkHelper.hasNetworkAccess(this)) {
            Log.d(TAG_DEBUG, "fetchBuildings: Has Network");
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(JSON_URI_SERVER);
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
            startService(intent);
            Log.d(TAG_DEBUG, "fetchBuildings:  after start Intent service");
        } else {
            Log.v(TAG_DEBUG, "fetchBuildings: No Network");
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayBuildings(){
        Log.v(TAG_DEBUG, "displayBuildings: " + " displayBuilding called.");
        if (mBuildingsList != null) {
        mAdapter = new CourseAdapter(this, mBuildingsList);
        mRecyclerView.setAdapter(mAdapter);
        }
    }


}
