package corp.nminhanh.placesaver;

import java.io.Serializable;

/**
 * Created by Minh Anh on 1/10/2018.
 */

public class Item implements Serializable {
    private long id;
    private String path;
    private String name;
    private String type;
    private String description;

    public Item() {
        id = 0;
        path = "";
        name = "";
        type = "";
        description = "";
    }

    public Item(long id, String name, String type, String description, String path) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean HasImage() {
        if (path.equals("")) {
            return false;
        }
        return true;
    }
}
