package mad9132.maddapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import mad9132.maddapp.model.CoursePOJO;


/**
 * Detailed Activity of a Course.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 */
public class DetailActivity extends Activity {

    private TextView tvCode;
    private TextView tvDescription;
    private TextView tvLevel;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        CoursePOJO selectedCourse = getIntent().getExtras().getParcelable(CourseAdapter.COURSE_KEY);
        if (selectedCourse == null) {
            throw new AssertionError("Null data item received!");
        }

        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvCode = (TextView) findViewById(R.id.tvEditCourse);
        tvLevel = (TextView) findViewById(R.id.tvLevel);

        tvCode.setText(selectedCourse.getCode());
        tvName.setText(selectedCourse.getName());
        tvDescription.setText(String.format(getResources().getString(R.string.descriptionFormat), selectedCourse.getDescription()));
        tvLevel.setText(String.format(getResources().getString(R.string.levelFormat), selectedCourse.getLevel()));
    }
}