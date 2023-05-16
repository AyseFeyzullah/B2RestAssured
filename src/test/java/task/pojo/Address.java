package task.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Address {
    public String street;
    public String suite;
    public String city;
    public String zipcode;
    public Geo geo;
}
