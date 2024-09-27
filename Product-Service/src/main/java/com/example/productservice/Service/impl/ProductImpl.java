package com.example.productservice.Service.impl;

import com.example.productservice.Entity.Promotion;
import com.example.productservice.Entity.SizeQuantity;
import com.example.productservice.Product.Order_Cart.ProductReponseCart_Order;
import com.example.productservice.Product.Order_Cart.SizeQuantityReponseCart_Order;
import com.example.productservice.Product.ProductRequest;
import com.example.productservice.Product.SizeQuantityRequest;
import com.example.productservice.Entity.Category;
import com.example.productservice.Entity.Product;
import com.example.productservice.Reponse.Order.OrderData;
import com.example.productservice.Reponse.Order.OrderDataRequest;
import com.example.productservice.Reponse.Order.PromotionData;
import com.example.productservice.Reponse.Product_Promotion_SizeQuantityy_GET;
import com.example.productservice.Reponse.PromotionRequest;
import com.example.productservice.Reponse.ReponseOrder.ReponseOrder;
import com.example.productservice.Reponse.ReponseOrder.ReponseOrderData;
import com.example.productservice.Repository.*;
import com.example.productservice.Service.ProducerService;
import com.example.productservice.Service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service

public class ProductImpl implements ProductService {
    @Autowired
    private  ProductRepository productRepository;
    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private SizeQuantityRepository sizeQuantityRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ProducerService<Product> kafkaTemplateProduct;
    @Autowired
    private ProducerService<ProductRequest> kafkaTemplateProductRequest;
    


    // elasticsearch
    @Override
    @Transactional 
    public Product addProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = ProductMapper.toEntity(productRequest, category);
        if(productRequest.getSizeQuantities() != null && productRequest.getSizeQuantities().size() != 0){
            for (SizeQuantity sizeQuantity : product.getSizeQuantities()) {
                sizeQuantity.setProduct(product);
            }
        }
        Product p =  productRepository.save(product);
        kafkaTemplateProduct.sendSuperHeroMessage("addProduct", p);
        return p;
    }
// elasticsearch
    @Override
    public Product updateProductBasic(Long id, ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            Product product1 = product.get();
            product1.setCategory(category);
            product1.setImages(productRequest.getImages());
            product1.setImage(productRequest.getImage());
            product1.setName(productRequest.getName());
            product1.setDescription(productRequest.getDescription());
            Product p =  productRepository.save(product1);
            productRequest.setId(p.getId());
            kafkaTemplateProductRequest.sendSuperHeroMessage("updateProductBasic", productRequest);
            return p;
        }else {
            throw new RuntimeException("Product not found");
        }

    }
// elasticsearch
    @Override
    public Product updateProductDetail(Long id, ProductRequest productRequest) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            Product product1 = product.get();
            product1.setShortDescription(productRequest.getShortDescription());
            Product p =  productRepository.save(product1);
            productRequest.setId(p.getId());
            kafkaTemplateProductRequest.sendSuperHeroMessage("updateProductDetail", productRequest);
            return p;
        }else {
            throw new RuntimeException("Product not found");
        }

    }
