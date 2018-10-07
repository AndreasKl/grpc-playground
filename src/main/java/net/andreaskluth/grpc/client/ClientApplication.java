package net.andreaskluth.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.andreaskluth.cart.Cart;
import net.andreaskluth.cart.CartId;
import net.andreaskluth.cart.CartServiceGrpc;
import net.andreaskluth.cart.CartServiceGrpc.CartServiceBlockingStub;
import net.andreaskluth.cart.CartServiceGrpc.CartServiceStub;

public class ClientApplication {

  public static void main(String[] args) {
    var client = new CartServiceClient("127.0.0.1", 9000);
    client.retrieveCart();
  }

  public static class CartServiceClient {

    private final ManagedChannel channel;
    private final CartServiceBlockingStub blockingStub;
    private final CartServiceStub asyncStub;

    CartServiceClient(String host, int port) {
      this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    private CartServiceClient(ManagedChannelBuilder<?> channelBuilder) {
      this.channel = channelBuilder.build();
      this.blockingStub = CartServiceGrpc.newBlockingStub(channel);
      this.asyncStub = CartServiceGrpc.newStub(channel);
    }

    void retrieveCart() {
      Cart cart = blockingStub.getCart(cartId());
      System.out.println(cart);
    }

    private CartId cartId() {
      return CartId.newBuilder().setValue(randomId()).build();
    }

    private String randomId() {
      return java.util.UUID.randomUUID().toString();
    }
  }
}
