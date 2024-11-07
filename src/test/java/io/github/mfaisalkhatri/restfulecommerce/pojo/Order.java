package io.github.mfaisalkhatri.restfulecommerce.pojo;

public record Order(String user_id, String product_id, String product_name, int product_amount, int qty, int tax_amt,
                    int total_amt) {
}
