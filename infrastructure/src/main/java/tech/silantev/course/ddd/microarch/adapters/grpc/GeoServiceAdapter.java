package tech.silantev.course.ddd.microarch.adapters.grpc;

import com.github.sviperll.result4j.Result;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;
import tech.silantev.course.ddd.microarch.ports.GeoService;

@Service
public class GeoServiceAdapter implements GeoService {

    @GrpcClient("geo")
    private GeoGrpc.GeoBlockingStub blockingStub;

    @Override
    public Result<Location, Exception> getLocation(String address) {
        try {
            GetGeolocationRequest request = GetGeolocationRequest.newBuilder()
                    .setStreet(address)
                    .build();
            GetGeolocationReply response = blockingStub.getGeolocation(request);
            Location location = Location.create(response.getLocation().getX(), response.getLocation().getY());
            return Result.success(location);
        } catch (Exception e) {
            return Result.error(e);
        }
    }
}
