package br.com.empessoa8.carcare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import br.com.empessoa8.carcare.data_base.DataBase;
import br.com.empessoa8.carcare.email.Act_Email;
import br.com.empessoa8.carcare.fragments.abastecer.Act_Abastecer;
import br.com.empessoa8.carcare.fragments.arrefecimento_freio.Act_Arrefecimento_Freio;
import br.com.empessoa8.carcare.fragments.correia_tensor.Act_Correias_Tensor;
import br.com.empessoa8.carcare.fragments.ignicao.Act_Ignicao;
import br.com.empessoa8.carcare.fragments.oleos_filtros.Act_Oleo_Filtro;
import br.com.empessoa8.carcare.fragments.viagem.Act_Viagem;
import br.com.empessoa8.carcare.notificacoes.NotificatioReceiverArrefecimentoFreio;
import br.com.empessoa8.carcare.notificacoes.NotificatioReceiverCorreiasTensor;
import br.com.empessoa8.carcare.notificacoes.NotificatioReceiverIgnicao;
import br.com.empessoa8.carcare.notificacoes.NotificatioReceiverOleoFiltro;
import br.com.empessoa8.carcare.persistencia.Per_Arrefecimento_Freio;
import br.com.empessoa8.carcare.persistencia.Per_Correia_Tensor;
import br.com.empessoa8.carcare.persistencia.Per_Oleo_Filtro;
import br.com.empessoa8.carcare.persistencia.Pers_Ignicao;

