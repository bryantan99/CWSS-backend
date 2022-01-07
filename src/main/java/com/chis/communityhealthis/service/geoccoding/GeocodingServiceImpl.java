package com.chis.communityhealthis.service.geoccoding;

import com.chis.communityhealthis.bean.AddressBean;
import com.chis.communityhealthis.repository.address.AddressDao;
import com.chis.communityhealthis.utility.AddressUtil;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${GOOGLE_API_KEY}")
    private String googleApiKey;

    @Override
    public Map<String, LatLng> googleGeocode(List<String> addressList) throws IOException, InterruptedException, ApiException {
        Map<String, LatLng> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(addressList)) {
            GeoApiContext context = new GeoApiContext.Builder().apiKey(googleApiKey).build();
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
                String fullAddress = AddressUtil.generateFullAddress(addressBean);
                if (StringUtils.isNotBlank(fullAddress)) {
                    LatLng latLng = googleGeocode(fullAddress);
                    if (latLng != null) {
                        addressBean.setLatitude(latLng.lat);
                        addressBean.setLongitude(latLng.lng);
                        addressDao.update(addressBean);
                        map.put(fullAddress, latLng);
                    }
                }
            }
        }
        return map;
    }

    private LatLng googleGeocode(String address) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(googleApiKey).build();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
            return results != null && results.length > 0 ? results[0].geometry.location : null;
        } catch (ApiException | InterruptedException | IOException e) {
            throw new Exception(e.getMessage());
        }
    }
}
