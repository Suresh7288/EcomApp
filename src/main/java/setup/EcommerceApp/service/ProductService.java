package setup.EcommerceApp.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import setup.EcommerceApp.dto.ProductRequestDto;
import setup.EcommerceApp.dto.ProductResponseDto;
import setup.EcommerceApp.model.Product;
import setup.EcommerceApp.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public ProductResponseDto addProduct(ProductRequestDto dto){
        Product product = new Product();
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setCost(dto.getCost());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setCreatedAt(LocalDateTime.now());
        product = productRepository.save(product);
        return toResponseDto(product);
    }
    public List<ProductResponseDto> addProducts(List<ProductRequestDto> dtoList) {
        List<ProductResponseDto> responseList = new ArrayList<>();
        for (ProductRequestDto dto : dtoList) {
            responseList.add(addProduct(dto)); // reuse existing method
        }
        return responseList;
    }

    public List<ProductResponseDto> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }
    public ProductResponseDto getProductById(Long id){
        Product product = productRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Product not found with id:"+ id));
        return toResponseDto(product);
    }

    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto){
        Product product = productRepository.findById(id).orElseThrow(()-> new NoSuchElementException("product not find with the id:" + id));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setStock(dto.getStock());
        product.setCost(dto.getCost());
        product.setImageUrl(dto.getImageUrl());
        product = productRepository.save(product);
        return toResponseDto(product);
    }
    public void deleteProductById(Long id){
        if(!productRepository.existsById(id))
            throw new NoSuchElementException("Product not found for id:"+id);
        productRepository.deleteById(id);
    }
    public ProductResponseDto toResponseDto(Product product){
        ProductResponseDto dto = new ProductResponseDto();
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        dto.setCost(product.getCost());
        dto.setStock(product.getStock());
        dto.setImageUrl(product.getImageUrl());
        dto.setCreatedAt(product.getCreatedAt());
        return dto;
    }
}
