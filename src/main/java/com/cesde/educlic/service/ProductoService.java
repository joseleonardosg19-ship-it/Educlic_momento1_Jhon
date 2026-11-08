package com.cesde.educlic.service;

import com.cesde.educlic.dto.ProductoDto;
import com.cesde.educlic.model.entity.Categoria;
import com.cesde.educlic.model.entity.Producto;
import com.cesde.educlic.repository.CategoriaRepository;
import com.cesde.educlic.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cesde.educlic.model.enums.TallaTenis;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository; // 👈 Requerido para buscar la categoría por ID

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardarProducto(ProductoDto dto) {
        // 1. Buscamos la categoría usando el ID que viene en el DTO
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con el ID: " + dto.getCategoriaId()));

        // 2. Creamos y rellenamos la entidad Producto a partir del DTO
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(categoria);
        producto.setTallas(dto.getTallas());
        producto.setColores(dto.getColores());
        producto.setActivo(dto.isActivo());

        // 3. Validamos las tallas
        validarTallas(producto);

        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, ProductoDto dto) {
        return productoRepository.findById(id).map(productoExistente -> {
            // Buscamos la categoría por si fue modificada
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con el ID: " + dto.getCategoriaId()));

            // Actualizamos los campos de la entidad existente con los datos del DTO
            productoExistente.setNombre(dto.getNombre());
            productoExistente.setDescripcion(dto.getDescripcion());
            productoExistente.setPrecio(dto.getPrecio());
            productoExistente.setStock(dto.getStock());
            productoExistente.setImagenUrl(dto.getImagenUrl());
            productoExistente.setCategoria(categoria);
            productoExistente.setTallas(dto.getTallas());
            productoExistente.setColores(dto.getColores());
            productoExistente.setActivo(dto.isActivo());

            // Validamos las tallas actualizadas
            validarTallas(productoExistente);

            return productoRepository.save(productoExistente);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con el ID: " + id));
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    // Método auxiliar para evitar duplicar la validación de tallas.
    // Las tallas numéricas (36-44) solo aplican a la categoría Tenis.
    private void validarTallas(Producto producto) {
        boolean esTenis = producto.getCategoria() != null
                && "Tenis".equalsIgnoreCase(producto.getCategoria().getNombre());
        if (esTenis && producto.getTallas() != null) {
            for (String talla : producto.getTallas()) {
                TallaTenis.desdeValor(talla);
            }
        }
    }
}