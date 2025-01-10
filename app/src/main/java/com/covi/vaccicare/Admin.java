package com.covi.vaccicare;

public class Admin
{
    private String password;
    private String key;

    public Admin(){}
    public Admin(String password)

    {
        this.password = password;
    }
    //password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setKey(String key) {
        this.key = key;
    }

}
