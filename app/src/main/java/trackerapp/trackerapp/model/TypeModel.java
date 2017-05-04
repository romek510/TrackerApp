package trackerapp.trackerapp.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Krystian on 04.05.2017.
 */

public class TypeModel {

    private int id;
    private String name;
    private String fullname;
    private String colorHex;
    private int sort;
    private int[] parameters;

    public TypeModel() {}

    public TypeModel(String name, String fullname, String colorHex) {
        this.name = name;
        this.fullname = fullname;
        this.colorHex = colorHex;
    }

    public TypeModel(int id, String name, String fullname, String colorHex, int sort) {
        this.id = id;
        this.name = name;
        this.fullname = fullname;
        this.colorHex = colorHex;
        this.sort = sort;
    }

    public TypeModel(int id, String name, String fullname, String colorHex, int sort, int[] parameters) {
        this.id = id;
        this.name = name;
        this.fullname = fullname;
        this.colorHex = colorHex;
        this.sort = sort;
        this.parameters = parameters;
    }

    public TypeModel(String activityTypeAsJson) {
        try {
            JSONObject a = new JSONObject(activityTypeAsJson);
            this.id = a.getInt("id");
            this.name = a.getString("name");
            this.fullname = a.getString("fullname");
            this.colorHex = a.getString("color");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int[] getParameters() {
        return parameters;
    }

    public void setParameters(int[] parameters) {
        this.parameters = parameters;
    }

}
