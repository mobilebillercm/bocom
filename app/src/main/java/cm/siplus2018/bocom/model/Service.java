package cm.siplus2018.bocom.model;

/**
 * Created by nkalla on 02/11/18.
 */

public class Service {
    private int id;
    private String serviceid, name, description;

    public Service(int id, String serviceid, String name, String description) {
        this.id = id;
        this.serviceid = serviceid;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
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
}