public class Act_Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txt_informar_manutencao;

    //
    private DataBase dataBase;
    private SQLiteDatabase connBanco;
    private Per_Arrefecimento_Freio per_arrefecimento_freio;
    private Per_Correia_Tensor per_correia_tensor;
    private Per_Oleo_Filtro per_oleo_filtro;
    private Pers_Ignicao pers_ignicao;
    private String dataAtualFormatada;
    private String dataProximaRevisaoArrefecimentoFreio;
    private String dataProximaRevisaoCorreiaTensor;
    private String dataProximaRevisaoIgnicao;
    private String dataProximaRevisaoOleoFiltros;
    private int kmProximaRevisaoArrefecimentoFreio;
    //

    private FloatingActionButton fab_main, fab_abastecer, fab_viagem, fab_servico;
    private Animation fab_open, fab_close, fab_sentido_horario, fab_anti_horario;
    boolean isOpenFab = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_sentido_horario = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_horario);
        fab_anti_horario = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anti_horario);

        fab_main = (FloatingActionButton) findViewById(R.id.fab_main);
        fab_abastecer = (FloatingActionButton) findViewById(R.id.fab_abastecer);
        fab_viagem = (FloatingActionButton) findViewById(R.id.fab_viagem);
        fab_servico = (FloatingActionButton) findViewById(R.id.fab_oleo);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenFab) {
                    fab_main.startAnimation(fab_anti_horario);
                    fab_abastecer.startAnimation(fab_close);
                    fab_viagem.startAnimation(fab_close);
                    fab_servico.startAnimation(fab_close);
                    fab_abastecer.setClickable(false);
                    fab_viagem.setClickable(false);
                    fab_servico.setClickable(false);
                    isOpenFab = false;

                } else {
                    fab_main.startAnimation(fab_sentido_horario);
                    fab_abastecer.startAnimation(fab_open);
                    fab_viagem.startAnimation(fab_open);
                    fab_servico.startAnimation(fab_open);
                    fab_abastecer.setClickable(true);
                    fab_viagem.setClickable(true);
                    fab_servico.setClickable(true);
                    isOpenFab = true;
                }
            }
        });

        fab_servico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Act_Main.this, Act_Oleo_Filtro.class));
            }
        });
        fab_abastecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Act_Main.this, Act_Abastecer.class));
            }
        });
        fab_viagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Act_Main.this, Act_Viagem.class));
            }
        });

        txt_informar_manutencao = (TextView) findViewById(R.id.txt_manutencao_preventica);
        dicasPreventiva();
        conectarBanco();
        verificarDataAtual();
    }

    private void conectarBanco() {
        try {
            dataBase = new DataBase(this);
            connBanco = dataBase.getReadableDatabase();

            per_arrefecimento_freio = new Per_Arrefecimento_Freio(connBanco);
            dataProximaRevisaoArrefecimentoFreio = per_arrefecimento_freio.exibirDataProximarevisao();
            kmProximaRevisaoArrefecimentoFreio = per_arrefecimento_freio.exibirKmProximaRevisao();

            per_correia_tensor = new Per_Correia_Tensor(connBanco);
            dataProximaRevisaoCorreiaTensor = per_correia_tensor.exibirDataProximarevisaoCorreiaTensor();

            per_oleo_filtro = new Per_Oleo_Filtro(connBanco);
            dataProximaRevisaoOleoFiltros = per_oleo_filtro.exibirDataProximarevisaoOleoFiltro();

            pers_ignicao = new Pers_Ignicao(connBanco);
            dataProximaRevisaoIgnicao = pers_ignicao.exibirDataProximarevisao();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void setAlarm() {
        AlarmManager alarmManager_1 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager_2 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager_3 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmManager_4 = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        // Cria um Calendar para o horário estipulado
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);

        long timerCalendar = calendar.getTimeInMillis();
        long timerProgramado = AlarmManager.INTERVAL_DAY;


        Long alertTime = new GregorianCalendar().getTimeInMillis();
        int timeProgramado = 30000;//24 * 60 * 60 * 1000;

        Intent alertIntentArrefecimentoFreio = new Intent(getApplicationContext(), NotificatioReceiverArrefecimentoFreio.class);
        Intent alertIntentCorreiasTensor = new Intent(getApplicationContext(), NotificatioReceiverCorreiasTensor.class);
        Intent alertIntentIgnicao = new Intent(getApplicationContext(), NotificatioReceiverIgnicao.class);
        Intent alertIntentOleoFiltro = new Intent(getApplicationContext(), NotificatioReceiverOleoFiltro.class);

        PendingIntent pendingIntentArrefecimentoFreio = PendingIntent.getBroadcast(getApplicationContext(), 1, alertIntentArrefecimentoFreio,
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentCorreiasTensor = PendingIntent.getBroadcast(getApplicationContext(), 1, alertIntentCorreiasTensor,
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentIgnicao = PendingIntent.getBroadcast(getApplicationContext(), 1, alertIntentIgnicao,
                PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentOleoFiltro = PendingIntent.getBroadcast(getApplicationContext(), 1, alertIntentOleoFiltro,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if ((dataAtualFormatada.equals(dataProximaRevisaoArrefecimentoFreio)) && (!dataProximaRevisaoArrefecimentoFreio.isEmpty())) {
            //setando o alarm
            alarmManager_1.setInexactRepeating(AlarmManager.RTC_WAKEUP, timerCalendar, timerProgramado,
                    pendingIntentArrefecimentoFreio);
        } else {
            alarmManager_1.cancel(pendingIntentArrefecimentoFreio);
        }

        if ((dataAtualFormatada.equals(dataProximaRevisaoCorreiaTensor)) && (!dataProximaRevisaoCorreiaTensor.isEmpty())) {
            //setando o alarm
            alarmManager_2.setInexactRepeating(AlarmManager.RTC_WAKEUP, timerCalendar, timerProgramado,
                    pendingIntentCorreiasTensor);
        } else {
            alarmManager_2.cancel(pendingIntentCorreiasTensor);
        }

        if ((dataAtualFormatada.equals(dataProximaRevisaoIgnicao)) && (!dataProximaRevisaoIgnicao.isEmpty())) {
            //setando o alarm
            alarmManager_3.setInexactRepeating(AlarmManager.RTC_WAKEUP, timerCalendar, timerProgramado,
                    pendingIntentIgnicao);
        } else {
            alarmManager_3.cancel(pendingIntentIgnicao);
        }

        if ((dataAtualFormatada.equals(dataProximaRevisaoOleoFiltros)) && (!dataProximaRevisaoOleoFiltros.isEmpty())) {
            //setando o alarm
            alarmManager_4.setInexactRepeating(AlarmManager.RTC_WAKEUP, timerCalendar, timerProgramado,
                    pendingIntentOleoFiltro);
        } else {
            alarmManager_4.cancel(pendingIntentOleoFiltro);
        }
    }

    private void verificarDataAtual() {
        try {
            Calendar calendar = Calendar.getInstance();
            int ano = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            String mesAtual;
            String diaAtual;
            mes += 1;
            if (mes < 10) {
                mesAtual = "0" + mes;
            } else {
                mesAtual = String.valueOf(mes);
            }
            if (dia < 10) {
                diaAtual = "0" + dia;
            } else {
                diaAtual = String.valueOf(dia);
            }
            dataAtualFormatada = diaAtual + "/" + mesAtual + "/" + ano;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act__main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_abastecer) {
            startActivity(new Intent(Act_Main.this, Act_Abastecer.class));
        } else if (id == R.id.nav_viagem) {
            startActivity(new Intent(Act_Main.this, Act_Viagem.class));
        } else if (id == R.id.nav_ignicao) {
            startActivity(new Intent(Act_Main.this, Act_Ignicao.class));
        } else if (id == R.id.nav_oleo_filtro) {
            startActivity(new Intent(Act_Main.this, Act_Oleo_Filtro.class));
        } else if (id == R.id.nav_send_email) {
            startActivity(new Intent(Act_Main.this, Act_Email.class));
        } else if (id == R.id.nav_correia_tensor) {
            startActivity(new Intent(Act_Main.this, Act_Correias_Tensor.class));
        } else if (id == R.id.nav_arrefecimento_freio) {
            startActivity(new Intent(Act_Main.this, Act_Arrefecimento_Freio.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void dicasPreventiva() {
        String[] prevencao = {
                "ÓLEO DO MOTOR: \nDeve ser verificado toda semana. Antes de usar o carro pela primeira vez no dia e em um piso nivelado," +
                        " retire a vareta do óleo e limpe-a para poder medir corretamente o nível do óleo. Faça isso usando um papel " +
                        "ou pano.  Em seguida, basta inserí-la novamente. Ao retirá-la: a marca do óleo deve estar entre as marcas “Mín” e “Máx” da vareta.",
                "FREIOS: \nEm geral, o fluído do freio deve ser trocado a cada dois anos, ou conforme a recomendação da fabricante," +
                        " já que tem a propriedade de absorver água, que pode evaporar com o calor dos freios e formar vapor no sistema, " +
                        "que prejudica bastante as frenagens. Se estiverem abaixo da espessura minima, as pastilhas e lonas de freio devem ser substituídas.",
                "CORREIA DENTADA: \nNão vale a pena correr o risco dela se romper, o que pode causar sérios danos ao motor, como o empenamento de válvulas" +
                        " ou furos nos pistões. Deve ser trocada por um bom mecânico. Os prazos de troca variam de acordo com cada modelo de carro e " +
                        "variam entre 30 mil e 50 mil km.",
                "ÁGUA DO RADIADOR: \nAssim como o nível do óleo do motor, deve ser vista toda semana. Com o motor frio e o veículo nivelado, " +
                        "a água deve estar entre o “Mín” e o “Máx” indicados no reservatório. É muito importante que o líquido de arrefecimento " +
                        "tenha a proporção correta de aditivo de boa qualidade, nunca apenas água pura.",
                "VELAS DA IGNIÇÃO: \nSe não estiverem em bom estado o consumo aumenta e o desempenho diminui. Portanto, são fundamentais para o" +
                        " bom funcionamento do motor. O tempo entre as trocas varia conforme a recomendação do fabricante. Em geral, os modelos " +
                        "convencionais duram entre 20.000 e 30.000 kms.",
                "FILTROS: \nNo caso dos filtros de ar deve-se fazer uma limpeza com ar comprimido a cada 7.000 km e trocá-los quando estiverem prejudicando" +
                        " a vazão do ar para dentro do motor. Os de combustível são trocados normalmente a cada 10.000 kms. E os de óleo alternadamente entre " +
                        "as trocas de óleo do motor.",
                "PNEUS E RODAS: \nOs pneus devem ser calibrados frios , a cada 15 dias e antes de viajar.Observe um triângulo ou as letras TWI, " +
                        "impressas nas laterais do pneu. Quando o nível de desgaste atingir este ponto é hora de trocar os pneus. " +
                        "Fazer o rodízio, o alinhamento e balanceamento a cada 10.000 km aumenta a vida dos pneus, melhorando a segurança e dirigibilidade do veículo.",
                "SUSPENSÃO: \nEntre os principais componentes estão os amortecedores e molas. Os primeiros costumam durar em torno de 30.000 kms, " +
                        "mas podem não aguentar tudo isso se o carro sempre passa por piso irregular ou muito esburacado. Molas com elos desgatados " +
                        "já devem estar precisando serem trocadas. Também vale verificar folgas, trincas e quebras em juntas dos braços e barras da suspensão.",
                "ÓLEO DO CÂMBIO: \nA grande maioria dos carros contam com caixas de câmmbios  imantadas e não querem troca de óleo. Nos outros, " +
                        "o lubrificante deve ser verificado a cada 30 mil quilômetros. Vazamentos são indicações de que o carro precisa de reparos. " +
                        "Para os carros de câmbio automático, é importante a troca de óleo e de filtro conforme indicação do fabricante.",
                "BATERIA: \nA maioria das baterias vendidas atualmente no mercado não que exige a adição da água e requer pouca manutencão. " +
                        "Deve-se apenas verificar se os cabos estão limpos e bem fixados aos pólos. Se o carro foi ficar parado por muito tempo," +
                        " deve-se desligar o cabo do polo negativo para que não descarregue."

        };
        Random random = new Random();
        int numAle = random.nextInt(prevencao.length);
        txt_informar_manutencao.setText(prevencao[numAle]);
    }

    @Override
    protected void onStop() {
        super.onStop();
        conectarBanco();
        setAlarm();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connBanco != null) {
            connBanco.close();
        }
    }
}
