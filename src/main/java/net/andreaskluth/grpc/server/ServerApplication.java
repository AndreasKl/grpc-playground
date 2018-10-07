package net.andreaskluth.grpc.server;

import grpc.health.v1.HealthCheck.HealthCheckRequest;
import grpc.health.v1.HealthCheck.HealthCheckResponse;
import grpc.health.v1.HealthCheck.HealthCheckResponse.ServingStatus;
import grpc.health.v1.HealthGrpc.HealthImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import net.andreaskluth.cart.Cart;
import net.andreaskluth.cart.CartId;
import net.andreaskluth.cart.CartServiceGrpc.CartServiceImplBase;
import net.andreaskluth.cart.LineItem;
import net.andreaskluth.cart.Listing;
import net.andreaskluth.cart.ListingId;
import net.andreaskluth.cart.PriceInCent;
import net.andreaskluth.cart.ServiceType;
import net.andreaskluth.cart.Version;

public class ServerApplication {

  public static void main(String[] args) throws InterruptedException, IOException {
    Server server =
        ServerBuilder.forPort(8080)
            .addService(new CartServiceImpl())
            .addService(new HealthCheckServiceImpl())
            .build();
    server.start();
    server.awaitTermination();
  }

  public static class HealthCheckServiceImpl extends HealthImplBase {

    @Override
    public void check(HealthCheckRequest request, StreamObserver<HealthCheckResponse> observer) {
      HealthCheckResponse healthCheckResponse =
          HealthCheckResponse.newBuilder().setStatus(ServingStatus.SERVING).build();

      observer.onNext(healthCheckResponse);
      observer.onCompleted();
    }
  }

  public static class CartServiceImpl extends CartServiceImplBase {

    @Override
    public void getCart(CartId request, StreamObserver<Cart> observer) {
      Cart cart =
          Cart.newBuilder()
              .setId(request)
              .setVersion(Version.newBuilder().setValue(23).build())
              .addServiceType(ServiceType.newBuilder().setValue("DELIVERY").build())
              .addServiceType(ServiceType.newBuilder().setValue("PARCEL").build())
              .addLineItem(lineItem())
              .addLineItem(lineItem())
              .addLineItem(lineItem())
              .build();

      observer.onNext(cart);
      observer.onCompleted();
    }

    private LineItem lineItem() {
      return LineItem.newBuilder()
          .setPriceWhenAdded(price(299))
          .setListing(listing())
          .setVersion(Version.newBuilder().setValue(42).build())
          .build();
    }

    private Listing listing() {
      return Listing.newBuilder().setId(listingId()).setCurrentPrice(price(329)).build();
    }

    private PriceInCent price(long priceInCent) {
      return PriceInCent.newBuilder().setValue(priceInCent).build();
    }

    private ListingId listingId() {
      return ListingId.newBuilder().setValue(randomId()).build();
    }

    private String randomId() {
      return java.util.UUID.randomUUID().toString();
    }
  }
}
