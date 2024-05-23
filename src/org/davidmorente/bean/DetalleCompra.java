/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.davidmorente.bean;

/**
 *
 * @author david
 */
public class DetalleCompra {
    private int codigoDetalleCompra;
    private double costoUnitario;
    private int unidad;
    private String productosCodigoProducto;
    private int comprasNumeroDocumento;

    public DetalleCompra() {
    }

    public DetalleCompra(int codigoDetalleCompra, double costoUnitario, int unidad, String productosCodigoProducto, int comprasNumeroDocumento) {
        this.codigoDetalleCompra = codigoDetalleCompra;
        this.costoUnitario = costoUnitario;
        this.unidad = unidad;
        this.productosCodigoProducto = productosCodigoProducto;
        this.comprasNumeroDocumento = comprasNumeroDocumento;
    }

    public int getCodigoDetalleCompra() {
        return codigoDetalleCompra;
    }

    public void setCodigoDetalleCompra(int codigoDetalleCompra) {
        this.codigoDetalleCompra = codigoDetalleCompra;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(double costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public int getUnidad() {
        return unidad;
    }

    public void setUnidad(int unidad) {
        this.unidad = unidad;
    }

    public String getProductosCodigoProducto() {
        return productosCodigoProducto;
    }

    public void setProductosCodigoProducto(String productosCodigoProducto) {
        this.productosCodigoProducto = productosCodigoProducto;
    }

    public int getComprasNumeroDocumento() {
        return comprasNumeroDocumento;
    }

    public void setComprasNumeroDocumento(int comprasNumeroDocumento) {
        this.comprasNumeroDocumento = comprasNumeroDocumento;
    }
    
}
