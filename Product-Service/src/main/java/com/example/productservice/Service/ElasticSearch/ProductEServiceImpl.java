package com.example.productservice.Service.ElasticSearch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.ProductReview;
import com.example.productservice.Entity.ElasticSearch.ProductE;
import com.example.productservice.Entity.ElasticSearch.ProductReviewE;
import com.example.productservice.Entity.ElasticSearch.SizeQuantityE;
import com.example.productservice.Mapper.ProductMapper;
import com.example.productservice.Product.ProductRequest;
import com.example.productservice.Repository.ElasticSearch.ProductERepository;

@Service
public class ProductEServiceImpl implements ProductEService{
    

    @Autowired
    private ProductERepository productERepository;

    @Override
    public void addProduct(Product product) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'addProduct'");
        ProductE productE = ProductMapper.toProductElas(product);
        productERepository.save(productE);
    }

    @Override
    public void updateProductBasic(ProductRequest productRequest) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateProductBasic'");

        ProductE productE = productERepository.findById(productRequest.getId()).get();
        productE.setImages(productRequest.getImages());
        productE.setImage(productRequest.getImage());
        productE.setName(productRequest.getName());
        productE.setDescription(productRequest.getDescription());

        productERepository.save(productE);
    }

    @Override
    public void updateProductDetail(ProductRequest productRequest) {
        // TODO Auto-generated method stub
        ProductE productE = productERepository.findById(productRequest.getId()).get();
        productE.setShortDescription(productRequest.getShortDescription());
        productERepository.save(productE);
    }

    @Override
    public void updateProductSell(Product product) {
        // TODO Auto-generated method stub
        ProductE productE = productERepository.findById(product.getId()).get();
        productE.setPrice(product.getPrice());
        productE.setQuantity(product.getQuantity());
        if(product.getSizeQuantities() != null && product.getSizeQuantities().size() != 0){
            List<SizeQuantityE> productEs = ProductMapper.mapSizeQuantities(product.getSizeQuantities());
            productE.setSizeQuantities(productEs);
          
        }
        productERepository.save(productE);
    }

    @Override
    public void updateProductShip(ProductRequest productRequest) {
        // TODO Auto-generated method stub
        ProductE productE = productERepository.findById(productRequest.getId()).get();
        productE.setWeight(productRequest.getWeight());
        productE.setHeight(productRequest.getHeight());
        productE.setWidth(productRequest.getWidth());
        productE.setLength(productRequest.getLength());

        productERepository.save(productE);
    }

    @Override
    public void addReview(ProductReview productReview) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'addReview'");
        ProductE productE = productERepository.findById(productReview.getProduct().getId()).get();
        ProductReviewE productReviewE = ProductMapper.mapReview(productReview);
        productE.getReviews().add(productReviewE);
        productERepository.save(productE);

    }

    @Override
    public void updateIsPublic(String data) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateIsPublic'");
        // id-isPublic
        Long id = null;
        Boolean isPublic = false;

        if(data != null){

            String[] splitData = data.split("-");
            id = Long.parseLong(splitData[0]);
            if(splitData[1].equals("1")){
                isPublic = true;
            }


            ProductE productE = productERepository.findById(id).get();
            productE.setPublic(isPublic);
            productERepository.save(productE);
        }
    }

    @Override
    public void deleteSizeQuantityById(String data) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'deleteSizeQuantityById'");
        // idSizeQuantity-idProduct
        if(data != null && !data.isEmpty()){
            String[] splStrings = data.split("-");
            Long idSizeQuantity = Long.parseLong(splStrings[0]);
            Long idProduct = Long.parseLong(splStrings[1]);

            ProductE productE = productERepository.findById(idProduct).get();
            List<SizeQuantityE> sizeQuantities = productE.getSizeQuantities();
            sizeQuantities = sizeQuantities.stream().filter(sizeQuantity -> sizeQuantity.getId() != idSizeQuantity).toList();
            productE.setSizeQuantities(sizeQuantities);
            productERepository.save(productE);
        }

    }

    @Override
    public void plusStockNotSize(String data) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'plusStockNotSize'");
        // idProduct-idSizeQuantity-quantity(int)
        if(data == null || data.isEmpty()){
            return;
        }
        String[] splStrings = data.split("-");
        Long idProduct = Long.parseLong(splStrings[0]);
        Long idSizeQuantity = Long.parseLong(splStrings[1]);
        int quantity = Integer.parseInt(splStrings[2]);
        ProductE productE = productERepository.findById(idProduct).get();
        productE.setQuantity(productE.getQuantity() + quantity);
        if(idSizeQuantity == 0){

        
        }
        else{
            List<SizeQuantityE> sizeQuantities = productE.getSizeQuantities();
            sizeQuantities = sizeQuantities.stream()
                .map(sizeQuantity -> {
                    if (sizeQuantity.getId().equals(idSizeQuantity)) {
                        // Increase quantity for the matched size
                        sizeQuantity.setQuantity(sizeQuantity.getQuantity() + quantity);
                    }
                    return sizeQuantity;
                }).toList();
    
            productE.setSizeQuantities(sizeQuantities);
        }

        productERepository.save(productE);
    }

    @Override
    public void minusStockNotSize(String data) {
         // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'plusStockNotSize'");
        // idProduct-idSizeQuantity-quantity
        if(data == null || data.isEmpty()){
            return;
        }
        String[] splStrings = data.split("-");
        Long idProduct = Long.parseLong(splStrings[0]);
        Long idSizeQuantity = Long.parseLong(splStrings[1]);
        int quantity = Integer.parseInt(splStrings[2]);
        ProductE productE = productERepository.findById(idProduct).get();
        productE.setQuantity(productE.getQuantity() - quantity);
        if(idSizeQuantity == 0){
        }
        else{
            List<SizeQuantityE> sizeQuantities = productE.getSizeQuantities();
            sizeQuantities = sizeQuantities.stream()
                .map(sizeQuantity -> {
                    if (sizeQuantity.getId().equals(idSizeQuantity)) {
                        // Increase quantity for the matched size
                        sizeQuantity.setQuantity(sizeQuantity.getQuantity() - quantity);
                    }
                    return sizeQuantity;
                }).toList();
    
            productE.setSizeQuantities(sizeQuantities);
        }
        productERepository.save(productE);
    }

    @Override
    public void plusView(String id) {
        Long data = Long.parseLong(id);
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'plusView'");
        ProductE productE = productERepository.findById(data).get();
        productE.setView(productE.getView()+1);
        productERepository.save(productE);
    }


    
}
