package com.example.productservice.Service.ElasticSearch;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.productservice.Entity.Product;
import com.example.productservice.Entity.ElasticSearch.ProductE;
import com.example.productservice.Entity.ElasticSearch.ProductReviewE;
import com.example.productservice.Entity.ElasticSearch.SizeQuantityE;
import com.example.productservice.Mapper.ProductMapper1;
import com.example.productservice.Repository.ElasticSearch.ProductERepository;

@Service
public class ProductEServiceImpl implements ProductEService{
    

    @Autowired
    private ProductERepository productERepository;

    @Override
    public void addProduct(Product product) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'addProduct'");
        ProductE productE = ProductMapper1.toProductElas(product);
        productERepository.save(productE);
    }

    @Override
    public void updateProductBasic(Product product) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'updateProductBasic'");

        ProductE productE = productERepository.findById(product.getId())
    .orElseThrow(() -> new RuntimeException("Product not found"));

        productE.setImages(product.getImages());
        productE.setImage(product.getImage());
        productE.setName(product.getName());
        productE.setDescription(product.getDescription());

        productERepository.save(productE);
    }

    @Override
    public void updateProductDetail(Product product) {
        // TODO Auto-generated method stub
        ProductE productE = productERepository.findById(product.getId())
    .orElseThrow(() -> new RuntimeException("Product not found"));
        productE.setShortDescription(product.getShortDescription());
        productERepository.save(productE);
    }

    @Override
    public void updateProductSell(Product product) {
        // TODO Auto-generated method stub
        ProductE productE = productERepository.findById(product.getId())
    .orElseThrow(() -> new RuntimeException("Product not found"));
        productE.setPrice(product.getPrice());
        productE.setQuantity(product.getQuantity());
        if(product.getSizeQuantities() != null && product.getSizeQuantities().size() != 0){
            List<SizeQuantityE> productEs = ProductMapper1.mapSizeQuantities(product.getSizeQuantities());
            productE.setSizeQuantities(productEs);
          
        }
        productERepository.save(productE);
    }

    @Override
    public void updateProductShip(Product product) {
        // TODO Auto-generated method stub
        ProductE productE = productERepository.findById(product.getId())
    .orElseThrow(() -> new RuntimeException("Product not found"));
        productE.setWeight(product.getWeight());
        productE.setHeight(product.getHeight());
        productE.setWidth(product.getWidth());
        productE.setLength(product.getLength());

        productERepository.save(productE);
    }




    @Override
    public void addReview(Product product) {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'addReview'");
        ProductE productE = productERepository.findById(product.getId())
    .orElseThrow(() -> new RuntimeException("Product not found"));
        List<ProductReviewE> list = ProductMapper1.mapReviews(product.getReviews());
        productE.setReviews(list);
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
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'plusView'");
        Long id_ = Long.parseLong(id);
        ProductE productE = productERepository.findById(id_)
    .orElseThrow(() -> new RuntimeException("Product not found"));
        productE.setView(productE.getView()+1);
        productERepository.save(productE);
    }


    
}
