package com.example.SystemVentas.model;

public class DetalleVenta {
    private Producto producto;
    private int cantidad;
    private double precioTotal;

    private double precioConIva;
    private static final double IVA = 0.12;

    public double getPrecioConIva() {
        return precioConIva;
    }

    public void setPrecioConIva(double precioConIva) {
        this.precioConIva = precioConIva;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
        this.calcularTotales();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.calcularTotales();
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    private void calcularTotales(){
        if (this.producto != null && this.cantidad > 0) {
            this.precioConIva = this.producto.getPrecio() * (1 + IVA);
            this.precioTotal = this.precioConIva * this.cantidad;
        }
    }
}
