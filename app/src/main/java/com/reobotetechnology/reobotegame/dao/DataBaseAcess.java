package com.reobotetechnology.reobotegame.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.util.Log;

import com.reobotetechnology.reobotegame.model.BooksOfBibleModel;
import com.reobotetechnology.reobotegame.model.CheckChaptherModel;
import com.reobotetechnology.reobotegame.model.DescriptionBookModel;
import com.reobotetechnology.reobotegame.model.QuestionModel;
import com.reobotetechnology.reobotegame.model.VerseDayModel;
import com.reobotetechnology.reobotegame.model.VersesBibleModel;
import com.reobotetechnology.reobotegame.model.ThemeslistModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataBaseAcess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DataBaseAcess instance;
    private static String TABELA_PERGUNTAS = "perguntas";
    private static String TABELA_TEMAS = "temas";
    private static String TABELA_VERSE_DAY = "verse_day";
    private static String TABELA_DESCRIPTION_BOOK = "description_book";
    private static String TABELA_CHECK_CHAPTHER = "check_chapther";
    private Cursor cursor = null;
    private int id = 0;


    private DataBaseAcess(Context context) {

        this.openHelper = new DataBaseOpenHelper(context);
    }

    public static DataBaseAcess getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseAcess(context);
        }

        return instance;
    }

    //abrirDatabase
    private void open() {
        this.db = openHelper.getWritableDatabase();
    }

    //fecharDatabase

    private void close() {

        if (db != null) {
            this.db.close();
        }
    }

    public void onCreate() {

        verificarTabelas();

        open();

        if (id == 0) {

            String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_PERGUNTAS
                    + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "pergunta VARCHAR(100) NOT NULL, " +
                    "questaoA VARCHAR(50)," +
                    "questaoB VARCHAR(50)," +
                    "questaoC VARCHAR(50)," +
                    "questaoD VARCHAR(50)," +
                    "questaoCorreta VARCHAR(50), " +
                    "questaoDica VARCHAR(100) " +
                    "); ";

            String sqlThemes = "CREATE TABLE IF NOT EXISTS " + TABELA_TEMAS
                    + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "book_id INTEGER NOT NULL, " +
                    "chapter_id INTEGER NOT NULL," +
                    "verse_id INTEGER NOT NULL," +
                    "theme_name VARCHAR(50) NOT NULL" +
                    "); ";

            String sqlVerseDay = "CREATE TABLE IF NOT EXISTS " + TABELA_VERSE_DAY
                    + " (" +
                    "id INTEGER PRIMARY KEY, " +
                    "book_id INTEGER NOT NULL, " +
                    "chapter_id INTEGER NOT NULL," +
                    "verse_id INTEGER NOT NULL," +
                    "date VARCHAR(20) NOT NULL," +
                    "time VARCHAR(20) NOT NULL" +
                    "); ";

            String sqlDescriptionBook = "CREATE TABLE IF NOT EXISTS " + TABELA_DESCRIPTION_BOOK
                    + " (" +
                    "book_id INTEGER PRIMARY KEY NOT NULL, " +
                    "sigle VARCHAR(50) NOT NULL," +
                    "author VARCHAR(50) NOT NULL," +
                    "description VARCHAR(800) NOT NULL," +
                    "availabled INTEGER NOT NULL, " +
                    "favorited INTEGER(1) NOT NULL," +
                    "date VARCHAR(20) NOT NULL," +
                    "learning INTEGER(3) NOT NULL," +
                    "reference VARCHAR(50) NOT NULL" +
                    "); ";


            String sqlCheckChapther = "CREATE TABLE IF NOT EXISTS " + TABELA_CHECK_CHAPTHER
                    + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "book_id INTEGER NOT NULL, " +
                    "chapter_id INTEGER NOT NULL" +
                    "); ";


            try {
                db.execSQL(sql);
                db.execSQL(sqlThemes);
                db.execSQL(sqlVerseDay);
                db.execSQL(sqlDescriptionBook);
                db.execSQL(sqlCheckChapther);

                this.inserirPerguntas();
                this.insertThemes();
                this.insertDescriptionBook();
                this.insertVerseDay();

                Log.i("INFO DB", "Sucesso ao criar as tabelas");

            } catch (Exception e) {
                Log.i("INFO DB", "Erro ao criar a tabela: " + e.getMessage());
            }

        } else {
            Log.i("INFO DB", "NÃO Executei");
        }

        close();

    }

    public void onUpdate() {

        open();

        String sql = "DROP TABLE IF EXISTS perguntas";
        String sql2 = "DROP TABLE IF EXISTS temas";
        String sql3 = "DROP TABLE IF EXISTS verse_day";
        String sql4 = "DROP TABLE IF EXISTS description_book";
        String sql5 = "DROP TABLE IF EXISTS check_chapther";


        try {
            db.execSQL(sql);
            db.execSQL(sql2);
            db.execSQL(sql3);
            db.execSQL(sql4);
            db.execSQL(sql5);
            this.onCreate();
            Log.i("INFO DB", "Sucesso ao atualizar App");
        } catch (Exception e) {
            Log.i("INFO DB", "Erro ao atualizar App: " + e.getMessage());
        }

        close();
    }

    //Verifica se as tabelas Existem senão exister chama o onCreate
    private void verificarTabelas() {

        try {
            open();
            String sql = "SELECT * FROM perguntas";
            try {
                cursor = db.rawQuery(sql, null);

                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {

                            id = cursor.getInt(0);


                        } while (cursor.moveToNext());
                    }
                }
                cursor.close();
            } catch (Exception e) {
                id = 0;
            }

            close();

        } catch (Exception e) {
            e.printStackTrace();
            id = 0;
        }

    }

    public List<BooksOfBibleModel> listAllBooks() {

        open();

        List<BooksOfBibleModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM book";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(1);
                    int testamento = cursor.getInt(2);
                    String nome = cursor.getString(3);
                    BooksOfBibleModel b = new BooksOfBibleModel(id, testamento, 0, nome);
                    lista.add(b);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;

    }

    public List<BooksOfBibleModel> listarAntigoTestamento() {

        open();

        List<BooksOfBibleModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM book where Testament_reference_id=1";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(1);
                    int testamento = cursor.getInt(2);
                    String nome = cursor.getString(3);
                    BooksOfBibleModel b = new BooksOfBibleModel(id, testamento, 0, nome);
                    lista.add(b);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;

    }

    public List<BooksOfBibleModel> listarNovoTestamento() {

        open();

        List<BooksOfBibleModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM book where Testament_reference_id=2";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(1);
                    int testamento = cursor.getInt(2);
                    String nome = cursor.getString(3);
                    BooksOfBibleModel b = new BooksOfBibleModel(id, testamento, 0, nome);
                    lista.add(b);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;

    }

    //Listar Livros Pelo Nome like% WHERE text LIKE '"+theme+"%'
    public List<BooksOfBibleModel> listarLivrosPesquisa(String texto) {

        open();
        List<BooksOfBibleModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM book WHERE Name LIKE '" + texto + "%'";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(1);
                    int testamento = cursor.getInt(2);
                    String nome = cursor.getString(3);
                    BooksOfBibleModel b = new BooksOfBibleModel(id, testamento, 0, nome);
                    lista.add(b);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();

        return lista;

    }

    public List<String> listarLivrosNome() {
        open();

        List<String> lista = new ArrayList<>();

        String sql = "SELECT * FROM book";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(1);
                    int testamento = cursor.getInt(2);
                    String nome = cursor.getString(3);
                    BooksOfBibleModel b = new BooksOfBibleModel(id, testamento, 0, nome);
                    lista.add(b.getNome());

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;
    }

    public List<VersesBibleModel> findThemes(int book_id, int chapter_id, int verse_id) {


        open();
        List<VersesBibleModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM verse where book_id=" + book_id + " and chapter=" + chapter_id + " and verse=" + verse_id + "";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(0);
                    int livro = cursor.getInt(1);
                    int cap = cursor.getInt(2);
                    int verso = cursor.getInt(3);
                    String nome = cursor.getString(4);
                    VersesBibleModel v = new VersesBibleModel(id, livro, cap, verso, nome, false);
                    lista.add(v);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;
    }

    public String findVerses(int book_id, int chapter_id, int verse_id) {

        String textReturn = "";

        open();
        List<VersesBibleModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM verse where book_id=" + book_id + " and chapter=" + chapter_id + " and verse=" + verse_id + "";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {


                    textReturn = cursor.getString(4);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return textReturn;
    }

    public List<VersesBibleModel> listarVersos(String l, String c) {

        open();
        List<VersesBibleModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM verse where Book_id=" + l + " and Chapter=" + c + "";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(0);
                    int livro = cursor.getInt(1);
                    int cap = cursor.getInt(2);
                    int verso = cursor.getInt(3);
                    String nome = cursor.getString(4);
                    VersesBibleModel v = new VersesBibleModel(id, livro, cap, verso, nome, false);
                    lista.add(v);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;

    }

    public List<VersesBibleModel> listAllVerses(String text) {

        open();
        List<VersesBibleModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM verse where text LIKE '%" + text + "%'";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(0);
                    int livro = cursor.getInt(1);
                    int cap = cursor.getInt(2);
                    int verso = cursor.getInt(3);
                    String nome = cursor.getString(4);
                    VersesBibleModel v = new VersesBibleModel(id, livro, cap, verso, nome, false);
                    lista.add(v);

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;

    }

    //PESQUISAR PELO NUM DO LIVRO
    public Integer num(int livro) {

        int num = 0;
        open();

        String sql = "SELECT * FROM verse where Book_id=" + livro;
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(0);
                    int livro2 = cursor.getInt(1);
                    int cap = cursor.getInt(2);
                    int verso = cursor.getInt(3);
                    String nome = cursor.getString(4);
                    num = cap;
                    VersesBibleModel v = new VersesBibleModel(id, livro2, cap, verso, nome, false);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return num;

    }

    //PESQUISAR PELO NUM DO LIVRO
    public String findBook(int livro) {

        String name = "";
        open();

        String sql = "SELECT * FROM book where id=" + livro;
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    name = cursor.getString(3);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return name;

    }

    //PESQUISAR PELO NOME DO LIVRO
    public Integer findBookName(String livro) {

        int id = 0;
        open();

        String sql = "SELECT * FROM book where name='" + livro + "'";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    id = cursor.getInt(1);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return id;

    }


    //PALAVRA DO DIA

    public Integer listarVersosPalavra(int l, int c) {

        int qtdVersos = 0;

        open();

        String sql = "SELECT * FROM verse where Book_id=" + l + " and Chapter=" + c + "";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    qtdVersos = cursor.getInt(3);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return qtdVersos;

    }

    public List<BooksOfBibleModel> listarLivroPalavra() {

        List<BooksOfBibleModel> palavra = new ArrayList<>();

        open();


        String sql = "SELECT * FROM book ORDER BY RANDOM() LIMIT 1";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(1);
                    int testamento = cursor.getInt(2);
                    String nome = cursor.getString(3);
                    BooksOfBibleModel b = new BooksOfBibleModel(id, testamento, 0, nome);
                    palavra.add(b);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return palavra;
    }

    public Integer capituloPalavra(int livro) {

        int num = 0;
        open();

        String sql = "SELECT * FROM verse where Book_id=" + livro + " ORDER BY RANDOM() LIMIT 1";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(0);
                    int livro2 = cursor.getInt(1);
                    int cap = cursor.getInt(2);
                    int verso = cursor.getInt(3);
                    String nome = cursor.getString(4);
                    num = cap;
                    VersesBibleModel v = new VersesBibleModel(id, livro2, cap, verso, nome, false);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return num;

    }

    public List<VersesBibleModel> listarVersiculoPalavra(int livro, int capitulo) {

        List<VersesBibleModel> lista = new ArrayList<>();
        open();


        String sql = "SELECT * FROM verse where Book_id=" + livro + " and Chapter=" + capitulo + " ORDER BY RANDOM() LIMIT 1";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {


                    int id = cursor.getInt(0);
                    int livro2 = cursor.getInt(1);
                    int cap = cursor.getInt(2);
                    int verso = cursor.getInt(3);
                    String nome = cursor.getString(4);
                    VersesBibleModel v = new VersesBibleModel(id, livro, cap, verso, nome, false);
                    lista.add(v);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;
    }

    //Perguntas

    private void inserirPerguntas() {

        List<QuestionModel> list = new ArrayList<>();
        list.add(new QuestionModel(1, "'Deuteronômio', nome do quinto livro da Bíblia, quer dizer:", "Quinto Livro", "Leis de Moisés", "Aliança", "Segunda Lei", "Segunda Lei", ""));
        list.add(new QuestionModel(2, "A Páscoa, antes de ser uma festa cristã, era (e é) uma festa judaica que recordava:", "A Criação do Mundo", "A libertação da escravidão de Israel no Egito", "As vitórias do rei Davi", "Nunca foi uma festa dos judeus", "A libertação da escravidão de Israel no Egito", "Deus enviou as 10 pragas"));
        list.add(new QuestionModel(3, "Jesus foi batizado:", "no rio Jordão", "no rio Eufrates", "no rio Nilo", "no lago de Tiberíades", "no rio Jordão", ""));
        list.add(new QuestionModel(4, "O que significa o nome ‘Emanuel’?", "Deus é mais", "Deus conosco", "Jesus", "Luz do mundo", "Deus conosco", ""));
        list.add(new QuestionModel(5, "Paulo, segundo Atos dos Apóstolos, aprovou a morte daquele que seria o primeiro mártir cristão. O seu nome é:", "Barnabé", "Estêvão", "Silas", "João Marcos", "Estêvão", "Foi apedrejado até a morte Atos 7:58"));
        list.add(new QuestionModel(6, "Qual das seguintes línguas não foi usada na escrita dos textos originais da Bíblia?", "Grego", "Latim", "Hebraico", "Aramaico", "Latim", ""));
        list.add(new QuestionModel(7, "Gênesis é o primeiro livro da Bíblia. O seu nome quer dizer:", "Revelação", "Sabedoria", "Origem", "Criação do Mundo", "Origem", ""));
        list.add(new QuestionModel(8, "Alguns Evangelhos apresentam os antepassados de Jesus as genealogias. Quais?", "Mateus e Lucas", "Todos", "Nenhum", "Marcos e João", "Mateus e Lucas", "Um dos dois evangelistas era médico"));
        list.add(new QuestionModel(9, "Paulo era natural de:", "Roma", "Corinto", "Tessalônica", "Tarso", "Tarso", ""));
        list.add(new QuestionModel(10, "Em que cidade Jesus nasceu?", "Nazaré", "Cafarnaum", "Belém", "Betsaida", "Belém", "Tem o mesmo nome da capital do Estado do Pará no Brasil"));
        list.add(new QuestionModel(11, "A quem Paulo chamou de 'meu companheiro de lutas'?", "Apolo", "Arquipo", "Silas", "Áfia", "Arquipo", ""));
        list.add(new QuestionModel(12, "Qual era o nome da serpente de bronze que Moisés tinha feito?", "Aserá", "Leviatã", "Neustã", "Baal", "Neustã", ""));
        list.add(new QuestionModel(13, "Quantas pragas foram enviadas ao Egito?", "7 Pragas", "10 Pragas", "17 Pragas", "20 Pragas", "10 Pragas", ""));
        list.add(new QuestionModel(14, "Qual o nome de uma das esposas de Jacó?", "Raquel", "Joana", "Eva", "Ada", "Raquel", ""));
        list.add(new QuestionModel(15, "Qual foi o primeiro filho de Adão e Eva?", "Caim", "Abel", "Sete", "Enoque", "Caim", ""));
        list.add(new QuestionModel(16, "Como são chamados os 5 primeiros livros da Bíblia?", "Septuaginta", "Profecias", "Pentateuco", "Cânticos", "Pentateuco", ""));
        list.add(new QuestionModel(17, "Qual o maior livro da Bíblia?", "Mateus", "Salmos", "Isaias", "Gênesis", "Salmos", ""));
        list.add(new QuestionModel(18, "Quem foi o profeta que voou num redemoinho?", "Jonas", "Eliseu", "Elias", "Moisés", "Elias", ""));
        list.add(new QuestionModel(19, "Qual desses homens morreu decapitado?", "Davi", "Pedro", "Tiago", "João Batista", "João Batista", ""));
        list.add(new QuestionModel(20, "O véu do santuário rasgou-se de alto a baixo em que ocasião?", "Na santa ceia", "Festa de pentecostes", "Na ressurreição", "Na morte de Cristo", "Na morte de Cristo", ""));
        list.add(new QuestionModel(21, "Em qual livro da Bíblia é descrita a Nova Jerusalém?", "Apocalipse", "Salmos", "1 Reis", "Hebreus", "Apocalipse", "Este livro também é conhecido como: 'O LIVRO DA REVELAÇÃO' "));
        list.add(new QuestionModel(22, "Qual é o versículo mais longo da Bíblia?", "1 Samuel 16:7", "Rute 1:1", "Ester 8:9", "João 3:16", "Ester 8:9", "Jovem que casou com o Rei Assuero"));
        list.add(new QuestionModel(23, "Qual instrumento Davi gostava de tocar?", "Flauta", "Violão", "Harpa", "Tambor", "Harpa", ""));
        list.add(new QuestionModel(24, "Quando Jesus nasceu, onde Ele foi colocado?", "Foi colocado em uma Caixa", "Foi colocado em uma Cama", "Foi colocado em um Trono", "Foi colocado em uma Manjedoura", "Foi colocado em uma Manjedoura", ""));
        list.add(new QuestionModel(25, "Quando a mulher com fluxo de sangue tocou nas vestes de Jesus, no que ela pensava?", "'Se eu tão somente tocar em seu manto, ficarei curada'", "'Tenho que chamar a atenção de Jesus para ser curada'", "'Tenho que interromper o caminho de Jesus'", "'Tenho que passar na frente de Jairo'", "'Se eu tão somente tocar em seu manto, ficarei curada'", ""));
        list.add(new QuestionModel(26, "Na parábola do semeador, quais sementes que cresceram e deram uma boa colheita?", "As sementes que caíram nas pedras", "As sementes que caíram em boa terra", "As sementes que caíram entre os espinhos", "As sementes que caíram na água", "As sementes que caíram em boa terra", ""));
        list.add(new QuestionModel(27, "Quem são conhecidos como os patriarcas na Bíblia?", "Jesus, Maria e José", "Pedro, João e Tiago", "Paulo, Silas e João Marcos", "Abraão, Isaque e Jacó", "Abraão, Isaque e Jacó", ""));
        list.add(new QuestionModel(28, "Quem foi o 'assistente' do profeta Elias?", "Micaias", "Adonias", "Inlá", "Eliseu", "Eliseu", ""));
        list.add(new QuestionModel(29, "Na transfiguração, quem apareceu ao lado de Jesus?", "Elias e Enoque", "Abraão e Isaque", "Elias e Moisés", "Jacó e José", "Elias e Moisés", ""));
        list.add(new QuestionModel(30, "Quem era os gentios na Bíblia?", "Eram os judeus", "Eram os religiosos", "Eram os 'não judeus'", "Eram os pecadores", "Eram os 'não judeus'", ""));
        list.add(new QuestionModel(31, "O que aconteceu quando Paulo e Silas louvavam na prisão?", "Houve um terremoto na prisão e todas as portas se abriram!", "Os saldados pediram para que os dois se calassem", "Os dois foram agredidos pelos outros prisioneiros", "Os prisioneiros começaram a gritar", "Houve um terremoto na prisão e todas as portas se abriram!", "Atos 16:25-31"));
        list.add(new QuestionModel(32, "Quem era a mãe de Samuel?", "Penina", "Maria", "Ana", "Isabel", "Ana", "Fez um voto com Deus"));
        list.add(new QuestionModel(33, "Quem a Bíblia diz que foi pior que todos os reis de Israel?", "Salomão", "Acabe", "Davi", "Saul", "Acabe", "Foi casado com Jezabel"));
        list.add(new QuestionModel(34, "Quem era a mãe de Ismael, filho de Abraão?", "Hagar", "Sara", "Ana", "Maria Madalena", "Hagar", "Serva que se deitou com Abraão"));
        list.add(new QuestionModel(35, "Quando Moisés foi colocado num cesto e lançado no rio, quem o encontrou?", "A rainha de Sabá", "A filha do faraó", "Joquebede", "Um escravo hebreu", "A filha do faraó", "Era parente de Faraó"));
        list.add(new QuestionModel(36, "Quem foi o Rei da Babilônia?", "Jeocaz", "Nabucodonosor", "Davi", "Faraó", "Nabucodonosor", "Nome dificl de escrever"));
        list.add(new QuestionModel(37, "Como morreu Judas?", "Assassinado", "Acidente", "Suicídio", "Cruscificado", "Suicídio", "Mateus 27:5, Atos 1:18"));
        list.add(new QuestionModel(38, "Sadraque, Mezaque e Abedenego foram jogados aonde?", "Na cova dos leões", "Na fornalha", "No Coliseu", "No mar", "Na fornalha", "Lugar muito quente"));
        list.add(new QuestionModel(39, "Qual foi o primeiro nome de Abraão?", "Adão", "Abrão", "Israel", "Enoque", "Abrão", ""));
        list.add(new QuestionModel(40, "Quanto tempo durou a chuva do dilúvio?", "40 dias e 40 noites", "7 dias e 7 noites", "3 dias e 3 noites", "1 ano", "40 dias e 40 noites", ""));
        list.add(new QuestionModel(41, "Quem foi lançado na cova dos leões?", "Daniel", "Paulo", "Pedro", "Davi", "Daniel", "Orava três vezes por dia"));
        list.add(new QuestionModel(42, "Quem ensinou aos discípulos a oração do 'Pai nosso?'", "Pedro", "Maria", "Jesus", "João Batista", "Jesus", "Era filho de um Carpinteiro"));
        list.add(new QuestionModel(43, "Qual é o último capítulo da Bíblia?", "Apocalipse 20", "Apocalipse 22", "Apocalipse 7", "3 João 1", "Apocalipse 22", "Versículo 14- Bem-aventurados aqueles que lavam as suas vestes [no sangue do Cordeiro] para que tenham direito à arvore da vida, e possam entrar na cidade pelas portas."));
        list.add(new QuestionModel(44, "Quem é chamado na Bíblia como o 'Príncipe da Paz'?", "Abraão", "Moisés", "Miguel", "Jesus", "Jesus", "Também conhecido como: 'O PÃO DA VIDA'"));
        list.add(new QuestionModel(45, "Quem negou Jesus 3 vezes?", "João", "Paulo", "Lucas", "Pedro", "Pedro", "Sua profissão era pescador"));
        list.add(new QuestionModel(46, "Quantos discípulos tinha Jesus", "5", "3", "12", "25", "12", "O mesmo número de tribos em Israel"));
        list.add(new QuestionModel(47, "Qual o nome do anjo que apareceu a Maria, mãe de Jesus?", "Miguel", "Gabriel", "Rafael", "Lúcifer", "Gabriel", "Foi o mesmo anjo que apareceu para Zacarias pai de João Batista"));
        list.add(new QuestionModel(48, "Quantos livros tem na Bíblia", "65 Livros", "62 Livros", "66 Livros", "70 Livros", "66 Livros", "37 Livros + 29 Livros"));
        list.add(new QuestionModel(49, "Quem ajudou Jesus a carregar a cruz?", "Judas", "Pedro", "Lucas", "Simão Cirineu", "Simão Cirineu", ""));
        list.add(new QuestionModel(50, "Antes de ser Rei, qual era o trabalho de Davi?", "Padeiro", "Carpinteiro", "Pastor de ovelhas", "Caçador", "Pastor de ovelhas", "Cuidar das ovelhas"));
        list.add(new QuestionModel(51, "Quem perseguiu Davi?", "Samuel", "Faraó", "Nabucodonosor", "Saul", "Saul", "Primeiro Rei de Israel"));
        list.add(new QuestionModel(52, "O que a mulher de Samaria saiu para fazer?", "Plantar", "Orar", "Tirar água", "Colher trigo", "Tirar água", "Jesus é a fonte inesgotável de água"));
        list.add(new QuestionModel(53, "Depois que Pedro negou a Jesus pela terceira vez...", "O céu abriu", "o véu do templo se rasgou", "o galo cantou", "o céu ficou em trevas", "o galo cantou", "Um animal cantou"));
        list.add(new QuestionModel(54, "Sansão se apaixonou por qual mulher?", "Débora", "Ester", "Rute", "Dalila", "Dalila", "Cortou o cabelo de Sansão"));
        list.add(new QuestionModel(55, "Eliseu sucedeu a qual profeta?", "Davi", "Elias", "Jeremias", "João Batista", "Elias", "Orou ao Senhor para cair fogo do céu"));
        list.add(new QuestionModel(56, "Quem escreveu Cantares?", "Davi", "Lucas", "Moisés", "Salomão", "Salomão", "Tinha 1000 mulheres"));
        list.add(new QuestionModel(57, "Quem NÃO foi Apóstolo?", "Paulo", "Pedro", "Jeremias", "João", "Jeremias", "Casa do oleiro"));
        list.add(new QuestionModel(58, "Quem foi governador do Egito?", "Abraão", "José", "Isaque", "Ismael", "José", "Foi vendido pelos seus irmãos quando tinha 17 anos"));
        list.add(new QuestionModel(59, "Lázaro foi ressuscitado depois de quantos dias?", "3 dias", "2 dias", "1 dia", "4 dias", "4 dias", "Mesma quantidade de estações do ano"));
        list.add(new QuestionModel(60, "Quanto tempo o povo hebreu ficou no deserto?", "40 anos", "40 dias", "4 anos", "400 anos", "40 anos", ""));
        list.add(new QuestionModel(61, "Quanto tempo o povo hebreu ficou como escravo no Egito?", "400 anos", "430 anos", "500 anos", "100 anos", "430 anos", ""));
        list.add(new QuestionModel(62, "Quem escreveu o livro de Lamentações?", "Habacuque", "Elias", "Neemias", "Jeremias", "Jeremias", "Casa do oleiro"));
        list.add(new QuestionModel(63, "O que quer dizer 'Rabi'?", "Pai", "Mestre", "Amigo", "Rei", "Mestre", "Chamavam Jesus assim"));
        list.add(new QuestionModel(64, "Quem escreveu o livro de Atos dos Apóstolos? ", "João Marcos", "Pedro", "Paulo", "Lucas", "Lucas", "Era médico e advogado de Paulo"));
        list.add(new QuestionModel(65, "Qual era a profissão de José, pai de Jesus?", "Cobrador de impostos", "Carpinteiro", "Pastor de ovelhas", "Pescador", "Carpinteiro", "Mateus 13:55"));
        list.add(new QuestionModel(66, "'Aquele que está em Cristo nova criatura é; as coisas velhas já passaram;...'", "'eis que passaram da morte para a vida'", "'eis que já ressuscitou dos mortos'", "'eis que estão vivos'", "'eis que tudo se fez novo'", "'eis que tudo se fez novo'", "2 Coríntios 5:17"));
        list.add(new QuestionModel(67, "Qual foi o primeiro milagre de Jesus?", "A transformação da água em vinho", "A ressureição de Lázaro", "A multiplicação dos pães e peixes", "A ressureição da filha de Jairo", "A transformação da água em vinho", "Bodas de Caná da Galiléia"));
        list.add(new QuestionModel(68, "Quem escreveu o livro de Apocalipse?", "João", "Lucas", "João Batista", "Paulo", "João", "O discípulo amado escreveu quando foi arrebatado na ilha de Patmos"));
        list.add(new QuestionModel(69, "Quem foi arrebatado por Deus e não viu a morte?", "Moisés", "Jó", "Enoque", "Davi", "Enoque", "Seu livro não faz parte da Bíblia"));
        list.add(new QuestionModel(70, "Qual o nome do lugar onde habitavam Adão e Eva?", "Samaria", "Judá", "Jardim do Éden", "Torre de Babel", "Jardim do Éden", "Deus descia neste lugar todos os dias para conversar com Adão"));
        list.add(new QuestionModel(80, "Qual profeta foi engolido por um grande peixe?", "Jonas", "Elias", "Malaquias", "Davi", "Jonas", "Era de Nazaré"));
        list.add(new QuestionModel(81, "Quem subiu na árvore para ver Jesus passar?", "Ananias", "Tadeu", "Zaqueu", "Pedro", "Zaqueu", "Era um homem rico, com uma boa carreira como chefe dos publicanos em Jericó"));
        list.add(new QuestionModel(82, "Qual foi o Apóstolo que ficou temporariamente cego?", "Tiago", "Paulo", "João", "Pedro", "Paulo", "Também era conhecido como Saulo. Atos 9"));
        list.add(new QuestionModel(83, "Quem escreveu o Salmo 23?", "Davi", "Salomão", "Moisés", "Absalão", "Davi", "Era Pastor de ovelhas"));
        list.add(new QuestionModel(84, "Jesus curou 10 leprosos. Quantos voltaram para agradecer?", "9", "TODOS", "NENHUM", "1", "1", "Só 10% voltaram"));
        list.add(new QuestionModel(85, "Quem entrou na Terra Prometida?", "Moisés", "Arão", "Noé", "Josué", "Josué", "Foi o sucessor de Moisés na liderança de Israel"));
        list.add(new QuestionModel(86, "Quem derrubou as Muralhas de Jericó", "Moisés", "Josué", "Arão", "Deus", "Deus", "Josué 6"));
        list.add(new QuestionModel(87, "Quem foi colocado num cesto e lançado no rio ainda quando era bebê?", "Moisés", "Samuel", "Jesus", "Davi", "Moisés", "Escreveu o Pentateuco e foi usado por Deus para libertar o povo Hebreu da escravidão no Egito"));
        list.add(new QuestionModel(88, "Quem foi companheiro de batalhas de Josué?", "José", "Davi", "Gideão", "Calebe", "Calebe", "Entrou com Josué na terra prometida"));
        list.add(new QuestionModel(89, "Quem derrotou Golias?", "Josué", "Sansão", "Gideão", "Davi", "Davi", "Era Pastor de ovelhas e tinha apenas 17 anos"));
        list.add(new QuestionModel(90, "Quem foi abandonado pelo seus irmãos e vendido como escravo?", "Moisés", "José do Egito", "Jesus", "Elias", "José do Egito", "Foi vendido para o Egito com 17 anos"));
        list.add(new QuestionModel(91, "Quem cantava na prisão com Paulo antes do terremoto?", "Pedro", "João Marcos", "Silas", "Apolo", "Silas", "Atos 16:25-31"));
        list.add(new QuestionModel(92, "Quem entrou na cidade dançando com a arca da aliança?", "Salomão", "Davi", "Paulo", "Pedro", "Davi", "Era Pastor de ovelhas"));
        list.add(new QuestionModel(93, "Qual era o nome da montanha onde Moisés recebeu os 10 Mandamentos?", "Monte Horebe", "Monte Sinai", "Monte das Oliveiras", "Monte Carmelo", "Monte Sinai", "Êxodo 19"));
        list.add(new QuestionModel(94, "Quem lutou com o anjo até ser abençoado?", "Jacó", "Abraão", "Isaque", "José", "Jacó", "Era casado com Leia e Raquel"));
        list.add(new QuestionModel(95, "Quem se tornou uma coluna de sal depois que olhou para trás?", "Maria Madalena", "Sara", "Noé", "Mulher de Ló", "Mulher de Ló", "Destruição de Sodoma e Gomorra"));
        list.add(new QuestionModel(96, "Qual cidade teve a sua muralha derrubada?", "Creta", "Corinto", "Jericó", "Nazaré", "Jericó", "Josué 6"));
        list.add(new QuestionModel(97, "O que Gideão usou contra os midianitas?", "Escudos, espadas e lanças", "Trombetas, cântaros e lâmpadas", "Carruagens e arqueiros", "Pedras", "Trombetas, cântaros e lâmpadas", "Gideão e os 300"));
        list.add(new QuestionModel(98, "O Livro de Ageu está em que parte da Bíblia?", "Novo Testamento", "Não existe na Bíblia", "Antigo Testamento", "Dentro de outro livro da bíblia", "Antigo Testamento", "Vem depois de Sofonias"));
        list.add(new QuestionModel(99, "O Livro de Enoque está em que parte da Bíblia?", "Novo Testamento", "Não existe na Bíblia", "Antigo Testamento", "Dentro de outro livro da bíblia", "Não existe na Bíblia", "Foi arrebatado"));
        list.add(new QuestionModel(100, "Quem foi escolhido pelos discípulos para substituir Judas Iscariotes ?", "Matias", "Paulo", "Barnabé", "Lucas", "Matias", "Atos 1:26"));
        list.add(new QuestionModel(101, "O que estava escrito na placa da cruz onde Jesus foi crucificado?", "'Ele não fez nada'", "'Soltem este homem'", "'Jesus Nazareno, O Reis dos Judeus'", "'Culpado'", "'Jesus Nazareno, O Reis dos Judeus'", "Tá escrito! Tá escrito!"));

        this.cadastrarPergunta(list);

    }

    private void cadastrarPergunta(List<QuestionModel> lista) {

        open();

        ContentValues valores = new ContentValues();


        for (int i = 0; i < lista.size(); i++) {

            int id = lista.get(i).getId();
            String pergunta = lista.get(i).getPergunta();
            String questaoA = lista.get(i).getQuestaoA();
            String questaoB = lista.get(i).getQuestaoB();
            String questaoC = lista.get(i).getQuestaoC();
            String questaoD = lista.get(i).getQuestaoD();
            String questaoCorreta = lista.get(i).getQuestaoCorreta();
            String questaoDica = lista.get(i).getQuestaoDica();

            valores.put("id", id);
            valores.put("pergunta", pergunta);
            valores.put("questaoA", questaoA);
            valores.put("questaoB", questaoB);
            valores.put("questaoC", questaoC);
            valores.put("questaoD", questaoD);
            valores.put("questaoCorreta", questaoCorreta);
            valores.put("questaoDica", questaoDica);

            long resultado = db.insert(TABELA_PERGUNTAS, null, valores);


            if (resultado == -1) {

                Log.i("Banco", "Erro ao cadastrar todas as perguntas");
            } else {
                Log.i("Banco", "Sucesso ao cadastrar todas as perguntas");
            }

        }

        close();

    }

    public List<QuestionModel> listarPerguntas() {

        List<QuestionModel> lista = new ArrayList<>();

        open();

        int contador = 1;

        String sql = "SELECT * FROM perguntas ORDER BY RANDOM() LIMIT 10";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(0);
                    String pergunta = contador + " - " + cursor.getString(1);
                    String questaoA = cursor.getString(2);
                    String questaoB = cursor.getString(3);
                    String questaoC = cursor.getString(4);
                    String questaoD = cursor.getString(5);
                    String questaoCorreta = cursor.getString(6);
                    String questaoDica = cursor.getString(7);

                    QuestionModel v = new QuestionModel(id, pergunta, questaoA, questaoB, questaoC, questaoD, questaoCorreta, questaoDica);
                    lista.add(v);

                    contador += 1;

                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;

    }


    //Themes
    private void insertThemes() {

        List<ThemeslistModel> listTheme = new ArrayList<>();

        //Amor
        listTheme.add(new ThemeslistModel(43, 3, 16, "Amor"));
        listTheme.add(new ThemeslistModel(40, 22, 37, "Amor"));
        listTheme.add(new ThemeslistModel(40, 22, 39, "Amor"));
        listTheme.add(new ThemeslistModel(46, 13, 1, "Amor"));
        listTheme.add(new ThemeslistModel(46, 13, 2, "Amor"));
        listTheme.add(new ThemeslistModel(46, 13, 13, "Amor"));
        listTheme.add(new ThemeslistModel(51, 3, 14, "Amor"));

        //Amizade
        listTheme.add(new ThemeslistModel(59, 4, 4, "Amizade"));
        listTheme.add(new ThemeslistModel(21, 4, 10, "Amizade"));
        listTheme.add(new ThemeslistModel(45, 12, 10, "Amizade"));
        listTheme.add(new ThemeslistModel(20, 17, 17, "Amizade"));
        listTheme.add(new ThemeslistModel(20, 13, 20, "Amizade"));
        listTheme.add(new ThemeslistModel(20, 27, 17, "Amizade"));
        listTheme.add(new ThemeslistModel(20, 27, 9, "Amizade"));

        //Ansiedade
        listTheme.add(new ThemeslistModel(60, 5, 7, "Ansiedade"));
        listTheme.add(new ThemeslistModel(40, 6, 31, "Ansiedade"));
        listTheme.add(new ThemeslistModel(40, 6, 32, "Ansiedade"));
        listTheme.add(new ThemeslistModel(40, 6, 33, "Ansiedade"));
        listTheme.add(new ThemeslistModel(40, 6, 34, "Ansiedade"));
        listTheme.add(new ThemeslistModel(19, 56, 3, "Ansiedade"));

        //Namoro
        listTheme.add(new ThemeslistModel(22, 8, 4, "Namoro"));
        listTheme.add(new ThemeslistModel(30, 3, 3, "Namoro"));
        listTheme.add(new ThemeslistModel(20, 4, 23, "Namoro"));
        listTheme.add(new ThemeslistModel(20, 19, 14, "Namoro"));
        listTheme.add(new ThemeslistModel(1, 2, 18, "Namoro"));
        listTheme.add(new ThemeslistModel(47, 6, 14, "Namoro"));
        listTheme.add(new ThemeslistModel(47, 6, 15, "Namoro"));
        listTheme.add(new ThemeslistModel(46, 6, 18, "Namoro"));
        listTheme.add(new ThemeslistModel(46, 6, 19, "Namoro"));
        listTheme.add(new ThemeslistModel(46, 6, 20, "Namoro"));
        listTheme.add(new ThemeslistModel(46, 15, 33, "Namoro"));

        //Casamento
        listTheme.add(new ThemeslistModel(54, 5, 14, "Casamento"));
        listTheme.add(new ThemeslistModel(51, 3, 18, "Casamento"));
        listTheme.add(new ThemeslistModel(51, 3, 19, "Casamento"));
        listTheme.add(new ThemeslistModel(20, 19, 14, "Casamento"));
        listTheme.add(new ThemeslistModel(1, 2, 18, "Casamento"));


        //Santidade
        listTheme.add(new ThemeslistModel(49, 5, 3, "Santidade"));
        listTheme.add(new ThemeslistModel(58, 12, 14, "Santidade"));
        listTheme.add(new ThemeslistModel(60, 1, 15, "Santidade"));
        listTheme.add(new ThemeslistModel(60, 1, 16, "Santidade"));
        listTheme.add(new ThemeslistModel(30, 5, 14, "Santidade"));

        //Pecado
        listTheme.add(new ThemeslistModel(20, 10, 9, "Pecado"));
        listTheme.add(new ThemeslistModel(20, 28, 13, "Pecado"));
        listTheme.add(new ThemeslistModel(59, 3, 10, "Pecado"));
        listTheme.add(new ThemeslistModel(45, 8, 1, "Pecado"));
        listTheme.add(new ThemeslistModel(45, 8, 2, "Pecado"));

        //Tristeza
        listTheme.add(new ThemeslistModel(19, 38, 9, "Tristeza"));
        listTheme.add(new ThemeslistModel(47, 6, 10, "Tristeza"));
        listTheme.add(new ThemeslistModel(45, 12, 15, "Tristeza"));
        listTheme.add(new ThemeslistModel(20, 10, 1, "Tristeza"));
        listTheme.add(new ThemeslistModel(19, 34, 17, "Tristeza"));
        listTheme.add(new ThemeslistModel(19, 34, 18, "Tristeza"));


        //Sabedoria
        listTheme.add(new ThemeslistModel(21, 8, 1, "Sabedoria"));
        listTheme.add(new ThemeslistModel(21, 7, 10, "Sabedoria"));
        listTheme.add(new ThemeslistModel(20, 27, 1, "Sabedoria"));
        listTheme.add(new ThemeslistModel(20, 10, 1, "Sabedoria"));
        listTheme.add(new ThemeslistModel(19, 32, 8, "Sabedoria"));
        listTheme.add(new ThemeslistModel(45, 8, 28, "Sabedoria"));
        listTheme.add(new ThemeslistModel(20, 3, 13, "Sabedoria"));


        //Aprender
        listTheme.add(new ThemeslistModel(23, 26, 9, "Aprender"));
        listTheme.add(new ThemeslistModel(52, 5, 11, "Aprender"));
        listTheme.add(new ThemeslistModel(20, 27, 1, "Aprender"));
        listTheme.add(new ThemeslistModel(19, 25, 5, "Aprender"));
        listTheme.add(new ThemeslistModel(62, 2, 27, "Aprender"));
        listTheme.add(new ThemeslistModel(20, 12, 1, "Aprender"));


        //Oração
        listTheme.add(new ThemeslistModel(19, 63, 1, "Oração"));
        listTheme.add(new ThemeslistModel(52, 5, 16, "Oração"));
        listTheme.add(new ThemeslistModel(52, 5, 17, "Oração"));
        listTheme.add(new ThemeslistModel(52, 5, 18, "Oração"));
        listTheme.add(new ThemeslistModel(24, 33, 3, "Oração"));
        listTheme.add(new ThemeslistModel(40, 21, 22, "Oração"));
        listTheme.add(new ThemeslistModel(43, 15, 7, "Oração"));


        this.createVerseTheme(listTheme);

    }

    private void createVerseTheme(List<ThemeslistModel> lista) {

        open();

        ContentValues valores = new ContentValues();

        for (int i = 0; i < lista.size(); i++) {

            int book = lista.get(i).getBook_id();
            int chapter = lista.get(i).getChapter_id();
            int verse = lista.get(i).getVerse_id();
            String theme = lista.get(i).getThemeName();


            valores.put("book_id", book);
            valores.put("chapter_id", chapter);
            valores.put("verse_id", verse);
            valores.put("theme_name", theme);

            long resultado = db.insert(TABELA_TEMAS, null, valores);


            if (resultado == -1) {

                Log.i("Temas", "Erro ao cadastrar todos os temas");
            } else {
                Log.i("Temas", "Sucesso ao cadastrar todos os temas");
            }

        }

        close();

    }

    public List<ThemeslistModel> listThemesVerses(String theme) {

        List<ThemeslistModel> lista = new ArrayList<>();

        open();

        String sql = "SELECT * FROM temas where theme_name='" + theme + "'";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    //int id = cursor.getInt(0);
                    int book_id = cursor.getInt(1);
                    int chapter_id = cursor.getInt(2);
                    int verse_id = cursor.getInt(3);
                    String theme_name = cursor.getString(4);

                    ThemeslistModel v = new ThemeslistModel(book_id, chapter_id, verse_id, theme_name);
                    lista.add(v);


                } while (cursor.moveToNext());
            }


        }
        cursor.close();
        close();
        return lista;


    }


    //BookDescription
    private void insertDescriptionBook() {

        List<DescriptionBookModel> listDescriptionBook = new ArrayList<>();

        /*ANTIGO TESTAMENTO */

        //Gênesis
        listDescriptionBook.add(new DescriptionBookModel(1, "Gn", "Moisés",
                "A palavra Gênesis quer dizer “Começo, Início”. Este é o livro da bíblia que conta como tudo que " +
                        "existe começou, como surgiram os seres humanos, " +
                        "inclusive a entrada do pecado e do sofrimento na humanidade. " +
                        "Neste livro estão descritas as ações de Deus na criação do mundo, " +
                        "no cuidar das pessoas e na justiça divina que castiga os ímpios e abençoa " +
                        "os justos.", 5, 0, "1400 aC", 0, "Fonte: https://www.infoescola.com/biblia/genesis"));


        //Êxodo
        listDescriptionBook.add(new DescriptionBookModel(2, "Êx", "Moisés",
                "A palavra Êxodo quer dizer “Saída”. Este é o livro da bíblia que conta a passagem " +
                        "considerada mais importante da história do povo de Israel: a saída dos israelitas do Egito, " +
                        "onde viviam como escravos no Egito. Essa libertação deu origem a primeira páscoa.   " +
                        "Ao longo de 40 capítulos o livro relata além dos detalhes sobre a vida de escravidão, " +
                        "o nascimento e grande parte da vida de Moisés.", 5, 0, "1400 aC", 0, "Fonte: https://www.infoescola.com/biblia/exodo"));

        //Levitico
        listDescriptionBook.add(new DescriptionBookModel(3, "Lv", "Moisés",
                "O terceiro livro da Bíblia recebe esse nome porque contém a lei dos sacerdotes da Tribo de Levi – uma das doze tribos de Israel " +
                        "que foi designada para exercer a função sacerdotal no meio do seu povo. E assim como Gênesis e Êxodo, " +
                        "a autoria de Levítico é atribuída a Moisés, profeta que viveu por volta de 1400 aC e teria conduzido " +
                        "o povo israelita para fora do Egito, sobre os desígnios do próprio Deus, a fim de libertar o " +
                        "povo da escravidão. Nos livros seguintes a expressão “Lei de Moisés” faz referência aos cinco " +
                        "primeiros livros da bíblia (o chamado “Pentateuco”) que, entre outras informações, trazem orientações " +
                        "de conduta ao povo de Deus, como por exemplo, os 10 mandamentos.",
                5, 0, "1400 aC", 0, "Fonte: https://www.infoescola.com/biblia/levitico"));

        //Números
        listDescriptionBook.add(new DescriptionBookModel(4, "Nm", "Moisés",
                "O quarto livro da Bíblia possui esse nome como uma referência aos " +
                        "dois censos que foram realizados para a contagem do povo de Israel. " +
                        "O primeiro censo ocorreu logo após a saída do Egito e o segundo um pouco " +
                        "antes de entrarem em Canaã, quase quarenta anos depois. " +
                        "E assim como Gênesis, Êxodo e Levítico, este livro tem a sua autoria atribuída" +
                        " a Moisés (profeta que viveu por volta de 1400 AC e teria " +
                        "conduzido o povo israelita para fora do Egito, sobre os desígnios do próprio Deus, " +
                        "a fim de libertar o povo da escravidão).",
                5, 0, "1400 aC", 0, "Fonte: https://www.infoescola.com/biblia/numeros"));

        //Deuteronômio
        listDescriptionBook.add(new DescriptionBookModel(5, "Dt", "Moisés",
                "Este é o quinto livro da bíblia, que compõe o Pentateuco, " +
                        "cuja autoria é atribuída a Moisés (profeta que viveu1400 AC e liderou a " +
                        "libertação do povo israelita da escravidão no Egito, sob a orientação do " +
                        "próprio Deus), e recebe este nome por significar “repetir a lei” ou “segunda lei”..\n" +
                        "Ao longo de 34 capítulos, as passagens trazem os discursos de Moisés quando o povo ainda " +
                        "estava na terra de Moabe, a leste do Rio Jordão.",
                5, 0, "1400 aC", 0, "Fonte: https://www.infoescola.com/biblia/deuteronomio"));

        //Josué
        listDescriptionBook.add(new DescriptionBookModel(6, "Js", "Incerto, (Josué)",
                "O sexto livro da Bíblia relata ao longo de 24 capítulos, a história " +
                        "de como os israelitas conquistaram a terra de Canaã e " +
                        "passaram a morar nela. E Josué foi responsável por comandar a " +
                        "conquista da terra, tendo sucedido Moisés como líder do povo de Deus.\n\n" +
                        "Ao nascer, Josué recebeu o nome de Oseias (hebr. Hishea, “salvação”; Nm 13.8), " +
                        "mas Moisés o chamou de Josué (hebr. Yehoshua, “o Senhor salva”; Nm 13.16).",
                5, 0, "1400-1375 aC", 0, "Fonte: https://www.infoescola.com/biblia/josue"));

        //Juízes
        listDescriptionBook.add(new DescriptionBookModel(7, "Jz", "Incerto, (Samuel)",
                "Apesar de não trazer nenhuma declaração explícita sobre de quem seja a autoria," +
                        " ela é atribuída a Samuel. Este, que é o sétimo livro da Bíblia, e faz parte do" +
                        " Antigo Testamento, conta a história de Israel desde a conquista da Terra de Canaã" +
                        " até o começo da monarquia. Neste tempo surgiram os líderes militares conhecidos" +
                        " como “Juízes” que foram levantados por Deus, e por isso o governo deles não era hereditário.",
                5, 0, "1050-1000 aC", 0, "Fonte: https://www.infoescola.com/biblia/juizes"));


        //Rute
        listDescriptionBook.add(new DescriptionBookModel(8, "Rt", "Incerto, (Samuel)",
                "Rute é bisavó de Davi, considerado maior rei de Israel. Tendo em vista a " +
                        "semelhança de linguagem entre os livros de Rute, Juízes e Samuel, a autoria " +
                        "deste oitavo livro da bíblia é atribuída a Samuel. Além dele, há indícios de " +
                        "participações de Ezequias e Davi, embora as evidências textuais não confirmem essa teoria.\n\n" +
                        "A história se passa no tempo em que o povo de Israel ainda era governado por Juízes. " +
                        "Rute era casada com um israelita, ela era uma jovem do país de Moabe, e após da morte do " +
                        "marido se une ainda mais à sua sogra.",
                5, 0, "1210-1030 aC", 0, "Fonte: https://www.infoescola.com/biblia/rute"));

        //1 Samuel
        listDescriptionBook.add(new DescriptionBookModel(9, "1Sm", "Samuel",
                "Samuel foi o último dos juízes, Saul foi o primeiro rei de Israel e Davi o segundo. " +
                        "O livro de I Samuel conta a história de transição do período dos juizes para o período dos reis.\n\n" +
                        "Após um longo período de clamor e oração para que tivesse filhos, a ponto do marido achar que estivesse" +
                        " embriagada, Ana, mãe de Samuel orou assim que soube que esperava o menino: “Meu coração exulta no Senhor; " +
                        "no Senhor minha força é exaltada”. Minha boca se exalta sobre os meus inimigos, pois me alegro em tua libertação....",
                5, 0, "1100 até 1000 aC", 0, "Fonte: https://www.infoescola.com/biblia/i-e-ii-samuel"));

        //2 Samuel
        listDescriptionBook.add(new DescriptionBookModel(10, "2Sm", "Samuel",
                "Por sua vez o II livro de Samuel detalha o inicio do reinado de Davi, " +
                        "que derrotou um gigante nas lutas entre os pretendentes ao trono de Jerusalém." +
                        " Ele era um homem de profunda devoção a Deus e que conquistou a lealdade de" +
                        " seu povo. O livro não poupa nem mesmo seus pecados, e assim que o profeta Natã" +
                        " os apontou, Davi aceitou a correção da parte de Deus, e ainda obteve vitória num" +
                        " período de guerra civil.",
                5, 0, "960 aC", 0, "Fonte: https://www.infoescola.com/biblia/i-e-ii-samuel"));


        //1 Reis
        listDescriptionBook.add(new DescriptionBookModel(11, "1Rs", "Incerto, (Jeremias)",
                "O livro de I Reis traz informações sobre um contexto que começou no livro de Samuel, que é a história dos reis israelitas. " +
                        "Samuel foi o último dos juízes, Saul foi o primeiro rei de Israel e Davi o segundo. " +
                        "Após a morte da Davi, que reinou em Jerusalém, seu filho Salomão herda o trono e um dos " +
                        "destaques de seu reinado foi construir um grande templo na região. Conhecido nos dias " +
                        "atuais como “Templo de Salomão”.",
                5, 0, "560-538 aC", 0, "Fonte: https://www.infoescola.com/biblia/i-e-ii-reis"));

        //2 Reis
        listDescriptionBook.add(new DescriptionBookModel(12, "2Rs", "Incerto, (Jeremias)",
                "Este livro pode ter uma espécie de divisão, pois relata a história de dois reinos, no Norte e do Sul." +
                        " O primeiro com a queda de Samaria e o segundo coma conquista de " +
                        "Jerusalém pelo rei Nabucodonosor. A queda dos reinos de Israel " +
                        "acontece porque seus reis haviam sido infiéis com os principio do " +
                        "Senhor, deixando toda a nação vulnerável.",
                5, 0, "560-538 aC", 0, "Fonte: https://www.infoescola.com/biblia/i-e-ii-reis"));


        //1 Crônicas
        listDescriptionBook.add(new DescriptionBookModel(13, "1Cr", "Atribuído a Esdras",
                "A boa nova deste livro é mostrar ao longo de 29 capítulos, que embora estivessem fragilizados " +
                        "pela perda que sofreram em Jerusalém para o rei Nabucodonosor, as promessas de Deus se cumpririam. " +
                        "Ainda que seu povo tivesse sido infiel, o Senhor é fiel para cumprir as suas promessas, e mostra isso " +
                        "sobretudo para os fiéis que viviamem Judá. Alémda genealogia de Adão até Davi, estão relatadas as conquistas" +
                        " de Davi e seu filho Salomão. O livro conta as reformas promovidas por Josafá, Ezequias, e Josias, e ainda, " +
                        "sobre a grande parte do povo ter continuado fiel a Deus.",
                5, 0, "425-400 aC", 0, "Fonte: https://www.infoescola.com/biblia/i-e-ii-cronicas/"));

        //2 Crônicas
        listDescriptionBook.add(new DescriptionBookModel(14, "2Cr", "Atribuído a Esdras",
                "Esta que é a continuação do I Crônicas traz mais informações sobre o reinado de Salomão em " +
                        "Israel e em Judá, a visita da rainha de Sabá ao reinado dele, e a divisão em dois reinos após" +
                        " a morte de Salomão.  Ao longo de 36 capítulos, o livro termina com o decreto do rei Ciro, " +
                        "que reinava sobre a Pérsia. Foi ele que permitiu que os judeus voltassem para Jerusalém e " +
                        "reconstruíssem o templo.",
                5, 0, "425-400 aC", 0, "Fonte: https://www.infoescola.com/biblia/i-e-ii-cronicas/"));

        //Esdras
        listDescriptionBook.add(new DescriptionBookModel(15, "Ed", "Atribuído a Esdras",
                "O livro de Esdras faz parte do Antigo Testamento, e é continuação do segundo livro " +
                        "de crônicas. A tradição atribui este livro a Esdras, um escriba e sacerdote, " +
                        "descendente de Arão, e que dedicou a vida a estudar e " +
                        "ensinar a palavra de Deus durante o exílio dos israelitas " +
                        "na Babilônia. No entanto ainda não há um consenso entre " +
                        "os estudiosos quanto a sua autoria. Inclusive, existe um " +
                        "segmento de historiadores que afirmam que manuscritos mais antigos, " +
                        "Esdras e Neemias constituíam um único livro com características semelhantes quanto ao estilo e conteúdo.",
                5, 0, "538-457 aC", 0, "Fonte: https://www.infoescola.com/biblia/esdras/"));

        //Neemias
        listDescriptionBook.add(new DescriptionBookModel(16, "Ne", "Neemias",
                "Este livro conta ao longo de 13 capítulos, que Neemias sempre dependeu " +
                        "de Deus e foi um homem de oração. Ele foi mandando pelo próprio Ciro, " +
                        "Rei da Pérsia, para governar Judá. O livro de Neemias faz parte do Antigo " +
                        "Testamento, e é a continuação do livro de Esdras (sacerdote, descendente de " +
                        "Arão, e que dedicou a vida para estudar e ensinar a palavra de Deus durante o " +
                        "exílio dos israelitas na Babilônia, e acompanhou a restauração do tempo após " +
                        "o decreto de Ciro Rei da Pérsia libertando este povo).",
                5, 0, "423 aC", 0, "Fonte: https://www.infoescola.com/biblia/neemias/"));


        //Ester
        listDescriptionBook.add(new DescriptionBookModel(17, "Et", "Desconhecido",
                "O livro de Ester faz parte do Antigo Testamento, e o seu autor é desconhecido. " +
                        "Há indícios de que tenha sido escrito por algum judeu que conhecia Susã , o palácio real" +
                        " e os costumes persas. O Talmude atribui este livro aos homens da grande sinagoga, " +
                        "que seriam mestres anônimos que viveram no período que se passou entre os últimos profetas" +
                        " e os primeiros estudiosos rabínicos. Já os patriarcas da igreja primitiva (por exemplo Clemente de Alexandria)" +
                        " e autoridades judaicas (por exemplo Josefo), atribuem o livro a Mordecai. Esta segunda autoria é mais aceita " +
                        "e difindida pela tradição, que ainda reconhecem a possibilidade de terem escritos de Esdras e Neemias.",
                5, 0, "465 aC", 0, "Fonte: https://www.infoescola.com/biblia/ester/"));

        //Jó
        listDescriptionBook.add(new DescriptionBookModel(18, "Jó", "Incerto, (Moisés ou Salomão)",
                "O livro de Jó faz parte do Antigo Testamento, aparece depois do livro de Ester e antes do " +
                        "livro de Salmos. Seu autor é desconhecido, pois o nome não parece de forma explícita. " +
                        "Entretanto há indícios de que tenha sido escrito pelo próprio Jó, ou ainda por Eliú,  " +
                        "Ezequias, Isaías, Moisés e Salomão. Caso tenha sido Moisés a data possível é de 1441 AC e " +
                        "caso tenha sido Salomão a data possível é de950 AC. Uma constatação sobre quem escreveu o livro, " +
                        "é que sem dúvida, tinha aptidões literárias e vasto conhecimento sobre os animais, a mineração, " +
                        "a astronomia, a caça e a consciência sobre o desenvolvimento embrionário.",
                5, 0, "séc. V ao II aC", 0, "Fonte: https://www.infoescola.com/biblia/jo/"));

        //Salmos
        listDescriptionBook.add(new DescriptionBookModel(19, "Sl", "Davi, Asafe, filhos de Coré e outros",
                "O livro dos Salmos é um livro de orações e hinos da Bíblia. Ele contém cânticos de adoração que foram" +
                        " compostos por um longo período da história de Israel. Entre os anos de 1000 a 333 AC, e " +
                        "diversos autores os escreveram, eram usados pelo povo de Israel nas reuniões de adoração a Deus. Alguns salmos " +
                        "possuem em seu título a atribuição daquele salmo a um determinado autor como Davi e Salomão, outros " +
                        "salmos por sua vez não trazem menção de quem os teria escrito.",
                5, 0, "1000 e 300 aC", 0, "Fonte: https://www.infoescola.com/biblia/salmos/"));

        //Provérbios
        listDescriptionBook.add(new DescriptionBookModel(20, "Pv", "Salomão, Agur e rei Lemuel",
                "A Bíblia faz referencia ao rei Salomão como o rei mais sábio da história de Israel. Salomão ora ao Senhor: " +
                        "“Dá, pois ao teu servo coração compreensivo para julgar a teu povo para que prudentemente " +
                        "discirna entre o bem e o mal, pois quem poderia julgar a este grande povo?” (IRs 3:9)  " +
                        "e a autoria da maioria deste livro é atribuída a ele, e há suspeita de que além de seus " +
                        "próprios ensinamentos, Salomão tenha editado provérbios de outros escritores.",
                5, 0, "950 aC", 0, "Fonte: https://www.infoescola.com/biblia/proverbios/"));

        //Eclesiastes
        listDescriptionBook.add(new DescriptionBookModel(21, "Ec", "Salomão",
                "Embora o livro de Eclesiastes não identifique diretamente o seu autor, a forte" +
                        " tradição se baseia em alguns versículos que dão a entender que Salomão e " +
                        "dá crédito a ele como o pregador que escreveu este livro: “Palavra do pregador, " +
                        "filho de Davi, rei de Jerusalém: Vaidade de vaidades, diz o pregador, vaidade de " +
                        "vaidades, tudo é vaidade” (Ec 1,1:2)  Isso não anula a possibilidade de alguma " +
                        "outra pessoa possa ter feito acréscimos ao livro centenas de anos após a morte " +
                        "de Salomão.",
                5, 0, "931 aC", 0, "Fonte: https://www.infoescola.com/biblia/eclesiastes/"));

        //Cantares
        listDescriptionBook.add(new DescriptionBookModel(22, "Ct", "Salomão",
                "Em latim é chamado somente de Cânticos. O título hebraico é uma expressão " +
                        "Cântico dos Cânticos, com significado superlativo “o mais excelente cântico”. " +
                        "Na bíblia hebraica, Cânticos dos Cânticos faz parte de uma coletânea lida nos dias " +
                        "festivos do calendário judaico. Os outros livros dessa coletânea são Rute, Ester, " +
                        "Eclesiastes e Lamentações.",
                5, 0, "970-930 aC", 0, "Fonte: https://www.infoescola.com/biblia/canticos-dos-canticos/"));

        //Isaías
        listDescriptionBook.add(new DescriptionBookModel(23, "Is", "Isaías",
                "Isaías, filho de Amoz, é uma dos maiores profetas do Antigo Testamento, " +
                        "e anunciou suas mensagens proféticas ao povo do reino de Judá e aos moradores " +
                        "da cidade de a partir do ano da morte do rei Uzias, e após Uzias reinaram no " +
                        "tempo de Jeremias os reis Jotão, Acaz e Ezequias, indicando que estes acontecimentos " +
                        "se passaram no período de 742 a 687 AC. “No ano da morte do rei Uzias, eu vi o " +
                        "Senhor assentado num alto e sublime trono, e as abas de suas vestes enchiam o templo” (Is 6:1).",
                5, 0, "700-690 aC", 0, "Fonte: https://www.infoescola.com/biblia/isaias/"));

        //Jeremias
        listDescriptionBook.add(new DescriptionBookModel(24, "Jr", "Jeremias",
                "Grande foi o chamado para o profeta Jeremias, homem humilde perante Deus " +
                        "“Olha para mim, Senhor, e ouve a voz dos que contendem comigo”  (Jr 18:19), " +
                        "e que chegou a se dizer criança para fazer tamanha obra a mando do próprio Deus, " +
                        "que entre outras promessas o disse “Antes que eu te formasse no ventre materno, eu te conheci, e, " +
                        "antes que saísses da madre, te consagrei, e te constituí profeta às nações” (Jr 1:5) .",
                5, 0, "626-586 aC", 0, "Fonte: https://www.infoescola.com/biblia/jeremias/"));

        //Lamentações
        listDescriptionBook.add(new DescriptionBookModel(25, "Lm", "Jeremias",
                "O livro de Lamentações de Jeremias faz parte do Antigo Testamento e é continuação" +
                        " do livro de Jeremias, e é de sua autoria. Ocorreu que 586 AC a cidade de Jerusalém " +
                        "foi destruída e o profeta chora essa destruição, efetivada pelo rei Nabucodonosor " +
                        "e o exercito da Babilônia.  O profeta havia anunciado mensagens finais sobre Judá, " +
                        "alertando-lhe sobre a destruição que se aproximava caso a nação não se arrependesse, " +
                        "e clamava, pedindo ao povo que se arrependesse e a voltasse para Deus.",
                5, 0, "587 aC", 0, "Fonte: https://www.infoescola.com/biblia/lamentacoes-de-jeremias/"));

        //Ezequiel
        listDescriptionBook.add(new DescriptionBookModel(26, "Ez", "Ezequiel",
                "A autoria deste livro é atribuída ao profeta Ezequiel “Veio expressamente a " +
                        "palavra do Senhor a Ezequiel, filho de Buzi, o sacerdote, na terra dos caldeus, " +
                        "junto do rio Quebar, e ali esteve sobre ele a mão do Senhor”(Ez 1:3) e ele foi " +
                        "contemporâneo de Jeremias e Daniel. E no tempo do profeta Ezequiel (entre 593 a 565 AC) " +
                        "a cidade de Jerusalém foi tomada pelos babilônicos, e o profeta viveu na Babilônia " +
                        "onde os israelitas haviam sido levados como prisioneiros. As mensagens do profeta " +
                        "eram direcionadas a todo povo que vivia ali na Babilônia e aos moradores de Jerusalém.",
                5, 0, "593-573 aC", 0, "Fonte: https://www.infoescola.com/biblia/ezequiel/"));

        //Daniel
        listDescriptionBook.add(new DescriptionBookModel(27, "Dn", "Daniel",
                "A história de Daniel na cova dos leões está relatada na bíblia, no livro do próprio profeta, no antigo testamento.\n" +
                        "\n" +
                        "Daniel era um dos três príncipes governantes e se destacava entre eles, pois nele havia um espírito excelente e o rei pensava" +
                        " constituí-lo sobre todo o reino. Então os outros dois presidentes e os príncipes procuravam achar algo contra Daniel a respeito " +
                        "do reino e não achavam, porque ele era fiel, e não se achava nele nenhum erro nem culpa.",
                5, 0, "Final do séc. VI AC", 0, "Fonte: https://www.infoescola.com/biblia/daniel-na-cova-dos-leoes/"));

        //Oséias
        listDescriptionBook.add(new DescriptionBookModel(28, "Os", "Oséias",
                "O livro Oséias faz parte do Velho Testamento e sua autoria é atribuída ao profeta, " +
                        "onde estabelece uma relação entre sua vida pessoal com as mensagens proféticas para " +
                        "o povo de Deus. Ele era filhos de Beeri e foi o único profeta que registrou durante " +
                        "os últimos anos de sua vida um conjunto de profecias para Israel. Ele viveu entre 785 e 725 aC, " +
                        "e registrou as profecias por volta de 755 a 725 aC.",
                5, 0, "750 aC", 0, "Fonte: https://www.infoescola.com/biblia/oseias/"));

        //Joel
        listDescriptionBook.add(new DescriptionBookModel(29, "Jl", "Joel",
                "O livro Joel faz parte do Velho Testamento e sua autoria é atribuída ao " +
                        "próprio profeta. O ponto de partida para Joel foi uma terrível praga de gafanhotos, " +
                        "e que foi seguida por uma grande seca que devastaram a cidade de Judá. Para ele, " +
                        "esses acontecimentos eram avisos de que a cidade precisava se arrepender e voltar-se para Deus.",
                5, 0, "835-805 aC", 0, "Fonte: https://www.infoescola.com/biblia/joel/"));

        //Amós
        listDescriptionBook.add(new DescriptionBookModel(30, "Am", "Amós",
                "Amós era pastor de ovelhas em Tecoa (Judá) “As palavras de Amós, que estava entre os pastores de Tecoa, as quais viu a respeito de Israel, " +
                        "nos dias de Uzias, rei de Judá, e nos dias de Jeroboão, filho de Joás, rei de Israel, " +
                        "dois anos antes do terremoto” (Am 1:1) e em nome de Deus anuncia a injustiça, opressão e " +
                        "corrupção que assolavam ao país, pois aquela região apesar de estar em uma situação material boa, " +
                        "estava em pecado “Portanto assim farei ó Israel! Prepara-te ó Israel para encontrares com o Senhor teu Deus. ",
                5, 0, "760–750 aC", 0, "Fonte: https://www.infoescola.com/biblia/amos/"));


        this.createDescriptionBook(listDescriptionBook);

    }

    private void createDescriptionBook(List<DescriptionBookModel> listDescriptionBook) {

        open();

        ContentValues valores = new ContentValues();

        for (int i = 0; i < listDescriptionBook.size(); i++) {


            int book = listDescriptionBook.get(i).getBook_id();
            String sigle = listDescriptionBook.get(i).getSigle();
            String author = listDescriptionBook.get(i).getAuthor();
            String description = listDescriptionBook.get(i).getDescription();
            int availabled = listDescriptionBook.get(i).getBook_id();
            int favorited = listDescriptionBook.get(i).getFavorited();
            String date = listDescriptionBook.get(i).getDate();
            double learning = listDescriptionBook.get(i).getLearning();
            String reference = listDescriptionBook.get(i).getReference();


            valores.put("book_id", book);
            valores.put("sigle", sigle);
            valores.put("author", author);
            valores.put("description", description);
            valores.put("availabled", availabled);
            valores.put("favorited", favorited);
            valores.put("date", date);
            valores.put("learning", learning);
            valores.put("reference", reference);

            long resultado = db.insert(TABELA_DESCRIPTION_BOOK, null, valores);


            if (resultado == -1) {

                Log.i("Description Book", "Erro ao cadastrar todos os descriptions");
            } else {
                Log.i("Description Book", "Sucesso ao cadastrar todos os descriptions");
            }

        }

        close();

    }

    public List<DescriptionBookModel> listDescriptionBook(int id) {

        List<DescriptionBookModel> list = new ArrayList<>();

        open();

        String sql = "SELECT * FROM description_book where book_id='" + id + "'";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int book_id = cursor.getInt(0);
                    String sigle = cursor.getString(1);
                    String author = cursor.getString(2);
                    String description = cursor.getString(3);
                    int availabled = cursor.getInt(4);
                    int favorited = cursor.getInt(5);
                    String date = cursor.getString(6);
                    int learning = cursor.getInt(7);
                    String reference = cursor.getString(8);


                    DescriptionBookModel v = new DescriptionBookModel(book_id, sigle, author, description, availabled, favorited, date, learning, reference);
                    list.add(v);


                } while (cursor.moveToNext());
            }


        }
        cursor.close();
        close();
        return list;

    }

    public Integer learningBook(int id) {

        int learning = 0;

        open();

        String sql = "SELECT * FROM description_book where book_id=" + id;
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    learning = cursor.getInt(7);
                    Log.i("Learning", ""+learning);

                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        close();
        return learning;

    }

    public boolean favorited(int id) {

        open();

        boolean favorited = false;

        String sql = "SELECT * FROM description_book where book_id='" + id + "'";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int f = cursor.getInt(5);
                    if (f != 0) {
                        favorited = true;
                    }
                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        close();
        return favorited;

    }

    public void updateFavoritedBook(int id, int favorited) {
        open();
        ContentValues valores = new ContentValues();

        valores.put("favorited", favorited);

        long resultado = db.update(TABELA_DESCRIPTION_BOOK, valores, "book_id=" + id, null);

        if (resultado == -1) {
            Log.i("Update Favorited", "Erro ao atualizar favorito");
        } else {
            Log.i("Update Favorited", "Sucesso ao atualizar favorito");
        }


        close();

    }

    public void updateLearningBook(int id, int learning) {
        open();
        ContentValues valores = new ContentValues();

        valores.put("learning", learning);

        long resultado = db.update(TABELA_DESCRIPTION_BOOK, valores, "book_id=" + id, null);

        if (resultado == -1) {
            Log.i("Update Favorited", "Erro ao atualizar favorito");
        } else {
            Log.i("Update Favorited", "Sucesso ao atualizar favorito");
        }


        close();

    }


    //verseDay

    private void insertVerseDay() {

        List<VerseDayModel> listVerseDay = new ArrayList<>();

        listVerseDay.add(new VerseDayModel(1, 1, 26, 22, "10-04-2021", "03:27"));

        this.createVerseDay(listVerseDay);

    }

    private void createVerseDay(List<VerseDayModel> listVerseDay) {
        open();
        ContentValues valores = new ContentValues();

        for (int i = 0; i < listVerseDay.size(); i++) {

            int id = listVerseDay.get(i).getId();
            int book_id = listVerseDay.get(i).getBook_id();
            int chapter_id = listVerseDay.get(i).getChapter_id();
            int verse_id = listVerseDay.get(i).getVerse_id();
            String date = listVerseDay.get(i).getDate();
            String time = listVerseDay.get(i).getTime();

            valores.put("id", id);
            valores.put("book_id", book_id);
            valores.put("chapter_id", chapter_id);
            valores.put("verse_id", verse_id);
            valores.put("date", date);
            valores.put("time", time);

            long resultado = db.insert(TABELA_VERSE_DAY, null, valores);


            if (resultado == -1) {

                Log.i("Verse Day", "Erro ao cadastrar todos os versesDay");
            } else {
                Log.i("Verse Day", "Sucesso ao cadastrar todos os versesDay");
            }

        }

        close();

    }

    public void updateVerseDay(List<VerseDayModel> listVerseDay) {

        open();
        ContentValues valores = new ContentValues();

        for (int i = 0; i < listVerseDay.size(); i++) {

            int book_id = listVerseDay.get(i).getBook_id();
            int chapter_id = listVerseDay.get(i).getChapter_id();
            int verse_id = listVerseDay.get(i).getVerse_id();
            String date = listVerseDay.get(i).getDate();
            String time = listVerseDay.get(i).getTime();

            valores.put("book_id", book_id);
            valores.put("chapter_id", chapter_id);
            valores.put("verse_id", verse_id);
            valores.put("date", date);
            valores.put("time", time);

            long resultado = db.update(TABELA_VERSE_DAY, valores, "id=" + 1, null);


            if (resultado == -1) {

                Log.i("Verse Day", "Erro ao atualizar versesDay");
            } else {
                Log.i("Verse Day", "Sucesso ao atualizar versesDay: ");
            }

        }

        close();

    }

    public List<VerseDayModel> listVerseDay() {

        List<VerseDayModel> list = new ArrayList<>();

        open();

        String sql = "SELECT * FROM verse_day";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    int id = cursor.getInt(0);
                    int book_id = cursor.getInt(1);
                    int chapter_id = cursor.getInt(2);
                    int verse_id = cursor.getInt(3);
                    String date = cursor.getString(4);
                    String time = cursor.getString(5);

                    VerseDayModel v = new VerseDayModel(id, book_id, chapter_id, verse_id, date, time);
                    list.add(v);


                } while (cursor.moveToNext());
            }


        }
        cursor.close();
        close();
        return list;


    }


    //check_chapther

    public void createCheckChapther(List<CheckChaptherModel> list) {
        open();
        ContentValues valores = new ContentValues();

        for (int i = 0; i < list.size(); i++) {

            int book_id = list.get(i).getBook_id();
            int chapter_id = list.get(i).getChapter_id();

            valores.put("book_id", book_id);
            valores.put("chapter_id", chapter_id);

            long resultado = db.insert(TABELA_CHECK_CHAPTHER, null, valores);


            if (resultado == -1) {

                Log.i("Check", "Erro ao cadastrar todos os checks");
            } else {
                Log.i("Check", "Sucesso ao cadastrar todos os checks");
            }

        }

        close();
    }

    public List<CheckChaptherModel> listCheck(int book, int chapther) {

        List<CheckChaptherModel> list = new ArrayList<>();

        open();

        String sql = "SELECT * FROM check_chapther where book_id=" + book + " and chapter_id=" + chapther;
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {


                    int book_id = cursor.getInt(1);
                    int chapter_id = cursor.getInt(2);

                    CheckChaptherModel v = new CheckChaptherModel(book_id, chapter_id);
                    list.add(v);


                } while (cursor.moveToNext());
            }


        }
        cursor.close();
        close();
        return list;


    }

    public void dropCheckChapther(List<CheckChaptherModel> list) {
        /*open();
        ContentValues valores = new ContentValues();

        for (int i = 0; i < list.size(); i++) {

            int book_id = list.get(i).getBook_id();
            int chapter_id = list.get(i).getChapter_id();

            valores.put("book_id", book_id);
            valores.put("chapter_id", chapter_id);

            long resultado = db.delete(TABELA_CHECK_CHAPTHER, null, "id=" + 1);


            if (resultado == -1) {

                Log.i("Check", "Erro ao cadastrar todos os checks");
            } else {
                Log.i("Check", "Sucesso ao cadastrar todos os checks");
            }

        }

        close();*/
    }

    public Integer findChapthersLearningBook(int id) {
        int countChapther = 0;
        open();

        String sql = "SELECT count(chapter_id) FROM check_chapther where book_id=" + id;
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {

                    countChapther = cursor.getInt(0);


                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return countChapther;

    }


}
