package ru.dankoy.otus;

import com.google.common.net.InetAddresses;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HelloOtus {

    public static void main(String[] args) throws UnknownHostException {

        String stringIpAddress = "10.10.0.1";
        System.out.println(InetAddresses.isInetAddress(stringIpAddress));

        int intIpAddress = convertStringAddresstoInteger(stringIpAddress);
        System.out.println(intIpAddress);

        stringIpAddress = convertIntegerAddressToString(intIpAddress);
        System.out.println(stringIpAddress);

    }

    private static Integer convertStringAddresstoInteger(String address) throws UnknownHostException {
        return InetAddresses.coerceToInteger(InetAddress.getByName(address));
    }

    private static String convertIntegerAddressToString(Integer address) {
        return InetAddresses.fromInteger(address).getHostAddress();
    }

}
