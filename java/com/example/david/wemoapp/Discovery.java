package com.example.david.wemoapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Discovery
{
    final static String url = "http://10.0.0.39:49153"; // ip and port depends on network

    public static String turnOn() throws Exception
    {
        URL obj = new URL(url + "/upnp/control/basicevent1");
        HttpURLConnection con = (HttpURLConnection)obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("SOAPACTION", "\"urn:Belkin:service:basicevent:1#SetBinaryState\"");
        con.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
        con.setRequestProperty("Accept", "");

        String msg = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:SetBinaryState xmlns:u=\"urn:Belkin:service:basicevent:1\"><BinaryState>1</BinaryState></u:SetBinaryState></s:Body></s:Envelope>";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(msg);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            return "Switch turned on.";
        }
        else
        {
            return "Error: Switch did not respond.";
        }
    }

    public static String turnOff() throws Exception
    {
        URL obj = new URL(url + "/upnp/control/basicevent1");
        HttpURLConnection con = (HttpURLConnection)obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("SOAPACTION", "\"urn:Belkin:service:basicevent:1#SetBinaryState\"");
        con.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
        con.setRequestProperty("Accept", "");

        String msg = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:SetBinaryState xmlns:u=\"urn:Belkin:service:basicevent:1\"><BinaryState>0</BinaryState></u:SetBinaryState></s:Body></s:Envelope>";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(msg);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            return "Switch turned off.";
        }
        else
        {
            return "Error: Switch did not respond.";
        }
    }

    public static String checkState() throws Exception
    {
        URL obj = new URL(url + "/upnp/control/basicevent1");
        HttpURLConnection con = (HttpURLConnection)obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("SOAPACTION", "\"urn:Belkin:service:basicevent:1#GetBinaryState\"");
        con.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
        con.setRequestProperty("Accept", "");

        String msg = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:GetBinaryState xmlns:u=\"urn:Belkin:service:basicevent:1\"><BinaryState>1</BinaryState></u:GetBinaryState></s:Body></s:Envelope>";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(msg);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            in.close();

            String state = response.toString().substring(213, 214);
            if (Integer.parseInt(state) == 8)
                return "Device state: Standby";
            else if (Integer.parseInt(state) == 1)
                return "Device state: Online";
            else // state == 0
                return "Device state: Offline";
        }
        else
        {
            return "Error: Switch did not respond.";
        }
    }

    public static Double getPower() throws Exception // Returns current power draw in milliwatts
    {
        URL obj = new URL(url + "/upnp/control/insight1");
        HttpURLConnection con = (HttpURLConnection)obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("SOAPACTION", "\"urn:Belkin:service:insight:1#GetInsightParams\"");
        con.setRequestProperty("Content-Type", "text/xml; charset=\"utf-8\"");
        con.setRequestProperty("Accept", "");

        String msg = "<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><s:Body><u:GetInsightParams xmlns:u=\"urn:Belkin:service:insight:1\"></u:GetInsightParams></s:Body></s:Envelope>";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(msg);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null)
            {
                response.append(inputLine);
            }
            in.close();

            String[] arr = response.toString().split("\\|", 0);
            double power = Double.parseDouble(arr[7]); // in milliwatts
            return power;
        }
        else
        {
            return -1.0;
        }
    }
}
