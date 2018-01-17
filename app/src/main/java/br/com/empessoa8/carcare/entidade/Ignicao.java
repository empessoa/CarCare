package br.com.empessoa8.carcare.entidade;

/**
 * Created by elias on 14/06/2017.
 */

public class Ignicao {
    private int _id;
    private String data_do_servico;
    private int km_do_servico;
    private float valor_do_servico;
    private String data_proxima_revisao;
    private int km_proxima_revisao;
    private String velas_ignicao;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getData_do_servico() {
        return data_do_servico;
    }

    public void setData_do_servico(String data_do_servico) {
        this.data_do_servico = data_do_servico;
    }

    public int getKm_do_servico() {
        return km_do_servico;
    }

    public void setKm_do_servico(int km_do_servico) {
        this.km_do_servico = km_do_servico;
    }

    public float getValor_do_servico() {
        return valor_do_servico;
    }

    public void setValor_do_servico(float valor_do_servico) {
        this.valor_do_servico = valor_do_servico;
    }

    public String getData_proxima_revisao() {
        return data_proxima_revisao;
    }

    public String setData_proxima_revisao(String data_proxima_revisao) {
        this.data_proxima_revisao = data_proxima_revisao;
        return data_proxima_revisao;
    }

    public int getKm_proxima_revisao() {
        return km_proxima_revisao;
    }

    public int setKm_proxima_revisao(int km_proxima_revisao) {
        this.km_proxima_revisao = km_proxima_revisao;
        return km_proxima_revisao;
    }

    public String getVelas_ignicao() {
        return velas_ignicao;
    }

    public void setVelas_ignicao(String velas_ignicao) {
        this.velas_ignicao = velas_ignicao;
    }
}
