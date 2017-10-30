package mad9132.maddapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mad9132.maddapp.model.CoursePOJO;

import static mad9132.maddapp.MainActivity.EDIT_COURSE_DATA;
import static mad9132.maddapp.MainActivity.NEW_COURSE_DATA;


/**
 * Edit Course Activity.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 */
public class EditCourseActivity extends Activity {

    private TextView codeEditText;
    private EditText descriptionEditText;
    private EditText levelEditText;
    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        final CoursePOJO selectedCourse = getIntent().getExtras().getParcelable(CourseAdapter.COURSE_KEY);
        if (selectedCourse == null) {
            throw new AssertionError("Null data item received!");
        }

        codeEditText = (EditText) findViewById(R.id.editCourseCode);
        descriptionEditText = (EditText) findViewById(R.id.editCourseDescription);
        levelEditText = (EditText) findViewById(R.id.editCourseLevel);
        nameEditText = (EditText) findViewById(R.id.editCourseName);

        // Prevent User from editing Course Code
        codeEditText.setEnabled(false);

        codeEditText.setText(selectedCourse.getCode());
        descriptionEditText.setText(selectedCourse.getDescription());
        levelEditText.setText(selectedCourse.getLevel() + "");
        nameEditText.setText(selectedCourse.getName());

        Button saveButton = findViewById(R.id.saveEditCourse);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = descriptionEditText.getText().toString();
                String levelString = levelEditText.getText().toString();
                String name = nameEditText.getText().toString();

                // Validation Rule: all fields are required
                if (name.isEmpty()) {
                    nameEditText.setError( "Please Enter the Course Name");
                    nameEditText.requestFocus();
                    return;
                }

                if (levelString.isEmpty()) {
                    levelEditText.setError( "Please Enter the Course Level: 1 to 4");
                    levelEditText.requestFocus();
                    return;
                }

                if (description.isEmpty()) {
                    descriptionEditText.setError( "Please Enter the Course Description.");
                    descriptionEditText.requestFocus();
                    return;
                }

                // Validation Rule: course level is in range: 1 - 4 (inclusive)
                int level = 0;
                try {
                    level = Integer.parseInt(levelString);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if ( (level < 1) || (level > 4) ) {
                    levelEditText.setError( "Please Enter the Course Level: 1 to 4");
                    levelEditText.requestFocus();
                }

                selectedCourse.setName( name );
                selectedCourse.setDescription( description );
                selectedCourse.setLevel( level );

                Intent intent = new Intent();
                intent.putExtra(EDIT_COURSE_DATA, selectedCourse);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

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