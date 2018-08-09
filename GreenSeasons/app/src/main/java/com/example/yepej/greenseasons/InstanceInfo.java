package com.example.yepej.greenseasons;

public class InstanceInfo
{
    private static InstanceInfo info = null;

    // Global variable
    private String serverIP;
    private String encodeFormat = "UTF-8";

    // Restrict the constructor from being instantiated
    private InstanceInfo(){}

    public static InstanceInfo getInstance()
    {
        if(info == null)
        {
            info = new InstanceInfo();
        }
        return info;
    }

    public String getServerIP(){ return serverIP; }

    public void setServerIP(String serverIP)
    {
        this.serverIP = serverIP;
    }

    public String getEncodeFormat()
    {
        return encodeFormat;
    }
}
