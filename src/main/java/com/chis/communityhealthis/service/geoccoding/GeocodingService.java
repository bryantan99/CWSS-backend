package com.chis.communityhealthis.service.geoccoding;

import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface GeocodingService {
    Map<String, LatLng> googleGeocode(List<String> addressList) throws IOException, InterruptedException, ApiException;
    Map<String, LatLng> generateLatLng() throws Exception;
}
