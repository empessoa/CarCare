package br.com.empessoa8.carcare.data_base;

/**
 * Created by elias on 13/06/2017.
 */

public class ScriptSQL {

    public static String getCreateTableAbastecer(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS ABASTECER( ");
        sqlBuilder.append("_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("data_do_abastecimento VARCHAR(10), ");
        sqlBuilder.append("combustivel_tipo VARCHAR(10), ");
        sqlBuilder.append("km_do_abastecimento INTEGER(10), ");
        sqlBuilder.append("valor_total_abastecido INTEGER(10), ");
        sqlBuilder.append("valor_do_litro INTEGER(10), ");
        sqlBuilder.append("total_de_litro INTEGER(4)");
        sqlBuilder.append(");");
        return  sqlBuilder.toString();
    }

    public static String getCreateTableIgnicao(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS IGNICAO( ");
        sqlBuilder.append("_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("data_do_servico VARCHAR(10), ");
        sqlBuilder.append("km_do_servico INTEGER(10), ");
        sqlBuilder.append("valor_do_servico INTEGER(10), ");
        sqlBuilder.append("data_proxima_revisao VARCHAR(10), ");
        sqlBuilder.append("km_proxima_revisao VARCHAR(10), ");
        sqlBuilder.append("velas_ignicao VARCHAR(10)");
        sqlBuilder.append(");");
        return  sqlBuilder.toString();
    }

    public static String getCreateTableOleoFreio(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS OLEOFREIO( ");
        sqlBuilder.append("_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("data_do_servico VARCHAR(10), ");
        sqlBuilder.append("km_do_servico INTEGER(10), ");
        sqlBuilder.append("valor_do_servico INTEGER(10), ");
        sqlBuilder.append("data_proxima_revisao VARCHAR(10), ");
        sqlBuilder.append("km_proxima_revisao VARCHAR(10), ");
        sqlBuilder.append("oleo_do_motor VARCHAR(10), ");
        sqlBuilder.append("oleo_da_direcao VARCHAR(10), ");
        sqlBuilder.append("filtro_do_oleo VARCHAR(10), ");
        sqlBuilder.append("filtro_do_combustivel VARCHAR(10), ");
        sqlBuilder.append("filtro_do_ar VARCHAR(10), ");
        sqlBuilder.append("filtro_do_ar_condicionado VARCHAR(10), ");
        sqlBuilder.append("lubrificante_tipo VARCHAR(10)");
        sqlBuilder.append(");");
        return  sqlBuilder.toString();
    }
    public static String getCreateTableCorreiaTensor(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS CORREIATENSOR( ");
        sqlBuilder.append("_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("data_do_servico VARCHAR(10), ");
        sqlBuilder.append("km_do_servico INTEGER(10), ");
        sqlBuilder.append("valor_do_servico INTEGER(10), ");
        sqlBuilder.append("data_proxima_revisao VARCHAR(10), ");
        sqlBuilder.append("km_proxima_revisao VARCHAR(10), ");
        sqlBuilder.append("correia_dentada VARCHAR(10), ");
        sqlBuilder.append("correia_poly_v VARCHAR(10), ");
        sqlBuilder.append("tensor_correia VARCHAR(10), ");
        sqlBuilder.append("bomba_de_agua VARCHAR(10)");
        sqlBuilder.append(");");
        return  sqlBuilder.toString();
    }

    public static String getCreateTableArrefecimentoFreio(){
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS ARREFECIMENTOFREIO( ");
        sqlBuilder.append("_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("data_do_servico VARCHAR(10), ");
        sqlBuilder.append("km_do_servico INTEGER(10), ");
        sqlBuilder.append("valor_do_servico INTEGER(10), ");
        sqlBuilder.append("data_proxima_revisao VARCHAR(10), ");
        sqlBuilder.append("km_proxima_revisao VARCHAR(10), ");
        sqlBuilder.append("agua_arrefecimento VARCHAR(10), ");
        sqlBuilder.append("oleo_freio VARCHAR(10), ");
        sqlBuilder.append("pastilhas_freios VARCHAR(10)");
        sqlBuilder.append(");");
        return  sqlBuilder.toString();
    }



}
