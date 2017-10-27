package mad9132.maddapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mad9132.maddapp.model.CoursePOJO;

import static mad9132.maddapp.MainActivity.NEW_COURSE_DATA;


/**
 * New Course Activity.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 */
public class NewCourseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);

        Button saveButton = findViewById(R.id.bSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoursePOJO newCourse = new CoursePOJO();
                newCourse.setCode("MAD9021");
                newCourse.setName("Intro to Object-Oriented Programming");
                newCourse.setDescription("Let's learn OO!");
                newCourse.setLevel(2);

                Intent intent = new Intent();
                intent.putExtra(NEW_COURSE_DATA, newCourse);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button cancelButton = findViewById(R.id.bCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }
}