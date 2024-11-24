package br.com.ifsp.ifome.exceptions.client;

public class OrderAlreadyReviewedException extends RuntimeException {
  public OrderAlreadyReviewedException() {
    super("Este pedido já foi avaliado.");
  }

  public OrderAlreadyReviewedException(String message) {
    super(message);
  }
}
