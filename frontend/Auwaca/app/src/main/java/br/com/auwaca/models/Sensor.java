package br.com.auwaca.models;

public class Sensor {
    private String status;
    private Integer statusMotor;
    private Integer statusNivel;
    private Integer dividendo;

    public Sensor(String status, Integer statusMotor, Integer statusNivel) {
        this.status = status;
        this.statusMotor = statusMotor;
        this.statusNivel = statusNivel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusMotor() {
        return statusMotor;
    }

    public void setStatusMotor(Integer statusMotor) {
        this.statusMotor = statusMotor;
    }

    public Integer getStatusNivel() {
        return statusNivel;
    }

    public void setStatusNivel(Integer statusNivel) {
        this.statusNivel = statusNivel;
    }

    public Integer getDividendo() {
        return dividendo;
    }

    public void setDividendo(Integer dividendo) {
        this.dividendo = dividendo;
    }
}
