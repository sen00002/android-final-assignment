package mad9132.maddapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import mad9132.maddapp.model.CoursePOJO;

import static mad9132.maddapp.MainActivity.REQUEST_EDIT_COURSE;

/**
 * CourseAdapter.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 *
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    public static final String  COURSE_KEY = "course_key";
    private static final String TAG        = "CourseAdapter";

    private Activity              mContext;
    private ArrayList<CoursePOJO> mCourses;

    public CourseAdapter(Activity context, String buildingsJSON) {
        this.mContext = context;

        Gson gson = new Gson();
        mCourses = new ArrayList<>(Arrays.asList(gson.fromJson(buildingsJSON, CoursePOJO[].class)));
    }

    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View planetView = inflater.inflate(R.layout.course_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(planetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CourseAdapter.ViewHolder holder, final int position) {
        final CoursePOJO aCourse = mCourses.get(position);

        holder.tvCode.setText(aCourse.getCode());
        holder.tvName.setText(aCourse.getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(COURSE_KEY, aCourse);
                mContext.startActivity(intent);
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if ( (holder.bDeleteCourse.getVisibility() == View.VISIBLE)
                        && (holder.bEditCourse.getVisibility() == View.VISIBLE)) {
                    holder.bDeleteCourse.setVisibility(View.INVISIBLE);
                    holder.bEditCourse.setVisibility(View.INVISIBLE);
                } else {
                    holder.bDeleteCourse.setVisibility(View.VISIBLE);
                    holder.bEditCourse.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });

        holder.bDeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                // TODO - externalize strings to strings.xml
                builder.setTitle("Confirm")
                    .setMessage("Delete " + aCourse.getCode() + " - " + aCourse.getName() + "?")

                        // Displays: OK
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Delete this course
                            Toast.makeText(mContext, "Deleted Course: " + aCourse.getCode(), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Deleted Course: " + aCourse.getCode());
                            holder.bDeleteCourse.setVisibility(View.INVISIBLE);
                            holder.bEditCourse.setVisibility(View.INVISIBLE);

                            // TODO - for Doors Open app, replace these two lines:
                            mCourses.remove(position);
                            CourseAdapter.this.notifyDataSetChanged();
                            // With call to intent service to DELETE /buildings/:id
                            //
                            // RequestPackage requestPackage = new RequestPackage();
                            // requestPackage.setMethod( HttpMethod.DELETE );
                            // requestPackage.setEndPoint( JSON_URL + aBuilding.getBuildingId() );

                            // Intent intent = new Intent(this, MyService.class);
                            // intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
                            // mContext.startService(intent);

                            dialog.dismiss();
                    }
                    })

                        // Displays: Cancel
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                            dialog.dismiss();

                            Toast.makeText(mContext, "CANCELLED: Deleted Course: " + aCourse.getCode(), Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "CANCELLED: Deleted Course: " + aCourse.getCode());
                        }
                    });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        holder.bEditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Edit Course: " + aCourse.getCode(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, EditCourseActivity.class);
                intent.putExtra(COURSE_KEY, aCourse);
                mContext.startActivityForResult(intent, REQUEST_EDIT_COURSE);
                holder.bDeleteCourse.setVisibility(View.INVISIBLE);
                holder.bEditCourse.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public void addCourse(CoursePOJO newCourse) {
        mCourses.add(newCourse);
        notifyDataSetChanged();
        Log.i(TAG, "Added Course: " + newCourse.getName());
    }

    public void updateCourse(CoursePOJO updatedCourse) {
        int index = mCourses.indexOf(updatedCourse);
        if (index >= 0) {
            mCourses.set(index, updatedCourse);
            notifyDataSetChanged();
            Log.i(TAG, "Updated Course: " + updatedCourse.getCode());
        }
    }

    public String getCourseDataAsString() {
        return new Gson().toJson(mCourses);
    }

    public void setCourseDataWithString(String courseDataJSON) {
        Gson gson = new Gson();
        mCourses.clear();
        mCourses.addAll(Arrays.asList(gson.fromJson(courseDataJSON, CoursePOJO[].class)));
        this.notifyDataSetChanged();
        Log.i(TAG, "Reverting to.... Original list of courses");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View        mView;
        public TextView    tvCode;
        public TextView    tvName;
        public ImageButton bEditCourse;
        public ImageButton bDeleteCourse;

        public ViewHolder(View courseView) {
            super(courseView);

            mView = courseView;

            tvCode = (TextView) courseView.findViewById(R.id.courseCodeText);
            tvName = (TextView) courseView.findViewById(R.id.courseNameText);
            bEditCourse = (ImageButton) courseView.findViewById(R.id.editCourseButton);
            bDeleteCourse = (ImageButton) courseView.findViewById(R.id.deleteCourseButton);
        }
    }
}
