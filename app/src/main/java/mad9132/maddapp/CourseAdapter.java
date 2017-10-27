package mad9132.maddapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import mad9132.maddapp.model.CoursePOJO;

/**
 * CourseAdapter.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 *
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    public static final String COURSE_KEY = "course_key";

    private Context               mContext;
    private ArrayList<CoursePOJO> mCourses;

    public CourseAdapter(Context context, String buildingsJSON) {
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
    public void onBindViewHolder(CourseAdapter.ViewHolder holder, final int position) {
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
                Toast.makeText(mContext, "DELETING Course: " + aCourse.getCode(), Toast.LENGTH_SHORT).show();
                mCourses.remove(position);
                CourseAdapter.this.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public String getCourseDataAsString() {
        return new Gson().toJson(mCourses);
    }

    public void setCourseDataWithString(String courseDataJSON) {
        Gson gson = new Gson();
        mCourses.clear();
        mCourses.addAll(Arrays.asList(gson.fromJson(courseDataJSON, CoursePOJO[].class)));
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View     mView;
        public TextView tvCode;
        public TextView tvName;

        public ViewHolder(View courseView) {
            super(courseView);

            mView = courseView;

            tvCode = (TextView) courseView.findViewById(R.id.courseCodeText);
            tvName = (TextView) courseView.findViewById(R.id.courseNameText);
        }
    }
}
