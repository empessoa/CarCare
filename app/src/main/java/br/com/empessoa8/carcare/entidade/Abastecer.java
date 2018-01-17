package br.com.empessoa8.carcare.entidade;

/**
 * Created by elias on 13/06/2017.
 */

public class Abastecer {

    private int _id;
    private String data_do_abastecimento;
    private String combustivel_tipo;
    private int km_do_abastecimento;
    private float valor_total_abastecido;
    private float valor_do_litro;
    private float total_de_litro;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getData_do_abastecimento() {
        return data_do_abastecimento;
    }

    public String setData_do_abastecimento(String data_do_abastecimento) {
        this.data_do_abastecimento = data_do_abastecimento;
        return data_do_abastecimento;
    }

    public String getCombustivel_tipo() {
        return combustivel_tipo;
    }

    public void setCombustivel_tipo(String combustivel_tipo) {
        this.combustivel_tipo = combustivel_tipo;
    }

    public int getKm_do_abastecimento() {
        return km_do_abastecimento;
    }

    public int setKm_do_abastecimento(int km_do_abastecimento) {
        this.km_do_abastecimento = km_do_abastecimento;
        return km_do_abastecimento;
    }

    public float getValor_total_abastecido() {
        return valor_total_abastecido;
    }

    public void setValor_total_abastecido(float valor_total_abastecido) {
        this.valor_total_abastecido = valor_total_abastecido;
    }

    public float getValor_do_litro() {
        return valor_do_litro;
    }

    public void setValor_do_litro(float valor_do_litro) {
        this.valor_do_litro = valor_do_litro;
    }

    public float getTotal_de_litro() {
        return total_de_litro;
    }

    public void setTotal_de_litro(float total_de_litro) {
        this.total_de_litro = total_de_litro;
    }
}
