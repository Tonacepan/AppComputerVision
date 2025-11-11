package com.vision.core.math;

public class Complex {
    private final double real;
    private final double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public double getReal() {
        return real;
    }

    public double getImag() {
        return imag;
    }

    public Complex plus(Complex other) {
        return new Complex(this.real + other.real, this.imag + other.imag);
    }

    public Complex minus(Complex other) {
        return new Complex(this.real - other.real, this.imag - other.imag);
    }

    public Complex times(Complex other) {
        double newReal = this.real * other.real - this.imag * other.imag;
        double newImag = this.real * other.imag + this.imag * other.real;
        return new Complex(newReal, newImag);
    }

    public double magnitude() {
        return Math.sqrt(real * real + imag * imag);
    }

    public static Complex fromPolar(double magnitude, double phase) {
        return new Complex(magnitude * Math.cos(phase), magnitude * Math.sin(phase));
    }
    
    @Override
    public String toString() {
        if (imag == 0) return real + "";
        if (real == 0) return imag + "i";
        if (imag <  0) return real + " - " + (-imag) + "i";
        return real + " + " + imag + "i";
    }
}
