package org.launchcode.javawebdevtechjobspersistent.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employer extends AbstractEntity {
    @OneToMany(mappedBy = "employer")
    private List<Job> jobs = new ArrayList<>();

   @NotBlank
    private String location;

   public Employer(){}

   public Employer(String location){
       super();
       this.location=location;
   }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
