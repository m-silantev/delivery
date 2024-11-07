package tech.silantev.course.ddd.microarch.adapters.grpc;

import com.github.sviperll.result4j.Result;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.silantev.course.ddd.microarch.domain.sharedkernel.Location;
import tech.silantev.course.ddd.microarch.ports.GeoService;

@Slf4j
@Service
public class GeoServiceAdapter implements GeoService {

    @Override
    public Result<Location, Exception> getLocation(String address) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5004)
                .usePlaintext()
                .build();

        GeoGrpc.GeoBlockingStub stub = GeoGrpc.newBlockingStub(channel);

        GetGeolocationRequest request = GetGeolocationRequest.newBuilder().setStreet(address).build();
        GetGeolocationReply response;
        try {
            // Call the original method on the server.
            response = stub.getGeolocation(request);
            log.info("response {}", response);
        } catch (StatusRuntimeException e) {
            // Log a warning if the RPC fails.
        }

        return null;
    }
}
