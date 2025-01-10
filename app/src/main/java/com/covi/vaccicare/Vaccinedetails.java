package com.covi.vaccicare;

public class Vaccinedetails
{
    private String name;
    private String mobile;
    private String aadhar;
    private String role;
    private String vaccine1;
    private String vdate1;
    private String vaccine2;
    private String vdate2;
    private String vstatus;
    private String clsdiv;
    private String dist;
    private String cmp;
    private String cmpname;
    private String ward;
    private String dose;


    private String key;
    public Vaccinedetails(){}
    public Vaccinedetails(String name,String mobile,String role,String clsdiv,String aadhar,
                          String vaccine1,String vdate1,String vaccine2,String vdate2,
                          String vstatus,String dist,String cmp,String cmpname,String ward,
                          String dose)
    {
        this.name = name;
        this.mobile = mobile;
        this.aadhar = aadhar;
        this.role = role;
        this.vaccine1 = vaccine1;
        this.vdate1 = vdate1;
        this.vaccine2 = vaccine2;
        this.vdate2= vdate2;
        this.vstatus = vstatus;
        this.clsdiv = clsdiv;
        this.dist = dist;
        this.cmp = cmp;
        this.cmpname = cmpname;
        this.ward = ward;
        this.dose = dose;

    }
    //name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    //mobile
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    //aadhar
    public String getAadhar() {
        return aadhar;
    }
    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    //role
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    //vaccine1
    public String getVaccine1() {
        return vaccine1;
    }
    public void setVaccine1(String vaccine1) {
        this.vaccine1 = vaccine1;
    }

    //vdate1
    public String getVdate1() {
        return vdate1;
    }
    public void setVdate1(String vdate1) {
        this.vdate1 = vdate1;
    }

    //vaccine2
    public String getVaccine2() {
        return vaccine2;
    }
    public void setVaccine2(String vaccine2) {
        this.vaccine2 = vaccine2;
    }

    //vdate2
    public String getVdate2() {
        return vdate2;
    }
    public void setVdate2(String vdate2) {
        this.vdate2 = vdate2;
    }

    //vstatus
    public String getVstatus() {
        return vstatus;
    }
    public void setVstatus(String vstatus) {
        this.vstatus = vstatus;
    }

    //clsdiv
    public String getClsdiv() {
        return clsdiv;
    }
    public void setClsdiv(String clsdiv) {
        this.clsdiv = clsdiv;
    }

    //ward
    public String getWard() { return ward; }
    public void setWard(String ward) {this.ward = ward;}

    //cmp
    public String getCmp() { return cmp; }
    public void setCmp(String cmp) { this.cmp = cmp; }

    //selection
    public String getCmpname() { return cmpname; }
    public void setCmpname(String cmpname) { this.cmpname = cmpname; }

    //district
    public String getDist() { return dist; }
    public void setDist(String dist) { this.dist = dist; }

    //dose
    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }

    //key
    public void setKey(String key) {
        this.key = key;
    }

}
