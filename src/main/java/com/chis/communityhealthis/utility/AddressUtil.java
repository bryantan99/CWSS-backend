package com.chis.communityhealthis.utility;

import com.chis.communityhealthis.bean.AddressBean;
import org.apache.commons.lang3.StringUtils;

public class AddressUtil {
    public static String generateFullAddress(AddressBean addressBean) {
        String address = "";
        if (addressBean != null) {
            if (StringUtils.isNotBlank(addressBean.getAddressLine1())) {
                address += addressBean.getAddressLine1() + " ";
            }

            if (StringUtils.isNotBlank(addressBean.getAddressLine2())) {
                address += addressBean.getAddressLine2() + " ";
            }

            if (StringUtils.isNotBlank(addressBean.getPostcode())) {
                address += addressBean.getPostcode() + " ";
            }

            if (StringUtils.isNotBlank(addressBean.getCity())) {
                address += addressBean.getCity() + " ";
            }

            if (StringUtils.isNotBlank(addressBean.getState())) {
                address += getFullStateName(addressBean.getState());
            }
        }
        return StringUtils.isBlank(address) ? null : address;
    }

    public static String getFullStateName(String state) {
        switch (state) {
            case "PLS":
                return "PERLIS";
            case "KDH":
                return "KEDAH";
            case "PNG":
                return "PULAU PINANG";
            case "PRK":
                return "PERAK";
            case "SGR":
                return "SELANGOR";
            case "NSN":
                return "NEGERI SEMBILAN";
            case "MLK":
                return "MELAKA";
            case "JHR":
                return "JOHOR";
            case "KTN":
                return "KELANTAN";
            case "PHG":
                return "PAHANG";
            case "TRG":
                return "TERENGGANU";
            case "SBH":
                return "SABAH";
            case "SWK":
                return "SARAWAK";
            case "KUL":
                return "KUALA LUMPUR";
            case "LBN":
                return "LABUAN";
            case "PJY":
                return "PUTRAJAYA";
            default:
                return null;
        }
    }
}
