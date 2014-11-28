package samples;

import java.io.Serializable;

/**
 * 
 * @author williamz@synnex.com 2014年10月29日
 */
public class Address implements Serializable {
    private static final long serialVersionUID = -6597807526106389640L;
    private String country;
    private String area;
    private String region;
    private String city;
    private String county;
    private String isp;

    @Override
    public String toString() {
        String str = country + " " + area + " " + region + " " + city + " " + county + " " + isp;
        return str;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }
}
