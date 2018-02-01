package mad9132.maddapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mad9132.maddapp.MyServices.MyService;
import mad9132.maddapp.model.CoursePOJO;
import mad9132.maddapp.utils.HttpMethod;
import mad9132.maddapp.utils.RequestPackage;

import static mad9132.maddapp.MainActivity.JSON_URI_SERVER;
import static mad9132.maddapp.MainActivity.NEW_COURSE_DATA;


/**
 * New Course Activity.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 */
public class NewCourseActivity extends Activity {

    private EditText newCodeEditText;
    private EditText newDescriptionEditText;
    private EditText newLevelEditText;
    private EditText newNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_course);


        newCodeEditText = (EditText) findViewById(R.id.editCourseCode);
        newDescriptionEditText = (EditText) findViewById(R.id.editCourseDescription);
        newLevelEditText = (EditText) findViewById(R.id.editCourseLevel);
        newNameEditText = (EditText) findViewById(R.id.editCourseName);

        Button saveButton = findViewById(R.id.saveEditCourse);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newCode = newCodeEditText.getText().toString();
                String newDescription = newDescriptionEditText.getText().toString();
                String newLevelString = newLevelEditText.getText().toString();
                String newName = newNameEditText.getText().toString();

                // Validation Rule: all fields are required
                if (newCode.isEmpty()) {
                    newCodeEditText.setError("Please Enter the Course Code: MADnnnn");
                    newCodeEditText.requestFocus();
                    return;
                }

                if (newName.isEmpty()) {
                    newNameEditText.setError("Please Enter the Course Name");
                    newNameEditText.requestFocus();
                    return;
                }

                if (newLevelString.isEmpty()) {
                    newLevelEditText.setError("Please Enter the Course Level: 1 to 4");
                    newLevelEditText.requestFocus();
                    return;
                }

                if (newDescription.isEmpty()) {
                    newDescriptionEditText.setError("Please Enter the Course Description.");
                    newDescriptionEditText.requestFocus();
                    return;
                }

                // Validation Rule: course code's pattern is: MADnnn
                if (newCode.matches("[M][A][D][0-9][0-9][0-9][0-9]") == false) {
                    newCodeEditText.setError("Please Enter the Course Code: MADnnnn");
                    newCodeEditText.requestFocus();
                    return;
                }

                // Validation Rule: course level is in range: 1 - 4 (inclusive)
                int newLevel = 0;
                try {
                    newLevel = Integer.parseInt(newLevelString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if ((newLevel < 1) || (newLevel > 4)) {
                    newLevelEditText.setError("Please Enter the Course Level: 1 to 4");
                    newLevelEditText.requestFocus();
                }

                CoursePOJO newCourse = new CoursePOJO();
                newCourse.setCode(newCode);
                newCourse.setName(newName);
                newCourse.setDescription(newDescription);
                newCourse.setLevel(newLevel);

                RequestPackage requestPackage = new RequestPackage();
                requestPackage.setMethod(HttpMethod.POST);
                requestPackage.setEndPoint(JSON_URI_SERVER);
                requestPackage.setParam("courseId", String.valueOf(newCourse.getCourseId()));
                requestPackage.setParam("code", newCourse.getCode());
                requestPackage.setParam("name", newCourse.getName());
                requestPackage.setParam("description", newCourse.getDescription());
                requestPackage.setParam("level", String.valueOf(newCourse.getLevel()));


                Intent intent = new Intent(getApplicationContext(), MyService.class);
                intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
                intent.putExtra(NEW_COURSE_DATA, newCourse);
                setResult(RESULT_OK, intent);
                startService(intent);

//                intent = new Intent();
//                intent.putExtra(NEW_COURSE_DATA, newCourse);
//                setResult(RESULT_OK, intent);
                finish();
            }
        });


        // Cancel button
        Button cancelButton = findViewById(R.id.cancelEditCourse);
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