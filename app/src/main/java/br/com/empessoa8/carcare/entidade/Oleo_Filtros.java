package br.com.empessoa8.carcare.entidade;

/**
 * Created by elias on 21/06/2017.
 */

public class Oleo_Filtros {
    private int    _id;
    private String data_do_servico;
    private int    km_do_servico;
    private float  valor_do_servico;
    private String data_proxima_revisao;
    private int km_proxima_revisao;
    private String oleo_do_motor;
    private String oleo_da_direcao;
    private String filtro_do_oleo;
    private String filtro_do_combustivel;
    private String filtro_do_ar;
    private String filtro_do_ar_condicionado;
    private String lubrificante_tipo;

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

    public String getOleo_do_motor() {
        return oleo_do_motor;
    }

    public void setOleo_do_motor(String oleo_do_motor) {
        this.oleo_do_motor = oleo_do_motor;
    }

    public String getOleo_da_direcao() {
        return oleo_da_direcao;
    }

    public void setOleo_da_direcao(String oleo_da_direcao) {
        this.oleo_da_direcao = oleo_da_direcao;
    }

    public String getFiltro_do_oleo() {
        return filtro_do_oleo;
    }

    public void setFiltro_do_oleo(String filtro_do_oleo) {
        this.filtro_do_oleo = filtro_do_oleo;
    }

    public String getFiltro_do_combustivel() {
        return filtro_do_combustivel;
    }

    public void setFiltro_do_combustivel(String filtro_do_combustivel) {
        this.filtro_do_combustivel = filtro_do_combustivel;
    }

    public String getFiltro_do_ar() {
        return filtro_do_ar;
    }

    public void setFiltro_do_ar(String filtro_do_ar) {
        this.filtro_do_ar = filtro_do_ar;
    }

    public String getFiltro_do_ar_condicionado() {
        return filtro_do_ar_condicionado;
    }

    public void setFiltro_do_ar_condicionado(String filtro_do_ar_condicionado) {
        this.filtro_do_ar_condicionado = filtro_do_ar_condicionado;
    }

    public String getLubrificante_tipo() {
        return lubrificante_tipo;
    }

    public void setLubrificante_tipo(String lubrificante_tipo) {
        this.lubrificante_tipo = lubrificante_tipo;
    }
}
