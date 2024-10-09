package com.example.cartservice.Service;

import com.example.cartservice.Dto.CartData;
import com.example.cartservice.Dto.CartItemDTO;
import com.example.cartservice.Dto.Order_Cart.ProductReponseCart_Order;
import com.example.cartservice.Dto.SuccessResponse;
import com.example.cartservice.Entity.Cart;
import com.example.cartservice.Entity.CartItem;
import com.example.cartservice.Repository.CartItemRepository;
import com.example.cartservice.Repository.CartRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartImpl {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper mapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WebClient webClient;

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Cart findCartByUser(Long iduser) {
        Optional<Cart> optionalCartEntity = cartRepository.findByIdCustomer(iduser);
        if(optionalCartEntity.isPresent()){
            return  optionalCartEntity.get();
        }
        return null;
    }

    public void deleteCartItemsByIds(List<Long> ids) {
        cartItemRepository.deleteAllById(ids);
    }

    @Transactional
    public Cart addCart(Long idCustomer, CartItemDTO cartItemDTO) {
        // Kiểm tra xem người dùng đã có giỏ hàng hay không
        Cart cartExist = findCartByUser(idCustomer);
        Cart cart;
        if (cartExist != null) {
            cart = cartExist;
        } else {
            cart = new Cart();
            cart.setIdCustomer(idCustomer);
            cart.setCartItems(new ArrayList<>());  // Initialize the cart items list
        }

        boolean check = false;

        // Check if cartExist is not null and has cartItems
        if (cartExist != null && cartExist.getCartItems() != null && !cartExist.getCartItems().isEmpty()) {
            for (CartItem cartItem : cartExist.getCartItems()) {
                if (cartItem.getIdProduct().equals(cartItemDTO.getIdProduct())) {
                    check = true;
                    cartItem.setQuantity(cartItem.getQuantity() + cartItemDTO.getQuantity());

                    // kiểm tra có đủ hàng trong kho

                }
            }
        }

        if (check) {


            entityManager.persist(cart);
            return cart;
        }

        CartItem cartItem = new CartItem();
        cartItem.setIdProduct(cartItemDTO.getIdProduct());
        cartItem.setQuantity(cartItemDTO.getQuantity());

        // kiểm tra có đủ hàng trong kho



        if (cartItemDTO.getIdSizeQuantity() != null) {
            cartItem.setIdSizeQuantity(cartItemDTO.getIdSizeQuantity());
        }else {
            cartItem.setIdSizeQuantity(0l);
        }

        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);

        // Lưu CartEntity và CartItemEntity vào cơ sở dữ liệu
        entityManager.persist(cart);
        entityManager.persist(cartItem);


        return cart;
    }
    public Cart deleteCartItem(Long idCart, Long idCartItem) {
        Optional<Cart> cart = cartRepository.findById(idCart);
        if (cart.isPresent()) {
            // Check Exist idCartItem
            if(!cartItemRepository.existsById(idCartItem)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find CartItem with id = " + idCartItem);

            cart.get().getCartItems().removeIf(cartItem -> cartItem.getId().equals(idCartItem));
            cartItemRepository.deleteById(idCartItem);
            return cartRepository.save(cart.get());
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find Cart with id = " + idCart);
    }


    public SuccessResponse findCartByUserID(Long id, String token){
        Cart cart = this.findCartByUser(id);
        List<CartData> cartReponseList = new ArrayList<>();
        if(cart == null){
            SuccessResponse successResponse = new SuccessResponse("NOT", cartReponseList);
            return  successResponse;
        }

        cartReponseList = Flux.fromIterable(cart.getCartItems())
                .flatMap(cartItem -> getProductResponse(cartItem.getIdProduct().toString(), token)
                        .map(productResponse -> {
                            CartData cartReponse = new CartData();
                            cartReponse.set_id(cartItem.getId().toString());
                            cartReponse.setBuy_count(cartItem.getQuantity());
                            cartReponse.setPrice(productResponse.getPrice());
                            cartReponse.setPrice_before_discount(productResponse.getPriceBeforeDiscount());
                            cartReponse.setStatus(-1);
                            cartReponse.setUser(cart.getIdCustomer().toString());
                            cartReponse.setId_size_quantity_color(cartItem.getIdSizeQuantity());
                            cartReponse.setCreatedAt("20/12/2023");
                            cartReponse.setUpdatedAt("20/12/2024");
                            cartReponse.setProduct(productResponse);
                            return cartReponse;
                        }))
                .collectList()
                .block(); // Wait for all asynchronous calls to complete


        

        SuccessResponse successResponse = new SuccessResponse("OK", cartReponseList);
        return  successResponse;
    }

    private Mono<ProductReponseCart_Order> getProductResponse(String productId, String token) {
        return webClient.get()
                .uri("http://localhost:8222/api/v1/products/"+productId+"/size/" + "0")
                .headers(headers -> headers.setBearerAuth(token.substring(7)))
                .retrieve()
                .bodyToMono(ProductReponseCart_Order.class);
    }


}