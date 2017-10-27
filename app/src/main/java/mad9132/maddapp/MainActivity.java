package mad9132.maddapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import mad9132.maddapp.utils.JSONFileManager;

/**
 * MAD&D App: list the courses of the MAD&D program.
 *
 * Features:
 * a) read / write JSON data using local storage
 * b) RecyclerView + Adapter + ViewHolder
 */
public class MainActivity extends Activity {
    private static final String JSON_FILE = "courses.json";

    private CourseAdapter mAdapter;
    private RecyclerView mRecyclerView;

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
        String courseDataJSON;
        if ( JSONFileManager.exitsAsLocalFile(this, JSON_FILE) ) {
            courseDataJSON = JSONFileManager.readJSON(this, JSON_FILE);
        } else{
            courseDataJSON = JSONFileManager.readJSON(this, R.raw.courses);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rvCourses);
        mAdapter = new CourseAdapter(this, courseDataJSON);
        mRecyclerView.setAdapter(mAdapter);
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

            case R.id.action_reset:
                String originalCourseDataJSON = JSONFileManager.readJSON(this, R.raw.courses);
                mAdapter.setCourseDataWithString(originalCourseDataJSON);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        /* Write the course data as a JSON string to local storage */
        boolean isSuccess = JSONFileManager.writeJSON(this, JSON_FILE, mAdapter.getCourseDataAsString());

        if ( ! isSuccess) {
            Toast.makeText(this, "Error: writing to local file: " + JSON_FILE, Toast.LENGTH_LONG).show();
        }
        super.onStop();
    }
}
