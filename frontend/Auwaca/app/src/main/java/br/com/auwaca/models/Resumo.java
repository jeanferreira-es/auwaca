package br.com.auwaca.models;

public class Resumo {
    private Integer menorPico;
    private Integer umidadeMedia;

    public Resumo(Integer menorPico, Integer umidadeMedia) {
        this.menorPico = menorPico;
        this.umidadeMedia = umidadeMedia;
    }

    public Integer getMenorPico() {
        return menorPico;
    }

    public void setMenorPico(Integer menorPico) {
        this.menorPico = menorPico;
    }

    public Integer getUmidadeMedia() {
        return umidadeMedia;
    }

    public void setUmidadeMedia(Integer umidadeMedia) {
        this.umidadeMedia = umidadeMedia;
    }
}
