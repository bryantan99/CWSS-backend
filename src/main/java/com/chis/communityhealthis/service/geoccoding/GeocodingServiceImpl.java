package com.chis.communityhealthis.service.geoccoding;

import com.chis.communityhealthis.bean.AddressBean;
import com.chis.communityhealthis.repository.address.AddressDao;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    @Autowired
    private AddressDao addressDao;

    @Override
    public Map<String, LatLng> googleGeocode(List<String> addressList) throws IOException, InterruptedException, ApiException {
        Map<String, LatLng> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(addressList)) {
            GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyDM52dfVP_MjYHd6UB24KHPMhSACq-d6wA").build();
            for (String address : addressList) {
                GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
                map.put(address, results[0].geometry.location);
            }
            context.shutdown();
        }
        return map;
    }

    @Override
    @Transactional
    public Map<String, LatLng> generateLatLng() throws Exception {
        Map<String, LatLng> map = new HashMap<>();
        List<AddressBean> addressBeans = addressDao.getAll();
        if (!CollectionUtils.isEmpty(addressBeans)) {
            for (AddressBean addressBean : addressBeans) {
                String fullAddress = getFullAddress(addressBean);
                LatLng latLng = googleGeocode(fullAddress);
                if (latLng != null) {
                    addressBean.setLatitude(latLng.lat);
                    addressBean.setLongitude(latLng.lng);
                    addressDao.update(addressBean);
                    map.put(fullAddress, latLng);
                }
            }
        }
        return map;
    }

    private LatLng googleGeocode(String address) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyDM52dfVP_MjYHd6UB24KHPMhSACq-d6wA").build();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
            return results[0].geometry.location;
        } catch (ApiException | InterruptedException | IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    private String getFullAddress(AddressBean addressBean) {
        return addressBean.getAddressLine1() + " " +
                addressBean.getAddressLine2() + " " +
                addressBean.getPostcode() + " " +
                addressBean.getCity() + " " +
                getFullStateName(addressBean.getState());
    }

    private String getFullStateName(String state) {
        switch (state) {
            case "PLS":
                return "Perlis";
            case "KDH":
                return "Kedah";
            case "PNG":
                return "Pulau Pinang";
            case "PRK":
                return "Perak";
            case "SGR":
                return "Selangor";
            case "NSN":
                return "Negeri Sembilan";
            case "MLK":
                return "Melaka";
            case "JHR":
                return "Johor";
            case "KTN":
                return "Kelantan";
            case "PHG":
                return "Pahang";
            case "TRG":
                return "Terengganu";
            case "SBH":
                return "Sabah";
            case "SWK":
                return "Sarawak";
            case "KUL":
                return "Kuala Lumpur";
            case "LBN":
                return "Labuan";
            case "PJY":
                return "Putrajaya";
            default:
                return null;
        }
    }
}
