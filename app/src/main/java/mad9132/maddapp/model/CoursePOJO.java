package mad9132.maddapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO class for a Course JSON object.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 */
public class CoursePOJO implements Parcelable {

    public static final Creator<CoursePOJO> CREATOR = new Creator<CoursePOJO>() {
        @Override
        public CoursePOJO createFromParcel(Parcel source) {
            return new CoursePOJO(source);
        }

        @Override
        public CoursePOJO[] newArray(int size) {
            return new CoursePOJO[size];
        }
    };
    private int courseId;
    private String code;
    private String description;
    private int level;
    private String name;

    public CoursePOJO() {
    }

    protected CoursePOJO(Parcel in) {
        this.courseId = in.readInt();
        this.code = in.readString();
        this.description = in.readString();
        this.level = in.readInt();
        this.name = in.readString();
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.courseId);
        dest.writeString(this.code);
        dest.writeString(this.description);
        dest.writeInt(this.level);
        dest.writeString(this.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoursePOJO that = (CoursePOJO) o;

        return courseId == that.courseId;

    }

    @Override
    public int hashCode() {
        return courseId;
    }
}
