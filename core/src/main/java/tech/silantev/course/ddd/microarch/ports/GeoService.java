package tech.silantev.course.ddd.microarch.ports;

import com.github.sviperll.result4j.Result;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;

public interface GeoService {
    Result<Location, Exception> getLocation(String address);
}