// elasticsearch
    @Override
    public Product updateProductSell(Long id, ProductRequest productRequest) {
        Optional<Product> product = productRepository.findById(id);

        if(product.isPresent()){
            Product product1 = product.get();
            product1.setPrice(productRequest.getPrice());
            if(productRequest.getSizeQuantities() == null || productRequest.getSizeQuantities().size() == 0){
                product1.setQuantity(productRequest.getQuantity());
                product1.setSizeQuantities(new ArrayList<>());
                return productRepository.save(product1);

            }else {
// Remove sizeQuantities not in the request
                product1.setQuantity(productRequest.getQuantity());

                // Find size quantities to be removed
                List<SizeQuantity> toRemove = new ArrayList<>();

                List<SizeQuantity> currentSizeQuantities = product1.getSizeQuantities();

                currentSizeQuantities.removeIf(sizeQuantity ->
                        productRequest.getSizeQuantities().stream().noneMatch(req -> req.getId() != null && req.getId().equals(sizeQuantity.getId())));

                for (SizeQuantityRequest request : productRequest.getSizeQuantities()) {
                    SizeQuantity sizeQuantity;
                    if (request.getId() != null) {
                        // Update existing SizeQuantity
                        sizeQuantity = sizeQuantityRepository.findById(request.getId())
                                .orElseThrow(() -> new RuntimeException("SizeQuantity not found"));
                        sizeQuantity.setSize(request.getSize());
                        sizeQuantity.setColor(request.getColor());
                        sizeQuantity.setQuantity(request.getQuantity());
                    } else {
                        // Add new SizeQuantity
                        sizeQuantity = new SizeQuantity();
                        sizeQuantity.setSize(request.getSize());
                        sizeQuantity.setColor(request.getColor());
                        sizeQuantity.setQuantity(request.getQuantity());
                        sizeQuantity.setProduct(product1);
                        product1.getSizeQuantities().add(sizeQuantity);
                    }
                    sizeQuantityRepository.save(sizeQuantity);
                }

                Product product_ =  productRepository.save(product1);
                kafkaTemplateProduct.sendSuperHeroMessage("updateProductSell", product_);

                return product_;
            }


        }else {
            throw new RuntimeException("Product not found");
        }
    }

