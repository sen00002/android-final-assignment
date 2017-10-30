package mad9132.maddapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import mad9132.maddapp.model.CoursePOJO;
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
    public static final String  NEW_COURSE_DATA = "NEW_COURSE_DATA";
    public static final String  EDIT_COURSE_DATA = "EDIT_COURSE_DATA";
    public static final int     REQUEST_NEW_COURSE = 1;
    public static final int     REQUEST_EDIT_COURSE = 2;

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

            case R.id.action_add_course:
                Intent intent = new Intent(this, NewCourseActivity.class);
                startActivityForResult(intent, REQUEST_NEW_COURSE);
                return true;

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

        if ( ! isSuccess ) {
            Toast.makeText(this, "Error: writing to local file: " + JSON_FILE, Toast.LENGTH_LONG).show();
        }
        super.onStop();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_COURSE) {
            if (resultCode == RESULT_OK) {
                CoursePOJO newCourse = data.getExtras().getParcelable(NEW_COURSE_DATA);
                Toast.makeText(this, "Added Course: " + newCourse.getName(), Toast.LENGTH_SHORT).show();
                mAdapter.addCourse(newCourse);
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
}
