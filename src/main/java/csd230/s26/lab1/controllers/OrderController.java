
package csd230.s26.lab1.controllers;

import csd230.s26.lab1.entities.CartEntity;
import csd230.s26.lab1.entities.OrderEntity;
import csd230.s26.lab1.entities.ProductEntity;
import csd230.s26.lab1.entities.PublicationEntity;
import csd230.s26.lab1.repositories.CartRepository;
import csd230.s26.lab1.repositories.OrderRepository;
import csd230.s26.lab1.repositories.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class OrderController {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    /**
     * POST /cart/checkout
     * Handles the checkout logic securely within a database transaction context.
     */
    @PostMapping("/cart/checkout")
    @Transactional
    public String checkout() {
        Long defaultCartId = 1L;
        CartEntity cart = cartRepository.findById(defaultCartId).orElse(null);
        if (cart == null || cart.getProducts().isEmpty()) {
            return "redirect:/cart"; // Safe fallback if cart is missing or empty
        }

        OrderEntity order = new OrderEntity();
        double runningTotal = 0.0;

        for (ProductEntity product : cart.getProducts()) {

            if (product instanceof PublicationEntity) {
                PublicationEntity publication = (PublicationEntity) product;
                runningTotal += publication.getPrice();
                publication.setCopies(publication.getCopies() - 1);
                productRepository.save(publication);
            }
            order.getProducts().add(product);
        }
        order.setTotalAmount(runningTotal);
        cart.getProducts().clear();
        OrderEntity savedOrder = orderRepository.save(order);
        cartRepository.save(cart);
        return "redirect:/order/details/" + savedOrder.getId();
    }

    /**
     * GET /order/details/{id}
     * Renders the confirmed order info into orderDetails.html
     */
    @GetMapping("/order/details/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model) {
        Optional<OrderEntity> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            return "orderDetails";
        }
        return "redirect:/books";
    }
}