// elasticsearch
    @Override
    public Product updateProductShip(Long id, ProductRequest productRequest) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            Product product1 = product.get();
            product1.setWeight(productRequest.getWeight());
            product1.setHeight(productRequest.getHeight());
            product1.setWidth(productRequest.getWidth());
            product1.setLength(productRequest.getLength());
            Product p =  productRepository.save(product1);
            productRequest.setId(p.getId());
            kafkaTemplateProductRequest.sendSuperHeroMessage("updateProductShip", productRequest);
            return p;
        }else {
            throw new RuntimeException("Product not found");
        }
    }


    @Override
    public List<Product> getListByShopId(Long shopId) {
        return productRepository.findByIdShop(shopId);
    }

    @Override
    public SizeQuantity findByIdSizeQuantity(Long id) {
        Optional<SizeQuantity> sizeQuantity = sizeQuantityRepository.findById(id);
        return sizeQuantity.orElse(null);
    }

    @Override
    public Product getById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Insert new product"));
        product.setView(product.getView() + 1);
        productRepository.save(product);
        kafkaTemplate.send( "plusView",id.toString());
        return product;
    }

    private List<Product> findProductsByIds(List<String> ids) {
        List<Long> idLongs = ids.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());
        return productRepository.findAllById(idLongs);
    }

    @Override
    public Page<Product> findAllWithFiltersAndSorting(String deeplearning,String name, Long idcategory, Double price_min, Double price_max, String sortBy, String order, Integer rating_filter, Pageable pageable) {
        if(deeplearning != null){
            String[] listIds = deeplearning.split("_");
            List<String> idsList = Arrays.asList(listIds);
            List<Product> products = findProductsByIds(idsList);
            // Tạo một Map để ánh xạ sản phẩm theo ID
            Map<Long, Product> productMap = products.stream()
                    .collect(Collectors.toMap(Product::getId, product -> product));
            List<Product> products1 = new ArrayList<>();
            for (String id : listIds) {
                Long id1 = Long.parseLong(id);
                Product product = productMap.get(id1);
                if (product != null) {
                    products1.add(product);
                }
            }

            // Chia danh sách thành trang
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), products1.size());
            List<Product> productPage = products1.subList(start, end);

            // Tạo Page<Product> từ danh sách sản phẩm và tổng số sản phẩm
            return new PageImpl<>(productPage, pageable, products1.size());

        }else {
            return productRepository.findAllWithFiltersAndSorting(name, idcategory, price_min, price_max, sortBy, order, rating_filter, pageable);
        }
    }

    @Override
    public Product findProductWithSize(Long idProduct, Long idSizeQuantity) {
        Product product1 = this.getById(idProduct);
        return product1;
    }

    @Override
    public Product_Promotion_SizeQuantityy_GET Product_Promotion_SizeQuantityy_GET_(Long idproduct, Long idSizeQuantity, Long idPromotion, int quantity) {


        Product product1 = this.getById(idproduct);
        if (product1 == null) {
            return null; // Handle not found case
        }

        if(idSizeQuantity == null){
            idSizeQuantity = 0l;
        }
        SizeQuantity sizeQuantity = this.findByIdSizeQuantity(idSizeQuantity);


        Promotion promotion1 =null;
        if(idPromotion != null){
            Optional<Promotion> promotion = promotionRepository.findById(idPromotion);
            if(promotion.isPresent()) {
                promotion1 = promotion.get();
            }
        }



        ProductReponseCart_Order productResponse = modelMapper.map(product1, ProductReponseCart_Order.class);
        SizeQuantityReponseCart_Order sizeQuantityResponse = sizeQuantity != null ? modelMapper.map(sizeQuantity, SizeQuantityReponseCart_Order.class) : null;
        PromotionRequest promotionRequest = promotion1 != null ? modelMapper.map(promotion1, PromotionRequest.class) : null;

        return Product_Promotion_SizeQuantityy_GET.builder()
                .product(productResponse)
                .promotionRequest(promotionRequest)
                .sizeQuantity(sizeQuantityResponse)
                .quantity(quantity)
                .build();
    }

    @Override
    public List<Product_Promotion_SizeQuantityy_GET> getOderDetails(ReponseOrderData reponseOrderData) {
        List<Product_Promotion_SizeQuantityy_GET> list = new ArrayList<>();
        List<ReponseOrder> reponseOrderList = reponseOrderData.getOrderItems();

        for(ReponseOrder order : reponseOrderList){
            Product_Promotion_SizeQuantityy_GET productPromotionSizeQuantityyGet= this.Product_Promotion_SizeQuantityy_GET_(
                    order.getProductId(),order.getIdSizeQuantity(),order.getPromotionId(),order.getQuantity()
            );
            list.add(productPromotionSizeQuantityyGet);
        }
        return list;
    }


    // elasticsearch
    @Override
    public void plusView(Long id) {
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(p -> {
            p.setView(p.getView() + 1);
            productRepository.save(p);
        });
    }

    @Override
    public List<OrderData> listOrderData(List<OrderDataRequest> orderDataRequests) {
        List<OrderData> list = new ArrayList<>();

        for (OrderDataRequest o : orderDataRequests) {
            // Fetch product by productId
            Product product = this.getById(o.getProductId());

            // Initialize promotion variable
            Promotion promotion = null;

            // Check if promotionId is not null before querying the repository
            if (o.getPromotionId() != null) {
                Optional<Promotion> promotionOptional = promotionRepository.findById(o.getPromotionId());
                if (promotionOptional.isPresent()) {
                    promotion = promotionOptional.get();
                }
            }

            // Map promotion to PromotionData if it exists
            PromotionData promotionRequest = promotion != null ? modelMapper.map(promotion, PromotionData.class) : null;

            // Create OrderData object and use default values for null fields
            OrderData orderData = new OrderData(
                    o.getId(),
                    product,
                    o.getIdSizeQuantity() != null ? o.getIdSizeQuantity() : 0, // Use a default value if null
                    promotionRequest,
                    o.getQuantity() != null ? o.getQuantity() : 0, // Use a default value if null
                    o.getNote() != null ? o.getNote() : "" // Use an empty string if null
            );

            list.add(orderData);
        }
        return list;
    }

    @Override
    public List<Product> getProductsByIds(List<Long> ids) {
        return productRepository.findByIdIn(ids);
    }

    @Override
    public String HandleQuantityProductAdd(String o) {
        String[] list1 = o.split("@");
        String[] list = list1[0].split("_");
        for (String data: list){
            String[] dataTemp = data.split("-");
            Long idProduct = Long.parseLong(dataTemp[0]);
            int quantity = Integer.parseInt(dataTemp[1]);
            Long idSizeQuantity = Long.parseLong(dataTemp[2]);
            handleDataQuantityAddOrder(idProduct,quantity,idSizeQuantity, list1[1]);
        }
        return "ok";
    }

    @Override
    public String HandleQuantityProductSub(String o) {
        String[] list = o.split("_");
        for (String data: list){
            String[] dataTemp = data.split("-");
            Long idProduct = Long.parseLong(dataTemp[0]);
            int quantity = Integer.parseInt(dataTemp[1]);
            Long idSizeQuantity = Long.parseLong(dataTemp[2]);
            handleDataQuantityCancelOrder(idProduct,quantity,idSizeQuantity);
        }
        return "ok";
    }


    

    // elasticsearch
    private void handleDataQuantityAddOrder(Long idProduct, int quantity, Long idSizeQuantity, String idorder){

        Product product = this.getById(idProduct);
        if(product.getQuantity() < quantity){
            // bắn kafka update cancel order
            kafkaTemplate.send("updatestatus", idorder);
        }else {
            product.setQuantity(product.getQuantity() - quantity);
            Long idSQ = 0l;
            if (idSizeQuantity != null && idSizeQuantity != 0) {
                SizeQuantity sizeQuantity = sizeQuantityRepository.findById(idSizeQuantity)
                        .orElseThrow(() -> new EntityNotFoundException("SizeQuantity not found"));
                sizeQuantity.setQuantity(sizeQuantity.getQuantity() - quantity);
                sizeQuantityRepository.save(sizeQuantity);
                idSQ = idSizeQuantity;
            }
            product.setSold(product.getSold() + quantity);
            productRepository.save(product);
            kafkaTemplate.send( "minusStock",idProduct + "-" + idSQ + "-" + quantity);
        }

    }


    // elasticsearch
    private void handleDataQuantityCancelOrder(Long idProduct, int quantity, Long idSizeQuantity){
        Product product = this.getById(idProduct);
        product.setQuantity(product.getQuantity() + quantity);
        Long idSQ = 0l;
        if (idSizeQuantity != null && idSizeQuantity != 0) {
            SizeQuantity sizeQuantity = sizeQuantityRepository.findById(idSizeQuantity)
                    .orElseThrow(() -> new EntityNotFoundException("SizeQuantity not found"));
            sizeQuantity.setQuantity(sizeQuantity.getQuantity() + quantity);
            sizeQuantityRepository.save(sizeQuantity);
            idSQ = idSizeQuantity;
        }

        productRepository.save(product);
        kafkaTemplate.send( "plusStock",idProduct + "-" + idSQ + "-" + quantity);
    }


    // elasticsearch
    @Override
    public void updateIsPublic(Long id, boolean isPublic) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product product1 = product.get();
            product1.setPublic(isPublic);
            productRepository.save(product1);
            if(isPublic){
                String data = id + "-" + "1";
                kafkaTemplate.send("updateIsPublic", data);
            }else{
                String data = id + "-" + "0";
                kafkaTemplate.send("updateIsPublic", data);
            }
        } else {
            throw new RuntimeException("Product not found");
        }
    }


    // elasticsearch
    @Transactional
    @Override
    public void deleteSizeQuantityById(Long id) {
        // Fetch the SizeQuantity entity by ID
        SizeQuantity sizeQuantity = sizeQuantityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("SizeQuantity not found with ID: " + id));

        // Get the associated Product
        Product product = sizeQuantity.getProduct();

        // Update the Product's quantity
        product.setQuantity(product.getQuantity() - sizeQuantity.getQuantity());

        // Remove SizeQuantity from Product's sizeQuantities list
        List<SizeQuantity> sizeQuantities = product.getSizeQuantities();
        sizeQuantities.remove(sizeQuantity); // Use remove() directly on the list
        String data = id + "-" + product.getId();

        // Save the updated Product
        productRepository.save(product);
    
        // Delete the SizeQuantity entity
        sizeQuantityRepository.delete(sizeQuantity);

        kafkaTemplate.send("deleteSizeQuantityById",data );
    }


    // elasticsearch
    @Override
    public void incrementProductView(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setView(product.getView() + 1);
            productRepository.save(product);
        } else {
            throw new RuntimeException("Product not found");
        }
    }
    @Override
    public Page<Product> findAllWithFiltersAndSortingE(String deeplearning, String name, Long idcategory,
            Double price_min, Double price_max, String sortBy, String order, Integer rating_filter, Pageable pageable) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'findAllWithFiltersAndSortingE'");
        return productRepository.findAllWithFiltersAndSorting(name, idcategory, price_min, price_max, sortBy, order, rating_filter, pageable);

    }

}
