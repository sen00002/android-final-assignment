package mad9132.maddapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO class for a Course JSON object.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 */
public class CoursePOJO implements Parcelable {

    private String code;
    private String name;
    private String description;
    private int level;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.level);
    }

    public CoursePOJO() {
    }

    protected CoursePOJO(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.level = in.readInt();
    }

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
}
