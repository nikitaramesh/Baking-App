package example.android.bakingapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    @JsonProperty("id")
    private int rId;
    @JsonProperty("name")
    private String rName;
    @JsonProperty("servings")
    private int rServings;
    @JsonProperty("image")
    private String rImage;
    @JsonProperty("ingredients")
    private List<Ingredients> rIngredients;
    @JsonProperty("steps")
    private List<Steps> rSteps;

    public Recipe() {
        this.rId = 0;
        this.rName = "";
        this.rServings = 0;
        this.rImage = "";
        this.rIngredients = new ArrayList<>();
        this.rSteps = new ArrayList<>();
    }

    protected Recipe(Parcel in) {
        rId = in.readInt();
        rName = in.readString();
        rServings = in.readInt();
        rImage = in.readString();
        rIngredients = new ArrayList<>();
        in.readList(this.rIngredients, Ingredients.class.getClassLoader());
        rSteps = new ArrayList<>();
        in.readList(this.rSteps, Steps.class.getClassLoader());
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getrId() {
        return rId;
    }

    public String getrName() {
        return rName;
    }

    public int getrServings() {
        return rServings;
    }

    public String getrImage() {
        return rImage;
    }

    public List<Ingredients> getrIngredients() {
        return rIngredients;
    }

    public List<Steps> getrSteps() {
        return rSteps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.rId);
        parcel.writeString(this.rName);
        parcel.writeInt(this.rServings);
        parcel.writeString(this.rImage);
        parcel.writeList(this.rIngredients);
        parcel.writeList(this.rSteps);
    }
}
